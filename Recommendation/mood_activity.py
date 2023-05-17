import re
import pandas as pd
import numpy as np
import pymysql

from tqdm import tqdm
from tabulate import tabulate

from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error
from sklearn.metrics.pairwise import cosine_similarity

#유사도를 검사하는 함수
def cos_sim_matrix(a,b):
    cos_sin = cosine_similarity(a,b)
    result_df = pd.DataFrame(data=cos_sin, index=[a.index])

    return result_df

#print를 위한 함수
def print_dictionary(dic_name):
    for key, value in dic_name.items():
        print(f"{key}: {value}")

#비슷한 태그 remove 함수
def remove_similar_item(list):
    new_list = []
    for i in range(len(list)):
        # 첫번째 요소일 경우 그대로 추가
        if i == 0:
            new_list.append(list[i])
        else:
            # 앞서 추가된 요소들과 앞 세 글자가 겹치지 않으면 추가
            for j in range(len(new_list)):
                if new_list[j][:1] == list[i][:1]:
                    break
            else:
                new_list.append(list[i])
    return new_list


# ----------------- DB 연동 --------------------
# conn = pymysql.connect(host='datebuzz.cuigayul1fx5.ap-northeast-2.rds.amazonaws.com', user='buzz123', password='n58#zwqT3VL^VzN', port = 3306, db='DateBuzz', charset='utf8')
# cur = conn.cursor()

# ----------------- DB 연동 --------------------
conn = pymysql.connect(host='127.0.0.1', user='root', password='1234', db='pythonDB', charset='utf8')
cur = conn.cursor()

# --------------------hastag Data 가져오기-------------------
sql = 'select * from hashtag'
cur.execute(sql)
res = cur.fetchall()
prototypeData = pd.DataFrame.from_records(res, columns=[desc[0] for desc in cur.description])
print('----- mariadb에서 hastag data 받아오기 -------')
print (tabulate(prototypeData, headers='keys', tablefmt='psql', showindex=False))

# --------------------Data 전처리-------------------
#1. 필요한 데이터만 남기고 다른 열 삭제
hastag_df = prototypeData[['course_id', 'tag_name']]
hastag_df = hastag_df.groupby('course_id')['tag_name'].apply(lambda x: ' '.join(x)).reset_index()

#2. 한글이 아니거나 값이나 중복되는 값 삭제
hastag_df['tag_name'] = hastag_df['tag_name'].apply(lambda x: re.sub('[^가-힣\s]', '', x))
# print('----- 전처리 후 hastag_df -------')
# print (tabulate(hastag_df, headers='keys', tablefmt='psql', showindex=False))


#----------------03. 게시글에 사용된 태그 목록 생성--------------------
num_of_course = len(hastag_df)
hastag_list = []
for tags in hastag_df['tag_name']:
    hastag_list += tags.split(' ')
    hastag_list = list(set(hastag_list))
    hastag_list = remove_similar_item(hastag_list)
#print('----- hastag list 생성 -------')
#print(hastag_list)

# --------------------04. 해시태그 별 가중치 파악--------------------
hastag_weighting = dict.fromkeys(hastag_list)
for each_hastag in hastag_df['tag_name']:
    for tag in each_hastag.split(' '):
        tag = tag.strip()
        if tag in hastag_weighting:
            if hastag_weighting[tag] == None:
                hastag_weighting[tag] = 1
            else:
                hastag_weighting[tag] = hastag_weighting[tag]+1 
        else:
            continue

for each_hastag in hastag_weighting:
    hastag_weighting[each_hastag] = np.log10(num_of_course/hastag_weighting[each_hastag])
print('--------- hastag별 가중치 파악 ---------')
#print_dictionary(hastag_weighting)

#--------------------05. 해시태그 가중치를 바탕으로 게시글 표현--------------------
# 게시글을 hastag에 따라 나타내는 datafram 틀 생성
hastag_representation = pd.DataFrame(columns=(hastag_list), index=hastag_df.index)

for index, each_row in tqdm(hastag_df.iterrows(), disable=True):
    dict_temp = {} 
    #각 게시글 별로 어떤 hastag가 있는지 나타낸 hastag_df의 행들을 순회하면서 공백을 기준으로 모든 태그를 탐색함
    for tag in each_row['tag_name'].split(' '):
        #hashtag_df에 있는 tag가 hastag_weighting에 있는 tag인지 확인 후 dict_temp에 태그의 이름과 가중치를 담아둠.
        if tag in hastag_weighting:
            dict_temp[tag] = hastag_weighting[tag]
    row_to_add = pd.DataFrame(dict_temp, index=[index])
    #print (tabulate(row_to_add, headers='keys', tablefmt='psql'))
    hastag_representation.update(row_to_add)

hastag_representation = hastag_representation.fillna(0) #NAN값을 0으로 변경
#print(tabulate(hastag_representation.loc[0].to_frame(), headers='keys', tablefmt='psql', floatfmt=".6f"))

hastag_similarity = cos_sim_matrix(hastag_representation, hastag_representation)
#print(tabulate(hastag_similarity, headers='keys', tablefmt='psql', floatfmt=".6f"))


# # --------------------06. 특정 사용자가 북마크 좋아요 한 record data가져오기 --------------------
# # bookmarkd와 like DB에서 record_id 가져오기
user_id = 3;

sql = 'select user_id, record_id from bookmark where user_id = %s'
cur.execute(sql, (user_id))
bookmark_df = pd.DataFrame(cur.fetchall(), columns=['user_id', 'record_id'])
print(tabulate(bookmark_df, headers='keys', tablefmt='psql', showindex=False))

sql = 'select user_id, record_id from likes where user_id = %s'
cur.execute(sql, (user_id))
like_df = pd.DataFrame(cur.fetchall(), columns=['user_id', 'record_id'])
print(tabulate(like_df, headers='keys', tablefmt='psql', showindex=False))

# 두 데이터프레임을 합치고 중복제거
user_df = pd.concat([bookmark_df, like_df])
user_df.drop_duplicates(inplace=True)
print(tabulate(user_df, headers='keys', tablefmt='psql', showindex=False))

#--------------------07. 유사도가 높은 것을 기준으로 추천 리스트 생성--------------------
user_records = list(user_df['record_id'] - 1)
print(user_records)
user_similarities = hastag_similarity.loc[user_records].sort_index()
for idx in user_similarities.index:  # 각 행에 대해 반복
    col = idx[0]  # 행의 인덱스 값
    user_similarities.loc[idx, col] = 0  # 인덱스 값과 같은 열에 해당하는 값을 0으로 설정
print(tabulate(user_similarities, headers='keys', tablefmt='psql', floatfmt=".3f"))

# #recommend에 추천 아이템 5개가 쌓일 때까지 가장 높은 항목을 추출함.
recommended = []
user_similarities = user_similarities.stack()

while len(recommended) < 5:
    top_similarities = user_similarities.nlargest(1)  # 유사도가 가장 높은 항목을 추출    
    index1 = top_similarities.index[0][0]
    index2 = top_similarities.index[0][1]
    if index2 not in recommended and index2 not in user_records:  # 이미 추천된 항목이 아니라면, 이미 북마크/좋아요를 누룬 항목이 아니라면
        print(top_similarities)
        recommended.append(index2)  # 추천 목록에 추가
    user_similarities[index1][index2] = 0

print(recommended)
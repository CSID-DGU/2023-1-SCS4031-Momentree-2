import os
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


# #----------------- DB 연동 --------------------
#conn = DB를 연동하기 위한 DBconnector변수
#cur = DB의 데이터를 GET/POST/PATCH하기 위해 데이터 위치를 담기위한 변수
conn = pymysql.connect(host='127.0.0.1', user='root', password='1234', db='pythonDB', charset='utf8')
cur = conn.cursor()

#--------------------Data 가져오기-------------------
sql = 'select * from hastag'
cur.execute(sql)
res = cur.fetchall()
prototypeData = pd.DataFrame.from_records(res, columns=[desc[0] for desc in cur.description])
#print(prototypeData)

#--------------------Data 전처리-------------------
#1. 필요한 데이터만 남기고 삭제
course_hastag = prototypeData[['course_id', 'tag_name']]
course_hastag = course_hastag.groupby('course_id')['tag_name'].apply(lambda x: ' '.join(x)).reset_index()

#2. 한글이 아닌 값이나 중복되는 값 삭제
course_hastag['tag_name'] = course_hastag['tag_name'].apply(lambda x: re.sub('[^가-힣\s]', '', x))
#print (tabulate(course_hastag, headers='keys', tablefmt='psql', showindex=False))


#----------------03. 게시글에 사용된 태그 목록 생성--------------------
num_of_course = len(course_hastag)
hastag_list = []
for tags in course_hastag['tag_name']:
    hastag_list += tags.split(' ')
    hastag_list = list(set(hastag_list))
    hastag_list = remove_similar_item(hastag_list)
#print(hastag_list)

#--------------------04. 단어의 가중치 파악--------------------
hastag_count = dict.fromkeys(hastag_list)
for each_hastag in course_hastag['tag_name']:
    for tag in each_hastag.split(' '):
        tag = tag.strip()
        if tag in hastag_count:
            if hastag_count[tag] == None:
                hastag_count[tag] = 1
            else:
                hastag_count[tag] = hastag_count[tag]+1 
        else:
            continue

# #--------------------05. 단어의 가중치 계산--------------------
for each_hastag in hastag_count:
    hastag_count[each_hastag] = np.log10(num_of_course/hastag_count[each_hastag])
#print_dictionary(hastag_count)


# #--------------------05. 가중치를 반영하여 게시글 표현--------------------
# #태그 가중치를 반영한 태그 representation
hastag_representation = pd.DataFrame(columns=sorted(hastag_list), index=course_hastag.index)
#print(hastag_representation)
for index, each_row in tqdm(course_hastag.iterrows(), disable=True):
    dict_temp = {} 
    #course_hastag에 있는 행들을 순회하면서 공백을 기준으로 모든 태그를 탐색함
    for tag in each_row['tag_name'].split(' '):
        #tag리스트에 있는 tag의 목록인지 확인 후 dict_temp에 담아둠
        if tag in hastag_count:
            dict_temp[tag] = hastag_count[tag]
    row_to_add = pd.DataFrame(dict_temp, index=[index])
    hastag_representation.update(row_to_add)
#print(tabulate(hastag_representation.loc[0].to_frame(), headers='keys', tablefmt='psql', floatfmt=".6f"))

hastag_representation = hastag_representation.fillna(0)
hastag_similarity = cos_sim_matrix(hastag_representation, hastag_representation)
#print(tabulate(hastag_similarity, headers='keys', tablefmt='psql', floatfmt=".6f"))


# --------------------06. 해당 표를 기반으로 사용자가 좋아요/북마크 게시글과 유사도 판별--------------------
# bookmarkd와 like DB에서 record_id 가져오기
user_id = 3;
sql = 'select user_id, record_id from bookmark where user_id = %s'
cur.execute(sql, (user_id))
bookmark_df = pd.DataFrame(cur.fetchall(), columns=['user_id', 'record_id'])
#print(tabulate(bookmark_df, headers='keys', tablefmt='psql', showindex=False))

sql = 'select user_id, record_id from likes where user_id = %s'
cur.execute(sql, (user_id))
like_df = pd.DataFrame(cur.fetchall(), columns=['user_id', 'record_id'])
#print(tabulate(like_df, headers='keys', tablefmt='psql', showindex=False))

# 두 데이터프레임을 합치기
user_df = pd.concat([bookmark_df, like_df])
user_df.drop_duplicates(inplace=True)
#print(tabulate(user_df, headers='keys', tablefmt='psql', showindex=False))


#--------------------07. 유사도가 높은 것을 기준으로 추천 리스트 생성--------------------
user_records = list(user_df['record_id'] - 1)
user_similarities = hastag_similarity.loc[user_records].sort_index()
for idx in user_similarities.index:  # 각 행에 대해 반복
    col = idx[0]  # 행의 인덱스 값
    user_similarities.loc[idx, col] = 0  # 인덱스 값과 같은 열에 해당하는 값을 0으로 설정
#print(tabulate(user_similarities, headers='keys', tablefmt='psql', floatfmt=".3f"))

recommended = []
while len(recommended) < 5:
    top_similarities = user_similarities.stack().nlargest(6)  # 유사도가 가장 높은 6개 항목을 추출 (자기 자신 제외)
    top_similarities = top_similarities[~top_similarities.index.isin(recommended)]  # 추천 목록에 이미 존재하는 항목 제외
    recommended.extend(top_similarities.index.get_level_values(1).tolist())  # 추천 목록에 추가
    user_similarities.loc[top_similarities.index] = 0  # 추천 목록에 있는 게시글과 유사도를 0으로 만들어서 중복 추천 방지

print(recommended)
import os
import re
import pandas as pd
import numpy as np
import pprint

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


#--------------------Data경로 > 이후에 database와 연동해야 함--------------------
path = '/Users/seoyoung/Desktop/DateBuzz/Recommendation/data'

#--------------------01. Data가져오기--------------------
#static_tag가 담긴 파일로 csv파일을 읽어옴
static_tag_df = pd.read_csv(os.path.join(path, 'mood.csv'), encoding='utf-8')

#--------------------02. Data전처리--------------------
#mood, activity 두 가지로 열로 나눠져있는 것을 하나의 열로 통합
static_tag_df['mood_activity'] = static_tag_df.apply(lambda x: ', '.join([x['mood'], x['activity']]), axis=1)
#하나로 통합한 열을 하나 만들었으므로 기존의 열은 삭제
static_tag_df = static_tag_df.drop(['mood', 'activity'], axis=1)
#한글이 아닌 문자들이 들어가있는 경우 전처리
static_tag_df['mood_activity'] = static_tag_df['mood_activity'].apply(lambda x: re.sub('[^가-힣\s]', '', x))
#print (tabulate(static_tag_df, headers='keys', tablefmt='psql', showindex=False))


#----------------03. 게시글에 사용된 태그 목록 생성--------------------
total_count = len(static_tag_df)
static_tag_list = []
for tags in static_tag_df['mood_activity']:
    static_tag_list += tags.split(' ')
    static_tag_list = list(set(static_tag_list))
static_tag_list = remove_similar_item(static_tag_list)
#print(static_tag_list)


#--------------------04. 단어의 가중치 파악--------------------
tag_count = dict.fromkeys(static_tag_list)
for each_static_tag in static_tag_df['mood_activity']:
    for tag in each_static_tag.split(' '):
        tag = tag.strip()
        if tag in tag_count:
            if tag_count[tag] == None:
                tag_count[tag] = 1
            else:
                tag_count[tag] = tag_count[tag]+1 
        else:
            continue
#print_dictionary(tag_count)
for each_static_tag in tag_count:
    tag_count[each_static_tag] = np.log10(total_count/tag_count[each_static_tag])


#--------------------05. 단어의 가중치를 반영하여 게시글 표현--------------------
#태그 가중치를 반영한 태그 representation
static_tag_representation = pd.DataFrame(columns=sorted(static_tag_list), index=static_tag_df.index)
#print(static_tag_representation)
for index, each_row in tqdm(static_tag_df.iterrows(), disable=True):
    dict_temp = {} 
    #static_tag_df에 있는 행들을 순회하면서 공백을 기준으로 모든 태그를 탐색함
    for tag in each_row['mood_activity'].split(' '):
        #tag리스트에 있는 tag의 목록인지 확인 후 dict_temp에 담아둠
        if tag in tag_count:
            dict_temp[tag] = tag_count[tag]
    row_to_add = pd.DataFrame(dict_temp, index=[index])
    #print (tabulate(row_to_add, headers='keys', tablefmt='psql'))
    static_tag_representation.update(row_to_add)
#print(tabulate(static_tag_representation.loc[0].to_frame(), headers='keys', tablefmt='psql', floatfmt=".6f"))

#--------------------06. 해당 표를 기반으로 사용자가 좋아요/북마크 게시글과 유사도 판별--------------------
static_tag_representation = static_tag_representation.fillna(0)
#print(tabulate(static_tag_representation.loc[0].to_frame(), headers='keys', tablefmt='psql', floatfmt=".6f"))

cs_df = cos_sim_matrix(static_tag_representation, static_tag_representation)
#print(cs_df.head())
print(cs_df[0].sort_values(ascending=False))

#--------------------07. 유사도가 높은 것을 기준으로 추천 리스트 생성--------------------
import os
import re
import pandas as pd
import numpy as np

from tqdm import tqdm

from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error

path = '/Users/seoyoung/Desktop/DateBuzz/Recommendation/data'

#mood_activity가 담긴 파일로 dataframe생성
mood_activity_df = pd.read_csv(os.path.join(path, 'mood.csv'), encoding='utf-8')
#두 가지로 열로 나눠져있는 것을 하나로 통합
mood_activity_df['mood_activity'] = mood_activity_df.apply(lambda x: ', '.join([x['mood'], x['activity']]), axis=1)
#하나로 통합한 열을 하나 만들었으므로 기존의 열은 삭제
mood_activity_df = mood_activity_df.drop(['mood', 'activity'], axis=1)
#한글이 아닌 문자들이 들어가있는 경우 전처리
mood_activity_df['mood_activity'] = mood_activity_df['mood_activity'].apply(lambda x: re.sub('[^가-힣\s]', '', x))
#print(mood_activity_df)

#게시글 개수와 게시글에 사용된 태그를 담은 리스트 생성
total_count = len(mood_activity_df)
tag_list = []
for tags in mood_activity_df['mood_activity']:
    print(tags)
    tag_list += tags.split(' ')
    tag_list = list(set(tag_list))

# 단어의 가중치를 파악하기 위해 모든 문서에서 단어가 몇 번 사용되었는지 확인
tag_count = dict.fromkeys(tag_list)
#print(tag_count)

for each_tag_list in mood_activity_df['mood_activity']:
    for tag in each_tag_list.split(' '):
        tag = tag.strip()
        if tag_count[tag] == None:
            tag_count[tag] = 1
        else:
            tag_count[tag] = tag_count[tag]+1 

#빈도수를 바탕으로 단어의 가중치 계산
for each_tag in tag_count:
    tag_count[each_tag] = np.log10(total_count/tag_count[each_tag])


# IDF를 바탕으로한 게시물 representation 생성
tag_representation = pd.DataFrame(columns=sorted(tag_list), index=mood_activity_df.index)

# 1번 영화가 속한 장르에 대해 가중치를 가져옴. 가중치를 부여함.
# 본인이 해당되는 장르에 가중치가 부여됨.
# 1번의 경우 Adventure는 그렇게 중요한 feature가 아니고, Animation이 중요한 feature임
print('hello')
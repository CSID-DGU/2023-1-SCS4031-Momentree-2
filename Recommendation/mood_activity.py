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
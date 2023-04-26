import os
import pandas as pd
import numpy as np

from tqdm import tqdm

from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error

path = '/Users/seoyoung/Desktop/DateBuzz/Recommendation/data'
print(path)

mood_activity_df = (pd.read_csv(os.path.join(path, 'mood.csv'), encoding='utf-8'))
mood_activity_df['mood_activity'] = mood_activity_df.apply(lambda x: ', '.join([x['mood'], x['activity']]), axis=1)
mood_activity_df = mood_activity_df.drop(['mood', 'activity'], axis=1)

# 출력
print(mood_activity_df.to_string(index=False, justify = 'left'))

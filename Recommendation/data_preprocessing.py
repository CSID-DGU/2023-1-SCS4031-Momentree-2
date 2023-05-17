import re
import pandas as pd

def preprocess_data(prototypeData):
    hashtag_df = prototypeData[['course_id', 'tag_name']]
    hashtag_df = hashtag_df.groupby('course_id')['tag_name'].apply(lambda x: ' '.join(x)).reset_index()

    hashtag_df['tag_name'] = hashtag_df['tag_name'].apply(lambda x: re.sub('[^가-힣\s]', '', x))
    return hashtag_df
import numpy as np
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
from tqdm import tqdm

from utils import cos_sim_matrix, print_dictionary, remove_similar_item

def create_hashtag_list(hashtag_df):
    num_of_course = len(hashtag_df)
    hashtag_list = []
    for tags in hashtag_df['tag_name']:
        hashtag_list += tags.split(' ')
        hashtag_list = list(set(hashtag_list))
        hashtag_list = remove_similar_item(hashtag_list)
    return hashtag_list

def calculate_hashtag_weight(hashtag_df, hashtag_list):
    num_of_course = len(hashtag_df)
    hashtag_weighting = dict.fromkeys(hashtag_list)
    for each_hashtag in hashtag_df['tag_name']:
        for tag in each_hashtag.split(' '):
            tag = tag.strip()
            if tag in hashtag_weighting:
                if hashtag_weighting[tag] == None:
                    hashtag_weighting[tag] = 1
                else:
                    hashtag_weighting[tag] = hashtag_weighting[tag]+1 
            else:
                continue

    for each_hashtag in hashtag_weighting:
        hashtag_weighting[each_hashtag] = np.log10(num_of_course/hashtag_weighting[each_hashtag])
    return hashtag_weighting


def create_hashtag_representation(hashtag_df, hashtag_list, hashtag_weighting):
    hashtag_representation = pd.DataFrame(columns=(hashtag_list), index=hashtag_df.index)

    for index, each_row in tqdm(hashtag_df.iterrows(), disable=True):
        dict_temp = {} 
        for tag in each_row['tag_name'].split(' '):
            if tag in hashtag_weighting:
                dict_temp[tag] = hashtag_weighting[tag]
        row_to_add = pd.DataFrame(dict_temp, index=[index])
        hashtag_representation.update(row_to_add)

    hashtag_representation = hashtag_representation.fillna(0)
    return hashtag_representation

def create_hashtag_similarity(hashtag_representation):
    hashtag_similarity = cos_sim_matrix(hashtag_representation, hashtag_representation)
    return hashtag_similarity

def get_user_dataframes(bookmark_df, like_df):
    user_df = pd.concat([bookmark_df, like_df])
    user_df.drop_duplicates(inplace=True)
    return user_df

def recommend_items(user_df, hashtag_similarity, user_records, n_recommendations=5):
    recommended = []
    user_similarities = hashtag_similarity.loc[user_records].sort_index()
    for idx in user_similarities.index:
        col = idx[0]
        user_similarities.loc[idx, col] = 0

    user_similarities = user_similarities.stack()

    while len(recommended) < n_recommendations:
        top_similarities = user_similarities.nlargest(1)
        index1 = top_similarities.index[0][0]
        index2 = top_similarities.index[0][1]
        if index2 not in recommended and index2 not in user_records:
            recommended.append(index2)
        user_similarities[index1][index2] = 0

    return recommended
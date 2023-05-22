import pandas as pd
import sys

from db_connector import connect_to_db, fetch_all_data_from_table, fetch_data_for_user
from data_preprocessing import preprocess_data
from hashtag_analysis import create_hashtag_list, calculate_hashtag_weight, create_hashtag_representation, create_hashtag_similarity, get_user_dataframes, recommend_items


def main(user_id):
    cur = connect_to_db()
    res = fetch_all_data_from_table(cur, 'hashtag')
    prototypeData = pd.DataFrame.from_records(
        res, columns=[desc[0] for desc in cur.description])

    hashtag_df = preprocess_data(prototypeData)

    hashtag_list = create_hashtag_list(hashtag_df)
    hashtag_weighting = calculate_hashtag_weight(hashtag_df, hashtag_list)
    hashtag_representation = create_hashtag_representation(
        hashtag_df, hashtag_list, hashtag_weighting)
    hashtag_similarity = create_hashtag_similarity(hashtag_representation)

    bookmark_df = fetch_data_for_user(cur, 'bookmark', user_id)
    like_df = fetch_data_for_user(cur, 'likes', user_id)

    user_df = get_user_dataframes(bookmark_df, like_df)
    user_records = list(user_df['record_id'])

    recommended = recommend_items(
        hashtag_similarity, user_records, n_recommendations=5)

    return recommended


if __name__ == "__main__":
    user_id = sys.argv[1]
    recommended = main(user_id)
    print(recommended)

import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity

def cos_sim_matrix(a, b):
    cos_sin = cosine_similarity(a, b)
    result_df = pd.DataFrame(data=cos_sin, index=[a.index])
    return result_df

def print_dictionary(dic_name):
    for key, value in dic_name.items():
        print(f"{key}: {value}")

def remove_similar_item(lst):
    new_list = []
    for i in range(len(lst)):
        if i == 0:
            new_list.append(lst[i])
        else:
            for j in range(len(new_list)):
                if new_list[j][:1] == lst[i][:1]:
                    break
            else:
                new_list.append(lst[i])
    return new_list
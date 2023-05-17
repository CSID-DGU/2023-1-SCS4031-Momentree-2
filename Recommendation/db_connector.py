import pymysql
import pandas as pd

def connect_to_db():
    conn = pymysql.connect(host='127.0.0.1', user='root', password='1234', db='pythonDB', charset='utf8')
    return conn.cursor()

def fetch_all_data_from_table(cursor, table_name):
    sql = f"select * from {table_name}"
    cursor.execute(sql)
    res = cursor.fetchall()
    return res

def fetch_data_for_user(cursor, table_name, user_id):
    sql = f"select user_id, record_id from {table_name} where user_id = %s"
    cursor.execute(sql, (user_id))
    res = pd.DataFrame(cursor.fetchall(), columns=['user_id', 'record_id'])
    return res
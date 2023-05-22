import pymysql
import pandas as pd
from dotenv import load_dotenv
import os

load_dotenv()

def connect_to_db():
    conn = pymysql.connect(
        host=os.getenv('DB_HOST'), 
        user=os.getenv('DB_USER'), 
        password=os.getenv('DB_PASSWORD'), 
        port=int(os.getenv('DB_PORT')), 
        db=os.getenv('DB_NAME'), 
        charset='utf8')
    cur = conn.cursor()
    return cur

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
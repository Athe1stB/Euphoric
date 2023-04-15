from uri2features import uri2features
import pandas as pd
from tqdm import tqdm
import re


def main():
    data_path = "data/raw_data.csv"
    df = pd.read_csv(data_path)
    df['track_uri'] = df['track_uri'].apply(lambda x: re.findall(r'\w+$', x)[0])
    track_uri_list = [uri for uri in df['track_uri'].unique()]
    feature_list = []
    for i in tqdm(track_uri_list):
        try:
            feature_list.append(uri2features(i))
        except:
            continue
    feature_df = pd.DataFrame(feature_list)
    new_df = pd.merge(df, feature_df, left_on='track_uri', right_on='id')
    new_df.to_csv("processed_data.csv")


if __name__ == '__main__':
    main()
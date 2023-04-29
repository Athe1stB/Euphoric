import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
import warnings
warnings.filterwarnings("ignore")


def playlist_preprocess(df):
    def drop_duplicates(df):
        df['artists_song'] = df.apply(lambda row: row['artist_name'] + row['track_name'], axis=1)
        return df.drop_duplicates('artists_song')
    
    def select_cols(df):
        cols = [
            'artist_name', 'id', 'track_name', 'danceability', 'energy', 'key', 'loudness', 'mode', 'speechiness', 
            'acousticness', 'instrumentalness', 'liveness', 'valence', 'tempo', 'artist_pop', 'genres', 'track_pop']
        return df[cols]

    def genre_preprocess(df):
        df['genres_list'] = df['genres'].apply(lambda x: x.split(' '))
        return df

    df = drop_duplicates(df)
    df = select_cols(df)
    df = genre_preprocess(df)
    return df
    

def generate_playlist_feature(complete_feature_set, playlist_df):
    # Find song features in the playlist
    complete_feature_set_playlist = complete_feature_set[complete_feature_set['id'].isin(playlist_df['id'].values)]
    # Find all non-playlist song features
    complete_feature_set_nonplaylist = complete_feature_set[~complete_feature_set['id'].isin(playlist_df['id'].values)]
    complete_feature_set_playlist_final = complete_feature_set_playlist.drop(columns='id')
    return complete_feature_set_playlist_final.sum(axis=0), complete_feature_set_nonplaylist


def generate_playlist_recos(df, features, nonplaylist_features, top=1):
    non_playlist_df = df[df['id'].isin(nonplaylist_features['id'].values)]
    # Find cosine similarity between the playlist and the complete song set
    non_playlist_df['sim'] = cosine_similarity(nonplaylist_features.drop('id', axis=1).values, features.values.reshape(1, -1))[:, 0]
    non_playlist_df_top = non_playlist_df.sort_values('sim', ascending=False).head(top)
    return non_playlist_df_top
    

def main():
    complete_feature_set = pd.read_csv("data/complete_feature.csv")
    song_df = pd.read_csv("data/all_song_data.csv")
    playlist_test = pd.read_csv("data/test_playlist.csv")
    print(playlist_test.head())
    playlist_test = playlist_preprocess(playlist_test)
    # Generate the features
    complete_feature_set_playlist_vector, complete_feature_set_nonplaylist = generate_playlist_feature(complete_feature_set, playlist_test)
    # Genreate recommendation
    recommend = generate_playlist_recos(song_df, complete_feature_set_playlist_vector, complete_feature_set_nonplaylist)
    print(recommend.values)


if __name__ == '__main__':
    main()

import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
import re 


def uri2features(uri):
    with open("secret.txt") as f:
        secret_ls = f.readlines()
        cid = secret_ls[0][:-1]
        secret = secret_ls[1][:-1]
    client_credentials_manager = SpotifyClientCredentials(client_id=cid, client_secret=secret)
    sp = spotipy.Spotify(client_credentials_manager=client_credentials_manager)

    features = sp.audio_features(uri)[0]
    artist = sp.track(uri)["artists"][0]["id"]
    artist_pop = sp.artist(artist)["popularity"]
    artist_genres = sp.artist(artist)["genres"]
    track_pop = sp.track(uri)["popularity"]
    features["artist_pop"] = artist_pop
    if artist_genres:
        features["genres"] = ' '.join([re.sub(' ', '_', i) for i in artist_genres])
    else:
        features["genres"] = "unknown"
    features["track_pop"] = track_pop
    return features


if __name__ == '__main__':
    sample_uri = "1o0nAjgZwMDK9TI4TTUSNn"
    result = uri2features(sample_uri)
    print(result)

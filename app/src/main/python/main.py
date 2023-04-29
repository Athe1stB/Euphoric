from fastapi import FastAPI
from pydantic import BaseModel
import cv2
import numpy as np
import urllib
import random
import torch
from facenet_pytorch.models.mtcnn import MTCNN
from deepface import DeepFace as emotion_model
from recommend import *


class URLImage(BaseModel):
    url_image: str


class URIList(BaseModel):
    uri_list: list
    
    
app = FastAPI(
    title="Emosic API",
    docs_url="/",
    description="Music Recommendation System"
)


complete_feature_set = pd.read_csv("data/complete_feature.csv")
song_df = pd.read_csv("data/all_song_data.csv")
processed_df = pd.read_csv("data/processed_data.csv")
test_df = pd.read_csv("data/test_playlist.csv")
processed_df = processed_df.drop(columns=["Unnamed: 0"], axis=1)
dic = {val[22]: val for val in processed_df.values}
dic_vals = list(dic.values())
emotion_mapping = {
    'angry': 'negative', 'fear': 'negative', 'disgust': 'negative', 'sad': 'negative', 
    'happy': 'positive', 'surprise': 'positive', 'neutral': 'neutral'
}
song_mapping = {
    'positive': ['pop', 'energetic', 'edm', 'party', 'dance', 'hip_hop', 'rock'],
    'negative': ['country', 'calm', 'focus', 'chill', 'slow'],
}


def mtcnn_detect(img: np.ndarray):
    device = torch.device('cuda:0' if torch.cuda.is_available() else 'cpu')
    mtcnn = MTCNN(keep_all=True, device=device)
    boxes, probs = mtcnn.detect(img)
    try:
        for box in boxes:
            x_left  = int(min(box[0], box[2]))
            x_right = int(max(box[0], box[2]))
            y_left  = int(min(box[1], box[3]))
            y_right = int(max(box[1], box[3]))
            img_cropped = img[x_left:x_right, y_left:y_right]
            img = cv2.rectangle(img, (x_left, y_left), (x_right, y_right), (255, 0, 0), 2)
        return img
    except:
        pass



def fetch_tracks(emotion, count):
    dic_vals_copy = dic_vals.copy()
    random.shuffle(dic_vals_copy)
    tracks_found = []
    for dic_val in dic_vals_copy:
        genre_set = dic_val[-2].split(' ')
        for genre in genre_set:
            for keyword in song_mapping[emotion_mapping[emotion]]:
                keyword_ = '_' + keyword + '_'
                if genre.startswith(keyword_[1:]) or genre.endswith(keyword_[:-1]) or keyword_ in genre:
                    tracks_found.append(dic_val)
                    if len(tracks_found) == count:
                        return tracks_found
                    break
            else:
                continue
            break
                        

def jsonify(recom_lists): 
    kv_pairs = (
        ("id", 3), ("track_name", 5), ("track_uri", 23), ("artist_name", 2), ("genres", -2),
        ("artist_uri", 4),("album_name", 8), ("album_uri", 6), ("duration_ms", 7)
    )
    recommendation_dict = {"items": []}
    for recom_list in recom_lists:
        recom_item = dict()
        for key, val in kv_pairs:
            if key == 'genres':
                genre_str = recom_list[val]
                genre_list = list(map(lambda s: ' '.join(s.title().split('_')), genre_str.split(' ')))
                recom_item[key] = genre_list[:5]
            elif key == 'duration_ms':
                recom_item[key] = str(recom_list[val])
            else:
                recom_item[key] = recom_list[val]
        recommendation_dict["items"].append(recom_item)
    return recommendation_dict


@app.post("/recommend1")
def emotion_based(url_image: URLImage):
    req = urllib.request.urlopen(url_image.url_image)
    arr = np.asarray(bytearray(req.read()), dtype=np.uint8)
    img = cv2.imdecode(arr, -1) 
    faces = mtcnn_detect(img)
    if faces is None:
        return None
    else:
        obj = emotion_model.analyze(img_path=img, actions=['emotion'])
        emotion = obj['dominant_emotion']
        if emotion_mapping[emotion] == 'neutral':
            tracks_found = random.choices(dic_vals, k=10)
        else:
            tracks_found = fetch_tracks(emotion, 10)
        recommendation_dict = jsonify(tracks_found)
        recommendation_dict["emotion"] = emotion
        recommendation_dict["mood"] = emotion_mapping[emotion]
        return recommendation_dict
    
    
@app.post("/recommend2")
def content_based(track_uri_list: URIList):
    track_uri_list = track_uri_list.uri_list
    data = [dic[track_uri] for track_uri in track_uri_list]
    df = pd.DataFrame(data, columns=processed_df.columns)   
    df = playlist_preprocess(df)
    playlist_vector, nonplaylist_vector = generate_playlist_feature(complete_feature_set, df)
    recommendation = generate_playlist_recos(song_df, playlist_vector, nonplaylist_vector, 50)
    tracks_found = [dic[recommendation["id"].values[i]] for i in  range(10)]
    recommendation_dict = jsonify(tracks_found)
    return recommendation_dict

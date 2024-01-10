import spotipy
import os
import json
from spotipy import SpotifyClientCredentials
import sys

CLIENT_ID = "f2d21739649a48418aa02984400f7345"
CLIENT_SECRET = "355dcfc9109a486b9d8031770a519f5c"

spot = spotipy.Spotify(
    client_credentials_manager=SpotifyClientCredentials(client_id=CLIENT_ID, client_secret=CLIENT_SECRET))


def find_songs(song_name: str):
    results = spot.search(q=song_name, type="track")
    tracks = results['tracks']['items']
    for track in tracks:
        print("Track name:", track['name'])
        print("Album name:", track['album']['name'])
        print("Artist name:", track['artists'][0]['name'])
        print("Album image:", track['album']['images'][0]['url'])
        print("Artist link:", track['artists'][0]['external_urls']['spotify'])


find_songs(sys.argv[1])

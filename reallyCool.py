from flask import Flask
import pusher
import time
import json
import requests

pusher_client = pusher.Pusher(
	app_id='198468',
	key='7cf17138b96636bf08be',
	secret='956eb99e339f31a3c3f9',
	ssl=True
	)

app = Flask(__name__)
if __name__== '__main__':
    app.run()

@app.route('/recognise')
def doit():
    return makeShazamRequest()

def makeShazamRequest():
    headers = {
    'X-Shazam-Api-Key': '03789B8E-A8CE-4229-A880-7FDE4C4FAEFC',
    'Content-Type': 'application/octet-stream',
    }

    data = open('ex3.wav', 'rb').read()
    json_obj = json.loads(requests.post('http://beta.amp.shazam.com/partner/recognise', headers=headers, data=data).text)
    if json_obj['matches'] != []:
        artist = json_obj['matches'][0]['metadata']['artist']
        title = json_obj['matches'][0]['metadata']['title']
        return artist + " - " + title
    else :
        return "No match found"

#@app.route('/test', methods=['GET', 'POST'])
#def test():
#    pusher_client.trigger('test_channel','my_event',{'message':'hello world'})
#    return "WORKS"

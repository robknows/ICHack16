import json
import requests

from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt

# Create your views here.

ELASTIC_URL = (
    "https://search-getridofyoshit-mwboftivifwhy2cwtkdqnsc2fy."
    "eu-west-1.es.amazonaws.com/"
)
INDEX_TYPE = "test4/stuff/"

@csrf_exempt
def add(req):
    res = requests.post(
        ELASTIC_URL + INDEX_TYPE,
        data=req.body.decode("utf-8")
    )

    return HttpResponse(res.text)


@csrf_exempt
def search(req):
    location = json.loads(req.body.decode("utf-8"))["location"]

    query = {
        "query": {
            "filtered": {
                "query": {
                    "match_all": {}
                },
                "filter": {
                    "geo_distance": {
                        "distance": "20km",
                        "location": location
                    }
                }
            }
        }
    }

    res = requests.post(
        ELASTIC_URL + INDEX_TYPE + "_search?pretty=true",
        data=json.dumps(query),
    )

    return HttpResponse(res.text)

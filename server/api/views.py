import requests

from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt

# Create your views here.

ELASTIC_URL = (
    "https://search-getridofyoshit-mwboftivifwhy2cwtkdqnsc2fy."
    "eu-west-1.es.amazonaws.com/"
)
INDEX_TYPE = "test/stuff/"


@csrf_exempt
def add(req):
    res = requests.post(
        ELASTIC_URL + INDEX_TYPE,
        data=req.body.decode("utf-8")
    )

    return HttpResponse(res.text)


@csrf_exempt
def search(req):
    return HttpResponse("This is a search request")

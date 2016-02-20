import json
import requests

from django.http import HttpResponse

# Create your views here.

ELASTIC_URL = "https://search-getridofyoshit-mwboftivifwhy2cwtkdqnsc2fy.\
        eu-west-1.es.amazonaws.com/"
INDEX_TYPE = "freestuff/stuff/"


def add(req):
    return HttpResponse("This is an add request")


def search(req):
    return HttpResponse("This is a search request")

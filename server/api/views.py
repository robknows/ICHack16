import json

from django.http import HttpResponse

# Create your views here.


def add(req):
    return HttpResponse("This is an add request")


def search(req):
    return HttpResponse("This is a search request")

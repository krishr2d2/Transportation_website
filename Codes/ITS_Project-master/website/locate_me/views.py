from django.shortcuts import HttpResponse
from Transport.models import *
import pyrebase
config = {
    "apiKey": "AIzaSyAQHHnCqr-3FajALfmpg19PS44fsB6XkBA",
    "authDomain": "itsystems-162415.firebaseapp.com",
    "databaseURL": "https://itsystems-162415.firebaseio.com",
    "storageBucket" : "itsystems-162415.appspot.com"
  };
firebase = pyrebase.initialize_app(config);
db = firebase.database()
def index(request,vehi,lat,lon):
    try:
        lat = float(lat)
        lon = float(lon)
        if (lat/1000 != 1.0 or lon/1000 != 1.0):
            db.child("My_user").child("Driver").child(vehi).update({"latitude":lat, "longitude":lon})
            return HttpResponse("<h3>"+vehi+" @</br>lat: "+str(lat)+"</br>lon: "+str(lon)+"</h3>")
        else :
            return HttpResponse("<h3>The given lat/lon are inappropriate (1000's)...</h3>")
    except:
        return HttpResponse("The given Reg-Number DoesNotExist...")


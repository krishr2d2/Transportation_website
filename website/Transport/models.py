from __future__ import unicode_literals
from django.contrib.auth.models import Permission, User
from django.db import models
from datetime import datetime
#from geoposition.fields import GeopositionField

# Create your models here.
class Statelist(models.Model):  
    status_name = models.CharField(max_length=10)

class Booking(models.Model):
    startpoint = models.CharField(max_length=250)
    destination = models.CharField(max_length=200)
    date=models.DateTimeField('date of booking')
    status=models.ForeignKey(Statelist,on_delete=models.CASCADE)

class vehicle(models.Model):
        vehicle_name = models.CharField(max_length=200)
        vehicle_reg_num = models.CharField(max_length=200)
        vehicle_type = models.CharField(max_length=200)
        #def __str__(self):
        def __unicode__(self):
            return '---'+self.vehicle_name

class vehicle_live(models.Model):
        vehicle = models.ForeignKey(vehicle, on_delete=models.CASCADE)
        lon_pos = models.FloatField()
        lat_pos = models.FloatField()
        time_stamp = models.DateTimeField(default=datetime.now,blank=True)
        #def __str__(self):
        def __unicode__(self):
            return "{0}--@--{1}".format(self.vehicle.vehicle_name,self.time_stamp)
#class drvier_details(models.Model):
#        driver_name = models.CharField(max_length=250)
#        driver_phone = models.IntegerField()
#        driver_address = models.CharField(max_length=400)
#        driver_age = models.IntegerField()

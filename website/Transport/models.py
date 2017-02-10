from __future__ import unicode_literals
from django.contrib.auth.models import Permission, User
from django.db import models
from datetime import datetime
#from geoposition.fields import GeopositionField

# Create your models here.

class My_user(models.Model):
    GENDER_CHOICES = (
        ('M', 'Male'),
        ('F', 'Female'),
    )
    gender = models.CharField(max_length=1, choices=GENDER_CHOICES)
    user_name = models.CharField(max_length=250)
    user_phone = models.IntegerField()
    user_addr = models.CharField(max_length=250)
    user_email = models.CharField(max_length=250)
    #user_gender = models.CharField(max_length=1,choices = GENDER_CHOICES, default='M',null = True)
    def __unicode__(self):
        return self.user_name

class driver(models.Model):
    driver_name = models.CharField(max_length=250)
    driver_phone = models.IntegerField()
    driver_address = models.CharField(max_length=400)
    driver_age = models.IntegerField()

    vehicle_type = models.CharField(max_length=200)
    vehicle_reg_num = models.CharField(max_length=200)
    
    #temp_obj =  vehicle_live()
    #temp_obj.vehicle = self
    #temp_obj.lon_pos = 13.5481095
    #temp_obj.lat_pos = 80.0091568 

    def __unicode__(self):
        return '_-|-_'+self.driver_name

#class valid_stops(self):
#    avail_stops = models.CharField(max_length=250)
#    def __unicode__(self):
#        return self.stop

class Bookings(models.Model):
    startpoint = models.CharField(max_length=250)
    destination = models.CharField(max_length=200)
    booked_date=models.DateTimeField()
    user = models.ForeignKey(My_user, on_delete=models.CASCADE)
    booked_driver = models.ForeignKey(driver, default = None, on_delete=models.CASCADE)
    #user_lat = models.FloatField()
    #user_lon = models.FloatField()
    booking_status = models.CharField(max_length=50,default='pending')

#class vehicle(models.Model):
#        vehicle_name = models.CharField(max_length=200)
#        vehicle_reg_num = models.CharField(max_length=200)
#        vehicle_type = models.CharField(max_length=200)
#        #def __str__(self):
#        def __unicode__(self):
#            return '---'+self.vehicle_name

class vehicle_live(models.Model):
    vehicle = models.ForeignKey(driver, on_delete=models.CASCADE)
    lon_pos = models.FloatField()
    lat_pos = models.FloatField()
    time_stamp = models.DateTimeField(default=datetime.now,blank=True)
    #def __str __(self):
    def __unicode__(self):
        return "{0}--@--{1}".format(self.vehicle.driver_name,self.time_stamp)


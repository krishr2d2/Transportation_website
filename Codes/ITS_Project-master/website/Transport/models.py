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
    My_user_gender      = models.CharField(max_length=1, choices=GENDER_CHOICES,default=None)
    My_user_name        = models.CharField(max_length=250,default=None)
    My_user_phone       = models.IntegerField(default=None)
    My_user_addr        = models.CharField(max_length=250,default=None)
    My_user_email       = models.CharField(max_length=250,default=None)
    My_user_dateOfBirth = models.DateField(default=None,null=True)#To know the age of user
    def __str__(self):
        return '#'+self.My_user_name

class Vehicle(models.Model):#Stores information about vehicles
    Vehicle_name      = models.CharField(max_length=200,default=None)
    Vehicle_latitude  = models.FloatField(default=None)#latitude of vehicle
    Vehicle_longitude = models.FloatField(default=None)#longitude of vehicle
    Vehicle_rc        = models.CharField(max_length=200,default=None)#rc of the vehicle
    Vehicle_active    = models.BooleanField(default=None)#status of vehicle
    def __str__(self):
        return '@'+self.Vehicle_rc

class Driver(models.Model):#stores all driver details
    Driver_My_user        = models.ForeignKey(My_user,on_delete=models.CASCADE, default=0,blank=True)#to get all details from My user
    Driver_currentVehicle = models.ForeignKey(Vehicle,on_delete=models.CASCADE, default=0)
    Driver_liscence       = models.CharField(max_length=20,default=None)
    Driver_longitude      = models.FloatField(default=None,null=True)#longitude of driver
    Driver_latitude       = models.FloatField(default=None,null=True)#latitude  of driver
    #driver_currentVehicle gets current detail of vehicle driver
    def __str__(self):
        return '!'+self.Driver_My_user.My_user_name

class Passenger(models.Model):#stores all passangers details
    Passenger_My_user   = models.ForeignKey(My_user,on_delete=models.CASCADE, default=0)#to get all details from My user
    Passenger_latitude  = models.FloatField(default=None)#current latitude of passenger
    Passenger_longitude = models.FloatField(default=None)#current longitude of passenger
    Passenger_active    = models.BooleanField(default=None)#
    def __str__(self):
        return '$'+self.Passenger_My_user.My_user_name

class Valid_stop(models.Model):#This model stores all the valid stops
    Valid_stop_name      = models.CharField(max_length=50,default=None)#name of the stops
    Valid_stop_latitude  = models.FloatField(default=None)#latitude of valid stops
    Valid_stop_longitude = models.FloatField(default=None)#longitude of valid stops
    def __str__(self):
        return '%'+self.Valid_stop_name

class Booking(models.Model):#Stores all the information about bookings
    #Booking_startpoint  = models.ForeignKey(Valid_stop,default=0,on_delete=models.CASCADE,related_name='%(class)s_starting_point')#starting point of booking
    #Booking_destination = models.ForeignKey(Valid_stop, default=0,on_delete=models.CASCADE,related_name='%(class)s_destination_point')#ending point of booking
    Booking_startpoint  = models.CharField(max_length = 50,default=None)#starting point of booking
    Booking_destination = models.CharField(max_length = 50,default=None)#ending point of booking
    Booking_date        = models.DateTimeField(auto_now_add=True)
    Booking_passenger   = models.ForeignKey(Passenger, default=0,on_delete=models.CASCADE)#passanger
    Booking_vehicle     = models.ForeignKey(Vehicle,null=True,on_delete=models.CASCADE,blank=True)#vehicle booked
    Booking_status      = models.CharField(max_length=50,default='Pending')
    #booking_driver      = models.ForeignKey(driver,on_delete=models.CASCADE)#driver of the vehicle at the time of booking
    def __str__(self):
        return '^'+str(self.Booking_date)

class updatedriver(models.Model):
    drivername = models.CharField(max_length =100,default = None)
    driveremail = models.CharField(max_length =100,default = None)
    driveraddr = models.CharField(max_length =100,default = None)
    driverphone = models.CharField(max_length =100,default = None)



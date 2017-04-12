from django.http import HttpResponse
from django.shortcuts import render,redirect
from django.contrib.auth import authenticate, login
from django.contrib.auth import logout
from django.http import JsonResponse
from django.shortcuts import render, get_object_or_404
from django.contrib.auth.decorators import login_required
from django.db.models import Q
from Transport.models import *
from Transport.forms import *
from django import forms
from django.http import HttpResponseRedirect
from django.core.urlresolvers import reverse
import pyrebase
config = {
    "apiKey": "AIzaSyAQHHnCqr-3FajALfmpg19PS44fsB6XkBA",
    "authDomain": "itsystems-162415.firebaseapp.com",
    "databaseURL": "https://itsystems-162415.firebaseio.com",
    "storageBucket" : "itsystems-162415.appspot.com"
  };
firebase = pyrebase.initialize_app(config);
db = firebase.database()

def register(request):
    form = UserForm(request.POST or None)
    if form.is_valid():
        user = form.save(commit=False)
        username = form.cleaned_data['username']
        password = form.cleaned_data['password']
        user.set_password(password)
        user.save()
        user = authenticate(username=username, password=password)
        if user is not None:
            if user.is_active:
                login(request, user)

                return render(request, 'Transport/index.html')
    context = {
        "form": form,
    }
    return render(request, 'Transport/register.html', context)

def login_user(request):
    if request.method == "POST":
        username = request.POST['username']
        password = request.POST['password']
        user = authenticate(username=username, password=password)
        if user is not None:
            if user.is_active:
                login(request, user)
                return render(request, 'Transport/index.html')
            else:
                return render(request, 'mylogin/login.html', {'error_message': 'Your account has been disabled'})
        else:
            return render(request, 'mylogin/login.html', {'error_message': 'Invalid login'})
    return render(request, 'Transport/login.html')


def logout_user(request):
    logout(request)
    form = UserForm(request.POST or None)
    context = {
        "form": form,
    }
    return render(request, 'Transport/login.html', context)


def new_login(request):
    return render(request,'Transport/loggyn.html',{})     

def index(request):

   if not request.user.is_authenticated():
    return render(request,'mylogin/login.html')

   else:
        fire_vehi = db.child("My_user/Driver/").get()
        fire_rider = db.child("My_user/Rider/").get()
        # drivers    = Driver.objects.all()  
        # vehicles   = Vehicle.objects.all()                       
        passengers  = [[p.key(),p.val()] for p in fire_rider.each()]
        passengers = filter(lambda a: a[1]!= None, passengers)
        context    = {
                    # "drivers"   :drivers,
                    # "passengers":passengers,
                    # "vehicles"  :vehicles,
                    "drivs" : [p.val() for p in fire_vehi.each()],
                    "riders"  : passengers

                 }
        context['drivs'] = filter(lambda a: a!= None, context['drivs'])
        # print passengers[0]
        return render(request,'Transport/index.html',context)

     
@login_required
def allpassengers(request):                                     #displays all passengers
    users = db.child("My_user/Rider").get()
    use = [[p.key(),p.val()] for p in users.each() if p.val() != None]
    context = {
    "users" : use
    }
    print use
    return render(request,"Transport/passengers.html",context)

@login_required
def alldrivers(request):                                        #display all drivers
    users = db.child("My_user/Driver/").get()
    context = {
    "users" : [p.val() for p in users.each()]
    }
    return render(request, "Transport/drivers.html",context)

@login_required
def allvehicles(request):
    vehicles = db.child("My_user/Driver/").get()
    # vehicles = Vehicle.objects.all()                            #gets all vehicle details
    #vehicles = Vehicle.objects.all().count();
    # print vehicles
    context  = {                    
        "vehicles":[p.val() for p in vehicles.each()],
    }
    return render(request,"Transport/vehicles.html",context)

@login_required
def driver_details(request,name_id):                            #Details of driver with name_id as id
    driver  = Driver.objects.get(id=name_id)
    context = {
    "driver" : driver,
    }
    return render(request,"Transport/driver.html",context)

@login_required
def vehicle_details(request,name_id):                           #Details of vehicle with id name_id
        #vehicle = Vehicle.objects.get(id=name_id)
        print name_id
        vehicle = db.child("My_user/Driver/"+str(name_id)).get()
        riders = db.child("My_user/Rider/").get()
        l = [[p.key(),p.val()] for p in riders.each()]
        l = filter(lambda a: a[1]!= None, l)
        books = []
        try:
            # bookings  = Booking.objects.filter(Booking_vehicle=vehicle)
            for j in l:
                if ('Booking' in j[1]):
                    for ids in j[1]['Booking']:
                        if ('vehicle' in j[1]['Booking'][ids]):
                            if (j[1]['Booking'][ids]['vehicle'] == name_id):
                                books.append([j[1]['name'],j[1]['Booking'][ids],j[0]])

        except:
            books  = [-1]
            # print '----for vehicles-->'+str(name_id)+'<-------'

        context = {
        #"vehi_driver":vehi_driver,
        "vehi":{p.key():p.val() for p in vehicle.each() if p.val() != None},
        "bookings":books,
        }
        # context['vehi'] = filter(lambda a: a!= None, context['vehi'])
        # print context['vehi']
        return render(request,"Transport/vehicle.html",context)

@login_required
def update_driver(request,name_id):
    if(request.method == "POST"):
        form = UpdateDriver(request.POST)
        if form.is_valid():
            drivername = form.cleaned_data['drivername']
            driveremail = form.cleaned_data['driveremail']
            driveraddr = form.cleaned_data['driveraddr']
            driverphone = form.cleaned_data['driverphone']

            db.child("My_user").child("Driver").child(name_id).update({"Driver_name":drivername, "Driver_email":driveremail,"Driver_address":driveraddr,"phone":driverphone})
            # print 'yes__soka'   

            return HttpResponseRedirect('/Transport/allvehicles')
    else:
        # print 
        form = UpdateDriver()
    return render(request, "Transport/update_driver.html",{'form':form,'name':name_id})

@login_required
def passenger_details(request,name_id):                         #Details of passenger with id name_id

        users = db.child("My_user/Rider/"+str(name_id)).get()
        l = {p.key():p.val() for p in users.each() if p.val() != None}
        books = []
        try:
            for j in l:
                if (j=='Booking'):
                    for ids in l['Booking']:
                        books.append(l['Booking'][ids])
            # bookings  = Booking.objects.filter(Booking_passenger=passenger)
            print books
        except:
            books  = []
            print books
            # print '----->'+str(name_id)+'<-------'

        context = {
        "users" : l,
        "bookings": books
        }

        return render(request,"Transport/passenger.html",context)

@login_required
def aboutus(request):
    return render(request,"Transport/aboutus.html",{})



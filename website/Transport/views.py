from django.http import HttpResponse
from django.shortcuts import render
from django.contrib.auth import authenticate, login
from django.contrib.auth import logout
from django.http import JsonResponse
from django.shortcuts import render, get_object_or_404
from django.db.models import Q
from Transport.models import *
# Create your views here.
from Transport.forms import *

def index(request):
     if not request.user.is_authenticated():
           return render(request, 'Transport/login.html')
     else:
           return render(request,'Transport/index.html')


def place(request,name_id):
    temp_veh = driver.objects.get(id=name_id)
    temp_veh_liv = vehicle_live.objects.get(vehicle = temp_veh)
    #if (temp_veh_liv == None):
    #    temp_veh_liv = vehicle_live()
    #    temp_veh_liv.vehicle = temp_veh
    #    temp_veh_liv.lon_pos = 80.0091568
    #    temp_veh_liv.lat_pos = 13.5481095
    #    temp_veh.save()
    a = {
    "temp_veh_liv":temp_veh_liv,
    }
    return render(request,"Transport/place.html",a)

def vehicle_details(request):
    vehi = vehicle.objects.all()
    context = {
     "vehi":vehi,
     }
    return render(request,"Transport/vehicle_details.html",context)


def Booking(request):
    form = BookingForm()
    context = {
    "form": form,
    }
    return render(request,"Transport/Booking.html",context)

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
                return render(request, 'Transport/login.html', {'error_message': 'Your account has been disabled'})
        else:
            return render(request, 'Transport/login.html', {'error_message': 'Invalid login'})
    return render(request, 'Transport/login.html')


def logout_user(request):
    logout(request)
    form = UserForm(request.POST or None)
    context = {
        "form": form,
    }
    return render(request, 'Transport/login.html', context)


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

def user_accounts(request):
    temp_user_acc = My_user.objects.all()
    usr_cont = {
     "my_usr":temp_user_acc,
     }
    return render(request,"Transport/usr_acc.html",usr_cont)

def driver_accounts(request):
    temp_driver_acc = driver.objects.all()
    num_records = range(1,temp_driver_acc.count()+1)
    print num_records
    driver_cont = {
    "my_driv":temp_driver_acc,
    "total" : num_records
    }
    return render(request, "Transport/driv_acc.html",driver_cont)

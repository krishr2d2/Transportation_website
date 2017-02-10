from django.shortcuts import HttpResponse
from Transport.models import *

def index(request,vehi,lat,lon):
    try:
        temp_veh = driver.objects.get(vehicle_reg_num=vehi)
        try:
            temp_veh_liv = vehicle_live.objects.get(vehicle = temp_veh)
            temp_veh_liv.delete()
        except vehicle_live.DoesNotExist :
            temp_veh_liv = None

        temp_veh_liv = vehicle_live()
        temp_veh_liv.vehicle = temp_veh
        temp_veh_liv.lon_pos = lon
        temp_veh_liv.lat_pos = lat
        temp_veh_liv.save()

        #temp_veh_liv = vehicle_live()
        #temp_veh_liv.vehicle = temp_veh
        #temp_veh_liv.lon_pos = lon
        #temp_veh_liv.lat_pos = lat
        #temp_veh_liv.save()

        return HttpResponse("<h3>"+vehi+" @</br>lat: "+lat+"</br>lon: "+lon+"</h3>")
    except:
        return HttpResponse("The given Reg-Number DoesNotExist...")
    #try :
    #    temp_veh = driver.objects.get(vehicle_reg_num=vehi)
        #temp_veh = get_object_or_404(vehicle,vehicle_name=vehicle)
    #    temp_veh_liv = vehicle_live.objects.get(vehicle = temp_veh)
    #    if (temp_veh_liv == None):

     #       temp_veh_liv.delete()
     #       temp_veh_liv = vehicle_live()
     #       temp_veh_liv.vehicle = temp_veh
     #       temp_veh_liv.lon_pos = lon
     #       temp_veh_liv.lat_pos = lat
     #       temp_veh_liv.save() 
            #return HttpResponse("<h3>"+vehi+" @</br>lat: "+lat+"</br>lon: "+lon+"</h3>")
     #   else:
     #       return HttpResponse("<h1>There are no records for your vehicle.</h1>")
    #except :
     #   return HttpResponse("<h1>Bloody Fool...first,know how many buses you have in total!!</h1>")


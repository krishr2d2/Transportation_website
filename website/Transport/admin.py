from django.contrib import admin
from .models import Booking, vehicle, vehicle_live
# Register your models here.
admin.site.register(Booking)
admin.site.register(vehicle)
admin.site.register(vehicle_live)


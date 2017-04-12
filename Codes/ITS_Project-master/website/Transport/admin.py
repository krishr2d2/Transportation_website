from django.contrib import admin
from .models import *
# Register your models here.
admin.site.register(My_user)
admin.site.register(Vehicle)
admin.site.register(Passenger)
admin.site.register(Driver)
admin.site.register(Valid_stop)
admin.site.register(Booking)


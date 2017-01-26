from django.conf.urls import url
from . import views

app_name = 'Transport'

urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^register/$', views.register, name='register'),
    url(r'^login_user/$', views.login_user, name='login_user'),
    url(r'^logout_user/$', views.logout_user, name='logout_user'),
    url(r'^Booking/$',views.Booking,name="Booking"),
    url(r'^vehicle_details/$',views.vehicle_details,name="vehicle_details"),
    url(r'^vehicle_details/place/$',views.place,name="place"),
]

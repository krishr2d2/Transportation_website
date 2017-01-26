from django.conf.urls import url
from . import views

urlpatterns = [
    url(r'^(?P<vehi>IIITS-[1-6])/(?P<lat>[0-9]+.[0-9]+)/(?P<lon>[0-9]+.[0-9]+)/$',views.index,name = 'index'),
]

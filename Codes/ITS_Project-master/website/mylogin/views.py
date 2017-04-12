from django.shortcuts import render
#from mylogin.forms import *
from django.contrib.auth.decorators import login_required
from django.contrib.auth import authenticate, login
from django.contrib.auth import logout
from django.views.decorators.csrf import csrf_protect
from django.shortcuts import render_to_response
from django.http import HttpResponseRedirect
from django.template import RequestContext
from Transport.models import *
from django.db.models import Q
from .forms import *

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
    return render(request, 'mylogin/login.html')


def logout_user(request):
    logout(request)
    form = RegistrationForm(request.POST or None)
    context = {
        "form": form,
    }
    return render(request, 'mylogin/login.html', context)


def register(request):
    form = RegistrationForm(request.POST or None)
    if form.is_valid():
        #user = form.save(commit=False)
        user = User.objects.create_user(
        username = form.cleaned_data['username'],
        password = form.cleaned_data['password1'],
        email = form.cleaned_data['email']
        )
        #user.set_password(password)
        return HttpResponseRedirect('/register/success/')
        #user.save()
        #user = authenticate(username=username, password=password)
        #if user is not None:
        #    if user.is_active:
        #        login(request, user)

        #        return render(request, '/register/success/')
    context = {
        "form": form,
    }
    return render(request, 'mylogin/register.html', context)

def register_success(request):
	return render_to_response(
		'mylogin/success.html',
		)

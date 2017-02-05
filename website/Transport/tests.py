from django.core.urlresolvers import resolve,reverse
from django.contrib.auth.models import User
from django.contrib.auth import authenticate
from django.test import TestCase, Client
from django.http import HttpRequest
from Transport.views import index, login

class HomePageTest(TestCase):
    
    def setUp(self):
        self.c = Client()
        self.user = User.objects.create(username='testy',password='12345')
        self.user.set_password('hello')
        self.user.save()
        # A dummy user for testing...

    def test_root_url_resolves_to_index_view(self):
        found = resolve('/')
        self.assertEqual(found.func, index)
    
    def test_existing_login(self):
        login = self.c.login(username='testy',password='hello')
        if login :
            self.c.logout()
        self.assertTrue(login)

    def test_non_existing_login(self):
        login2 = authenticate(username='fake', password='hello')
        # login2 == None implies the user doesn't exist...
        self.assertEqual(login2,None)

    def test_incorrect_password(self):
        user3 = authenticate(username='testy',password='hello')
        login3 = self.c.login(username='testy',password='hell')
        # login3 == False implies the user's credentials are invalid...
        self.assertTrue(not login3 and user3 != None)

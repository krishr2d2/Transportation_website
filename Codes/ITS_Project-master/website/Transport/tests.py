from django.test import LiveServerTestCase
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions
from django.core.urlresolvers import resolve,reverse
from django.contrib.auth.models import User
from django.contrib.auth import authenticate
from django.test import TestCase, Client
from django.http import HttpRequest
from Transport.views import index, login
from time import sleep

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

# To run this automated test case, you must install geckodriver that lets selenium access to your browser
# otherwise you'd get an import error...for 'from selenium import Webdriver' line.

class AccountTestCase(LiveServerTestCase):

    def setUp(self):
        self.selenium = webdriver.Firefox()

    def tearDown(self):
        self.selenium.quit()
        
    def test_gprs(self):
        selenium = self.selenium
        selenium.get('http://127.0.0.1:8000/Transport/login')
        username = selenium.find_element_by_id('id_username')
        password = selenium.find_element_by_id('id_password')
        submit = selenium.find_element_by_class_name('btn-success')
        username.send_keys('root')
        password.send_keys('iiits@123')
        submit.send_keys(Keys.RETURN)
        assert "Log In" in selenium.title
        selenium.get('http://127:0.0.1:8000/Transport/index')
        assert "Transportation" in selenium.title

        selenium.get('http://127.0.0.1:8000/Transport/driver_accounts/1/')
        main_window = selenium.current_window_handle
        selenium.find_element_by_tag_name('body').send_keys(Keys.CONTROL + 't')

        steps = 0
        initial_lat = 13.550779
        initial_lon = 80.014409
        step_size = 0.001000
        while (steps < 15):
            initial_lat += step_size
            selenium.switch_to_window(selenium.window_handles[1])
            selenium.get('http://127.0.0.1:8000/locate/AP03AY8532/'+str(initial_lat)+'/'+str(initial_lon)+'/')
            selenium.switch_to_window(main_window)
            selenium.get('http://127.0.0.1:8000/Transport/driver_accounts/1/')
            sleep(1)
            steps += 1

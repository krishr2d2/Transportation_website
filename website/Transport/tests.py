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

class AccountTestCase(LiveServerTestCase):

    def setUp(self):
        self.selenium = webdriver.Firefox()
        #self.gprs_output = webdriver.Firefox()
        #super(AccountTestCase, self).setUp()

    def tearDown(self):
        self.selenium.quit()
        #temp0 = User.objects.get(username = 'unary')
        #temp0.delete() 

    def test_gprs(self):
        selenium = self.selenium
        selenium.get('http://127.0.0.1:8000/Transport/login')
       #first_name = selenium.find_element_by_id('id_first_name')
       #last_name = selenium.find_element_by_id('id_last_name')
        username = selenium.find_element_by_id('id_username')
        #email = selenium.find_element_by_id('id_email')   
        password = selenium.find_element_by_id('id_password')
        #password2 = selenium.find_element_by_id('id_password2')
        submit = selenium.find_element_by_class_name('btn-success')
        #submit = selenium.find_element_by_xpath('//input[@value="Submit"]')        
        #Fill the form with data
        #first_name.send_keys('Yusuf')
        #last_name.send_keys('Unary')
        username.send_keys('krishr2d2')
        #email.send_keys('krishna@gmail.com')
        password.send_keys('mkc@r2d2')
        #password2.send_keys('123456')
        #selenium.find_element_by_class_name('form-horizontal').submit()
        submit.send_keys(Keys.RETURN)
        assert "Log In" in selenium.title
    
        selenium.get('http://127:0.0.1:8000/Transport/index')
        #print selenium.title
        assert "Transportation" in selenium.title
        selenium.get('http://127.0.0.1:8000/Transport/driver_accounts/1/')
        #driver_1_button = selenium.find_element_by_id('1')        
        main_window = selenium.current_window_handle
        selenium.find_element_by_tag_name('body').send_keys(Keys.CONTROL + 't')
        #selenium.find_element_by_tag_name('body').send_keys(Keys.CONTROL + Keys.TAB)

        #selenium.switch_to_window(selenium.window_handles[1])
        #selenium.get('http://127.0.0.1:8000/locate/AP03AY8532/13.6410452/79.2847145/')
        #selenium.switch_to_window(main_window)
        #sleep(2)
        #selenium.find_element_by_tag_name('body').send_keys(Keys.CONTROL + 'w')

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
        #self.gprs_output = webdriver.Firefox()
        #self.gprs_input.get('http:/127.0.0.1:8000')
        #self.gprs_output.get('http://127:0.0.1:8000/Transport/index.html')
        #self.assertNotEqual( None, selenium.find_element_by_id('index_page'))
        
    #def remove_user_from_live_server(self):
    #    temp0 = User.objects.get(username='unary')
    #    print temp0.username
    #    temp0.delete()


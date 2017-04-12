import serial

import urllib2, time
import serial, io, sys


def publish(temp,temp1):
#   if len(temp) > 2 and len(temp1) > 2 and len(temp2)>2 and  len(temp3) > 2:
        url = "https://mkc.pythonanywhere.com/locate/car_1/" + temp +"/"+temp1+"/"
        print url
        result = urllib2.urlopen(url).read()





arduino = serial.Serial('/dev/ttyACM0', 115200, timeout=.1)
l = []
t = ['\r','\t','\n',' ',None]
a = ''
b =''
while True:
	 #time.sleep(11)
	 data = arduino.readline()[:-2] #the last bit gets rid of the new-line chars
	 if data:
	
		l = data.split(':')
		if l not in t and len(l) ==2:
			if l[0] == 'lat':
				a = str(l[1])
				print a
				l = []

	
	 data1 = arduino.readline()[:-2] #the last bit gets rid of the new-line chars
         if data1:

                l = data1.split(':')
                if l not in t and len(l) ==2:
                        if l[0] == 'lon':
                                b = str(l[1])
                                print b
                                l = []

       
	 if  a:
		if b:
			 publish(a,b)




		



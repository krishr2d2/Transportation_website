//import processing.serial.*;
#include <SoftwareSerial.h>
#include <TinyGPS.h>
//PrintWriter output1;
//PrintWriter output2;

TinyGPS gps;
SoftwareSerial ss(4, 5);
static void print_float(float val, float invalid, int len, int prec);
static void smartdelay(unsigned long ms);
double distance_on_geoid(double lat1, double lon1, double lat2, double lon2);

void setup()
{
  Serial.begin(115200);
  ss.begin(9600);
  //output1 = createwriter("data1.txt");
  //output2 = createwriter("data2.txt");
}

void loop()
{
  float flat, flon;
  unsigned long age, date, time, chars = 0;
  unsigned short sentences = 0, failed = 0;
  static const double LONDON_LAT = 51.508131, LONDON_LON = -0.128002;
    gps.f_get_position(&flat, &flon, &age);
    gps.stats(&chars, &sentences, &failed);
  
 Serial.print("lat:");
  //Serial.println()
  print_float(flat, TinyGPS::GPS_INVALID_F_ANGLE, 10, 6);
 Serial.println();
  Serial.print("lon:");
  print_float(flon, TinyGPS::GPS_INVALID_F_ANGLE, 11, 6);
  Serial.println();
  
  smartdelay(10000);
}

static void smartdelay(unsigned long ms)
{
  unsigned long start = millis();
  do 
  {
    while (ss.available())
      gps.encode(ss.read());
  } while (millis() - start < ms);
}
static void print_float(float val, float invalid, int len, int prec)
{   
     
    Serial.print(val, prec);
    int vi = abs((int)val);
    int flen = prec + (val < 0.0 ? 2 : 1); // . and -
    flen += vi >= 1000 ? 4 : vi >= 100 ? 3 : vi >= 10 ? 2 : 1;
    
}




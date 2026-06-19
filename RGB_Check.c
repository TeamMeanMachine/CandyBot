#include <Wire.h>
#include "Adafruit_TCS34725.h"

// set to false if using a common cathode LED
#define commonAnode true

#define NONE         0
#define RED_COLOR    1
#define GREEN_COLOR  2
#define BLUE_COLOR   3
#define YELLOW_COLOR 4
#define WHITE_COLOR  5

uint8_t colorDetected;

// our RGB -> eye-recognized gamma color
byte gammatable[256];


Adafruit_TCS34725 tcs = Adafruit_TCS34725(TCS34725_INTEGRATIONTIME_24MS, TCS34725_GAIN_4X);

void setup() {
  Serial.begin(9600);
  Serial.println("Color View Test!");

  pinMode(13,OUTPUT);

  if (tcs.begin()) {
    Serial.println("Found sensor");
  } else {
    Serial.println("No TCS34725 found ... check your connections");
    while (1); // halt!
  }

  // thanks PhilB for this gamma table!
  // it helps convert RGB colors to what humans see
  for (int i=0; i<256; i++) {
    float x = i;
    x /= 255;
    x = pow(x, 2.5);
    x *= 255;
      
    if (commonAnode) {
      gammatable[i] = 255 - x;
    } else {
      gammatable[i] = x;      
    }
    //Serial.println(gammatable[i]);
  }
}

uint32_t t0;

uint32_t rgbHigh;
uint32_t rgbLow;

uint16_t clear, red, green, blue;

void loop() {
  t0 = millis();  
  
  tcs.setInterrupt(false);      // turn on LED

  delay(30);  // takes 50ms to read 
  
  tcs.getRawData(&red, &green, &blue, &clear);

  tcs.setInterrupt(true);  // turn off LED

  if (red < 75) {
    red = 0;
  } else {
    red -=75;
  }

  if (green < 40) {
    green = 0;
  } else {
    green -= 40;
  }
  
  // Serial.print("C:\t"); Serial.print(clear);
  Serial.println(String(blue) + "\t" + String(red) + "\t" + String(green) + "\t0");

  rgbHigh = max(red, max(green, blue));
  rgbLow = min(red, min(green,blue));

  if (rgbHigh < 100) {
    colorDetected = NONE;
  } else if (rgbHigh == red) {
    if (green < .9*red) {
      colorDetected = RED_COLOR;
    } else {
      colorDetected = YELLOW_COLOR;
    }
  } else if (rgbHigh == green) {
    if (red < .9*green) {
      colorDetected = GREEN_COLOR;
    } else {
      colorDetected = YELLOW_COLOR;
    }
  } else if (rgbHigh == blue) {
    if (green < .8*blue){
      colorDetected = BLUE_COLOR;
    } else {
      colorDetected = GREEN_COLOR;
    }
  } else {
    Serial.println("Failed");
  }

  Serial.println(getColorName());
  //flashLED();
  
/*  // Figure out some basic hex code for visualization
  uint32_t sum = clear;
  float r, g, b;
  r = red; r /= sum;
  g = green; g /= sum;
  b = blue; b /= sum;
  r *= 256; g *= 256; b *= 256;
  /*Serial.print("\t");
  Serial.print((int)r, HEX); Serial.print((int)g, HEX); Serial.print((int)b, HEX);
  Serial.println();*/

  //Serial.print((int)r ); Serial.print(" "); Serial.print((int)g);Serial.print(" ");  Serial.println((int)b );
  //Serial.println("Loop took " + String(millis() - t0) + "ms");
 
}

String getColorName() {
  switch (colorDetected) {
    case NONE:
      return "no color";
    case RED_COLOR:
      return "red";
    case GREEN_COLOR:
      return "green";
    case BLUE_COLOR:
      return "blue";
    case YELLOW_COLOR:
      return "yellow";
    case WHITE_COLOR:
      return "white";
    default:
      return "failed";
  }
}

void flashLED() {
  for (uint8_t i=0; i<colorDetected; i++) {
    digitalWrite(13,HIGH);
    delay(100);
    digitalWrite(13,LOW);
    delay(100);
  }
  delay(250);
}

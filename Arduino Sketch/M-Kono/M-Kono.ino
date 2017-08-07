#include <Servo.h>


Servo thumb, index, middle, ring, pinky; // Define servo
int bend = 800;
int straight = 2200;
int half = 1800;
int wait = 500;
int pbend = 2200;
int pstraight = 800;
int phalf = 1300;

void setup() { 
  // Set servo to digital pins
//  Serial.begin(9600);
  thumb.attach(5);  
  index.attach(6); 
  middle.attach(9);  
  ring.attach(10);  
  pinky.attach(11);  
  
} 

void loop() {            // Loop through motion tests
  //  if(Serial.available())
//  {
//    phoneInput=Serial.read();
//    }  
  openHand();           
  delay(4000);        
  closeHand();           
  delay(2000);   
  char phoneInput;      
}
//method used to translate phone input into actions
//void action()
//{
//    switch (phoneInput)
//    {
//      case openHand:
//        openHand();
//        break;
//      case closeHand:
//        closeHand();
//        break;
//      }
//}
//method to drive individual fingers
void drive(Servo s, int pos)
{
  s.writeMicroseconds(pos);
}

void openHand() {         
  drive(thumb, straight);
  drive(index, straight);
  drive(middle, straight);
  drive(ring, straight);
  drive(pinky, straight);
}

void closeHand() {         
  drive(index, bend);
  drive(thumb, bend);
  drive(ring, bend);
  drive(middle, bend);
  drive(pinky, pbend);
}


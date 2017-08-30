#include <Servo.h>


Servo thumb, index, middle, ring, pinky; // Define servo
int bend = 800;
int straight = 2200;
int half = 1800;
int wait = 500;
int pbend = 2200;
int pstraight = 800;
int phalf = 1300;
char phoneInput;

void setup() { 
  // Set servo to digital pins
  Serial.begin(9600);
  thumb.attach(5);  
  index.attach(6); 
  middle.attach(9);  
  ring.attach(10);  
  pinky.attach(11);
   if(Serial.available())
      {
        phoneInput=Serial.read();
      } 
} 

void loop() {            
// Loop through motion tests  
//Servo Test
//  openHand();           
//  delay(4000);        
//  closeHand();           
//  delay(2000);        
}
//method used to translate phone input into actions
void action()
{
  String command= "";
  command.concat(phoneInput);
  if(command=="open")
    {
    openHand(); 
    }
  else if(command=="close")
    {
    closeHand();
    }
  else if(command=="point")
    {
    point();
    }
  else if(command=="greet")
    {
    greet();
    }
  else if(command=="good")
    {
    thumbsUp();
    }
}

//method to drive individual fingers
void drive(Servo s, int pos)
{
  s.writeMicroseconds(pos);
}

//methods containing the finger actions to be carried out
void openHand() {         
  drive(thumb, straight);
  drive(index, straight);
  drive(middle, straight);
  drive(ring, straight);
  drive(pinky, straight);
}

void closeHand() {   
  drive(pinky, pbend);
  drive(ring, bend);
  drive(middle, bend);      
  drive(index, bend);
  drive(thumb, bend);
}

void thumbsUp()
{
  drive(pinky, pbend);
  drive(ring, bend);
  drive(middle, bend);      
  drive(index, bend);
  drive(thumb, straight);
}
void point()
{
  drive(pinky, pbend);
  drive(ring, bend);
  drive(middle, bend);      
  drive(index, straight);
  drive(thumb, bend);
}
void greet()
{
  //Add the servo motion of degree
//  drive(pinky, );
//  drive(ring, );
//  drive(middle,);      
//  drive(index, );
//  drive(thumb, ); 
}


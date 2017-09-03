#include <Servo.h>


Servo thumb, index, middle, ring, pinky; // Define servo

void setup() {
  // put your setup code here, to run once:
  thumb.attach(5);  
  index.attach(6); 
  middle.attach(9);  
  ring.attach(10);  
  pinky.attach(11);
}

void loop() {
  // put your main code here, to run repeatedly:
  //Servo Test
  openHand();           
  delay(2000);        
  closeHand();           
  delay(2000); 
}
void openHand()
{
  thumb.write(180);
  ring.write(180);
  middle.write(180);
  index.write(180);
  pinky.write(180);
}
void closeHand()
{
  thumb.write(90);
  ring.write(90);
  middle.write(90);
  index.write(90);
  pinky.write(90);
}

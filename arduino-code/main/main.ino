#include <Stepper.h>
#define LED 2 //connect LED to digital pin2
int t =0;
const int stepsPerRevolution = 1000;  // change this to fit the number of steps per revolution
// for your motor

// initialize the stepper library on pins 8 through 11:
Stepper myStepper(stepsPerRevolution, 8, 9, 10, 11);

int stepCount = 0;  // number of steps the motor has taken

void setup() {
  Serial.begin(9600);
  pinMode(8, OUTPUT); //IN1
  pinMode(9, OUTPUT); //IN2
  pinMode(10, OUTPUT); //IN3
  pinMode(11, OUTPUT); //IN4
  // initialize the digital pin2 as an output.
  pinMode(LED, OUTPUT);
}

void loop() {
  
  for(int i=0; i<500; i++){
    digitalWrite(LED, HIGH);
    myStepper.step(1);
    Serial.print("steps:" );
    Serial.println(stepCount);
    stepCount++;
  }

    for(int i=0; i<500; i++){
      digitalWrite(LED, LOW);
      myStepper.step(-1);
      Serial.print("steps:" );
      Serial.println(stepCount);
      stepCount++;
 
  }

  
  // map it to a range from 0 to 25:
  int motorSpeed = 25;
  // set the motor speed:
  if (motorSpeed > 0) {
    myStepper.setSpeed(motorSpeed);
    // step 1/100 of a revolution:
  Serial.println(motorSpeed);//print how fast the motor is going in the serial monitor
  }
}
  
///*
// */
//#include <Stepper.h>
//
//const int stepsPerRevolution = 400; // change this to fit the number of steps per revolution
//                                     // for your motor
//
//// initialize the stepper library on pins 8 through 11:
//Stepper myStepper(stepsPerRevolution, 8,9,10,11); 
//
//int stepCount = 0; // number of steps the motor has taken
//
//void setup() {
//  // initialize the serial port:
//  Serial.begin(9600);
//  pinMode(8, OUTPUT); //IN1
//  pinMode(9, OUTPUT); //IN2
//  pinMode(10, OUTPUT); //IN3
//  pinMode(11, OUTPUT); //IN4
//}
//
//void loop() {
//  // step one step:
//  myStepper.step(1);
//  Serial.print("steps:" );
//  Serial.println(stepCount);
//  stepCount++;
//  delay(50);
//
//}

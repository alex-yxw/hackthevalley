#include <CurieBLE.h>
#include <Stepper.h>

const int red = 2; // set red LED pin (closing lock)
const int green = 3; // set green LED pin (opening lock)
const int piezoPin = 4; //set piezo buzzer pin
const int stepsPerRevolution = 1000;
bool locked = false;

BLEPeripheral blePeripheral; // create peripheral instance

BLEService ledService("19B10000-E8F2-537E-4F6C-D104768A1214"); // create service

// create switch characteristic and allow remote device to read and write
BLECharCharacteristic switchChar("19B10001-E8F2-537E-4F6C-D104768A1214", BLERead | BLEWrite);

Stepper myStepper(stepsPerRevolution, 8, 9, 10, 11);

int stepCount = 0;
int motorSpeed = 25;

void setup() {
  Serial.begin(9600);
  pinMode(8, OUTPUT); //IN1
  pinMode(9, OUTPUT); //IN2
  pinMode(10, OUTPUT); //IN3
  pinMode(11, OUTPUT); //IN4
  pinMode(red, OUTPUT); // use the LED on pin 2 as an output
  pinMode(green, OUTPUT); // use the LED on pin 3 as an output

  myStepper.setSpeed(motorSpeed);
  
  // set the local name peripheral advertises
  blePeripheral.setLocalName("EZLOCK");
  // set the UUID for the service this peripheral advertises
  blePeripheral.setAdvertisedServiceUuid(ledService.uuid());

  // add service and characteristic
  blePeripheral.addAttribute(ledService);
  blePeripheral.addAttribute(switchChar);

  // assign event handlers for connected, disconnected to peripheral
  blePeripheral.setEventHandler(BLEConnected, blePeripheralConnectHandler);
  blePeripheral.setEventHandler(BLEDisconnected, blePeripheralDisconnectHandler);

  // assign event handlers for characteristic
  switchChar.setEventHandler(BLEWritten, switchCharacteristicWritten);
// set an initial value for the characteristic
  switchChar.setValue(0);

  // advertise the service
  blePeripheral.begin();
  Serial.println(("Bluetooth device active, waiting for connections..."));
}

void loop() {
  // poll peripheral
  blePeripheral.poll();
  
}

void blePeripheralConnectHandler(BLECentral& central) {
  // central connected event handler
  Serial.print("Connected event, central: ");
  Serial.println(central.address());
}

void blePeripheralDisconnectHandler(BLECentral& central) {
  // central disconnected event handler
  Serial.print("Disconnected event, central: ");
  Serial.println(central.address());
}

void switchCharacteristicWritten(BLECentral& central, BLECharacteristic& characteristic) {
  // central wrote new value to characteristic, update LED
  Serial.print("Characteristic event, written: ");

  if (!locked && switchChar.value()==1){
    Serial.println("LED on, mechanism locking");
    digitalWrite(red, HIGH);
    digitalWrite(green, LOW);
    myStepper.step(1050);
    locked = true;
    tone(piezoPin, 500, 500);
  }
  
  if (locked && switchChar.value()==0){
    Serial.println("LED off, mechanism unlocked");
    digitalWrite(red, LOW);
    digitalWrite(green, HIGH);
    myStepper.step(1020);
    locked = false;
    tone(piezoPin, 500, 500);
  }
}

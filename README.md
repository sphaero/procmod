# ProcMod Mod Player for Processing

An old school mod player for Processing based on JavaMod! 

## Installation

Just unzip in the libraries folder.

## Example sketch

```
import procmod.*;

ModPlayer hello;
int bgcolor = 0;

void setup() {
  background(bgcolor);
  
  hello = new ModPlayer(this, "test.mod");
  hello.play();
}

void draw() {
  background(bgcolor);
  fill(255);
}

void modRowEvent( int channel, int instrument, int note )
{
  if (channel == 0)
  {
    println(channel +":"+ instrument +":"+ note);
    bgcolor = note;
  }
}
```
import procmod.*;

ModPlayer hello;
int bgcolor = 0;

void setup() {
  background(bgcolor);
  
  hello = new ModPlayer(this, dataPath("test.mod"));
  hello.play();
}

void draw() {
  background(bgcolor);
  fill(255);
  text("Helloow", 40, 200);
}

void modRowEvent( int channel, int instrument, int note )
{
  if (channel == 0)
  {
    println(channel +":"+ instrument +":"+ note);
    bgcolor = note * 3;
  }
}

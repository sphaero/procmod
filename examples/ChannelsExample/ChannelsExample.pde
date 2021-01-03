import procmod.*;

//  define a new instance of the ModPlayer
ModPlayer mplayer;
//  we'll use this to draw rectangle colors for every channel
color ch1color;
color ch2color;
color ch3color;
color ch4color;

void setup() {
  size(200,200);
  background(0);
  //  Load the supplied test.mod file
  mplayer = new ModPlayer(this, "test.mod");
  //  play it rightaway
  mplayer.play();
}

void draw() {
  background(64);
  stroke(255, 255, 0);
  strokeWeight(2);
  rectMode(CENTER);
  // channel 1 rect
  fill(ch1color);
  rect(50,50,88,88);
  // channel 2 rect
  fill(ch2color);
  rect(150,50,88,88);
  // channel 3 rect
  fill(ch3color);
  rect(50,150,88,88);
  // channel 4 rect
  fill(ch4color);
  rect(150,150,88,88);
}

//  This method is called every time an instrument is being played. 
//  Note: When no instrument is played on an iteration of the song 
//  (meaning continue playing the instrument) the instrument number
//  will be 0. We'll use that to change the color
void modRowEvent( int channel, int instrument, int note )
{
  println(channel +":"+ instrument +":"+ note);
  if (channel == 0)
  {
    ch1color = color(channel*64, note * 8, instrument*64);
  } 
  else if ( channel == 1 )
  {
    ch2color = color(instrument*64, channel*64, note * 8);
  }
  else if ( channel == 2 )
  {
    ch3color = color(channel*64, note * 8, instrument*64);
  }
  else if ( channel == 3 )
  {
    ch4color = color(0, instrument*64, note * 8);
  }
}

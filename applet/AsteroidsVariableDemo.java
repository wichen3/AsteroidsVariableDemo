import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class AsteroidsVariableDemo extends PApplet {

/**
Prese 5 to accelerate<br>
Press 4 or 6 to rotate<br>
Press 2 for hypserspace
*/
SpaceShip bob;
Stars stars;
boolean isAccelerating = false;
boolean isRotatingLeft = false;
boolean isRotatingRight = false;
boolean isHyperspace = false;

int countDown = 60;
public void setup()
{
  size(500,500);
  bob = new SpaceShip();
  stars = new Stars();
  textSize(24);
}
public void draw()
{
  if(!isHyperspace)
  {
  background(0);
  }
  else
  {
    fill(0,0,0,10);
    stroke(0,0,0,10);
    rect(0,0,width,height);
    countDown--;
    if(countDown == 0)
    {
      isHyperspace = false;
    }
  }
  if(keyPressed == true && key == '2') 
    countDown = 60;
  stars.show();
  bob.show(isHyperspace,countDown,isAccelerating);
  bob.move();
  if(isAccelerating == true)bob.accelerate(.1f);
  if(isRotatingLeft == true)bob.rotate(-5);
  if(isRotatingRight == true)bob.rotate(5);
  stroke(255);
  text("myCenterX: "+ bob.getX(),10,20);
  text("myCenterY: "+ bob.getY(),10,40);
  text("myPointDirection: "+ bob.getPointDirection(),10,60);
  text("myDirectionX: "+ (int)(bob.getDirectionX()*100)/100.0f,10,80);
  text("myDirectionY: "+ (int)(bob.getDirectionY()*100)/100.0f,10,100);
}
public void keyPressed()
{
  if(key == '5') isAccelerating = true;
  if(key == '4') isRotatingLeft = true;
  if(key == '6') isRotatingRight = true;
  if(key == '2')
  {
    bob.setX((int)(Math.random() * width));
    bob.setY((int)(Math.random() * height));
    bob.setPointDirection((int)(Math.random() * 360));
    bob.setDirectionX(0);
    bob.setDirectionY(0);
    isHyperspace = true;
    countDown = 60;
  }
}
public void keyReleased()
{
  if(key == '5') isAccelerating = false;
  if(key == '4') isRotatingLeft = false;
  if(key == '6') isRotatingRight = false;
}
abstract class Floater
{
  protected int[] xCorners;
  protected int[] yCorners;
  protected int corners;  //the number of corners, a triangular floater has 3
  protected int myColor;
  protected double myCenterX, myCenterY; //holds center coordinates
  protected double myDirectionX, myDirectionY; //holds x and y coordinates of the vector for direction of travel
  protected double myPointDirection; //holds current direction the ship is pointing in degrees

  abstract public void setX(int x);
  abstract public int getX();
  abstract public void setY(int y);
  abstract public int getY();
  abstract public void setDirectionX(double x);
  abstract public double getDirectionX();
  abstract public void setDirectionY(double y);
  abstract public double getDirectionY();
  abstract public void setPointDirection(int degrees);
  abstract public double getPointDirection();

  public void accelerate (double dAmount)
  {  
    //Accelerates the floater in the direction it is pointing
    //(myPointDirection)

    //convert the current direction the floater is pointing to radians
    double dRadians =myPointDirection*(Math.PI/180);

    //change coordinates of direction of travel
    myDirectionX += ((dAmount) * Math.cos(dRadians));
    myDirectionY += ((dAmount) * Math.sin(dRadians));
  }
  public void rotate (int nDegreesOfRotation)
  {  
    //rotates the floater by a given number of degrees
    myPointDirection+=nDegreesOfRotation;
  }
  public void move ()
  {  
    //Moves the floater towards the coordinates
    //myDirectionX and myDirectionY

    //move the floater in the current direction of travel
    myCenterX += myDirectionX;
    myCenterY += myDirectionY;

    //wrap around screen
    if(myCenterX >width) {
      myCenterX = 0;
    }
    else if (myCenterX<0) {
      myCenterX = width;
    }
    if(myCenterY >height) {
      myCenterY = 0;
    }
    else if (myCenterY < 0) {
      myCenterY = height;
    }
  }
  public void show (boolean isHyperspace, int countDown, boolean jets)
  {  
    //Draws the floater at the current position
    if(isHyperspace == false)
    {
      fill(myColor);
      stroke(myColor);
    }
    else
    {
      fill(255,255,255,60 - countDown);
      stroke(255,255,255,60 - countDown);
    }
    //convert degrees to radians for sin and cos     
    double dRadians = myPointDirection*(Math.PI/180);

    int xRotatedTranslated, yRotatedTranslated;
    beginShape();

    //rotate and translate the coordinates of the floater using current direction	
    for(int nI = 0; nI < corners; nI++)
    {
      xRotatedTranslated = (int)((xCorners[nI]* Math.cos(dRadians)) - (yCorners[nI] * Math.sin(dRadians))+myCenterX);
      yRotatedTranslated = (int)((xCorners[nI]* Math.sin(dRadians)) + (yCorners[nI] * Math.cos(dRadians))+myCenterY); 
      vertex(xRotatedTranslated,yRotatedTranslated);
    }


    endShape(CLOSE);

    int xStart = (int)((-12* Math.cos(dRadians)) - (0 * Math.sin(dRadians))+myCenterX);
    int yStart = (int)((-12* Math.sin(dRadians)) + (0 * Math.cos(dRadians))+myCenterY);
    int xEnd = (int)((-18* Math.cos(dRadians)) - (0 * Math.sin(dRadians))+myCenterX);
    int yEnd = (int)((-18* Math.sin(dRadians)) + (0 * Math.cos(dRadians))+myCenterY);
    int xStart2 = (int)((-12* Math.cos(dRadians)) - (5 * Math.sin(dRadians))+myCenterX);
    int yStart2 = (int)((-12* Math.sin(dRadians)) + (5 * Math.cos(dRadians))+myCenterY);
    int xEnd2 = (int)((-18* Math.cos(dRadians)) - (8 * Math.sin(dRadians))+myCenterX);
    int yEnd2 = (int)((-18* Math.sin(dRadians)) + (8 * Math.cos(dRadians))+myCenterY);
    int xStart3 = (int)((-12* Math.cos(dRadians)) - (-5 * Math.sin(dRadians))+myCenterX);
    int yStart3 = (int)((-12* Math.sin(dRadians)) + (-5 * Math.cos(dRadians))+myCenterY);
    int xEnd3 = (int)((-18* Math.cos(dRadians)) - (-8 * Math.sin(dRadians))+myCenterX);
    int yEnd3 = (int)((-18* Math.sin(dRadians)) + (-8 * Math.cos(dRadians))+myCenterY);
    if(jets)
    {
      line(xStart,yStart,xEnd,yEnd);
      line(xStart2,yStart2,xEnd2,yEnd2);
      line(xStart3,yStart3,xEnd3,yEnd3);
    }
  }
}

class SpaceShip extends Floater
{
  public SpaceShip() {
    corners = 3;
    xCorners = new int[corners];
    xCorners[0] = 16;
    xCorners[1] = -8;
    xCorners[2] = -8;
    yCorners = new int[corners];
    yCorners[0] = 0;
    yCorners[1] = 8;
    yCorners[2] = -8;
    myColor = color(255,255,255);
    myCenterX = myCenterY = 250; //holds center coordinates
    myDirectionX = myDirectionY = 0; //holds x and y coordinates of the vector for direction of travel
    myPointDirection = 0;
  }
  public void setX(int x) {
    myCenterX = x;
  }
  public int getX() {
    return (int)myCenterX;
  }
  public void setY(int y) {
    myCenterY = y;
  }
  public int getY() {
    return (int)myCenterY;
  }
  public void setDirectionX(double x) {
    myDirectionX = x;
  }
  public double getDirectionX() {
    return myDirectionX;
  }
  public void setDirectionY(double y) {
    myDirectionY = y;
  }
  public double getDirectionY() {
    return myDirectionY;
  }
  public void setPointDirection(int degrees) {
    myPointDirection = degrees;
  }
  public double getPointDirection() {
    return myPointDirection;
  }
}

class Stars
{
  int x[];
  int y[];
  int s[];
  int c[];
  Stars()
  {
    x = new int[200];
    y = new int[200];
    s = new int[200];
    c = new int[200];
    for(int i = 0; i< 200; i++)
    {
      x[i] = (int)(Math.random() * width);
      y[i] = (int)(Math.random() * width);
      s[i] = (int)(Math.random() * 5) + 1;
      c[i] = color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
    }
  }
  public void show()
  {
    
    for(int i = 0; i< 200; i++)
    {
      stroke(c[i]);
      strokeWeight(s[i]);
      point(x[i],y[i]);
    }
    strokeWeight(1);
  }
}
    
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "AsteroidsVariableDemo" });
  }
}

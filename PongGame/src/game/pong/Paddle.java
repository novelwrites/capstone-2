package game.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//Fulfills two capstone requirements:

//Inheritance - because it extends the KeyAdapter class
//Interface - this class implements the Runnable interface
public class Paddle extends KeyAdapter implements Runnable //java.lang is a package that contains the interface along
    //with all of the basic stuff you need and is automatically handled by the compiler

{

    private int x;  //holds the current x-position of the paddle
    private int y; ////holds the current y-position of the paddle



    //The direction in which the paddle is travelling along the y-axis - this actually  represents the size of
    // each step to make. Ex. a positive number indicates that the paddle is traveling pixels going down
    // the y-axis (towards the bottom), and a negative number means the paddle is traveling up the y-axis
    // (towards the top). 0 means the paddle is not moving at all:
    private int yDirection = 0;

    //Each time the paddle moves in a given direction, it will move by some number of pixels.
    //Here we default to 1:

    private int moveStepIncrement = 1;

    //-----------------------------------------------------------------------------------
    //**Below this line, the member variables are set to "final" because we do not expect and do not want
    //them to change once they have been initialized.
    //-----------------------------------------------------------------------------------

    //the (unique) id of this paddle
    private final int id;

    //geometric shape of this paddle
    private final Rectangle paddleShape;

    //the color which this paddle will have
    private final Color color;

    //the keyboard key that this paddle will designate as "UP"
    private final int upKey;

    //the keyboard key that this paddle will designate as "DOWN"
    private final int downKey;


    //Constructor Paddle (same name as the class): Builds and initializes the paddle
    //param x - the starting x position of the paddle
    //param y - the starting y position of the paddle
    //param id - the unique ID for the paddle

    public Paddle(int x, int y, int id, int moveStepIncrement, Color paddleColor, int upKey, int downKey, int paddleLength) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.paddleShape = new Rectangle(x, y, 10, paddleLength);
        this.moveStepIncrement = moveStepIncrement;
        this.color = paddleColor;
        this.upKey = upKey;
        this.downKey = downKey;
    }
    public Rectangle getShape() { //Method from Rectangle class
        return paddleShape;
    }

    //Override method from KeyAdapter: each paddle will test each key being typed to see if it matches their own.
    //If it does, the direction is altered.
    //param e

    @Override
    public void keyPressed(KeyEvent e) { //Defining Paddle Up/Down keys:
        // When a key is typed on the keyboard, this method is called and passed parameter variable e of class KeyEvent
        if (e.getKeyCode() == upKey) {//if the typed key matches this paddle's assigned UP key, then
            setYDirection(-1);
        }
        if (e.getKeyCode() == downKey) {//if the typed key matches this paddle's assigned DOWN key, then
            setYDirection(+1);
        }
    }

    //Override method from KeyAdapter - using an existing method with a different implementation.
    //param e

    @Override
    public void keyReleased(KeyEvent e) { //Checking for key release - is 0 for neutral (no change)
        if (e.getKeyCode() == upKey) {
            setYDirection(0);
        }
        if (e.getKeyCode() == downKey) {
            setYDirection(0);
        }

    }

    public void setYDirection(int yDir) { yDirection = yDir; } //Sets direction of paddles


    //Moves the paddle by one step (the size of the step and direction is determined by yDirection):

    public void move() {
        //update to the new Y position:
        paddleShape.y += (yDirection * moveStepIncrement);
        //Make sure we don't go beyond the bounds of the screen:
        if (paddleShape.y <= 15) {
            paddleShape.y = 15;
        }
        if (paddleShape.y >= 700) {
            paddleShape.y = 700;
        }
        }

        //Method called by java whenever this object needs to be redrawn on screen - any time it moves,
        //java will need to redraw it. (We are talking about the paddles)
        //param g

        public void draw(Graphics g) {
            g.setColor(color);
            g.fillRect(paddleShape.x, paddleShape.y, paddleShape.width, paddleShape.height);

        }


    //Because this class implements the Runnable interface, we are forced to implement this method.
    //The Runnable interface allows you to designate a given class as containing a 'chunk' of code that
    //a Thread can execute. This 'chunk' of code is your "run()" method.
    //When you create a Thread, the constructor will ask you to provide it with an object (An object is an instance
    // of a class) that implements the Runnable interface. Because by adhering to the Runnable interface, the Thread
    // will know for sure that the provided object, whatever type it may be, will definitely have the "run()" method
    // (the chunk of code that the thread will be looking to execute).

    @Override

    public void run() {
        try {
            while (true) {
                move(); //keeps game moving by continually calling this method.
                Thread.sleep(50); //pauses thread - why do we need 50ms pause - need the pause for multitasking
            }
        } catch (Exception e) {
            System.err.println(e.getMessage()); //Prints message on console
        }
    }
    }


//If a method throws an exception you have to wrap it in a try/catch
//An exception - some type of error occurred




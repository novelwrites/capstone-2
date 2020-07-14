package game.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

//Runnable is an interface that is implemented by a class whose instances are intended to be executed by a
//thread and is called by Java when the thread starts.
//Interface requirement for the capstone :)
public class Ball implements Runnable {

    private int x;
    private int y;
    private int xDirection = 1; //Initializing xDirection/yDirection
    private int yDirection = 1;

    //Each time the ball moves in a given direction, it will move by some number of pixels.
    //Here we default to 1 pixel:
    private int moveStepIncrement = 1;
    private int p1score; //variable for player1/2 score
    private int p2score; //variable for player3/4 score

    //Geometric shape of the ball
    private final Rectangle ballShape; //Rectangle class

    //Color of the ball
    private final Color color; //Color class

    //Up to 4 paddles may be involved in the game:
    private final Paddle paddle1;
    private final Paddle paddle2;
    private final Paddle paddle3;
    private final Paddle paddle4;

    //Constructor parameters:
    //param x - The starting x position of the ball
    //param y - The starting y position of the ball
    //param ballWidth - the width (in pixels) of the ball
    //param ballHeight - the height (in pixels) of the ball
    //param moveStepIncrement - the increment (in pixels) that the ball will move in X-Y directions
    //param ballColor - the color of the ball
    //param paddle1 - Paddle for player 1
    //param paddle2 - Paddle for player 2
    //param paddle3 - Paddle for player 3
    //param paddle4 - Paddle for player 4

    public Ball(int x, int y, int ballWidth, int ballHeight, int moveStepIncrement, Color ballColor, Paddle paddle1,
                Paddle paddle2, Paddle paddle3, Paddle paddle4) {

        //Initialize the local member variables, with the parameters that are being passed into the constructor.
        //We use the keyword "this" to differentiate between member variables and constructor parameters that
        //might have the exact same name:

        this.p1score = 0; //p1score is a field created on line 21
        this.p2score = 0; //p2score is a field created on line 22; both are initialized to 0

        //Initializing the local member variables (see explanation on line 51)

        this.x  = x; //this.x is a field (member variable) and x on right of equal is a parameter
        this.y = y; //this.y is a field (member variable) and y on right of equal is a parameter
        //x and y are the current coordinates of the ball location on screen - constantly changing

        this.color = ballColor; //color is a field (member variable), ballColor is a parameter
        this.moveStepIncrement = moveStepIncrement; //this.moveStepIncrement (local member variable) will
        //be the value that is passed as a parameter into the constructor (on right) moveStepIncrement.

        this.paddle1 = paddle1; //we initialize the "paddle1" member variable inside Ball (left side of the = sign)
        //with the constructor parameter "paddle1" (right side of the = sign)
        this.paddle2 = paddle2;
        this.paddle3 = paddle3;
        this.paddle4 = paddle4;

        //The constructor is the method of a class so it is the blueprint where each instance defines
        //their own values for that particular instance.

        //Set ball moving randomly
        Random r = new Random(); // Creating an instance of Random class called r
        int rXDir = r.nextInt(1); //Random starting point
        if (rXDir == 0) {
            rXDir--;
        }
        setXDirection(rXDir); //Sets x-axis direction of ball

        int rYDir = r.nextInt(1);
        if (rYDir == 0) {
            rYDir--;
        }
        setYDirection(rYDir); //Sets y-axis direction of ball

        //Code to create "ball"

        ballShape = new Rectangle(this.x, this.y, ballWidth,ballHeight); //ballShape is a new instance of Rectangle to
        // create the shape of the ball using the Rectangle class.
        }

        //Method to return The score for Team1. Team can be composed of 1 or 2 players.
        public int getTeam1Score() {
            return p1score;
        }

        //Method to return The score for Team2. Team can be composed of 1 or 2 players.
        public int getTeam2Score() {
        return p2score;
        }

        //Method to handle X-direction of ball

        //param xDir
        private void setXDirection(int xDir) {
            xDirection = xDir; //field xDirection = parameter xDir
        }

        //Method to handle Y-direction of ball

       //param yDir
       private void setYDirection(int yDir) {
           yDirection = yDir; //field yDirection = parameter yDir.  yDirection was initialized with a value of 1
           //upon creation but is now being passed the value of yDir.
    }

    //Draws the balls
    //param g
    public void draw(Graphics g) { //method from Graphics class
        g.setColor(color);
        g.fillRect(ballShape.x, ballShape.y, ballShape.width, ballShape.height);
    }

    //checkForCollision method checks if the ball has collided with any paddle and if so, adjusts the direction of
    //the ball. Note: These methods respond from the ball's perspective.

    private void checkForCollision() {
        if (ballShape.intersects(paddle1.getShape())) { //intersects part of rectangle class and getShape is method implemented in the Paddle class
            setXDirection(+1);
        }
        if (ballShape.intersects(paddle2.getShape())) {
            setXDirection(-1);
        }
        if (paddle3 != null && ballShape.intersects(paddle3.getShape())) { //had to put !null so program would be happy
            setXDirection(+1);
        }
        if (paddle4 != null && ballShape.intersects(paddle4.getShape())) {
            setXDirection(-1);
        }

    }
    //Super important in understanding how game works: move method
    //First moves the ball by 1 step increment in X and Y directions. The method will then check if the ball has collided
    //with any paddle and if yes, the direction will be updated. Otherwise, the method will check if the ball has hit
    // the wall and if yes, the score of the opposing team will increment and the direction will get updated.

    public void move() {

        //Check if there is any collision that would alter the direction of the ball:

        checkForCollision(); //calls the CheckForCollision method.

        //Update to the new X-Y position
        ballShape.x += (xDirection * moveStepIncrement);
        ballShape.y += (yDirection * moveStepIncrement);

        //Bounce the ball when it hits the edge of the screen

        if (ballShape.x <= 0) { //checking to see if ball reaches left edge of frame, if it does then invokes
            //setXDirection and passes +1 to xDir parameter in setXDirection method which causes direction of ball
            //to change to positive direction (towards the right edge) of x axis.
            setXDirection(+1);
            p2score++; //Score increments if ball reaches edge of defined x-axis
            System.out.println(p1score + " " + p2score); //verify score-keeping is working
        }

        if (ballShape.x >= 990) { //checking to see if ball reaches right edge of frame, if it does then invokes
            //setXDirection and passes -1 to xDir parameter in setXDirection method which causes direction of ball
            //to change to negative direction (towards the left edge) of x axis.
            setXDirection(-1);
            p1score++; //Score increments if ball reaches edge of defined x-axis
            System.out.println(p1score + " " + p2score); //verify score-keeping is working
        }

        if (ballShape.y <= 15) { //Checking to see if ball reaches top of frame, if it does then invokes
            //setYDirection and passes +1 to yDir parameter in setYDirection method which causes direction of ball
            //to change to positive direction (towards the bottom edge) of y axis.
            setYDirection(+1);
        }

        if (ballShape.y >= 790) { //Checking to see if ball reaches bottom of frame, if it does then invokes
            //setYDirection and passes -1 to yDir parameter in setYDirection method which causes direction of ball
            //to change to negative direction (towards the top edge) of y axis.
            setYDirection(-1);
        }

    }

    //Important to understand about run method:

    //Because this class (Ball) implements the Runnable interface, we are forced to implement this method (below).
    //The Runnable interface allows you to designate a given class as containing a 'chunk' of code that
    //a Thread can execute. This 'chunk' of code is your "run()" method.
    //When you create a Thread, the constructor will ask you to provide it with an object (An object is an instance
    // of a class) that implements the Runnable interface. Because by adhering to the Runnable interface,
    // the Thread will know for sure that the provided object, whatever type it may be, will definitely have
    // the "run()" method (the chunk of code that the thread will be looking to execute).


    @Override //Frpm GeekstoGeeks: Overriding is a feature that allows a child class to provide a specific
    // implementation of a method that is already provided by one of its parent classes. ... Method
    // overriding is one of the way by which java achieve Run Time Polymorphism.
    public void run() { //run is a method from the Thread class
        try {
            while (true) {
                move(); //while always true - keeps game moving by continually calling this method.
                Thread.sleep(50); //pauses thread so computer can multitask
            }
        } catch (Exception e) {
            System.err.println(e.getMessage()); //When an exception occurs, prints message to the console
        }
    }
}
//If a method throws an exception you have to wrap it in a try/catch
//An exception - some type of error has occurred
package game.pong;


import java.awt.*; //part of Java library - package for drawing 2D graphics.
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*; //GUI toolkit for java (meets requirement for gaming library)


public class GameDriver extends JFrame {
    //Scope: Local variables are visible only in the method or block they are declared whereas instance
    //variables can be seen by all methods in the class. Place where they are declared:
    //Local variables are declared inside a method or a block whereas instance variables are declared
    //inside a class but outside a method or block.

    private String playerOneName; //Will use this to display player name on GUI.
    private String playerTwoName;
    private String playerThreeName =""; //Initialize to empty string so won't show up on GUI in 2 player mode.
    private String playerFourName = "";

    //window size variables:
    private final int gameWindowWidth = 1000;  // private encapsulates it so only available to GameDriver class.
    private final int gameWindowHeight = 800;  // Final keeps field from changing from initial value.
    private final Dimension screenSize = new Dimension(gameWindowWidth, gameWindowHeight); //Creates a new instance of
    // Dimension called screenSize with variables gameWindowWidth and gameWindowHeight as the parameters for it.
    // Dimension is a class from java.awt.

    // Image is an actual class called Image with variable name blankCanvas.
    //Reminder: A field, also known as a member variable, is a variable declared as part of a class, so that each
    // instance of that class contains an instance of that variable.
    private Image blankCanvas; // blankCanvas being declared a field (member variable) of the class Image.
    private Graphics graphics; //Graphics is the class and graphics is the member variable being declared.


    // We can have up to 4 players. Playing mode is either 2 players or 4 players.
    private Paddle paddle1; //Paddle is class Paddle, paddle1 is variable created for the actual paddle and assigning
    //it type Paddle (Paddle class) so paddle1 is a placeholder.
    //We are declaring them and will initialize them in the constructor.
    private Paddle paddle2;
    private Paddle paddle3;
    private Paddle paddle4;

    //There is only 1 ball in the game - Ball is class Ball, ball is a variable created for the actual ball and
    //assigning it type Ball (ball class) so ball is a placeholder.
    private Ball ball;

    //Threads that will run game: game is multi-threaded with ball and each paddle a single thread.
    private Thread ballThread; //Thread is a class that can execute simultaneously with main.
    private Thread paddle1Thread;
    private Thread paddle2Thread;
    private Thread paddle3Thread;
    private Thread paddle4Thread;

    private int fourPlayerMode = 0;
    private int twoPlayerMode = 1;
    private int gameMode = fourPlayerMode;

    private BufferedImage backgroundImage; //Declaring variable for TEKcamp logo to be displayed on GUI.

    //Constructor for window - Every class has a constructor function that constructs an object
    //using the class as a blueprint. The class itself is nothing but the blueprint of how to create a
    //"GameDriver" object. Using the "new" keyword, we are able to instantiate the class and create our object
    //which we can then start using. In other words, we cannot do the following:
    //GameDriver.startGame()
    //because GameDriver is a class and not an object/instance. We can't use the class itself - we have to create an
    //object/instance. In order to invoke the startGame() method we need to create an instance of GameDriver such as
    //GameDriver gameDriver = new GameDriver();
    // gameDriver.startGame(); "gameDriver" is the instance we created & we can now invoke the startGame method using it.

    public GameDriver() { //Method used to initialize paddles, ball, threads and JFrame

        //If we want to have players enter names, set level etc., it's done through a dialogue box - stops the code and
        // doesn't continue until you acknowledge it.(Called a modal - meaning it will pause)

        //load the background image
        try {
            backgroundImage = ImageIO.read(new File("resources/TEKcamplogo.png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Ask the users for the mode, 2 player or 4 player
        Object[] options = {"4 Player Mode", "2 Player Mode"}; //(inverted it because was inverted on dialogue box)

        gameMode = JOptionPane.showOptionDialog(this,//parent container of JOptionPane
                "Please Select A Game Mode ",
                "Game Mode",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                new ImageIcon("resources/logo_small.png"),//use TEKcamp icon in resources
                options,//the titles of buttons
                options[0]);//default button title

        //Ask for the names of the players (selected method that also allows us to load TEKcamp image icon).
        //Used if statement to handle 2 different game modes.
        if(gameMode == twoPlayerMode) {
            playerOneName = (String)JOptionPane.showInputDialog(this,
                    "Enter Player 1 Name (Up W / Down S)",
                    "",JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/logo_small.png"),
                    null,"");
            playerTwoName = (String)JOptionPane.showInputDialog(this,
                    "Enter Player 2 Name (Up UP / Down DOWN)",
                    "",JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/logo_small.png"),
                    null,"");;
        }
        else{
            playerOneName = (String)JOptionPane.showInputDialog(this,
                    "Enter Player 1 Name (Up W / Down S)",
                    "",JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/logo_small.png"),
                    null,"");;
            playerTwoName = (String)JOptionPane.showInputDialog(this,
                    "Enter Player 2 Name (Up UP / Down DOWN)",
                    "",JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/logo_small.png"),
                    null,"");;
            playerThreeName = (String)JOptionPane.showInputDialog(this,
                    "Enter Player 3 Name (Up T / Down G)",
                    "",JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/logo_small.png"),
                    null,"");;
            playerFourName = (String)JOptionPane.showInputDialog(this,
                    "Enter Player 4 Name (Up O / Down L)",
                    "",JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/logo_small.png"),
                    null,"");;
        }

        //1) initialize paddles: location (origin 0,0), ID, step increments, color, up/down keystrokes
        int paddleMoveStepIncrements = 5; //allocates pixel size for move - larger number, bigger jump visually so
        //don't want number to be too large.

        int defaultPaddleLength = 100;

        if(gameMode == twoPlayerMode){

            int paddle1Length = defaultPaddleLength;
            int paddle2Length = defaultPaddleLength;

            //Easter egg code :)
            if(playerOneName.equalsIgnoreCase("Amir")){
                paddle1Length = 25;
            }
            else if(playerTwoName.equalsIgnoreCase("Amir")){
                paddle2Length = 25;
            }

            if(playerOneName.equalsIgnoreCase("Nancy")){
                paddle1Length = 200;
            }
            else if(playerTwoName.equalsIgnoreCase("Nancy")){
                paddle2Length = 200;
            }

            //Note: paddle1 was declared in line 38 and these parameters are passed to it. REMEMBER that each paddle
            // is an instance
            //of Paddle.
            paddle1 = new Paddle(10, 25, 1, paddleMoveStepIncrements, Color.red, KeyEvent.VK_W,
                    KeyEvent.VK_S, paddle1Length);

            paddle2 = new Paddle(980, 25, 2, paddleMoveStepIncrements, Color.blue, KeyEvent.VK_UP,
                    KeyEvent.VK_DOWN, paddle2Length);
        }
        else{
            paddle1 = new Paddle(10, 25, 1, paddleMoveStepIncrements, Color.red, KeyEvent.VK_W,
                    KeyEvent.VK_S, defaultPaddleLength);
            //paddle1 was declared in line 32 and these parameters are passed to it. REMEMBER that each paddle
            // is an instance
            //of Paddle.
            paddle2 = new Paddle(980, 25, 2, paddleMoveStepIncrements, Color.blue, KeyEvent.VK_UP,
                    KeyEvent.VK_DOWN, defaultPaddleLength);
            paddle3 = new Paddle(30, 25, 3, paddleMoveStepIncrements, Color.orange, KeyEvent.VK_T,
                    KeyEvent.VK_G, defaultPaddleLength);
            paddle4 = new Paddle(960, 25, 4, paddleMoveStepIncrements, Color.magenta, KeyEvent.VK_O,
                    KeyEvent.VK_L, defaultPaddleLength);
        }

        //Straightforward - enables control of paddles via defined keystrokes: registering each paddle to receive
        // key events (keystrokes) addKeyListener is a method inherited from JFrame.

        if(gameMode == twoPlayerMode) {
            this.addKeyListener(paddle1); //"this" refers to the current class.
            this.addKeyListener(paddle2);
        }
        else {
            this.addKeyListener(paddle1); //"this" refers to the current class.
            this.addKeyListener(paddle2);
            this.addKeyListener(paddle3);
            this.addKeyListener(paddle4);
        }

        //NOTE**Remember that when you instantiate a class it becomes what we call an "object"

        //2) Initialize ball
        int ballMoveStepIncrements = 5; //can change this to increase/decrease speed
        int ballWidth = 12;
        int ballHeight = 12;
        ball = new Ball(250, 200, ballWidth, ballHeight, ballMoveStepIncrements, Color.black, paddle1, paddle2,
                paddle3, paddle4);

        //3) Initialize threads
        //We are creating a new Thread (below) for each object that will handle that object. Thread is a class.
        //The Thread constructor takes a Runnable object as a parameter. It has to have it to do work.
        //Important Note:
        //The Ball and Paddle classes implement the Runnable interface - that is why we are able to pass them as a
        //parameter into the Thread constructor.

        ballThread = new Thread(ball); //Must have runnable for thread to execute - ball, paddle1, paddle2, paddle3,
        //paddle4 are all instances of classes with runnables

        if(gameMode == twoPlayerMode) {
            paddle1Thread = new Thread(paddle1); //Passing an object of type runnable
            paddle2Thread = new Thread(paddle2); //The Thread constructor that we are using takes only 1 parameter of
            // type Runnable. The Paddle and Ball class both implement the runnable interface.
        }
        else{
            paddle1Thread = new Thread(paddle1); //Passing an object of type runnable
            paddle2Thread = new Thread(paddle2);
            paddle3Thread = new Thread(paddle3);
            paddle4Thread = new Thread(paddle4);
        }

        //4) Initialize JFrame - setting the various aspects of the window that contains our game
        this.setTitle("TEKcamp Pong!"); //The "this" keyword points to an instant of a class.
        this.setSize(screenSize);
        this.setResizable(false);
        this.setVisible(true); // This line is the code that actually makes the window appear when starting the program.
        this.setBackground(Color.CYAN);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void startGame() {
        //start threads that run parallel with main. Thread.start() method causes this thread to begin execution, the
        // Java Virtual Machine calls the run method of this thread. The result is that two threads are running
        // concurrently: the current thread (which returns from the call to the start method) and the other thread
        // (which executes its run method). In this game - we actually have multiple threads depending on how many
        //paddles are being used in addition to the ball thread.

        ballThread.start();

        if(gameMode == twoPlayerMode) {
            paddle1Thread.start();
            paddle2Thread.start();
        }
        else { //fourPlayerMode
            paddle1Thread.start();
            paddle2Thread.start();
            paddle3Thread.start();
            paddle4Thread.start();
        }
    }

    // Method override so can give its own implementation of paint method

    @Override
    public void paint(Graphics g) { //Method from Graphics class in java with g a variable of the Graphics class.
        blankCanvas = createImage(getWidth(), getWidth()); //getWidth and getHeight are methods being passed. paint is
        // called by java automatically whenever it needs to refresh the screen. At each frame - each step needs to
        //be redrawn and java calls paint
        graphics = blankCanvas.getGraphics();
        draw(graphics); //invokes draw method with graphics as the parameter - this is the method draw (below)
        g.drawImage(blankCanvas, 0, 0, this);

    }

    //methods provided by Graphics class in java
    public void draw(Graphics g) { //Redrawing everything! Coordinates are constantly changing - too fast to see.

        //Draw the background image and center it.
        int x = (getWidth() - backgroundImage.getWidth()) / 2;
        int y = (getHeight() - backgroundImage.getHeight()) / 2;
        g.drawImage(backgroundImage, x, y, this);

        ball.draw(g); //draw ball

        if(gameMode == twoPlayerMode) {
            paddle1.draw(g);
            paddle2.draw(g);
        }
        else{
            paddle1.draw(g);
            paddle2.draw(g);
            paddle3.draw(g);
            paddle4.draw(g);
        }

        Font f1 = new Font("Dialogue", Font.BOLD, 22); //Font for Scores
        Font f2 = new Font("Dialogue", Font.BOLD, 26); //Font for Player Labels

        g.setColor(Color.BLACK); //sets font color to black
        g.setFont(f1);           //sets to attributes defined for variable f1

        //drawstring takes as parameters an instance of the String class containing the text to be drawn, and
        // two integer values specifying the coordinates where the text should start.
        g.drawString("" + ball.getTeam1Score(), 175, 750);
        //Graphics class drawstring writes score on GUI
        g.drawString("" + ball.getTeam2Score(), 800, 750);
        //Graphics class drawstring writes score on GUI
        g.setFont(f2);

        if(gameMode == twoPlayerMode) {
            g.drawString("Player: " + playerOneName, 95, 700); //Added Label for Paddles 1
            g.drawString("Player: " + playerTwoName, 650, 700); //Added Label for Paddles 2
        }
        else {
            g.drawString("Players: " + playerOneName + "/" + playerThreeName, 95, 700); //Added Label for Paddles 1-2
            g.drawString("Players: " + playerFourName + "/" + playerTwoName, 650, 700); //Added Label for Paddles 3-4
        }


        repaint(); //updates then calls paint method from Graphics class
    }

    //Main method - entry point for java to execute
    //param args

    public static void main(String[] args) {

        //To start the UI properly, we need to make sure it is running on the EDT Thread: Swing event handling code
        // runs on a special thread known as the event dispatch thread. Most code that invokes Swing methods also
        // runs on this thread. This is necessary because most Swing object methods are not "thread safe": invoking
        // them from multiple threads risks thread interference or memory consistency errors.
        //To do that, we use the following lambda expression to start our game:
        SwingUtilities.invokeLater(() -> { //lambda expression
            //The SwingUtilities.invokeLater() method is an extremely important method to know about if you are
            // writing a Java application that uses multithreading and your program uses Swing for its user interface.

            //Creating a new instance of the GameDriver class. This new instance is called "pongGame"
            //We create a new instance by calling the constructor of the class "GameDriver"
            //The constructor of a class ALWAYS carries the same name as the class.
            //Note that in this case, the constructor for the GameDriver class does not take any parameter.

            GameDriver pongGame = new GameDriver(); //We created a new instance of GameDriver called pongGame
            //Now that we have created this instance, we are ready to "start" the game.
            pongGame.startGame();
        });


        //Line 323 lambda expression is equivalent to the following code snippet:
        //SwingUtilities.invokeLater(new Runnable() {
        //public void run () {
        //GameDriver pongGame = new GameDriver();
        //pongGame.startGame();
        //}
        // });
    }
}


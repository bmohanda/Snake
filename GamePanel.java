import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    /* FRAME VARIABLES */
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    // calculates how many "blocks" the game will have by getting the total area of pixels and
    // dividing by the area of each "block"
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    // higher the int, slower the game
    static final int DELAY = 75;

    /* SNAKE VARIABLES */
    // stores the x coordinates of the snake body
    final int x[] = new int[GAME_UNITS];
    // stores the y coordinates of the snake body
    final int y[] = new int[GAME_UNITS];
    // starting size of snake body parts, will change as applesEaten changes
    int bodyParts = 6;
    // starting direction for snake to move in is right...will be changed throughout duration of program
    // 4 choices: R = right, L = left, U = up, D = down
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    /* APPLE VARIABLES */
    int applesEaten = 0;
    // x and y (randomly generated) for new apple to appear at appropriate call
    int appleX;
    int appleY;

    /* constructor performs these operations before the game begins */
    public GamePanel() {
        random = new Random();
        // creating starting size of panel as the chosen screen width and height
        // placed in a Dimension object because those are the acceptable parameters of setPreferredSize()
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    /* initiates the first apple, starts timer and defines game as running */
    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /* draws apples and grid lines */
    public void draw(Graphics g) {
        if (running) {
            // Optional grid! helps to approximate grid math; user can remove for loop to remove grid lines
            /* for (int i = 0; i < (SCREEN_HEIGHT / UNIT_SIZE); i++) {
                g.drawLine((i * UNIT_SIZE), 0, (i * UNIT_SIZE), SCREEN_HEIGHT);
                g.drawLine(0, (i * UNIT_SIZE), SCREEN_WIDTH, (i * UNIT_SIZE));
            } */

            // drawing apple
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // drawing snake
            for (int i = 0; i < bodyParts; i++) {
                // head is a different color than the body
                if (i == 0) {
                    g.setColor(Color.WHITE);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    if (i % 6 == 1) {
                        g.setColor(Color.RED);
                    }
                    else if (i % 6 == 2) {
                        g.setColor(Color.ORANGE);
                    }
                    else if (i % 6 == 3) {
                        g.setColor(Color.YELLOW);
                    }
                    else if (i % 6 == 4) {
                        g.setColor(Color.GREEN);
                    }
                    else if (i % 6 == 5) {
                        g.setColor(Color.BLUE);
                    }
                    else {
                        g.setColor(Color.MAGENTA);
                    }
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            // Displays score near top of game
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(("Score: " + applesEaten),
                    ((SCREEN_WIDTH - metrics.stringWidth( ("Score: " + applesEaten) )) / 2),
                    (g.getFont().getSize()));

        }
        else {
            gameOver(g);
        }
    }

    /* changes coordinates of apple object randomly */
    public void newApple() {
        // cast to an int to justify the random decimal; multiply by unit size so pixels are correctly
        // oriented with grid/unit boxing
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }


    /* moves the snake by default in a line OR as per users key presses */
    public void move() {
        // whenever the snake is rendered, will be done with these updated values where x&y have been shifted
        // shifts by 1 pixel at a time to seem
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        // changes the coordinates of snake based off of direction's value (which is updated based on key press)
        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    /* checking if any apples have been eaten (to update score) */
    public void checkApple() {
        if ( (x[0] == appleX) && (y[0] == appleY) ) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    /* checking to see if the snake has collided with itself (to end game if so) */
    public void checkCollisions() {
        // checks to see if the x coordinate of the head is equivalent to the x coordinate of any body part
        for (int i = bodyParts; i > 0; i--) {
            // if the coordinates are the same, that means they are touching/have collided
            //if ( (x[0] == x[i]) && (y[0] == y[i]) ) {
            //    running = false;
            if ( (x[0] < 0 && (y[0] < 0) )) {
                running = false;
            }
        }
        // checks if head has ran off horizontal axis
        if ( (x[0] < 0) | (x[0] > SCREEN_WIDTH) ) {
            running = false;
        }
        // checks if head has ran off vertical axis
        if ( (y[0] < 0) | (y[0] > SCREEN_HEIGHT) ) {
            running = false;
        }

        // if above checks made running = false, then timer must reset and game should be over.
        if (!running) {
            timer.stop();
        }
    }

    // finishes game and displays end of game screen
    public void gameOver(Graphics g) {
        // set up Score text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString(("Score: " + applesEaten),
                ((SCREEN_WIDTH - metrics1.stringWidth( ("Score: " + applesEaten) )) / 2), (g.getFont().getSize()));

        // Set up Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over!", ((SCREEN_WIDTH - metrics2.stringWidth("Game Over!")) / 2), (SCREEN_HEIGHT / 2));
    }

    // for every event, score and collisions are checked. Screen is then repainted.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    // Instantiating KeyAdapter to check for user key presses
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // updates direction of snake's movement based on keys pressed
            // will limit user to only 90 degree turns to avoid redundant collisions
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}

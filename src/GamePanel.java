import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 35;  // how big we want our objects in the game, unit or 'pixels' in the program
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;  // 'pixels' of the program
    static final int delay = 75;

    // create two arrays x,y to hold all coordinates for body and head of the snake
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 6;  // begins with 6 body parts
    int applesEaten;  // is zero
    int appleXCoordinate;
    int appleYCoordinate;
    char direction = 'R';  // snake begins facing right
    boolean isRunning = false;
    boolean isPaused = false;
    boolean isRestart = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        /* Let's say you have implemented a dialog with several text fields, and you want the user to enter some text.
           When the user starts typing, one text field needs to have the focus of the application:
           it will be the field that receives the keyboard input. */

        this.addKeyListener(new MyKeyAdaptor());
        // listener interface for receiving keyboard events

        startGame();
    }

    public void startGame() {
        newApple();
        isRunning = true;
        timer = new Timer(delay, this);
        timer.restart();
        isPaused = false;
    }

    public void restartGame() {
        if (!isRunning) {
            startGame();
            isRestart = true;
            direction = 'R';
            applesEaten = 0;
            bodyParts = 6;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (isPaused) {
            pausedScreen(g);  // need to implement this to work, does not show paused screen grr.
        }
        else if ((isRunning || isRestart) && !isPaused) {
            g.setColor(Color.red);
            g.fillOval(appleXCoordinate, appleYCoordinate, UNIT_SIZE, UNIT_SIZE);

            // create a for loop to iterate the body parts of the snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {  // head
                    g.setColor(new Color(180, 180, 180));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else if (i % 2 == 0) {
                    g.setColor(new Color(120, 30, 30));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(180, 30, 30));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Helvetica Bold", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        } else gameOver(g);
    }

    public void newApple() {
        for (int i = 0; i < bodyParts; i++) {
            if (appleXCoordinate == x[i] && appleYCoordinate == y[i]) {  // makes sure apple doesn't spawn 'on top' of snake body
                // generate coordinates for a new apple whenever one is eaten or creating a new game
                appleXCoordinate = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
                appleYCoordinate = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
            }
        }
    }

    public void move() {
        if (isRunning) {
            // iterate all the body parts for the snake
            for (int i = bodyParts; i > 0; i--) {
                x[i] = x[i - 1];  // shifting all coordinates in the array by one
                y[i] = y[i - 1];
            }
            // create a switch to change the direction which way the snake is going
            switch (direction) {
                case 'U' -> y[0] = y[0] - UNIT_SIZE;  // y[0] is the head of the snake, - UNIT_SIZE goes to next position
                case 'D' -> y[0] = y[0] + UNIT_SIZE;
                case 'L' -> x[0] = x[0] - UNIT_SIZE;
                case 'R' -> x[0] = x[0] + UNIT_SIZE;
            }
        }
    }


    public void checkApple() {
        if ((x[0]) == appleXCoordinate && (y[0]) == appleYCoordinate) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // check if head hits body, iterate through all body parts
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && y[0] == y[i]) {
                isRunning = false;
            }
        }
        // check if head touches left border
        if (x[0] < 0) {
            isRunning = false;
        }
        // check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            isRunning = false;
        }
        // check if head touches top border
        if (y[0] < 0) {
            isRunning = false;
        }
        // check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            isRunning = false;
        }
    }
    public void pauseGame() {
        isPaused = true;
        isRunning = false;
        timer.stop();
    }

    public void pausedScreen(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Helvetica Bold", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Paused", (SCREEN_WIDTH - metrics.stringWidth("Paused")) / 2, SCREEN_HEIGHT / 2);
    }

    public void gameOver(Graphics g) {
        // GAME OVER TEXT
        g.setColor(Color.red);
        g.setFont(new Font("Helvetica Bold", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        // font metrics are useful for lining up text at the centre of the screen
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 4);
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        g.setFont(new Font("Helvetica Bold", Font.BOLD, 40));
        metrics = getFontMetrics(g.getFont());
        g.drawString("Press Enter to play again", (SCREEN_WIDTH - metrics.stringWidth("Press Enter to play again")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdaptor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {  // keyboard 'left' is hit
                    if (direction != 'R') {  // limit the user to only 90 degree turns, otherwise snake will hit itself
                        direction = 'L';
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if (direction != 'L') {
                        direction = 'R';
                    }
                }
                case KeyEvent.VK_UP -> {
                    if (direction != 'D') {
                        direction = 'U';
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if (direction != 'U') {
                        direction = 'D';
                    }
                }
                case KeyEvent.VK_SPACE -> {
                    if (!isPaused) {
                        pauseGame();
                    } else {
                        startGame();
                    }
                }
                case KeyEvent.VK_ENTER -> {
                    restartGame();
                }
            }
        }
    }
}

/*
JPanel is a part of the Java Swing package that can store a group of components
    represents some area in which controls (buttons,...) and visuals (pictures,...) can appear
 */
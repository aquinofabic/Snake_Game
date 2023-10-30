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
    static final int UNIT_SIZE = 25;  // how big we want our objects in the game, unit or 'pixels' in the program
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int delay = 75;  // create two arrays x,y to hold all coordinates for body and head of the snake
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 6;  // begins with 6 body parts
    int applesEaten;  // is zero
    int appleXCoordinate;
    int appleYCoordinate;
    char direction = 'R';  // snake begins facing right
    boolean running = false;
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
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);  // vertical lines
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);  // horizontal lines
                // create a for loop to create more than one line
            }
            g.setColor(Color.red);
            g.fillOval(appleXCoordinate, appleYCoordinate, UNIT_SIZE, UNIT_SIZE);

            // create a for loop to iterate the body parts of the snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {  // head
                    g.setColor(new Color(0, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(0, 150, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        } else gameOver(g);
    }

    public void newApple() {
        // generate coordinates for a new apple whenever one is eaten or creating a new game
        appleXCoordinate = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleYCoordinate = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
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
                running = false;
            }
        }
        // check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        // check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        // check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
    }

    public void gameOver(Graphics g) {
        // GAME OVER TEXT
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        // font metrics are useful for lining up text at the centre of the screen
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
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
            }
        }
    }
}

/*
JPanel is a part of the Java Swing package that can store a group of components
    represents some area in which controls (buttons,...) and visuals (pictures,...) can appear

TO DO:
change body of snake so there's alternating green and other colour pattern!!
make sure apple doesnt spawn on the same coordinate as the snake!!
change apple image?
change snake head image?
 */
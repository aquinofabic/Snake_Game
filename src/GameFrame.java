import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame() {

        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

/*
JFrame is a top level container that provides a window on the screen, part of the Java Swing package
    represents a framed window
 */
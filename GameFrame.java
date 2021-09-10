import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        // creates a new GamePanel and adds it to every GameFrame object created
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        // set comparative location to nothing so frame appears in middle of screen
        this.setLocationRelativeTo(null);

    }

}

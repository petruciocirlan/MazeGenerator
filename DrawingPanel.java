import Maze.Maze;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawingPanel extends JPanel {
    final MainFrame frame;
    final static int W = 800, H = 600;

    BufferedImage image;    // the offscreen image
    Graphics2D graphics;    // the "tools" needed to draw in the image

    public DrawingPanel(MainFrame frame) {
        this.frame = frame;
        createOffscreenImage();
        init();
    }

    public void createOffscreenImage() {
        image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        clear();
    }

    private void init() {
        setPreferredSize(new Dimension(W, H));
        setBorder(BorderFactory.createEtchedBorder());

//        this.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
////                switch ((String) frame.configPanel.modeCombo.getSelectedItem()) {
////                    case "Add" -> drawShape(e.getX(), e.getY());
////                    case "Remove" -> deleteShape(e.getX(), e.getY());
////                }
////                repaint();
//            } //Can’t use lambdas, JavaFX does a better job in these cases
//        });
    }

    public void paintMaze() {
        clear();
        Maze maze = frame.mazeFactory.getMaze();
        if (maze != null) {
            // TODO

        }
    }

    public void clear() {
        graphics.setColor(Color.WHITE); //fill the image with white
        graphics.fillRect(0, 0, W, H);
        repaint();
    }
}

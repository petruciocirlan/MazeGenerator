package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.List;

import Maze.MazeCoordinate;
import Maze.Node;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

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
        setPreferredSize(new Dimension(W, H));  //don’t use setSize. Why?
        setBorder(BorderFactory.createEtchedBorder());  // for fun
    }

    public void animateMaze(boolean animated) {
        clear();

        frame.maze = frame.mazeFactory.getMaze();

        if (animated) {
            paintMaze();
            int animationDuration = 3000 / frame.maze.getDestroyedWallCount();
            new Timer(animationDuration, e -> {
                List<MazeCoordinate> updatedNodes = frame.maze.next();
                if (updatedNodes == null) {
                    frame.controlPanel.btnGenerate.setEnabled(true);
                    frame.controlPanel.btnSave.setEnabled(true);
//                    frame.controlPanel.btnExport.setEnabled(true);
//                    frame.controlPanel.btnImport.setEnabled(true);

                    ((Timer)e.getSource()).stop();
                } else {
                    frame.controlPanel.btnGenerate.setEnabled(false);
                    frame.controlPanel.btnSave.setEnabled(false);
//                frame.controlPanel.btnExport.setEnabled(false);
//                frame.controlPanel.btnImport.setEnabled(false);
                    for (MazeCoordinate mazeCoordinate : updatedNodes) {
                        paintUpdate(mazeCoordinate);
                    }
                }
            }).start();
        } else {

            while (true) {
                if (frame.maze.next() == null) break;
            }

            paintMaze();
        }
    }

    public void paintMaze() {
        clear();

        int level;
        synchronized (frame.lock) {
            level = frame.mazeLevel;
        }
        Node[][] matrix = frame.maze.getMazeLevelAsMatrix(level);

        int padding = 5;
        int cellSize = min((H - 2 * padding) / frame.maze.getHeight(), (W - 2 * padding) / frame.maze.getWidth());
        int matrixWidth = cellSize * frame.maze.getWidth();
        int matrixHeight = cellSize * frame.maze.getHeight();

        int marginTop = (H - matrixHeight) / 2;
        int marginLeft = (W - matrixWidth) / 2;

        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                drawNode(matrix[y][x], cellSize, marginTop + cellSize * y, marginLeft + cellSize * x);
            }
        }
        repaint();
    }

    public void paintUpdate(MazeCoordinate mazeCoordinate) {
        int level;
        synchronized (frame.lock) {
            level = frame.mazeLevel;
        }

        if (level != mazeCoordinate.getZ()) { return; }

        int padding = 5;
        int cellSize = min((H - 2 * padding) / frame.maze.getHeight(), (W - 2 * padding) / frame.maze.getWidth());
        int matrixWidth = cellSize * frame.maze.getWidth();
        int matrixHeight = cellSize * frame.maze.getHeight();

        int marginTop = (H - matrixHeight) / 2;
        int marginLeft = (W - matrixWidth) / 2;

        graphics.setColor(Color.WHITE);
        graphics.fillRect(marginLeft + cellSize * mazeCoordinate.getX() - padding,
                marginTop + cellSize * mazeCoordinate.getY() - padding,
                cellSize + 2 * padding, cellSize + 2 * padding);

        int lowerY = max(mazeCoordinate.getY() - 1, 0);
        int upperY = min(mazeCoordinate.getY() + 1, frame.maze.getHeight() - 1);

        int lowerX = max(mazeCoordinate.getX() - 1, 0);
        int upperX = min(mazeCoordinate.getX() + 1, frame.maze.getWidth() - 1);

        for (int y = lowerY; y <= upperY; y++) {
            for (int x = lowerX; x <= upperX; x++) {
                Node node = frame.maze.getNodeByCoordinate(new MazeCoordinate(x, y, level));
                drawNode(node, cellSize, marginTop + cellSize * y, marginLeft + cellSize * x);
            }
        }

        repaint(marginLeft + cellSize * mazeCoordinate.getX() - padding,
                marginTop + cellSize * mazeCoordinate.getY() - padding,
                cellSize + 2 * padding, cellSize + 2 * padding);
    }

    private void drawNode(Node node, int cellSize, int marginTop, int marginLeft) {
        graphics.setColor(Color.BLACK);

        boolean[] walls = node.getWalls();
        int levelWallCount = walls.length - 2;

        Coordinate center = new Coordinate(marginLeft + cellSize / 2.0, marginTop + cellSize / 2.0);

        int startingDegrees = 135;
        int stepDegrees = -90;

        double radius = cellSize / 2.0;
        radius = (int)Math.sqrt(radius * radius * 2.0);
//        switch (levelWallCount) {
//            case 4 -> {
//                startingDegrees = 135;
//                stepDegrees = -90;
//                radius = (int)Math.sqrt(radius * radius * 2.0);
//            }
//            case 6 -> {
//                startingDegrees = 120;
//                stepDegrees = -60;
//            }
//        }

        float strokeWidth = cellSize * 0.15f;
        graphics.setStroke(new BasicStroke(strokeWidth));
        int currentDegrees = startingDegrees;
        for (int wallIndex = 0; wallIndex < levelWallCount; wallIndex++) {

            Coordinate[] edgeCoordinates = new Coordinate[2];

            for (int indexEndpoint = 0; indexEndpoint < 2; indexEndpoint++) {
                double radians = - (currentDegrees * Math.PI / 180.0);

                double sine = Math.sin(radians);
                double cosine = Math.cos(radians);

                edgeCoordinates[indexEndpoint] = new Coordinate(center.posX + cosine * radius, center.posY + sine * radius);

                currentDegrees += stepDegrees;
            }
            currentDegrees -= stepDegrees;

            if (walls[wallIndex]) {
                drawEdge(edgeCoordinates[0], edgeCoordinates[1]);
            }

            if (currentDegrees <= startingDegrees - 360) {
                break;
            }
        }

        strokeWidth /= 2;
        graphics.setStroke(new BasicStroke(strokeWidth));


        Coordinate middleTop = new Coordinate();
        middleTop.posX = center.posX;
        middleTop.posY = marginTop + cellSize / 10.0 * 2.5;

        Coordinate middleBottom = new Coordinate();
        middleBottom.posX = center.posX;
        middleBottom.posY = marginTop + cellSize / 10.0 * 7.5;

        if (!walls[walls.length - 2] || !walls[walls.length - 1]) {
            drawEdge(middleBottom, middleTop);
        }

        if (!walls[walls.length - 2]) { // DOWN
            Coordinate arrowLeft = new Coordinate();
            arrowLeft.posX = center.posX - cellSize / 10.0;
            arrowLeft.posY = center.posY + cellSize / 10.0;

            Coordinate arrowRight = new Coordinate();
            arrowRight.posX = center.posX + cellSize / 10.0;
            arrowRight.posY = center.posY + cellSize / 10.0;

            drawEdge(arrowLeft, middleBottom);
            drawEdge(arrowRight, middleBottom);
        }

        if (!walls[walls.length - 1]) { // UP
            Coordinate arrowLeft = new Coordinate();
            arrowLeft.posX = center.posX - cellSize / 10.0;
            arrowLeft.posY = center.posY - cellSize / 10.0;

            Coordinate arrowRight = new Coordinate();
            arrowRight.posX = center.posX + cellSize / 10.0;
            arrowRight.posY = center.posY - cellSize / 10.0;

            drawEdge(arrowLeft, middleTop);
            drawEdge(arrowRight, middleTop);
        }
    }

    private void drawEdge(Coordinate endpointA, Coordinate endpointB) {
        graphics.draw(new Line2D.Double(endpointA.posX, endpointA.posY, endpointB.posX, endpointB.posY));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public void clear() {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, W, H);
        repaint();
    }
}

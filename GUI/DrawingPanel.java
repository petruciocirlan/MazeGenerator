package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import Maze.Node;

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

    public void animateMaze() {
        clear();

        frame.maze = frame.mazeFactory.getMaze();
//        paintMaze(maze);

        int animationDuration = 3000 / frame.maze.getDestroyedWallCount();
        new Timer(animationDuration, e -> {
            frame.controlPanel.btnGenerate.setEnabled(false);
            frame.controlPanel.btnSave.setEnabled(false);
            frame.controlPanel.btnExport.setEnabled(false);
            frame.controlPanel.btnImport.setEnabled(false);

            frame.controlPanel.btnLevelUp.setEnabled(false);
            frame.controlPanel.btnLevelDown.setEnabled(false);

            if (!frame.maze.next()) {
                frame.controlPanel.btnGenerate.setEnabled(true);
                frame.controlPanel.btnSave.setEnabled(true);
                frame.controlPanel.btnExport.setEnabled(true);
                frame.controlPanel.btnImport.setEnabled(true);

                if (frame.maze.getDepth() > 1) {
                    if (frame.mazeLevel + 1 < frame.maze.getDepth()) {
                        frame.controlPanel.btnLevelUp.setEnabled(true);
                    }
                    if (frame.mazeLevel - 1 >= 0) {
                        frame.controlPanel.btnLevelDown.setEnabled(true);
                    }
                }
                ((Timer)e.getSource()).stop();
            }

            // TODO(@petru): instead of repainting whole maze, paint only immediate section surrounding modified node

            paintMaze();
        }).start();
    }

    public void paintMaze() {
        clear();

        Node[][] matrix = frame.maze.getMazeLevelAsMatrix(frame.mazeLevel);
        // TODO(@petru): take into account levels
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

    private void drawNode(Node node, int cellSize, int marginTop, int marginLeft) {
        graphics.setColor(Color.BLACK);

        boolean[] walls = node.getWalls();
        int levelWallCount = walls.length - 2;

        int cellCenterPosX = marginLeft + cellSize / 2;
        int cellCenterPosY = marginTop + cellSize / 2;

        double radius = cellSize / 2.0;
        int startingDegrees = 0;
        int stepDegrees = -360;
        switch (levelWallCount) {
            case 4 -> {
                startingDegrees = 135;
                stepDegrees = -90;
                radius = (int)Math.sqrt(radius * radius * 2.0);
            }
            case 6 -> {
                startingDegrees = 120;
                stepDegrees = -60;
            }
        }

        float strokeWidth = cellSize * 0.15f;
        graphics.setStroke(new BasicStroke(strokeWidth));
//        System.out.println(Arrays.toString(walls));
        int currentDegrees = startingDegrees;
        for (int wallIndex = 0; wallIndex < levelWallCount; wallIndex++) {

            Coordinate[] edgeCoordinates = new Coordinate[2];

            for (int indexEndpoint = 0; indexEndpoint < 2; indexEndpoint++) {
                double radians = - (currentDegrees * Math.PI / 180.0);

                double sine = Math.sin(radians);
                double cosine = Math.cos(radians);

                edgeCoordinates[indexEndpoint] = new Coordinate();
                edgeCoordinates[indexEndpoint].posX = cellCenterPosX + cosine * radius;
                edgeCoordinates[indexEndpoint].posY = cellCenterPosY + sine * radius;

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

        Coordinate center = new Coordinate(marginLeft + cellSize / 2.0, marginTop + cellSize / 2.0);

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
//        System.out.printf("Drawing line from: (%d, %d) to (%d, %d)\n", endpointA.posX, endpointA.posY, endpointB.posX, endpointB.posY);
        graphics.draw(new Line2D.Double(endpointA.posX, endpointA.posY, endpointB.posX, endpointB.posY));
//        graphics.drawLine(endpointA.posX, endpointA.posY, endpointB.posX, endpointB.posY);
    }

    @Override
    public void update(Graphics g) { }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public void clear() {
        graphics.setColor(Color.WHITE); //fill the image with white
        graphics.fillRect(0, 0, W, H);
        repaint();
    }
}

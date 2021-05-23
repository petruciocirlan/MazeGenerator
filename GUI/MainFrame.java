package GUI;

import Maze.Maze;
import Maze.MazeFactory;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    ConfigPanel configPanel;
    ControlPanel controlPanel;
    DrawingPanel canvas;

    MazeFactory mazeFactory;
    Maze maze;

    Integer mazeLevel;
    final Object lock = new Object();

    public MainFrame(MazeFactory mazeFactory) {
        super("MazeGenerator");
        this.mazeFactory = mazeFactory;
        init();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // create the components
        configPanel = new ConfigPanel(this);
        controlPanel = new ControlPanel(this);
        canvas = new DrawingPanel(this);

        // arrange the components in the container (frame)
        // JFrame uses a BorderLayout by default
        add(configPanel, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // invoke the layout manager
        pack();
    }
}

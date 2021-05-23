package GUI;

import javax.swing.*;

public class ConfigPanel extends JPanel {
    final MainFrame frame;

    JLabel labelMazeSize;
    JSpinner spinnerMazeWidth;
    JSpinner spinnerMazeHeight;
    JSpinner spinnerMazeLevels; // applies only for 3D
    JComboBox<String> comboMazeDimensions;   // e.g. 2D, 3D
    JComboBox<String> comboTessellation;   // applies only for 2D; e.g. Orthogonal, Delta, Sigma etc.
    JComboBox<String> comboAlgorithm;   // Kruskal, Prim
    JComboBox<String> comboAnimation;   // animated or instant

    public ConfigPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        labelMazeSize = new JLabel("Maze H/W/D:");
        spinnerMazeWidth = new JSpinner(new SpinnerNumberModel(25, 3, 50, 1));
        spinnerMazeHeight = new JSpinner(new SpinnerNumberModel(20, 3, 50, 1));
        spinnerMazeLevels = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));

        comboMazeDimensions = new JComboBox<>(new String[] {"2D", "3D"});
        comboTessellation = new JComboBox<>(new String[] {"Orthogonal", "Delta", "Sigma", "Theta"});
        comboAlgorithm = new JComboBox<>(new String[] {"Kruskal", "Prim"});
        comboAnimation = new JComboBox<>(new String[] {"Animated", "Instant"});

        add(labelMazeSize);
        add(spinnerMazeWidth);
        add(spinnerMazeHeight);
        add(spinnerMazeLevels);
        spinnerMazeLevels.setEnabled(false);

        add(comboMazeDimensions);
        add(comboTessellation);
        add(comboAlgorithm);
        add(comboAnimation);
//        comboTessellation.setEnabled(true);
        comboTessellation.setEnabled(false);

        comboMazeDimensions.addActionListener(e -> eventMazeDimensions());
    }

    private void eventMazeDimensions() {
        String selection = (String)comboMazeDimensions.getSelectedItem();

        assert selection != null;
        spinnerMazeLevels.setEnabled(selection.equals("3D"));
//        comboTessellation.setEnabled(selection.equals("2D"));

        if (!selection.equals("2D")) {
            comboTessellation.setSelectedIndex(0);
        }

        if (!selection.equals("3D")) {
            spinnerMazeLevels.setValue(1);
        }
    }
}

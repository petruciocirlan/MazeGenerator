package GUI;

import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class ControlPanel extends JPanel {
    final MainFrame frame;
    JButton btnGenerate = new JButton("Generate");
    JButton btnLevelUp = new JButton("UP");
    JButton btnLevelDown = new JButton("DOWN");

    JButton btnSave = new JButton("Save");
    JButton btnExport = new JButton("Export");
    JButton btnImport = new JButton("Import");
    JButton btnExit = new JButton("Exit");

    public ControlPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    public void init() {
        setLayout(new GridLayout(1, 3));

//        btnGenerate.setBackground();

        add(btnGenerate);
        add(btnLevelUp);
        add(btnLevelDown);
        btnLevelUp.setEnabled(false);
        btnLevelDown.setEnabled(false);

        add(btnSave);
        add(btnExport);
        add(btnImport);
        add(btnExit);

        //configure listeners for all buttons
        btnGenerate.addActionListener(this::generateMaze);
        btnLevelUp.addActionListener(this::showNextMazeLevel);
        btnLevelDown.addActionListener(this::showPrevMazeLevel);


        btnSave.addActionListener(this::save);
        btnExport.addActionListener(this::exportMaze);
        btnImport.addActionListener(this::importMaze);
        btnExit.addActionListener(this::exit);

        btnSave.setEnabled(false);
        btnExport.setEnabled(false);
    }

    private void generateMaze(ActionEvent e) {
        String algorithm = (String)frame.configPanel.comboAlgorithm.getSelectedItem();
        String dimensions = (String)frame.configPanel.comboMazeDimensions.getSelectedItem();
        String type = (String)frame.configPanel.comboTessellation.getSelectedItem();
        int width = (int)frame.configPanel.spinnerMazeWidth.getValue();
        int height = (int)frame.configPanel.spinnerMazeHeight.getValue();
        int levels = (int)frame.configPanel.spinnerMazeLevels.getValue();

        assert dimensions != null;
        frame.mazeFactory.create(algorithm, dimensions, type, width, height, levels);

        frame.mazeLevel = 0;
        frame.canvas.animateMaze();
    }

    private void showNextMazeLevel(ActionEvent e) {
        if (frame.maze != null) {
            if (frame.mazeLevel + 1 < frame.maze.getDepth()) {
                frame.mazeLevel++;
                btnLevelDown.setEnabled(true);

                if (frame.mazeLevel + 1 >= frame.maze.getDepth()) {
                    btnLevelUp.setEnabled(false);
                }

                frame.canvas.paintMaze();
            }
        }
    }

    private void showPrevMazeLevel(ActionEvent e) {
        if (frame.maze != null) {
            if (frame.mazeLevel - 1 >= 0) {
                frame.mazeLevel--;
                btnLevelUp.setEnabled(true);

                if (frame.mazeLevel == 0) {
                    btnLevelDown.setEnabled(false);
                }


                frame.canvas.paintMaze();
            }
        }
    }

    private void save(ActionEvent e) {
        if (frame.mazeFactory.getMaze() != null) {
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (!FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("png")) {
                    file = new File(file + ".png");
                }

                try {
                    ImageIO.write(frame.canvas.image, "PNG", file);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    private void exportMaze(ActionEvent e) {
        // TODO
//        try {
////            ImageIO.write(frame.canvas.image, "PNG", new File("D:/test.png"));
//
//            JFileChooser chooser = new JFileChooser();
//            int returnVal = chooser.showOpenDialog(frame);
//            if (returnVal == JFileChooser.APPROVE_OPTION) {
//                FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile().getAbsolutePath());
//                ObjectOutputStream out = new ObjectOutputStream(fos);
//                out.writeObject(frame.canvas.shapes);
//                out.flush();
//                fos.close();
//            }
//        }
//        catch (IOException ex)
//        {
//            System.err.println(ex.toString());
//        }
    }

    private void importMaze(ActionEvent e) {
        // TODO
//        try {
//            JFileChooser chooser = new JFileChooser();
//            int returnVal = chooser.showOpenDialog(frame);
//            if (returnVal == JFileChooser.APPROVE_OPTION) {
//                FileInputStream fis = new FileInputStream(chooser.getSelectedFile().getAbsolutePath());
//                ObjectInputStream in = new ObjectInputStream(fis);
//                frame.canvas.shapes = (List<ColouredShape>) in.readObject();
//                fis.close();
//
//                frame.canvas.drawAll();
//            }
//        } catch (FileNotFoundException fileNotFoundException) {
////            fileNotFoundException.printStackTrace();
////            return;
//        } catch (IOException | ClassNotFoundException exception) {
//            exception.printStackTrace();
//        }
    }

    private void exit(ActionEvent e) {
        System.exit(0);
    }
}

package Maze;

public class MazeFactory {
    private Maze maze;

    public MazeFactory() {}

    public void create(String dimensions, String type, int width, int height, int levels) {
        switch (dimensions) {
            case "2D" -> create2D(type, width, height);
            case "3D" -> create3D(width, height, levels);
            case "Weave" -> createWeave(width, height);
        }
    }

    private void create2D(String type, int width, int height) {

    }

    private void create3D(int width, int height, int levels) {

    }

    private void createWeave(int width, int height) {

    }

    public Maze getMaze() {
        return maze;
    }
}

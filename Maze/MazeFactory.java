package Maze;

public class MazeFactory {
    private Maze maze;

    public MazeFactory() {}

    public void create(String algorithm, String dimensions, String type, int width, int height, int levels) {
        switch (dimensions) {
            case "2D" -> create2D(type, width, height);
            case "3D" -> create3D(width, height, levels);
        }

        maze.generate(algorithm);
    }

    private void create2D(String type, int width, int height) {
//        switch (type) {
//            case "Orthogonal" -> maze = new OrthogonalMaze(width, height, 1);
//            case "Delta" -> {}
//            case "Sigma" -> {}
//            case "Theta" -> {}
//        }
        assert type.equals("Orthogonal");
        maze = new OrthogonalMaze(width, height, 1);
    }

    private void create3D(int width, int height, int levels) {
        maze = new OrthogonalMaze(width, height, levels);
    }

    public Maze getMaze() {
        return maze;
    }
}

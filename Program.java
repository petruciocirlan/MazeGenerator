import Maze.MazeFactory;

public class Program {
    static public void main(String[] args) {
        MazeFactory mazeFactory = new MazeFactory();
        MainFrame frame = new MainFrame(mazeFactory);
        frame.setVisible(true);
    }
}

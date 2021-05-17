package Maze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Maze {
    List<Node> nodes = new ArrayList<>();
    Map<Integer, List<Integer>> graph = new HashMap<>();
    Integer width;
    Integer height;
//    Integer levels;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
//        this.levels = levels;
    }

    abstract public void generate();
}

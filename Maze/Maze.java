package Maze;

import java.util.*;

public abstract class Maze {
    protected List<Node> nodes = new ArrayList<>();
    protected Map<Integer, List<Integer>> graph = new HashMap<>();
    protected Random random = new Random(System.currentTimeMillis());
    protected PathGenerator pathGenerator;

    protected int width;
    protected int height;
    protected int levels;

    protected int nodeEntry;
    protected int nodeExit;
    protected int wallCount;

    public Maze(int width, int height, int levels) {
        this.width = width;
        this.height = height;
        this.levels = levels;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getDepth() { return levels; }

    protected void generate(String algorithm) {
        generateShape();
        generatePaths(algorithm);
    }

    protected void generatePaths(String algorithm) {
        switch (algorithm) {
            case "Kruskal" -> pathGenerator = new KruskalPathGenerator(this);
            case "Prim" -> pathGenerator = null;
        }

        assert pathGenerator != null;
        pathGenerator.generatePaths();
    }

    public boolean next() {
        return pathGenerator.next();
    }

    public int getDestroyedWallCount() {
        return pathGenerator.getDestroyedWallCount();
    }
//    abstract protected void generateWinningPath();

//    abstract protected List<Integer> getRandomWeightedDirections(int currentNode);
//    abstract protected void connectRemainingNodes();


    abstract protected void generateShape();
    abstract protected int getRandomMarginNodeId();

    abstract public Node[][] getMazeLevelAsMatrix(int mazeLevel);
    abstract protected boolean isInsideMatrix(int x, int y, int z);
}

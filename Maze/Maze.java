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
            case "Prim" -> pathGenerator = new PrimPathGenerator(this);
        }

        assert pathGenerator != null;
        pathGenerator.generatePaths();
    }

    public List<MazeCoordinate> next() {
        Wall wall = pathGenerator.next();
        if (wall == null) { return null; }

        List<MazeCoordinate> updatedNodes = new ArrayList<>();
        updatedNodes.add(getMazeCoordinate(wall.getNodeID()));
        if (wall.getNeighbour() >= 0) {
            updatedNodes.add(getMazeCoordinate(wall.getNeighbour()));
        }

        return updatedNodes;
    }

    public int getDestroyedWallCount() {
        return pathGenerator.getDestroyedWallCount();
    }


    abstract protected void generateShape();
    abstract protected int getRandomMarginNodeId();

    abstract public Node[][] getMazeLevelAsMatrix(int mazeLevel);
    abstract public Node getNodeByCoordinate(MazeCoordinate mazeCoordinate);

    abstract protected boolean isInsideMatrix(int x, int y, int z);
    abstract protected boolean isOnSameLevel(int idNodeA, int idNodeB);
    abstract protected MazeCoordinate getMazeCoordinate(int nodeID);
}

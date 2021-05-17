package Maze;

public class Node {
    private NodeType type;
    private boolean hasStaircase;
    private final boolean[] walls;

    public Node(NodeType type, int wallCount) {
        this.type = type;
        walls = new boolean[wallCount];
    }

    public void setStaircase(boolean hasStaircase) {
        this.hasStaircase = hasStaircase;
    }

    public boolean getStaircase() {
        return hasStaircase;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public NodeType getType() {
        return type;
    }

    public void setWall(int index) {
        assert index < walls.length;
        walls[index] = true;
    }

    public boolean[] getWalls() {
        return walls;
    }
}

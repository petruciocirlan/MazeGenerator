package Maze;

import java.util.Arrays;

public class Node {
    private final boolean[] walls;
    private NodeType type;

    public Node(int wallCount) {
        walls = new boolean[wallCount];
        Arrays.fill(walls, true);
        type = NodeType.NORMAL;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public void setWall(int index, boolean bool) {
        assert index < walls.length;
        walls[index] = bool;
    }

    public boolean[] getWalls() {
        return walls;
    }

    @Override
    public String toString() {
        return "Node{" +
                ", walls=" + Arrays.toString(walls) +
                '}';
    }
}

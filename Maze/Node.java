package Maze;

import java.util.Arrays;

public class Node {
    private final boolean[] walls;

    public Node(int wallCount) {
        walls = new boolean[wallCount];
        Arrays.fill(walls, true);
    }

    public void setWall(int index, boolean bool) {
        assert index < walls.length;
        walls[index] = bool;
    }

    public boolean[] getWalls() {
        return walls;
    }

//    @Override
//    public String toString() {
//        return "Node{" +
//                ", walls=" + Arrays.toString(walls) +
//                '}';
//    }
}

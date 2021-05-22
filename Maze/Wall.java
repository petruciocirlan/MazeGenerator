package Maze;

public class Wall {
    public int nodeID;
    public int wallIndex;
    public int neighbour;

    public Wall(int nodeID, int wallIndex, int neighbour) {
        this.nodeID = nodeID;
        this.wallIndex = wallIndex;
        this.neighbour = neighbour;
    }
}

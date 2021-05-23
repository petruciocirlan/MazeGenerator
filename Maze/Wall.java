package Maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wall {
    private final int nodeID;
    private final int wallIndex;
    private final int neighbour;
    private final int weight;

    public Wall(int nodeID, int wallIndex, int neighbour) {
        this.nodeID = nodeID;
        this.wallIndex = wallIndex;
        this.neighbour = neighbour;
        this.weight = 0;
    }

    public Wall(int nodeID, int wallIndex, int neighbour, int weight) {
        this.nodeID = nodeID;
        this.wallIndex = wallIndex;
        this.neighbour = neighbour;
        this.weight = weight;
    }

    public int getNodeID() {
        return nodeID;
    }

    public int getWallIndex() {
        return wallIndex;
    }

    public int getNeighbour() {
        return neighbour;
    }

    public int getWeight() {
        return weight;
    }

    // Credit to https://stackoverflow.com/a/23971598 for idea; modified it for walls
    public static void weightedSort(List<Wall> walls, Random random) {
        int totalWeight = walls.stream().map(wall -> wall.getWeight()).reduce(0, Integer::sum);
        List<Wall> remaining = new ArrayList<>(walls);
        walls.clear();

        do {
            int randomWeight = (int) (random.nextDouble() * totalWeight);

            int picked = 0;
            int currentWeight = 0;
            for (int wallIndex = 0; wallIndex < remaining.size(); wallIndex++) {
                if (currentWeight + remaining.get(wallIndex).getWeight() > randomWeight) {
                    picked = wallIndex;
                    break;
                }

                currentWeight += remaining.get(wallIndex).getWeight();
            }

            totalWeight -= remaining.get(picked).getWeight();
            walls.add(remaining.get(picked));
            remaining.remove(picked);

        } while (!remaining.isEmpty());
    }
}

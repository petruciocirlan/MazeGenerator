package Maze;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Integer.max;

public class PrimPathGenerator extends PathGenerator {
    private final Set<Integer> visited = new HashSet<>();

    public PrimPathGenerator(Maze maze) {
        super(maze);
    }

    @Override
    public void generatePaths() {
        addAdjacentWallList(maze.nodeEntry);
        visited.add(maze.nodeEntry);

        while (!walls.isEmpty() && visited.size() != maze.nodes.size()) {
            Wall wall = walls.remove(0);
            if (!visited.contains(wall.getNeighbour())) {
                visited.add(wall.getNeighbour());

                destroyedWalls.add(wall);

                addAdjacentWallList(wall.getNeighbour());
                Wall.weightedSort(walls, maze.random);
            }
        }

        breakOutsideWall(maze.nodeEntry);
        breakOutsideWall(maze.nodeExit);
    }

    protected void addAdjacentWallList(int nodeID) {
        // newer walls have more weight
        int newWeight = walls.size() + 1;
        for (int wallIndex = 0; wallIndex < maze.graph.get(nodeID).size(); wallIndex++) {
            int neighbour = maze.graph.get(nodeID).get(wallIndex);
            if (neighbour >= 0 && !visited.contains(neighbour)) {
                if (maze.isOnSameLevel(nodeID, neighbour)) {
                    walls.add(new Wall(nodeID, wallIndex, neighbour, newWeight));
                } else {
                    // ~5% change of going to a different level (too many "stairs" look ugly)
                    walls.add(new Wall(nodeID, wallIndex, neighbour, max((int)(newWeight * 0.05), 1)));
                }
            }
        }
    }
}

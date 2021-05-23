package Maze;

public class KruskalPathGenerator extends PathGenerator {
    private final DisjointForest forests;

    public KruskalPathGenerator(Maze maze) {
        super(maze);

        forests = new DisjointForest(maze.nodes.size());
        createWallsList();
    }

    @Override
    public void generatePaths() {
        for (Wall wall : walls) {
            int rootA = forests.find(wall.getNodeID());
            int rootB = forests.find(wall.getNeighbour());
            if (rootA != rootB) {
                forests.unite(rootA, rootB);

                destroyedWalls.add(wall);
            }
        }

        breakOutsideWall(maze.nodeEntry);
        breakOutsideWall(maze.nodeExit);
    }

    protected void createWallsList() {
        for (int nodeID = 0; nodeID < maze.nodes.size(); nodeID++) {
            for (int wallIndex = 0; wallIndex < maze.graph.get(nodeID).size(); wallIndex++) {
                int neighbour = maze.graph.get(nodeID).get(wallIndex);
                if (neighbour >= 0) {
                    if (maze.isOnSameLevel(nodeID, neighbour)) {
                        walls.add(new Wall(nodeID, wallIndex, neighbour, 100));
                    } else {
                        // 5% chance of going to a different level (too many "stairs" look ugly)
                        walls.add(new Wall(nodeID, wallIndex, neighbour, 5));
                    }
                }
            }
        }
        Wall.weightedSort(walls, maze.random);
//        Collections.shuffle(walls, maze.random);
    }
}

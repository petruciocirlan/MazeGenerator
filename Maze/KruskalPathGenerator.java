package Maze;

public class KruskalPathGenerator extends PathGenerator {
    public KruskalPathGenerator(Maze maze) {
        super(maze);
    }

    @Override
    public void generatePaths() {
        for (Wall wall : walls) {
            int rootA = forests.find(wall.nodeID);
            int rootB = forests.find(wall.neighbour);
            if (rootA != rootB) {
                forests.unite(rootA, rootB);

                destroyedWalls.add(wall);
            }
        }
    }
}

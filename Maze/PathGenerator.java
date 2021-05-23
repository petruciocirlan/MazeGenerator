package Maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

abstract public class PathGenerator {
    Maze maze;
    List<Wall> walls = new ArrayList<>();

    List<Wall> destroyedWalls = new ArrayList<>();
    int step = 0;

    public PathGenerator(Maze maze) {
        this.maze = maze;
    }

    abstract public void generatePaths();

    public Wall next() {
        if (step >= destroyedWalls.size()) return null;

        Wall wall = destroyedWalls.get(step);

        maze.nodes.get(wall.getNodeID()).setWall(wall.getWallIndex(), false);
        if (wall.getNeighbour() >= 0) {
            // -2 because we're ignoring last 2 walls which are up/down
            // they are not symmetric like other walls because the walls can change in number
            // (e.g. hexagonal nodes) and it's good to know the up/down walls are always at the end
            int levelWallCount = maze.wallCount - 2;
            int neighbourWall = (wall.getWallIndex() + levelWallCount / 2) % levelWallCount;
            if (wall.getWallIndex() >= levelWallCount) {
                neighbourWall = wall.getWallIndex() % levelWallCount;
                neighbourWall = 1 - neighbourWall;
                neighbourWall = neighbourWall + levelWallCount;
            }
            maze.nodes.get(wall.getNeighbour()).setWall(neighbourWall, false);
        }

        step++;

        return wall;
    }

    public int getDestroyedWallCount() {
        return destroyedWalls.size();
    }

    protected void breakOutsideWall(int nodeID) {
        int levelWallCount = maze.wallCount - 2;
        List<Integer> directions = IntStream.range(0, levelWallCount).boxed().collect(Collectors.toList());
        Collections.shuffle(directions, maze.random);

        List<Integer> neighbours = maze.graph.get(nodeID);
        // if node is on a corner (multiple edges exposed to outside), destroy only one of them
        for (int direction : directions) {
            if (neighbours.get(direction) == -1) {
                destroyedWalls.add(new Wall(nodeID, direction, neighbours.get(direction)));
                break;
            }
        }
    }
}

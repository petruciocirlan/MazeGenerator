package Maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

abstract public class PathGenerator {
    Maze maze;
    DisjointForest forests;
    List<Wall> walls = new ArrayList<>();

    List<Wall> destroyedWalls = new ArrayList<>();
    int step = 0;

    public PathGenerator(Maze maze) {
        this.maze = maze;
        forests = new DisjointForest(maze.nodes.size());
        createWallsList();
    }

    abstract public void generatePaths();

    public boolean next() {
        if (step >= destroyedWalls.size()) return false;

        Wall wall = destroyedWalls.get(step);

        maze.nodes.get(wall.nodeID).setWall(wall.wallIndex, false);
        if (wall.neighbour >= 0) {
            // -2 because we're ignoring last 2 walls which are up/down
            // they are not symmetric like other walls because the walls can change in number
            // (e.g. hexagonal nodes) and it's good to know the up/down walls are always at the end
            int levelWallCount = maze.wallCount - 2;
            int neighbourWall = (wall.wallIndex + levelWallCount / 2) % levelWallCount;
            if (wall.wallIndex >= levelWallCount) {
                neighbourWall = wall.wallIndex % levelWallCount;
                neighbourWall = 1 - neighbourWall;
                neighbourWall = neighbourWall + levelWallCount;
            }
            maze.nodes.get(wall.neighbour).setWall(neighbourWall, false);
        }

        step++;

        return true;
    }

    protected void createWallsList() {
        for (int nodeID = 0; nodeID < maze.nodes.size(); nodeID++) {
            for (int wallIndex = 0; wallIndex < maze.graph.get(nodeID).size(); wallIndex++) {
                int neighbour = maze.graph.get(nodeID).get(wallIndex);
                if (neighbour >= 0) {
                    walls.add(new Wall(nodeID, wallIndex, neighbour));
                }
            }
        }
        Collections.shuffle(walls, maze.random);

        breakOutsideWall(maze.nodeEntry);
        breakOutsideWall(maze.nodeExit);
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
//                maze.nodes.get(nodeID).setWall(direction, false);
                break;
            }
        }
    }
}

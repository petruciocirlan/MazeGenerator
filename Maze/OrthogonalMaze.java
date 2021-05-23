package Maze;

import java.util.*;

public class OrthogonalMaze extends Maze {
    public OrthogonalMaze(int width, int height, int levels) {
        super(width, height, levels);
        wallCount = 6; // sides + up/down
    }

    @Override
    protected void generateShape() {
        int[][] directions = {
                {  0, -1,  0 }, // NORTH
                {  0,  0,  1 }, // EAST
                {  0,  1,  0 }, // SOUTH
                {  0,  0, -1 }, // WEST
                { -1,  0,  0 }, // DOWN
                {  1,  0,  0 }  // UP
        };

        for (int z = 0; z < levels; ++z) {
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    nodes.add(new Node(wallCount));

                    // clockwise
                    List<Integer> neighbours = new ArrayList<>();
                    for (int[] direction : directions) {
                        int neighbourX = x + direction[2];
                        int neighbourY = y + direction[1];
                        int neighbourZ = z + direction[0];
                        if (isInsideMatrix(neighbourX, neighbourY, neighbourZ)) {
                            neighbours.add(neighbourZ * width * height +  neighbourY * width + neighbourX);
//                            System.out.println(z * width * height +  y * width + x);
                        } else {
                            neighbours.add(-1);
                        }
                    }

                    graph.put(z * width * height +  y * width + x, neighbours);
                }
            }
        }

        do {
            // clockwise, starting with top
            nodeEntry = getRandomMarginNodeId();
            nodeExit = getRandomMarginNodeId();
        } while (nodeEntry == nodeExit);
    }

    @Override
    protected int getRandomMarginNodeId() {
        int z = random.nextInt(levels);
        int y = 0;
        int x = 0;

        int side = random.nextInt(4);
        switch (side) {
            case 0, 2 -> {
                if (side == 2) {
                    y = height - 1;
                }

                x = random.nextInt(width);
            }
            case 1, 3 -> {
                if (side == 1) {
                    x = width - 1;
                }

                y = random.nextInt(height);
            }
        }

        int position = z * width * height + width * y + x;
        assert position < levels * width * height;
        return position;
    }

    @Override
    public Node[][] getMazeLevelAsMatrix(int mazeLevel) {
        Node[][] matrix = new Node[height][width];

        int index = mazeLevel * height * width;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (isInsideMatrix(j, i, mazeLevel)) {
                    matrix[i][j] = nodes.get(index);
                    index++;
                }
//                else {
//                    matrix[i][j] = null;
//                }
            }
        }

        return matrix;
    }

    @Override
    public Node getNodeByCoordinate(MazeCoordinate mazeCoordinate) {
        int nodeID = 0;
        nodeID += mazeCoordinate.getZ() * (width * height);
        nodeID += mazeCoordinate.getY() * width;
        nodeID += mazeCoordinate.getX();

        assert nodeID < width * height * levels;

        return nodes.get(nodeID);
    }

    @Override
    protected boolean isInsideMatrix(int x, int y, int z) {
        return (z >= 0 && z < levels) && (y >= 0 && y < height) && (x >= 0 && x < width);
    }

    @Override
    protected boolean isOnSameLevel(int idNodeA, int idNodeB) {
        int idDifference = Math.abs(idNodeA - idNodeB);
        return idDifference < width * height;
    }

    @Override
    protected MazeCoordinate getMazeCoordinate(int nodeID) {
        int z = nodeID / (width * height);
        nodeID %= width * height;

        int y = nodeID / width;
        nodeID %= width;

        int x = nodeID;

        return new MazeCoordinate(x, y, z);
    }
}

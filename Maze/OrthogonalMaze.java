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

        nodes.get(nodeEntry).setType(NodeType.ENTRY);
        nodes.get(nodeExit).setType(NodeType.EXIT);
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

//    @Override
//    protected void generatePaths() {
//        System.out.println("Started generating.");
////        generateWinningPath();
////        System.out.println("Generated winning path.");
////        System.out.println(winningPath);
//        breakOutsideWall(nodeEntry);
//        breakOutsideWall(nodeExit);
//        System.out.println("Broke walls for entry and exit.");
////        connectRemainingNodes();
////        System.out.println("Connected remaining nodes of maze.");
//    }

//    @Override
//    protected void generateWinningPath() {
//        Stack<Integer> path = new Stack<>();
//        Stack<Integer> indexes = new Stack<>();
//
//        path.add(nodeEntry);
//        indexes.add(0);
//
//        Set<Integer> visited = new HashSet<>();
//        visited.add(nodeEntry);
//
//        Stack<List<Integer>> shuffledDirections = new Stack<>();
//
//        while (!path.empty() && !visited.contains(nodeExit)) {
//            int currentNode = path.peek();
//            int index = indexes.peek();
//
//            if (index == 0) { // first time going through this node's neighbours, shuffle directions
//                /* NOTE(@petru): shuffle better; random has huge impact on performance
//                    try to do "weighted" shuffle with preference to directions that get
//                    us closer to the exit node
//                 */
//                shuffledDirections.add(getRandomWeightedDirections(currentNode));
//            }
//
//            List<Integer> directions = shuffledDirections.peek();
//            List<Integer> neighbours = graph.get(currentNode);
//
//            for (; index < directions.size(); index++) {
//                int direction = directions.get(index);
//                int neighbour = neighbours.get(direction);
//                if (neighbour >= 0 && !visited.contains(neighbour)) {
//                    path.push(neighbour);
//                    indexes.push(0);
//
//                    nodes.get(currentNode).setWall(direction, false);
//                    nodes.get(neighbour).setWall((direction + 2) % K_WALL_COUNT, false);
//
//                    visited.add(neighbour);
//
//                    break;
//                }
//            }
//
//
//            if (index >= directions.size()) {
//                path.pop();
//                indexes.pop();
//                shuffledDirections.pop();
//
//                visited.remove(currentNode);
//
//                if (!path.empty()) {
//                    index = indexes.peek();
//                    int direction = shuffledDirections.peek().get(index);
//                    nodes.get(path.peek()).setWall(direction, true);
//                    nodes.get(currentNode).setWall((direction + 2) % K_WALL_COUNT, true);
//                }
//            }
//        }
//
//        assert !path.empty();
//        assert visited.contains(nodeExit);
//
//        for (int nodeID : path) {
//            nodes.get(nodeID).setType(NodeType.PATH_MAIN);
//        }
//
//        winningPath = new ArrayList<>(path);
//    }

//    @Override
//    protected List<Integer> getRandomWeightedDirections(int currentNode) {
//        List<Integer> result = new ArrayList<>();
//
//
//    }


//    @Override
//    protected void connectRemainingNodes() {
//        int[] remainingNodes = IntStream
//                .range(0, nodes.size())
//                .filter(index -> nodes.get(index).getType() != NodeType.PATH_MAIN)
//                .toArray();
//
//        for (int index = 0; index < remainingNodes.length; index++) {
//            if (nodes.get(index).getType() != NodeType.PATH_MAIN) {
//                connectNode(index);
//            }
//        }
//    }
//
//    private void connectNode(int nodeStart) {
//        Stack<Integer> path = new Stack<>();
//        Stack<Integer> indexes = new Stack<>();
//
//        path.add(nodeStart);
//        indexes.add(0);
//
//        Set<Integer> visited = new HashSet<>();
//        visited.add(nodeStart);
//
//        Stack<List<Integer>> shuffledDirections = new Stack<>();
//        List<Integer> generatedDirections = IntStream.range(0, K_WALL_COUNT).boxed().collect(Collectors.toList());
//
//        while (!path.empty() && nodes.get(path.peek()).getType() != NodeType.PATH_MAIN) {
//            int currentNode = path.peek();
//            int index = indexes.peek();
//
//            if (index == 0) {
//                Collections.shuffle(generatedDirections, random);
//                shuffledDirections.add(generatedDirections);
//            }
//
//            List<Integer> directions = shuffledDirections.peek();
//            List<Integer> neighbours = graph.get(currentNode);
//
//            for (; index < directions.size(); index++) {
//                int direction = directions.get(index);
//                int neighbour = neighbours.get(direction);
//                // NOTE(@petru): remove !path.contains(neighbour) to have unreachable regions of maze
//                if (neighbour >= 0 && !visited.contains(neighbour)) {
//                    path.push(neighbour);
//                    indexes.push(0);
//
//                    nodes.get(currentNode).setWall(direction, false);
//                    nodes.get(neighbour).setWall((direction + 2) % K_WALL_COUNT, false);
//
//                    break;
//                }
//            }
//
//            if (index >= graph.get(currentNode).size()) {
//                path.pop();
//                indexes.pop();
//                shuffledDirections.pop();
//
//                // do not un-visit node
//
//                // NODE(@petru): commenting the following instructions may create more cross paths
//                if (!path.empty()) {
//                    index = indexes.peek();
//                    int direction = shuffledDirections.peek().get(index);
//                    nodes.get(path.peek()).setWall(direction, true);
//                    nodes.get(currentNode).setWall((direction + 2) % K_WALL_COUNT, true);
//                }
//            }
//        }
//
//        for (int nodeID : path) {
//            nodes.get(nodeID).setType(NodeType.PATH_MAIN);
//        }
//    }

    @Override
    public Node[][] getMazeLevelAsMatrix(int mazeLevel) {
        Node[][] matrix = new Node[height][width];

        int index = mazeLevel * height * width;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (isInsideMatrix(j, i, mazeLevel)) {
                    matrix[i][j] = nodes.get(index);
                    index++;
                }  else {
                    matrix[i][j] = null;
                }
            }
        }

        return matrix;
    }

    @Override
    protected boolean isInsideMatrix(int x, int y, int z) {
        return (z >= 0 && z < levels) && (y >= 0 && y < height) && (x >= 0 && x < width);
    }
}

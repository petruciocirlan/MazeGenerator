package Maze;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DisjointForest {
    private final List<Integer> roots;
    private final List<Integer> ranks;

    public DisjointForest(int size) {
        roots = IntStream.range(0, size).boxed().collect(Collectors.toList());
        ranks = IntStream.range(0, size).map(index -> 1).boxed().collect(Collectors.toList());
    }

    public void unite(int forestA, int forestB) {
        int rankA = ranks.get(forestA);
        int rankB = ranks.get(forestB);
        if (rankA < rankB) {
            roots.set(forestA, forestB);
        } else {
            roots.set(forestB, forestA);
        }

        if (rankA == rankB) {
            ranks.set(forestA, rankA + 1);
        }
    }

    public int find(int tree) {
        // find root
        int root = tree;
        while (roots.get(root) != root) {
            root = roots.get(root);
        }

        // collapse (compress) forest
        while (roots.get(tree) != tree) {
            int tmp = roots.get(tree);
            roots.set(tree, root);
            tree = tmp;
        }

        return root;
    }
}

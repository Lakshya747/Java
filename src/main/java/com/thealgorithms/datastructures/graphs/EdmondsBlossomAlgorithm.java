package com.thealgorithms.datastructures.graphs;

import java.util.*;

public final class EdmondsBlossomAlgorithm {
    private static final int UNMATCHED = -1;

    public static List<int[]> maximumMatching(List<int[]> edges, int vertexCount) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < vertexCount; i++) graph.add(new ArrayList<>());
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        int[] match = new int[vertexCount];
        Arrays.fill(match, UNMATCHED);
        int[] parent = new int[vertexCount], base = new int[vertexCount];
        boolean[] inBlossom = new boolean[vertexCount], inQueue = new boolean[vertexCount];

        for (int u = 0; u < vertexCount; u++) {
            if (match[u] == UNMATCHED && bfs(u, graph, match, parent, base, inBlossom, inQueue)) {
                updateMatching(match, parent, u);
            }
        }

        List<int[]> result = new ArrayList<>();
        for (int v = 0; v < vertexCount; v++) {
            if (match[v] != UNMATCHED && v < match[v]) result.add(new int[]{v, match[v]});
        }
        return result;
    }

    private static boolean bfs(int start, List<List<Integer>> graph, int[] match, int[] parent, int[] base, boolean[] inBlossom, boolean[] inQueue) {
        Arrays.fill(parent, UNMATCHED);
        for (int i = 0; i < base.length; i++) base[i] = i;
        Arrays.fill(inBlossom, false);
        Arrays.fill(inQueue, false);
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        inQueue[start] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : graph.get(u)) {
                if (match[u] == v || base[u] == base[v]) continue;
                if (parent[v] == UNMATCHED) {
                    if (match[v] == UNMATCHED) {
                        parent[v] = u;
                        return true;
                    }
                    int z = match[v];
                    parent[v] = u;
                    parent[z] = v;
                    if (!inQueue[z]) {
                        queue.add(z);
                        inQueue[z] = true;
                    }
                } else {
                    int lca = findBase(base, parent, u, v);
                    if (lca != UNMATCHED) contractBlossom(queue, parent, base, inBlossom, inQueue, match, u, v, lca);
                }
            }
        }
        return false;
    }

    private static void updateMatching(int[] match, int[] parent, int u) {
        while (u != UNMATCHED) {
            int v = parent[u], next = match[v];
            match[v] = u;
            match[u] = v;
            u = next;
        }
    }

    private static int findBase(int[] base, int[] parent, int u, int v) {
        boolean[] visited = new boolean[base.length];
        while (true) {
            u = base[u];
            visited[u] = true;
            if (parent[u] == UNMATCHED) break;
            u = parent[u];
        }
        while (true) {
            v = base[v];
            if (visited[v]) return v;
            v = parent[v];
        }
    }

    private static void contractBlossom(Queue<Integer> queue, int[] parent, int[] base, boolean[] inBlossom, boolean[] inQueue, int[] match, int u, int v, int lca) {
        for (int x = u; base[x] != lca; x = parent[match[x]]) {
            inBlossom[base[x]] = true;
            inBlossom[base[match[x]]] = true;
        }
        for (int x = v; base[x] != lca; x = parent[match[x]]) {
            inBlossom[base[x]] = true;
            inBlossom[base[match[x]]] = true;
        }
        for (int i = 0; i < base.length; i++) {
            if (inBlossom[base[i]]) {
                base[i] = lca;
                if (!inQueue[i]) queue.add(i);
                inQueue[i] = true;
            }
        }
    }
}

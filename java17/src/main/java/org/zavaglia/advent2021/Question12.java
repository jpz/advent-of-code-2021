package org.zavaglia.advent2021;

import java.util.*;

public class Question12 extends Question {

    public long part1() {
        var visitedPath = new ArrayList<String>();
        visitedPath.add("start");
        var edges = getEdges();
        return dfs(visitedPath, edges);
    }

    // depth first search, where UPPERCASE caves can be visited any number of times,
    // and lower-case caves can be visited once.  terminate on finding "end"
    int dfs(List<String> visitedPath, Map<String, Set<String>> edges) {
        if (visitedPath.get(visitedPath.size() - 1).equals("end"))
            return 1;
        var sum = 0;
        for (var node : edges.get(visitedPath.get(visitedPath.size() - 1))) {
            if (node.toLowerCase().equals(node) && visitedPath.contains(node))
                continue;
            visitedPath.add(node);
            sum += dfs(visitedPath, edges);
            visitedPath.remove(visitedPath.size() - 1);
        }
        return sum;
    }

    public long part2() {
        var visitedPath = new ArrayList<String>();
        visitedPath.add("start");
        var edges = getEdges();
        return dfs2(visitedPath, false, edges);
    }

    // depth first search, where UPPERCASE caves can be visited any number of times,
    // and lower-case caves can be visited once, excepting any one lower-case cave can be visited twice.
    // terminate on finding "end".
    int dfs2(List<String> visitedPath, boolean visitedSomeCaveTwice, Map<String, Set<String>> edges) {
        if (visitedPath.get(visitedPath.size() - 1).equals("end")) {
            return 1;
        }
        var sum = 0;
        final boolean visitedSomeCaveTwiceOriginal = visitedSomeCaveTwice;
        for (var node : edges.get(visitedPath.get(visitedPath.size() - 1))) {
            if (node.equals("start")) {
                // don't go back to the start
                continue;
            }
            if (node.toLowerCase().equals(node)) {
                if (visitedPath.contains(node)) {
                    // if we've visited some node twice, we can't revisit this node
                    if (visitedSomeCaveTwice)
                        continue;
                    else
                        visitedSomeCaveTwice = true;
                }
            }
            visitedPath.add(node);
            sum += dfs2(visitedPath, visitedSomeCaveTwice, edges);
            visitedPath.remove(visitedPath.size() - 1);
            visitedSomeCaveTwice = visitedSomeCaveTwiceOriginal; // reset to original value
        }
        return sum;
    }

    Map<String, Set<String>> getEdges() {
        var edges = new HashMap<String, Set<String>>();

        for (var line : getInputText()) {
            var arr = line.split("-");
            var s0 = edges.getOrDefault(arr[0], new HashSet<>());
            var s1 = edges.getOrDefault(arr[1], new HashSet<>());
            s0.add(arr[1]);
            s1.add(arr[0]);
            edges.put(arr[0], s0);
            edges.put(arr[1], s1);
        }
        return edges;
    }
}

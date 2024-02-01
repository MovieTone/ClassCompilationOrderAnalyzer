package com.movietone;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

/**
 * Graph class represents a graph as an adjacency list and a map to relate classes to indices. Contains methods to load a graph from a file, generate
 * topological order from a given vertex, add an edge and add a vertex to a graph
 *
 * @param <Type>
 */
public class Graph<Type> {

    // an adjacency list
    private final List<LinkedList<Integer>> adjList;
    // a map holding pairs of classes and relevant indices
    private final Map<Type, Integer> classesIndices;

    private static final String BUILD_SUCCESS = "Graph Built Successfully";
    private static final String FILE_NOT_OPEN = "File Did Not Open";

    /**
     * Graph constructor
     */
    public Graph() {
        adjList = new ArrayList<>();
        classesIndices = new HashMap<>();
    }

    /**
     * Reads data from file and initializes a graph with it
     *
     * @param fileName name of the file to read data from
     * @return a success message if graph is built with no error
     * @throws FileNotFoundException if the file is not found
     */
    public String loadGraph(String fileName) throws FileNotFoundException {
        try {
            List<LinkedList<Integer>> list = new ArrayList<>();
            Scanner file = new Scanner(new File(fileName));
            int vertexNum = 0;
            // reads the file line by line
            while (file.hasNextLine()) {
                LinkedList<Integer> vertices = new LinkedList<>();
                // splits the line into single words
                Type[] nodes = (Type[]) file.nextLine().split(" ");
                for (Type node : nodes) {
                    // if such class already exists
                    if (classesIndices.get(node) != null) {
                        vertices.add(classesIndices.get(node));
                        continue;
                    }

                    // puts this vertex to the map
                    classesIndices.put(node, vertexNum);
                    vertices.add(vertexNum);
                    vertexNum++;
                }
                list.add(vertices);
            }

            // initializes the adjacency list with the data read
            for (int i = 0; i < vertexNum; i++) {
                adjList.add(new LinkedList<>());
            }
            for (LinkedList<Integer> vertices : list) {
                int first = vertices.removeFirst();
                adjList.set(first, vertices);
            }
            // if file is not found
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(FILE_NOT_OPEN);
        }

        // if the graph is successfully built
        return BUILD_SUCCESS;
    }

    /**
     * Generates a string containing classes that need to be recompiled in topological order
     *
     * @param className name of the class to start from
     * @return string containing classes in topological order
     * @throws CycleDetectedException    if cycle is found
     * @throws InvalidClassNameException if the class does not match any class read from the file
     * @throws GraphNotBuiltException    if graph is not built yet
     */
    public String generateTopoString(String className)
        throws CycleDetectedException, InvalidClassNameException, GraphNotBuiltException {
        // if graph not built yet
        if (classesIndices.isEmpty()) {
            throw new GraphNotBuiltException();
        }

        // gets index of the class
        Integer vertex = classesIndices.get(className);
        // if no such class found
        if (vertex == null) {
            throw new InvalidClassNameException();
        }

        Stack<Integer> stack = new Stack<>();
        // marks all vertices as not visited
        boolean[] visited = new boolean[adjList.size()];
        // calls recursive helper function from the given vertex
        topoSort(vertex, visited, stack);

        // pops the stack elements into the res variable
        StringBuilder res = new StringBuilder();
        while (!stack.empty()) {
            res.append(getKeyByValue(classesIndices, stack.pop())).append(" ");
        }

        return res.toString();
    }

    /**
     * Adds an edge to the graph
     *
     * @param u first vertex index
     * @param v second vertex index
     */
    public void addEdge(int u, int v) {
        adjList.get(u).add(v);
    }

    /**
     * Adds a vertex to the graph
     *
     * @param node a node to add
     */
    public void addVertex(Type node) {
        int vertex = adjList.size();
        // if map does not already contain this class
        if (classesIndices.get(node) != null) {
            return;
        }
        classesIndices.put(node, vertex);

        // adds empty linked list of adjacent vertices
        adjList.add(new LinkedList<>());
    }

    /**
     * Recursive helper method used by generateTopoString
     *
     * @param v       current vertex
     * @param visited array defining which vertices are already visited
     * @param stack   resulting stack
     * @throws CycleDetectedException if cycle is detected
     */
    private void topoSort(int v, boolean[] visited, Stack<Integer> stack) throws CycleDetectedException {
        // if current vertex is already visited then we detect a cycle
        if (visited[v]) {
            throw new CycleDetectedException();
        }

        // marks current vertex as visited
        visited[v] = true;
        int i;
        // calls this method for all adjacent vertices to the given one
        for (Integer integer : adjList.get(v)) {
            i = integer;
            topoSort(i, visited, stack);
        }

        // pushes current vertex index to a resulting stack
        stack.push(v);
    }

    /**
     * Helper method to get a key by its value in the map
     *
     * @param map   map to get key from
     * @param value value to find the key by
     * @return
     */
    private static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public List<LinkedList<Integer>> getAdjList() {
        return adjList;
    }

    public Map<Type, Integer> getClassesIndices() {
        return classesIndices;
    }

    public static String getBuildSuccess() {
        return BUILD_SUCCESS;
    }

}

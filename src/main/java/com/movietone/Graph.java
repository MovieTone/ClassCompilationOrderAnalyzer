package com.movietone;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    // adjacency list
    private List<LinkedList<Integer>> adjList;
    // map holding pairs of classes and relevant indices
    private Map<Type, Integer> classesIndices;

    private static final String BUILT_SUCCESS = "Graph Built Sucessfully";
    private static final String FILE_NOT_OPEN = "File Did Not Open";

    /**
     * initializing adjacency list and a map of classes and indices objects
     */
    public Graph() {
        adjList = new ArrayList<>();
        classesIndices = new HashMap<>();
    }

    /**
     * reads data from file and initializes a graph with it
     *
     * @param fileName name of the file to read data from
     * @return a success message if graph is built with no error
     * @throws FileNotFoundException if the file is not found
     */
    @SuppressWarnings({"unchecked", "resource"})
    public String loadGraph(String fileName) throws FileNotFoundException {
        try {
            List<LinkedList<Integer>> list = new ArrayList<>();
            Scanner file = new Scanner(new File(fileName));
            int vertexNum = 0;
            // read the file line by line
            while (file.hasNextLine()) {
                LinkedList<Integer> vertices = new LinkedList<>();
                // split the line into single words
                Type nodes[] = (Type[]) file.nextLine().split(" ");
                for (Type node : nodes) {
                    // if such class already exists
                    if (classesIndices.get(node) != null) {
                        vertices.add(classesIndices.get(node));
                    } else {
                        // put this vertex to the map
                        classesIndices.put(node, vertexNum);
                        vertices.add(vertexNum);
                        vertexNum++;
                    }
                }
                list.add(vertices);
            }

            // initialize the adjacency list with the data read
            for (int i = 0; i < vertexNum; i++) {
                adjList.add(new LinkedList<Integer>());
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
        return BUILT_SUCCESS;
    }

    /**
     * generates a string containing classes that need to be recompiled in topological order
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

        // get index of the class
        Integer vertex = classesIndices.get(className);
        // if no such class found
        if (vertex == null) {
            throw new InvalidClassNameException();
        }

        Stack<Integer> stack = new Stack<>();
        // mark all vertices as not visited
        boolean visited[] = new boolean[adjList.size()];
        // call recursive helper function from the given vertex
        topoSort(vertex, visited, stack);

        // popping the stack elements into the res variable
        String res = "";
        while (!stack.empty()) {
            res += getKeyByValue(classesIndices, stack.pop()) + " ";
        }
        return res;
    }

    /**
     * adds an edge to the graph
     *
     * @param u first vertex index
     * @param v second vertex index
     */
    public void addEdge(int u, int v) {
        adjList.get(u).add(v);
    }

    /**
     * adds a vertex to the graph
     *
     * @param node a node to add
     */
    public void addVertex(Type node) {
        int vertex = adjList.size();
        // if map does not already contain this class
        if (classesIndices.get(node) == null) {
            classesIndices.put(node, vertex);
        } else {
            return;
        }
        // add empty linked list of adjacent vertices
        adjList.add(new LinkedList<>());
    }

    /**
     * recursive helper method used by generateTopoString
     *
     * @param v       current vertex
     * @param visited array defining which vertices are already visited
     * @param stack   resulting stack
     * @throws CycleDetectedException if cycle is detected
     */
    private void topoSort(int v, boolean visited[], Stack<Integer> stack) throws CycleDetectedException {
        // if current vertex is already visited then we detect a cycle
        if (visited[v]) {
            throw new CycleDetectedException();
        }

        // mark current vertex as visited
        visited[v] = true;
        int i;
        // call this method for all adjacent vertices to the given one
        Iterator<Integer> it = adjList.get(v).iterator();
        while (it.hasNext()) {
            i = it.next();
            topoSort(i, visited, stack);
        }

        // push current vertex index to a resulting stack
        stack.push(v);
    }

    /**
     * helper method to get key element by its value from the map
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

    public static String getBuiltSuccess() {
        return BUILT_SUCCESS;
    }

}

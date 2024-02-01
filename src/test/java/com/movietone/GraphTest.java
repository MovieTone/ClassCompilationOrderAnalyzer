package com.movietone;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

class GraphTest {

	private Graph<String> graph = new Graph<>();

	private static final String FILE1 = "data/test1.txt";
	private static final String FILE2 = "data/test2.txt";
	private static final String FILE3 = "data/test3.txt";
	
	// testing loadGraph method. file not found exception thrown test
	@Test
	void loadGraphFileNotFoundTest() {
		String fileName = "filenotfound";
		assertThrows(FileNotFoundException.class, 
				() -> {
					graph.loadGraph(fileName);
				});
	}
	
	// testing loadGraph method. Graph built successfully message
	@Test
	void loadGraphBuildSuccessTest() {
		String expected = Graph.getBuiltSuccess(); 
		String actual = "";
		try {
			actual = graph.loadGraph(FILE1);
		} catch (FileNotFoundException e) {
			fail(e);
		}
		assertEquals(expected, actual);
	}

	// testing loadGraph method. test1.txt
	@Test
	void loadGraphTest1() {
//		ClassA ClassB
//		ClassE ClassC
//		ClassB ClassG
//		ClassF ClassH
		
//		[[1], [4], [3], [], [], [6], []]
		List<LinkedList<Integer>> expected = new ArrayList<>();
		expected.add(new LinkedList<Integer>(Arrays.asList(1)));
		expected.add(new LinkedList<Integer>(Arrays.asList(4)));
		expected.add(new LinkedList<Integer>(Arrays.asList(3)));
		expected.add(new LinkedList<Integer>());
		expected.add(new LinkedList<Integer>());
		expected.add(new LinkedList<Integer>(Arrays.asList(6)));
		expected.add(new LinkedList<Integer>());
		try {
			graph.loadGraph(FILE1);
		} catch (FileNotFoundException e) {
			fail(e);
		}
		List<LinkedList<Integer>> actual = graph.getAdjList();
		assertEquals(expected, actual);
	}
	
	// testing loadGraph method. test2.txt
	@Test
	void loadGraphTest2() {
//		ClassA ClassB ClassE
//		ClassE ClassC
//		ClassB ClassG
//		ClassC ClassH
		
//		[[1, 2], [4], [3], [5], [], []]
		List<LinkedList<Integer>> expected = new ArrayList<>();
		expected.add(new LinkedList<Integer>(Arrays.asList(1, 2)));
		expected.add(new LinkedList<Integer>(Arrays.asList(4)));
		expected.add(new LinkedList<Integer>(Arrays.asList(3)));
		expected.add(new LinkedList<Integer>(Arrays.asList(5)));
		expected.add(new LinkedList<Integer>());
		expected.add(new LinkedList<Integer>());
		try {
			graph.loadGraph(FILE2);
		} catch (FileNotFoundException e) {
			fail(e);
		}
		List<LinkedList<Integer>> actual = graph.getAdjList();
		assertEquals(expected, actual);
	}
	
	// testing addVertex method
	@Test
	void addVertexTest() {
		// [[], []]
		List<LinkedList<Integer>> expected = new ArrayList<>();
		expected.add(new LinkedList<Integer>());
		expected.add(new LinkedList<Integer>());
		graph.addVertex("ClassA");
		graph.addVertex("ClassB");
		
		List<LinkedList<Integer>> actual = graph.getAdjList();
		assertEquals(expected, actual);
	}
	
	// testing addEdge method
	@Test
	void addEdgeTest() {
		// [[1], [0]]
		List<LinkedList<Integer>> expected = new ArrayList<>();
		expected.add(new LinkedList<Integer>(Arrays.asList(1)));
		expected.add(new LinkedList<Integer>(Arrays.asList(0)));
		graph.addVertex("ClassA");
		graph.addVertex("ClassB");
		graph.addEdge(0, 1);
		graph.addEdge(1, 0);
		List<LinkedList<Integer>> actual = graph.getAdjList();
		assertEquals(expected, actual);
	}
	
	// testing generateTopoString method with data from test1.txt
	@Test
	void generateTopoStringTest() {
//		ClassA ClassB
//		ClassE ClassC
//		ClassB ClassG
//		ClassF ClassH
		
//		ClassA ClassB ClassG 
		String expected = "ClassA ClassB ClassG ";
		String actual = "";
		try {
			graph.loadGraph(FILE1);
			actual = graph.generateTopoString("ClassA");
		} catch (FileNotFoundException | GraphNotBuiltException | InvalidClassNameException
				| CycleDetectedException e1) {
			fail(e1);
		}
		assertEquals(expected, actual);
	}
	
	// testing generateTopoString method with data from test2.txt 
	@Test
	void generateTopoStringTest2() {
//		ClassA ClassB ClassE
//		ClassE ClassC
//		ClassB ClassG
//		ClassC ClassH
		
//		ClassA ClassE ClassC ClassH ClassB ClassG
		String expected = "ClassA ClassE ClassC ClassH ClassB ClassG ";
		String actual = "";
		try {
			graph.loadGraph(FILE2);
			actual = graph.generateTopoString("ClassA");
		} catch (FileNotFoundException | GraphNotBuiltException | InvalidClassNameException
				| CycleDetectedException e1) {
			fail(e1);
		}
		assertEquals(expected, actual);
	}
	
	// testing generateTopoString method on throwing CycleDetectedException
	@Test
	void generateTopoStringCycleDetectedExceptionTest() {
//		ClassA ClassB 
//		ClassE ClassA
//		ClassB ClassE
//		ClassC ClassH
		
//		main.java.com.movietone.CycleDetectedException
		assertThrows(CycleDetectedException.class, () -> {
			graph.loadGraph(FILE3);
			String actual = graph.generateTopoString("ClassA");
		}); 
	}
	
	// testing generateTopoString method on throwing InvalidClassNameException
	@Test
	void generateTopoStringInvalidClassNameExceptionTest() {
//		main.java.com.movietone.InvalidClassNameException
		assertThrows(InvalidClassNameException.class, () -> {
			graph.loadGraph(FILE3);
			String actual = graph.generateTopoString("InvalidClassName");
		}); 
	}
	
	// testing generateTopoString method on throwing GraphNotBuiltException
	@Test
	void generateTopoStringGraphNotBuiltExceptionTest() {
//		main.java.com.movietone.GraphNotBuiltException
		assertThrows(GraphNotBuiltException.class, () -> {
			String actual = graph.generateTopoString("ClassA");
		}); 
	}
}

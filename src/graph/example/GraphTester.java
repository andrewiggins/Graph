/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package graph.example;

import graph.*;
import graph.util.DepthFirstSearch;
import graph.util.GraphMLWriter;

import java.io.IOException;
import java.util.*;

/**
 *
 * @author AWiggi5
 */
public class GraphTester
{

    public static void main(String[] args) throws IOException
    {
        Graph<Character, Integer> g = createGraph();
        DepthFirstSearch<Character, Integer> dfs = new DepthFirstSearch<Character, Integer>(g);

        System.out.printf("total time: %d\n", dfs.runDFS());
        System.out.println();

        Iterator<Vertex<Character, Integer>> dfsIter = dfs.verboseIterator();
        while(dfsIter.hasNext())
        	dfsIter.next();

        System.out.println();
        Iterator<Vertex<Character, Integer>> verIter = g.vertexSet().iterator();
        while (verIter.hasNext())
        {
        	Vertex<Character, Integer> curV = verIter.next();
        	System.out.printf("%s (%d:%d)\n", curV.toString(), dfs.getStartTiming(curV), dfs.getFinishTiming(curV));
        }

        GraphMLWriter out = new GraphMLWriter("graphtest.graphml");
        out.writeGraph(g);
        out.close();
    }

    public static Graph<Character, Integer> createGraph()
    {
        Graph<Character, Integer> g = new Graph<Character, Integer>();

        ArrayList<Character> verticeValues = new ArrayList<Character>();
        for (int i = 113; i < 123; i++)
            verticeValues.add((char) i);
        verticeValues.add('a');

        for (int i = 0; i < verticeValues.size(); i++)
            g.addVertex(new Vertex<Character, Integer>(verticeValues.get(i)));

        String edges = "vsvwswsqwqqttxxztyyryurua!";
        for (int i = 0, j = 1; i < edges.length(); i+=2, j++)
        {
        	Character v1 = edges.charAt(i), v2 = edges.charAt(i+1);
            System.out.printf("%d: %c -> %c\n", j, v1, v2);
            if (v1 != '!' && v2 != '!')
            	g.addEdge(new Vertex<Character, Integer>(v1), new Vertex<Character, Integer>(v2), j);
        }
        System.out.println();

        return g;
    }

    public static void test()
    {
    	HashMap<Character, Integer[]> hm = new HashMap<Character, Integer[]>();
    	char[] a = {'a', 'b', 'c', 'd'};

    	for (int i = 0; i < a.length; i++)
        {
            Integer[] initValue = {-1,-1};
            hm.put(a[i], initValue);
        }

    	for (int i = 0; i < a.length; i++)
    	{
    		hm.get(a[i])[0] = i;
    	}

    	for (int i = 0; i < a.length; i++)
    	{
    		System.out.printf("%c:%d\n", a[i], hm.get(a[i])[0]);
    	}
    }
}

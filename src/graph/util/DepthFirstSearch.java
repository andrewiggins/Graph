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
package graph.util;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.*;

public class DepthFirstSearch<V extends Comparable<V>, E extends Comparable<E>>
{
	private Graph<V, E> g;

	private Iterator<Vertex<V, E>> vertexIter;
    private HashMap<Vertex<V, E>, Integer[]> vertexTimings = new HashMap<Vertex<V, E>, Integer[]>();

    private int time = -1;

    public DepthFirstSearch(Graph<V, E> newG)
    {
    	this.g = newG;
    }

    public Integer getStartTiming(Vertex<V, E> v)
    {
    	return this.vertexTimings.get(v)[0];
    }

    public Integer getFinishTiming(Vertex<V, E> v)
    {
    	return this.vertexTimings.get(v)[1];
    }

    public Iterator<Vertex<V, E>> iterator()
    {
    	return new DFSIterator();
    }

    public Iterator<Vertex<V, E>> verboseIterator()
    {
    	return new DFSIterator(true);
    }

    public Integer runDFS()
    {
    	return this.runDFS(null);
    }

    public Integer runDFS(Vertex<V, E> v)
    {
    	Iterator<Vertex<V, E> > vertexEntries = g.vertexSet().iterator();
        while(vertexEntries.hasNext())
        {
        	Integer[] initValue = {-1,-1};
            vertexTimings.put(vertexEntries.next(), initValue);
        }

        this.time = -1;
        if (v != null)
        	this.time = this.runDFS(v, this.time);

        vertexIter = this.g.vertexSet().iterator();
        while (vertexIter.hasNext())
            this.time = this.runDFS(vertexIter.next(), this.time);

        return this.time;
    }

    private Integer runDFS(Vertex<V, E> v, Integer time)
    {
        if (this.vertexTimings.get(v)[0] != -1)
        {
            System.out.printf("  vertex %s already started.\n", v.toString());
            return time;
        }
        else
        {
            this.vertexTimings.get(v)[0] = ++time;
            System.out.printf("%s = {start: %d, finish: %d}\n", v.getValue(), this.vertexTimings.get(v)[0], this.vertexTimings.get(v)[1]);
        }

        Iterator<Edge<V, E>> edgeIter = v.edgesOf().iterator();
        Edge<V, E> e = null;
        while (edgeIter.hasNext())
        {
            e = edgeIter.next();
            System.out.printf("  traversing edge %s\n", e.toString());
            time = runDFS(e.opposite(v), time);
        }

        this.vertexTimings.get(v)[1] = ++time;
        System.out.printf("\n%s = {start: %d, finish: %d}\n", v.getValue(), this.vertexTimings.get(v)[0], this.vertexTimings.get(v)[1]);

        return time;
    }

    private class DFSIterator implements Iterator<Vertex<V, E>>
    {
    	private Stack<Map.Entry<Vertex<V, E>, Iterator<Edge<V, E>>>> edgeIters = new Stack<Map.Entry<Vertex<V, E>, Iterator<Edge<V, E>>>>();

    	private Vertex<V, E> previousVertex = null;
        private Vertex<V, E> nextVertex = null;

        private boolean verbose = false;

        public DFSIterator()
        {
        	this(false);
        }

        public DFSIterator(boolean verbose)
        {
        	this.verbose = verbose;
        	time = -1;
        	vertexIter = g.vertexSet().iterator();

            Iterator<Vertex<V, E> > vertexEntries = g.vertexSet().iterator();
            while(vertexEntries.hasNext())
            {
                Integer[] initValue = {-1,-1};
                vertexTimings.put(vertexEntries.next(), initValue);
            }

            this.setNextVertex(vertexIter.next());
        }

        public String currentStateInfo()
        {
        	Integer[] times = vertexTimings.get(this.previousVertex);
        	String prevVertexInfo = String.format("%s (%d:%d)", this.previousVertex, times[0], times[1]);

        	String stackInfo = "Stack:\n";
        	if (this.edgeIters.isEmpty())
        		stackInfo += "  (empty)\n";
        	else
        	{
        		Iterator<Map.Entry<Vertex<V, E>, Iterator<Edge<V, E>>>> edges = this.edgeIters.iterator();
            	while (edges.hasNext())
            		stackInfo += String.format("  %s\n", edges.next().getKey());
        	}


        	return String.format("%s\nTime: %d\n%s", prevVertexInfo, time, stackInfo);
        }

        public boolean hasNext()
        {
            return vertexIter.hasNext();
        }

        private Vertex<V, E> nextUnvisitedVertex(Vertex<V, E> v, Iterator<Edge<V, E>> eI)
        {
        	Vertex<V, E> unvisitedVertex = null, oppVertex;

        	while (eI.hasNext())
        	{
        		oppVertex = eI.next().opposite(v);
        		if (vertexTimings.get(oppVertex)[0] == -1)
        		{
        			unvisitedVertex = oppVertex;
        			break;
        		}
        	}

        	return unvisitedVertex;
        }

        private Vertex<V, E> nextUnvisitedVertex()
        {
        	Vertex<V, E> v = null;

        	while (vertexIter.hasNext())
    		{
    			v = vertexIter.next();
    			if (vertexTimings.get(v)[0] == -1)
    				break;
    		}

        	return v;
        }

        public Vertex<V, E> next()
        {
            this.previousVertex = this.nextVertex;

            Vertex<V, E> currentVertex = null;
            Iterator<Edge<V, E>> curEdgeIter = null;
            Vertex<V, E> unvisitedVertex = null;
            while (!this.edgeIters.isEmpty())
            {
            	currentVertex = this.edgeIters.peek().getKey();
            	curEdgeIter = this.edgeIters.peek().getValue();

            	unvisitedVertex = this.nextUnvisitedVertex(currentVertex, curEdgeIter);
            	if (unvisitedVertex != null)
            	{
            		this.setNextVertex(unvisitedVertex);
            		break;
            	}
            	else
            	{
            		vertexTimings.get(currentVertex)[1] = ++time;
            		this.edgeIters.pop();
            	}
            }

            if (this.edgeIters.isEmpty())
            {
        		Vertex<V, E> v = this.nextUnvisitedVertex();

        		if (v != null && vertexTimings.get(v)[0] == -1)
        			this.setNextVertex(v);
        		else
        			this.nextVertex = null;
            }

            if (this.verbose)
            	System.out.println(this.currentStateInfo());

            return this.previousVertex;
        }

        public void remove()
        {
        	g.removeVertex(this.previousVertex);
        }

        private void setNextVertex(Vertex<V, E> v)
        {
        	this.nextVertex = v;
            vertexTimings.get(this.nextVertex)[0] = ++time;
            this.edgeIters.add(new AbstractMap.SimpleEntry<Vertex<V, E>, Iterator<Edge<V, E>>>(this.nextVertex, this.nextVertex.edgesOf().iterator()));
        }

    }

}

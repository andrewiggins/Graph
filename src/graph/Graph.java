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
package graph;

import graph.decorations.WriteDecorations;

import java.util.*;

public class Graph<V, E>
{
    private HashMap<Vertex<V, E>, IntrusiveVertex> vertices = new HashMap<Vertex<V, E>, IntrusiveVertex>();
    private HashMap<Edge<V, E>, IntrusiveEdge> edges = new HashMap<Edge<V, E>, IntrusiveEdge>();;

    public boolean addEdge(Edge<V, E> e)
    {
        return e.equals(this.addEdge(e.getSourceVertex(), e.getTargetVertex(), e.getWeight()));
    }

    public Edge<V, E> addEdge(Vertex<V, E> v1, Vertex<V, E> v2, E e)
    {
        if (!this.vertices.containsKey(v1))
            throw new IllegalArgumentException("v1 not found in graph.");
        if (!this.vertices.containsKey(v2))
            throw new IllegalArgumentException("v2 not found in graph.");
        if (v1 == null || v2 == null)
            throw new NullPointerException("Parameters cannto be null");

        IntrusiveVertex source = this.vertices.get(v1);
        IntrusiveVertex target = this.vertices.get(v2);

        Edge<V, E> newEdge = new Edge<V, E>(source, target, e);
        if (this.edges.containsKey(newEdge))
            return null;

        source.addEdge(newEdge);
        target.addEdge(newEdge);

        this.edges.put(newEdge, new IntrusiveEdge(newEdge));

        return newEdge;
    }

    public boolean addVertex(Vertex<V, E> v)
    {
        if (v == null)
            throw new NullPointerException("Cannot add a `null` Vertex.");

        if (this.vertices.containsKey(v))
            return false;

        this.vertices.put(v, new IntrusiveVertex(v));
        return true;

    }

    public boolean containsEdge(Vertex<V, E> v1, Vertex<V, E> v2)
    {
        if (v1 == null || v2 == null)
            return false;

        Vertex<V, E> vertex1 = this.vertices.get(v1);
        Vertex<V, E> vertex2 = this.vertices.get(v2);

        if (vertex1 == null || vertex2 == null)
            return false;

        Vertex<V, E> smaller = (vertex1.degree() < vertex2.degree()) ? vertex1 : vertex2;

        Iterator<Edge<V, E>> i = smaller.edgesOf().iterator();
        while (i.hasNext())
            if (i.next().connects(vertex1, vertex2))
                return true;

        return false;
    }

    public boolean containsEdge(Edge<V, E> e)
    {
        return edges.containsKey(e);
    }

    public boolean containsVertex(Vertex<V, E> v)
    {
        return vertices.containsKey(v);
    }

    public Set<Edge<V, E>> edgeSet()
    {
        return Collections.unmodifiableSet(this.edges.keySet());
    }

    public Set<Edge<V, E>> edgesOf(Vertex<V, E> vertex)
    {
        return Collections.unmodifiableSet(this.vertices.get(vertex).edgesOf());
    }

    public SortedSet<Edge<V, E>> getAllEdges(Vertex<V, E> v1, Vertex<V, E> v2)
    {
        TreeSet<Edge<V, E>> connectedEdges = new TreeSet<Edge<V, E>>();

        Vertex<V, E> vertex1 = this.vertices.get(v1);
        Vertex<V, E> vertex2 = this.vertices.get(v2);
        if (vertex1 == null || vertex2 == null)
            return null;

        Iterator<Edge<V, E>> i = vertex1.edgesOf().iterator();
        Edge<V, E> edge = null;
        while (i.hasNext())
        {
            edge = i.next();
            if (edge.connects(vertex1, vertex2))
                connectedEdges.add(edge);
        }

        return Collections.unmodifiableSortedSet(connectedEdges);
    }

    public Edge<V, E> getEdge(Vertex<V, E> v1, Vertex<V, E> v2)
    {
        return this.getAllEdges(v1, v2).first();
    }

    public void putVertexDecoration(Vertex<V, E> v, Object key, Object value)
    {
    	this.vertices.get(v).put(key, value);
    }
    
    public void putEdgeDecoration(Edge<V, E> e, Object key, Object value)
    {
    	this.edges.get(e).put(key, value);
    }
    
    public boolean removeAllEdges(Collection<? extends Edge<V,E>> edges)
    {
        // TODO Write removeAllEdges(Collection<? extends ET> edges)
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Edge<V, E>> removeAllEdges(Vertex<V, E> v1, Vertex<V, E> v2)
    {
        // TODO Write removeAllEdges(VV v1, VV v2)
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeAllVertices(Collection<? extends Vertex<V, E>> vertices)
    {
        // TODO Write removeAllVertices(Collection<? extends VV> vertices)
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Edge<V, E> removeEdge(Vertex<V, E> v1, Vertex<V, E> v2, E weight)
    {
        IntrusiveVertex vertex1 = this.vertices.get(v1);
        IntrusiveVertex vertex2 = this.vertices.get(v2);
        Edge<V, E> edge1 = this.edges.get(new Edge<V, E>(vertex1, vertex2, weight));

        if (vertex1 == null || vertex2 == null || edge1 == null)
            return null;

        vertex1.removeEdge(edge1);
        vertex2.removeEdge(edge1);
        edges.remove(edge1);

        return edge1;
    }

    public boolean removeEdge(Edge<V, E> e)
    {
        return e.equals(this.removeEdge(e.getSourceVertex(), e.getTargetVertex(), e.getWeight()));
    }

    public boolean removeVertex(Vertex<V, E> v)
    {
        // TODO Write removeVertex(VV v)
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Set<Vertex<V,E>> vertexSet()
    {
        return vertices.keySet();
    }
    
    protected class IntrusiveVertex extends Vertex<V, E> implements WriteDecorations
    {
        public IntrusiveVertex()
        {
            super();
        }

        public IntrusiveVertex(V newValue)
        {
            super(newValue);
        }

        public IntrusiveVertex(V newValue, TreeSet<Edge<V, E>> newEdges)
        {
            super(newValue, newEdges);
        }
        
        public IntrusiveVertex(V newValue, TreeSet<Edge<V, E>> newEdges, HashMap<Object, Object> newDecorations)
        {
        	super(newValue, newEdges, newDecorations);
        }

        public IntrusiveVertex(Vertex<V, E> v)
        {
            super(v.getValue(), v.edges, v.decorations);
        }

        public void addEdge(Edge<V, E> e)
        {
        	this.edges.add(e);
        }

        public void put(Object key, Object value)
        {
        	this.decorations.put(key, value);
        }
        
        public boolean removeEdge(Edge<V, E> e)
        {
            return this.edges.remove(e);
        }

        public Vertex<V, E> readOnlyVertex()
        {
            return new Vertex<V, E>(this.getValue(), this.edges);
        }

    }
    
    protected class IntrusiveEdge extends Edge<V, E> implements WriteDecorations
    {
    	
    	public IntrusiveEdge()
        {
            super();
        }

    	public IntrusiveEdge(Vertex<V, E> vertex1, Vertex<V, E> vertex2, E newWeight)
    	{
    		super(vertex1, vertex2, newWeight);
    	}

    	public IntrusiveEdge(Vertex<V, E> vertex1, Vertex<V, E> vertex2, E newWeight, HashMap<Object, Object> newDecorations)
    	{
    		super(vertex1, vertex2, newWeight, newDecorations);
    	}
    	
    	public IntrusiveEdge(Edge<V, E> newEdge)
    	{
    		super(newEdge);
    	}

		public void put(Object key, Object value) 
		{
			this.decorations.put(key, value);			
		}
    	
    }
}

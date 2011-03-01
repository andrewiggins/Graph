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

import graph.decorations.ReadDecorations;

import java.util.*;

/**
 * A class representing a vertex of the graph.
 * @author Andre
 *
 * @param <V> The class that the Vertex value holds.
 * @param <E> The class that the Edge value holds.
 */
public class Vertex<V, E> implements ReadDecorations
{
	protected HashMap<Object, Object> decorations = new HashMap<Object, Object>();
    protected TreeSet<Edge<V, E>> edges;

    public Vertex()
    {
    	this.decorations.put("value", null);
    	this.edges = null;
    }

    public Vertex(V newValue)
    {
    	this.decorations.put("value", newValue);
    	this.edges = new TreeSet<Edge<V, E>>();
    }

    public Vertex(V newValue, TreeSet<Edge<V, E>> newEdges)
    {
    	this.decorations.put("value", newValue);
        this.edges = newEdges;
    }
    
    public Vertex(V newValue, TreeSet<Edge<V, E>> newEdges, HashMap<Object, Object> newDecorations)
    {
    	this.decorations = newDecorations;
    	this.decorations.put("value", newValue);
        this.edges = newEdges;
    }

    public Vertex(Vertex<V, E> v)
    {
        this.decorations = v.decorations;
        this.edges = v.edges;
    }

    public int degree()
    {
    	return edges.size();
    }

    public boolean equals(Object o)
    {
        if (o instanceof Vertex<?, ?>)
            return this.getValue().equals(((Vertex<?, ?>) o).getValue());
        else
            return false;
    }

    public Set<Edge<V, E>> edgesOf()
    {
    	return Collections.unmodifiableSet(this.edges);
    }

    public HashMap<Object, Object> decorations()
    {
    	return this.decorations;
    }

    public Object get(Object key)
    {
    	return this.decorations.get(key);
    }

    @SuppressWarnings("unchecked")
	public V getValue()
    {
        return (V) this.decorations.get("value");
    }

    public int hashCode()
    {
        return this.getValue().hashCode();
    }

    public boolean hasDecorations()
    {
    	return !this.decorations.isEmpty();
    }

    public String toString()
    {
        return this.getValue().toString();
    }

}
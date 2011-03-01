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

import java.util.HashMap;

/**
 *
 * @author AWiggi5
 */
public class Edge<V, E> implements ReadDecorations
{
	protected Vertex<V, E> sourceVertex;
	protected Vertex<V, E> targetVertex;

	protected HashMap<Object, Object> decorations = new HashMap<Object, Object>();

    public Edge()
    {
        this.sourceVertex = null;
        this.targetVertex = null;
        this.decorations.put("weight", null);
    }

	public Edge(Vertex<V, E> vertex1, Vertex<V, E> vertex2, E newWeight)
	{
		this.sourceVertex = vertex1;
		this.targetVertex = vertex2;
		this.decorations.put("weight", newWeight);
	}

	public Edge(Vertex<V, E> vertex1, Vertex<V, E> vertex2, E newWeight, HashMap<Object, Object> newDecorations)
	{
		this.sourceVertex = vertex1;
		this.targetVertex = vertex2;
		this.decorations = newDecorations;
		this.decorations.put("weight", newWeight);
	}
	
	public Edge(Edge<V, E> newEdge)
	{
		this(newEdge.sourceVertex, newEdge.targetVertex, newEdge.getWeight(), newEdge.decorations);
	}
	
    public Vertex<V, E> getSourceVertex()
    {
    	return this.sourceVertex;
    }

    public Vertex<V, E> getTargetVertex()
    {
    	return this.targetVertex;
    }

    public Vertex<V, E> opposite(Vertex<V, E> v)
    {
    	if (this.isConnectedTo(v))
    		return (v.equals(this.sourceVertex) ? this.targetVertex : this.sourceVertex);
    	else
    		return null;
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
	public E getWeight()
    {
    	return (E) this.decorations.get("weight");
    }

    public boolean hasDecorations()
    {
    	return !this.decorations.isEmpty();
    }

    public boolean isConnectedTo(Vertex<V, E> v)
    {
        return sourceVertex.equals(v) || targetVertex.equals(v);
    }

    public boolean connects(Vertex<V, E> v1, Vertex<V, E> v2)
    {
        return this.isConnectedTo(v1) && this.isConnectedTo(v2);
    }

    public boolean equals(Edge<V, E> o)
    {
    	return this.getWeight().equals(o.getWeight()) && this.connects(o.sourceVertex, o.targetVertex);
    }

    public int hashCode()
    {
    	return this.sourceVertex.hashCode() + this.targetVertex.hashCode() + this.getWeight().hashCode();
    }

    public String toString()
	{
		return String.format("(%s:%s(%s))", this.sourceVertex.toString(), this.targetVertex.toString(), this.getWeight().toString());
	}
}

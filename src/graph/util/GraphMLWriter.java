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
import graph.decorations.ReadDecorations;

import java.io.*;
import java.util.*;

public class GraphMLWriter implements Closeable
{
	private PrintWriter out;

	private HashMap<String, LinkedList<String>> dataKeys = new HashMap<String, LinkedList<String>>();
	private boolean graphLineWritten = false;

	private String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" +
							"<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"> \n";
	private String footer = "</graph> \n" +
							"</graphml>";

	private String keyLine = "<key id=\"%s\" for=\"%s\" attr.name=\"%s\" attr.type=\"%s\" /> \n";
	private String graphLine = "<graph edgedefault=\"%s\"> \n";
	private String nodeLine = "<node id=\"%s\"> \n";
	private String edgeLine = "<edge source=\"%s\" target=\"%s\"> \n";
	private String dataLine = "<data key=\"%s\">%s</data> \n";

	public GraphMLWriter(String filename) throws IOException
	{
		out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
		out.write(this.header);
	}

	private <T extends ReadDecorations> void loadDataKeys(Collection<T> objs, String data_struct)
	{
		if (!this.dataKeys.containsKey(data_struct))
			this.dataKeys.put(data_struct, new LinkedList<String>());

		Iterator<T> i = objs.iterator();
		Iterator<Object> keys;
		while(i.hasNext())
		{
			keys = i.next().decorations().keySet().iterator();
			while (keys.hasNext())
			{
				String key = keys.next().toString();
				if (!this.dataKeys.get(data_struct).contains(key))
					dataKeys.get(data_struct).add(key);
			}
		}
	}

	public <T extends ReadDecorations> GraphMLWriter writeDecorations(Collection<T> objs, String data_struct)
	{
		this.loadDataKeys(objs, data_struct);

		Iterator<String> keys = this.dataKeys.get(data_struct).iterator();
		String key, type;
		while (keys.hasNext())
		{
			key = keys.next();
			type = key.getClass().toString();
			this.writeDataKey(key, data_struct, type.substring(type.lastIndexOf('.')+1).toLowerCase());
		}

		return this;
	}

	public GraphMLWriter writeDataKey(String id, String data_struct, String type)
	{
		out.printf(this.keyLine, id, data_struct, id, type);
		return this;
	}

	public GraphMLWriter writeVertex(Vertex<?, ?> v)
	{
		this.checkGraphLineWritten();

		out.printf(this.nodeLine, v.hashCode());

		this.writeObjectDecorations(v);

		out.println("</node>");

		return this;
	}

	public <V extends Vertex<?, ?>> GraphMLWriter writeVertices(Collection<V> vCollection)
	{
		Iterator<V> i = vCollection.iterator();
		while (i.hasNext())
			this.writeVertex(i.next());

		return this;
	}

	public GraphMLWriter writeEdge(Edge<?, ?> e)
	{
		this.checkGraphLineWritten();

		out.printf(this.edgeLine, e.getSourceVertex().hashCode(), e.getTargetVertex().hashCode());

		this.writeObjectDecorations(e);

		out.println("</edge>");

		return this;
	}

	public <E extends Edge<?, ?>> GraphMLWriter writeEdges(Set<E> eSet)
	{
		Iterator<E> i = eSet.iterator();
		while(i.hasNext())
			this.writeEdge(i.next());

		return this;
	}

	private <T extends ReadDecorations> GraphMLWriter writeObjectDecorations(T obj)
	{
		Iterator<Map.Entry<Object, Object>> i = obj.decorations().entrySet().iterator();
		Map.Entry<Object, Object> entry;
		while (i.hasNext())
		{
			entry = i.next();
			out.printf(this.dataLine, entry.getKey().toString(), entry.getValue().toString());
		}

		return this;
	}

	public GraphMLWriter writeGraph(Graph<?, ?> g)
	{
		this.writeDecorations(g.vertexSet(), "node");
		this.writeDecorations(g.edgeSet(), "edge");
		this.writeVertices(g.vertexSet());
		this.writeEdges(g.edgeSet());

		return this;
	}

	private void checkGraphLineWritten()
	{
		if (!this.graphLineWritten)
		{
			this.writeGraphLine(false);
			this.graphLineWritten = true;
		}
	}

	private GraphMLWriter writeGraphLine(boolean isDirected)
	{
		if (isDirected)
			this.out.printf(this.graphLine, "directed");
		else
			this.out.printf(this.graphLine, "undirected");

		return this;
	}

	public void close()
	{
		this.out.write(this.footer);
		this.out.flush();
		this.out.close();
	}
}
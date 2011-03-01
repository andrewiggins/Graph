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
package graph.viewer;

import java.awt.*;
import java.awt.geom.*;

public class EllipseVertex extends Ellipse2D.Double
{
	private static final long serialVersionUID = 599174380481525896L;
	private static Point position = new Point(1,1);
	private static int defaultWidth = 25;
	private static int defaultHeight = 25;

	private Rectangle edgeBoundary = new Rectangle();;
	private Color fill = Color.BLACK;
	private Color border = Color.BLACK;

	private graph.Vertex vdata;

	public <V, E> EllipseVertex(graph.Vertex<V, E> v)
	{
        super(position.getX(), position.getY(), defaultWidth, defaultHeight);
        this.vdata = v;
	}

	public <V, E> EllipseVertex(graph.Vertex<V, E> v, int x, int y, int width, int height)
	{
        super(x, y, width, height);
        this.vdata = v;
	}

	public void setFillColor(Color newFill)
	{
		this.fill = newFill;
	}

	public Color getFillColor()
	{
		return fill;
	}

	// TODO Write getEdgeBoundary()
	public Rectangle getEdgeBoundary()
	{
		return this.edgeBoundary;
	}

	public graph.Vertex getVertexData()
	{
		return this.vdata;
	}
	
	public void paintVertex(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(this.getFillColor());
		g2.fill(this);
		g2.setColor(this.border);
		g2.draw(this);
	}

}

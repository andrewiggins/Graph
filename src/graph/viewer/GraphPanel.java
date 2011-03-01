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
import java.util.*;         
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class GraphPanel extends JPanel {

	private static final long serialVersionUID = -3112435339034420636L;

	private graph.Graph g;

	private Dimension minSize = new Dimension(500, 500);;
	private Point previousPoint;
	EllipseVertex selectedV = null;

	public <V, E> GraphPanel(graph.Graph<V, E> grph)
	{
		this.setDecorations(grph);
		
		this.g = grph;
		
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setOpaque(true);

		MouseAdapter moveCircleMouseAdapter = new MouseInputAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				previousPoint = new Point(e.getX(), e.getY());
				selectedV = getSelectedVertex(e.getX(), e.getY());
			}

			public void mouseReleased(MouseEvent e)
			{
				selectedV = null;
			}
			
			public void mouseDragged(MouseEvent e)
			{
				moveVertex(e.getX(), e.getY());
			}
		};

		this.addMouseListener(moveCircleMouseAdapter);
		this.addMouseMotionListener(moveCircleMouseAdapter);
	}

	private void moveVertex(int x, int y)
	{
		if (selectedV == null)
			return;

		final int CURR_X = (int)selectedV.getX();
		final int CURR_Y = (int)selectedV.getY();
		final int CURR_W = (int)selectedV.getWidth();
		final int CURR_H = (int)selectedV.getHeight();

		final int xChange = x - (int)this.previousPoint.getX();
		final int yChange = y - (int)this.previousPoint.getY();

		if (xChange != 0 || yChange != 0)
		{
			LineEdge line = null;
			
			// the square is moving, repaint background
			// over the old square location
			this.repaint(CURR_X, CURR_Y, CURR_W + 1 , CURR_H + 1);

			Iterator<graph.Edge> e_i = this.g.edgeSet().iterator();
			while (e_i.hasNext())
            {
                line = (LineEdge) e_i.next().get("line");
				this.repaint(line.getEdgeBounds());
            }

			selectedV.setFrame(CURR_X + xChange, CURR_Y + yChange, CURR_W, CURR_H);
			this.previousPoint = new Point(x, y);

			this.repaint(selectedV.getBounds());
			
			e_i = this.g.edgeSet().iterator();
			while (e_i.hasNext())
            {
                line = (LineEdge) e_i.next().get("line");
				this.repaint(line.getEdgeBounds());
            }
		}
	}

	public Dimension getMinimumSize()
	{
		return this.minSize;
	}

	protected void paintComponent(Graphics g)
	{
		// Let UI Delegate paint first, which
		// includes background filling since
		// this component is opaque
		super.paintComponent(g);

		Graphics2D g2D = (Graphics2D) g;

		LineEdge line = null;
		Iterator<graph.Edge> e_i = this.g.edgeSet().iterator();
		while (e_i.hasNext())
        {
            line = (LineEdge) e_i.next().get("line");
			line.paintEdge(g2D);
        }

		EllipseVertex ellipse;
		Iterator<graph.Vertex> v_i = this.g.vertexSet().iterator();
		while (v_i.hasNext())
        {
            ellipse = (EllipseVertex) v_i.next().get("ellipse");
			ellipse.paintVertex(g2D);
        }

	}

	private <V, E> void setDecorations(graph.Graph<V, E> grph)
	{
		graph.Vertex<V, E> v;
		Iterator<graph.Vertex<V, E>> i_v = grph.vertexSet().iterator();
		while (i_v.hasNext())
		{
			v = i_v.next();
            grph.putVertexDecoration(v, "ellipse", new EllipseVertex(v));
		}

		graph.Edge<V, E> e;
		Iterator<graph.Edge<V, E>> i_e = grph.edgeSet().iterator();
		while(i_e.hasNext())
		{
			e = i_e.next();
			grph.putEdgeDecoration(e, "line", new LineEdge(e));
		}
	}
	
	private EllipseVertex getSelectedVertex(int x, int y)
	{
		EllipseVertex v;
		Iterator<graph.Vertex> i = this.g.vertexSet().iterator();
		while (i.hasNext())
		{
			v = (EllipseVertex) i.next().get("ellipse");
			if (v.contains(x, y))
				return v;
		}
		
		return null;
	}
}

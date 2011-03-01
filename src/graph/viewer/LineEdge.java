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

public class LineEdge extends Line2D.Double
{

	private static final long serialVersionUID = 9151464197279489138L;

    private graph.Edge eData;
    private EllipseVertex v1Ellipse;
    private EllipseVertex v2Ellipse;

	public LineEdge (graph.Edge<?, ?> newEdge)
	{ 
        this.eData = newEdge;

        this.v1Ellipse = (EllipseVertex) newEdge.getSourceVertex().get("ellipse");
        this.v2Ellipse = (EllipseVertex) newEdge.getTargetVertex().get("ellipse");
	}

	public graph.Edge getEdgeData()
	{
		return this.eData;
	}
	
	//  FIXME Fix getEdgeBounds
	public Rectangle getEdgeBounds()
	{
		return new Rectangle((int)this.v1Ellipse.getX(),
                (int)this.v1Ellipse.getY(),
                (int)(this.v2Ellipse.getX() + this.v2Ellipse.getWidth()),
                (int)(this.v2Ellipse.getY() + this.v2Ellipse.getHeight()));
	}

	// TODO update edgebounds here?
	public void updateEndPoints()
	{
		this.x1 = this.v1Ellipse.getCenterX();
		this.y1 = this.v1Ellipse.getCenterY();
		this.x2 = this.v2Ellipse.getCenterX();
		this.y2 = this.v2Ellipse.getCenterY();
	}

	public void paintEdge(Graphics g)
	{
		this.updateEndPoints();

		Graphics2D g2 = (Graphics2D) g;

		g2.draw(this);

	}
}

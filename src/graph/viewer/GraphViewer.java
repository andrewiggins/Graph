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
import javax.swing.*;

public class GraphViewer extends JFrame
{

	private static final long serialVersionUID = 4485558618260101432L;
	private GraphPanel canvas;

	public GraphViewer(graph.Graph<?, ?> g)
	{
		this.initComponents(g);
	}

	private void initComponents(graph.Graph<?, ?> g)
	{
		this.setTitle("Graph Viewer");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		canvas = new GraphPanel(g);

		this.setMinimumSize(canvas.getMinimumSize());

		canvas.setBackground(Color.WHITE);

		this.getContentPane().add(canvas);

		this.pack();
	}

	public static void main(String[] args)
	{
		final graph.Graph<?, ?> g = graph.example.GraphTester.createGraph();

		java.awt.EventQueue.invokeLater(new Runnable()
		{
            public void run()
            {
                new GraphViewer(g).setVisible(true);
            }
        });
	}
}

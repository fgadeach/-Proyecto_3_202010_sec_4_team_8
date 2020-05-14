package implementaciones_extras;

import java.util.Stack;

import model.data_structures.Graph;
import model.data_structures.Queue;
import model.data_structures.Vertice;

public class BreadthFirstPaths 
{
	private boolean[] marked; // Is a shortest path to this vertex known?
	private Vertice[] edgeTo;     // last vertex on known path to this vertex
	private Vertice s;      // source


	public BreadthFirstPaths(Graph<?, Vertice> G, Vertice s)
	{
		marked = new boolean[G.V()]; edgeTo = new Vertice[G.V()]; this.s = s;
		bfs(G, s);
	}
	private void bfs(Graph<?, Vertice> G, Vertice s)
	{
		Queue<Vertice> queue = new Queue<Vertice>();
		marked[(int)s.darKey()] = true;
		queue.enqueue(s);
		
		while (!queue.isEmpty())
		{
			Vertice v = queue.dequeue(); 
			for (Vertice w : G.adj((int)v.darKey())) 
			{
				if (!marked[(int)s.darKey()])
				{
					edgeTo[(int)w.darKey()] = v; 
					marked[(int)w.darKey()] = true; 
					queue.enqueue(w);
				}
			}
		}
	}
	public boolean hasPathTo(int v)
	{  
		return marked[v];  
	}

	public Iterable<Vertice> pathTo(Vertice v)
	{
		if (!hasPathTo((int)v.darKey())) 
		{
			return null;
		}
		Stack<Vertice> path = new Stack<Vertice>();

		for (Vertice x = v; x != s; x = edgeTo[(int) x.darKey()]) 
			path.push(x); 
		path.push(s);
		return path;
	}

}

package implementaciones_extras;

import java.util.Stack;

import model.data_structures.Graph;
import model.data_structures.Vertice;

public class DepthFirstPaths {


	private boolean[] marked; // Has dfs() been called for this vertex?
	private Vertice[] edgeTo;  // last vertex on known path to this vertex
	private Vertice s;      // source

	public DepthFirstPaths(Graph<?, Vertice> G, Vertice s)
	{
		marked = new boolean[G.V()];
		edgeTo = new Vertice[G.V()]; 
		this.s = s;
		dfs(G, s);
	}

	private void dfs(Graph<?, Vertice> G, Vertice v) 
	{
		marked[(int)v.darKey()] = true;

		for (Vertice w : G.adj((int)v.darKey()))
			if (!marked[(int)w.darKey()])
			{
				edgeTo[(int)w.darinfo()] = v;
				dfs(G, w);
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

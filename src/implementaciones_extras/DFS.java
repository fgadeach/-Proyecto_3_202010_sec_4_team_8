package implementaciones_extras;

import model.data_structures.Graph;
import model.data_structures.Vertice;

public class DFS
{
	private boolean[] marked;
	private int count;

	public DFS(Graph<?, Vertice> G, Vertice s)
	{
		marked = new boolean[G.V()];
		dfs(G, s);
	}

	private void dfs(Graph<?, Vertice> G, Vertice v)
	{
		marked[(int)v.darKey()] = true;
		count++;
		for (Vertice w : G.adj((int)v.darinfo())) 
		{
			if (!marked[(int)w.darinfo()]) dfs(G, w);
		}
	}

	public boolean marked(Vertice<?, ?> w)
	{  
		return marked[(int)w.darKey()];  	
	}
	public int count()
	{  return count;  

	}
}
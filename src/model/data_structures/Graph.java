package model.data_structures;

import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.Iterator;


public class Graph <K extends Comparable<K>, V> implements IGraph<K,V> {

	private int V;
	private int E;

	private ArrayList<Vertice>[] adj;

	public Graph(int V) 
	{
		this.V = V; 
		this.E = 0;
		adj = (ArrayList<Vertice>[]) new ArrayList[V]; 
		for (int v = 0; v < V; v++) 
		{
			adj[v] = new ArrayList<Vertice>();
		}
	}
	/*
	 * Obtener el número de vertices.
	 */
	public int V() 
	{
		return V;
	}
	@Override
	/*
	 * Obtener el número de arcos.
	 */
	public int E() 
	{
		return E;
	}
	
	public void addEdge(Vertice v, Vertice w)
	{
		adj[(int)v.darKey()].add(w);	
		E++;
	}

	public Iterable<Vertice> adj(int v)
	{  
		return adj[v]; 
	}

}

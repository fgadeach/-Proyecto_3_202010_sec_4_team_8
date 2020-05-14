package model.data_structures;


public class Vertice <K extends Comparable<K>, V>
{
	private K key;
	private V info;

	private double latitud;
	private double longitud;

	private Array<K> adj = new Array<K>();
	private boolean marked;

	public Vertice(K pKey, V pinfo)
	{
		key = pKey;
		info = pinfo;
	}

	public K darKey()
	{
		return key;
	}

	public V darinfo()
	{
		return info; 
	}

	public void setinfo(V newinfo)
	{
		info = newinfo;
	}

	public void setLaYLo()
	{
		String lector = (String) info; 
		String linea[] = lector.split(",");

		longitud = Double.parseDouble(linea[1]);
		latitud = Double.parseDouble(linea[0]);

	}

	public double darLongitud()
	{
		return longitud; 
	}

	public double darLatitud()
	{
		return latitud; 
	}


	public Array<K> darAdj()
	{
		return adj;
	}

	public void agregarVerticeAdj(K idVertAdj)
	{
		adj.append(idVertAdj);
	}
}
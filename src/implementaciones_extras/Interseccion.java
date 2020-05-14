package implementaciones_extras;

public class Interseccion extends Superior
{
	public Interseccion(int id, double latitud, double longitud) 
	{
		super(id, longitud, latitud);
	}

	@Override
	public String toString() 
	{
		// TODO Auto-generated method stub
		return String.valueOf(id + ";" + latitud + ";" + longitud);
	}
}

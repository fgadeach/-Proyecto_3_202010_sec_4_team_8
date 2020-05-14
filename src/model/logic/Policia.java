package model.logic;

import implementaciones_extras.Superior;

public class Policia extends Superior implements Comparable<Policia> 
{

	private String name;
	private String city;
	private int dpCapacity;


	public Policia(int id, String name, String city, double latitude, double longitude, int dpCapacity) {
		
		super(id, longitude, latitude);

		this.name = name;
		this.city = city;

		this.dpCapacity = dpCapacity;

	}

	public String darName() 
	{
		return name;
	}


	public void cambiarName(String name) 
	{
		this.name = name;
	}


	public String darCity() 
	{
		return city;
	}


	public void cambiarCity(String city) 
	{
		this.city = city;
	}
	public int darDpCapacity() 
	{
		return dpCapacity;
	}


	public void cambiarDpCapacity(int dpCapacity) 
	{
		this.dpCapacity = dpCapacity;
	}


	@Override
	public int compareTo(Policia o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.valueOf(super.id + ";" + super.latitud + ";" + super.longitud + ";" + name + ";" 
				+ city + ";" + dpCapacity);
	}

}

package model.logic;

import model.data_structures.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.opencsv.CSVReader;

import controller.Controller;
import implementaciones_extras.Conexion;
import implementaciones_extras.Haversine;
import implementaciones_extras.Interseccion;
import implementaciones_extras.Superior;


/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {

	private Graph<Integer, Superior , Conexion> Graph = new Graph<Integer,Superior , Conexion>(0);

	private int numeroVerticesEstacion =0;
	private int numeroArcosMixtos = 0;

	public void loadData() throws NumberFormatException, Exception
	{
		long cronomInicial = System.currentTimeMillis();
		long memoryBeforeCase1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		this.loadIntersecciones(Controller.RUTA_ARCOS);
		this.loadMallaVial(Controller.RUTA_NODOS);
		this.loadStations(Controller.POLICIA);
		this.guardarGraph();

		long cronomFinal = System.currentTimeMillis();
		long duration = cronomFinal - cronomInicial;
		System.out.println("Total vertices cargados: " + Graph.E());
		System.out.println("Numero de vertices estacion: " + numeroVerticesEstacion);
		System.out.println("Total arcos cargados: " + Graph.V());
		System.out.println("Numero de arcos mixtos: " + numeroArcosMixtos);
		long memoryAfterCase1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("Tiempo en cargar: " + duration + " milisegundos \nMemoria utilizada:  "+ ((memoryAfterCase1 - memoryBeforeCase1)/1000000.0) + " MB");

	}

	/**
	 * Loads the trips of the file into a queue and a stack
	 * @param tripsFile the route of the file with the data that will be loaded
	 */

	private void loadStations(String stationsFile)
	{
		try 
		{
			CSVReader reader = new CSVReader(new FileReader (stationsFile));

			String [] nextLine;
			reader.readNext();

			while((nextLine = reader.readNext()) != null)
			{
				Policia station = new Policia (Integer.parseInt(nextLine[0]), nextLine[1],nextLine[2],Double.parseDouble(nextLine[3]),Double.parseDouble(nextLine[4]), Integer.parseInt(nextLine[5]));

				//hashStations.put(station.darId(), station);
				Graph.addVertex(Graph.V(), station);
				numeroVerticesEstacion++;
				double menorDistancia = Double.MAX_VALUE;
				Interseccion interseccionAConectar=null;

				Iterator<Integer> iter = Graph.iterarVertices();
				while(iter.hasNext())
				{	
					Integer act = iter.next();

					if(Graph.getInfoVertex(act) instanceof Interseccion)
					{
						Interseccion actual =  (Interseccion) Graph.getInfoVertex(act);
						double distanciaActual = Haversine.distance(station.darLatitud(), station.darLongitud(), actual.darLatitud(), actual.darLongitud());
						if(menorDistancia>distanciaActual)
						{
							menorDistancia = distanciaActual;
							interseccionAConectar=actual;
						}
					}
				}

				Conexion conexion = new Conexion(menorDistancia);
				Graph.addEdge(Graph.V() - 1, interseccionAConectar.darId(), conexion);
				numeroArcosMixtos++;

			}
			reader.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println("Se produjo un error. No se pudieron cargar los datos");
		}

	}

	private void loadIntersecciones(String interseccionesFile) throws IOException
	{

		File archivo = new File(interseccionesFile);

		try 
		{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo));
			String linea;
			String datos[];

			while((linea = bufferedReader.readLine())!=null)
			{
				datos = linea.split(",");

				Integer id = Integer.parseInt(datos[0]);
				double longitud = Double.parseDouble(datos[1]);
				double latitud = Double.parseDouble(datos[2]);
				Interseccion interseccion = new Interseccion(id, latitud, longitud);

				Graph.addVertex(id, interseccion);
			}

			bufferedReader.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void loadMallaVial(String mallaVialFile) throws NumberFormatException, Exception
	{
		File archivo = new File(mallaVialFile);

		try 
		{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo));
			String linea;
			String datos[];

			while((linea = bufferedReader.readLine())!=null)
			{
				datos = linea.split(" ");

				for(int i =1; i<datos.length;i++)
				{
					Interseccion int1 = (Interseccion) Graph.getInfoVertex(Integer.parseInt(datos[0]));
					Interseccion int2 = (Interseccion) Graph.getInfoVertex(Integer.parseInt(datos[i]));

					Conexion conexion = new Conexion(Haversine.distance(int1.darLatitud(), int1.darLongitud(), int2.darLatitud(), int2.darLongitud()));

					Graph.addEdge(Integer.parseInt(datos[0]), Integer.parseInt(datos[i]), conexion);
				}
			}

			bufferedReader.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void guardarGraph() throws IOException {
		JSONObject obj = new JSONObject();
		JSONArray intersecciones = new JSONArray();
		JSONArray estaciones = new JSONArray();
		JSONArray arcos = new JSONArray();

		Iterator<Integer> iter = Graph.iterarVertices();
		while(iter.hasNext()) {
			int id = iter.next();
			Superior nodo = Graph.getInfoVertex(id);

			if(nodo instanceof Interseccion) {
				intersecciones.add(nodo.toString());
			} else {
				estaciones.add(id + ";" + nodo.toString());
			}
		}

		Iterator<Arco<Integer, Conexion>> iterCalles = Graph.arcos().iterator();
		while(iterCalles.hasNext()) {
			Arco<Integer, Conexion> arco = iterCalles.next();
			String infoArco = String.valueOf(arco.darPrimerVertice() + ";" + arco.darSegundoVertice() + ";"
					+ arco.darInfo().toString());

			arcos.add(infoArco);
		}

		obj.put("Intersecciones", intersecciones);
		obj.put("Estaciones", estaciones);
		obj.put("Arcos", arcos);

		FileWriter fw = new FileWriter(new File("./src/data/GraphMallaVial.json"), false);
		fw.write(obj.toJSONString());
		fw.close();

	}

	public void cargarGraph() throws NumberFormatException, Exception {
		Graph = new Graph<Integer, Superior, Conexion>(0);
		int numArcos = 0;
		int numVertices = 0;

		JSONParser parser = new JSONParser();

		JSONObject obj = (JSONObject) parser.parse(new FileReader("./src/data/GraphMallaVial.json"));

		JSONArray array = (JSONArray) obj.get("Estaciones");
		for(Object o: array) {
			String object = String.valueOf(o);
			String[] params = object.split(";");
			
			Policia estacion = new Policia(Integer.valueOf(params[1]), params[4], params[5], Double.valueOf(params[2]), Double.valueOf(params[3]), Integer.valueOf(params[6]));
			//System.out.println(params[0]);
			Graph.addVertex(Integer.valueOf(params[0]), estacion);
			numVertices ++;
		}

		array = (JSONArray) obj.get("Intersecciones");
		for(Object o: array) {
			String object = String.valueOf(o);
			String[] params = object.split(";");


			Interseccion interseccion = new Interseccion(Integer.valueOf(params[0]), Double.valueOf(params[1]), 
					Double.valueOf(params[2]));
			Graph.addVertex(interseccion.darId(), interseccion);
		}

		array = (JSONArray) obj.get("Arcos");
		for(Object o: array) {
			String object = String.valueOf(o);
			String[] params = object.split(";");

			Conexion calle = new Conexion(Double.valueOf(params[2]));
			Graph.addEdge(Integer.valueOf(params[0]), Integer.valueOf(params[1]), calle);
			Superior nodo1 = Graph.getInfoVertex(Integer.valueOf(params[0]));
			Superior nodo2 = Graph.getInfoVertex(Integer.valueOf(params[1]));

			if(nodo1 instanceof Policia||nodo2 instanceof Policia ) 
			{
				numArcos ++;
			}
		}

		System.out.println("Número vertices (Interseccion): " + (Graph.V() - numVertices));
		System.out.println("Número vertices (Estacion): " + numVertices);
		System.out.println("Número arcos (Interseccion): " + (Graph.E() - numArcos));
		System.out.println("Número arcos (Mixtos): " + numArcos);
	}

	public void dibujarGraph() throws IOException
	{
		File archivo = new File("./src/data/Graph.html");
		PrintWriter pw = new PrintWriter(archivo);

		pw.println("<!DOCTYPE html>\r\n" + 
				"<html>\r\n" + 
				"<head>\r\n" + 
				"<meta charset=utf-8 />\r\n" + 
				"<title>A simple map</title>\r\n" + 
				"<meta name='viewport' content='initial-scale=1,maximum-scale=1,user-scalable=no' />\r\n" + 
				"<script src='https://api.mapbox.com/mapbox.js/v3.1.1/mapbox.js'></script>\r\n" + 
				"<link href='https://api.mapbox.com/mapbox.js/v3.1.1/mapbox.css' rel='stylesheet' />\r\n" + 
				"<style>\r\n" + 
				"  body { margin:0; padding:0; }\r\n" + 
				"  #map { position:absolute; top:0; bottom:0; width:100%; }\r\n" + 
				"</style>\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"<div id='map'></div>\r\n" + 
				"<script>\r\n" + 
				"L.mapbox.accessToken = 'pk.eyJ1IjoidmR1YXJ0ZSIsImEiOiJjam9kNzZid2Ewc3Q1M3JwYjc2amI0NTB1In0.cZCxfASnsAYOMzlKwJPG_A';\r\n" + 
				"var map = L.mapbox.map('map', 'mapbox.streets')\r\n" + 
				"    .setView([41.943139372530865, -87.68479897232211], 100);");

		Iterator<String> iter = Graph.iterarArcos();
		while(iter.hasNext())
		{	

			String act = iter.next();
			String info[] = act.split("-");

			Superior sup1 = Graph.getInfoVertex(Integer.parseInt(info[0]));
			Superior sup2 = Graph.getInfoVertex(Integer.parseInt(info[1]));


			pw.println("L.circle(L.latLng("+sup1.darLatitud()+", "+sup1.darLongitud()+"), 7, {\r\n" + 
					"    stroke: false,\r\n" + 
					"    fill: true,\r\n" + 
					"    fillOpacity: 1,\r\n" + 
					"    fillColor: \"#5b94c6\",\r\n" + 
					"    className: \"circle_500\"\r\n" + 
					"}).addTo(map);");

			pw.println("L.circle(L.latLng("+sup2.darLatitud()+", "+sup2.darLongitud()+"), 7, {\r\n" + 
					"    stroke: false,\r\n" + 
					"    fill: true,\r\n" + 
					"    fillOpacity: 1,\r\n" + 
					"    fillColor: \"#5b94c6\",\r\n" + 
					"    className: \"circle_500\"\r\n" + 
					"}).addTo(map);");

			pw.println("var line_point = [["+sup1.darLatitud()+", "+sup1.darLongitud()+"], ["+sup2.darLatitud()+","+sup2.darLongitud()+"]];\r\n" + 
					"    var polyline_opt = {\r\n" + 
					"      color : \"#5b94c6\"\r\n" + 
					"    }\r\n" + 
					"\r\n" + 
					"    L.polyline(line_point, polyline_opt).addTo(map);");

		}
		pw.println();

		pw.println("</script>");
		pw.println("</body>");
		pw.println("</html>");
		pw.close();

	}
}



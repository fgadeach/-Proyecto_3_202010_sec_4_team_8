package controller;

import java.io.FileReader;



import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.awt.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import model.logic.Modelo;
import view.View;

public class Controller {

	/*
	 * 
	 *
	/* Instancia del Modelo*/
	private Modelo modelo;

	/* Instancia de la Vista*/
	private View view;

	public static final String RUTA_ARCOS="./data/bogota_arcos.txt";
	public static final String RUTA_NODOS="./data/bogota_vertices.txt";
	public static final String jsonAV="./data/grafo.geojson";
	public static final String POLICIA="./data/estacionpolicia.geojson";
	/**
	 * Crear la vista y el modelo del proyecto
	 * @param capacidad tamaNo inicial del arreglo
	 */
	public Controller()
	{

		view = new View();
		modelo = new Modelo();
	}

	@SuppressWarnings("null")
	public void run() 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;

		while( !fin ){
			view.printMenu();

			int option = lector.nextInt();
			switch(option){

			case 0:
				modelo = new Modelo(); 
				try {
					modelo.loadData();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("no carga");
				}
				try {
					modelo.dibujarGraph();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("no dibuja");
				}
				break;

			case 1:
				try {
					modelo.cargarGraph();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("no carga el grafo");
				}
				break;


			default: 
				System.out.println("--------- \n Opcion Invalida !! \n---------");
				break;
			}
		}
	}
}	
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
	 */
	
	/* Instancia del Modelo*/
	private Modelo modelo;

	/* Instancia de la Vista*/
	private View view;



	public static final String ruta="./data/comparendos1.geojson";
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
					
			break;


			default: 
				System.out.println("--------- \n Opcion Invalida !! \n---------");
				break;

			}
		}
	}
}	
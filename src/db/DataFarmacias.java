package db;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//IAG
import java.util.Vector;

import domain.Farmacia;
import jdbc.GestorBDInitializerFarmacias;

public class DataFarmacias {
	public static Vector<Vector<Object>> cargaFarmacia(String path){
		Vector<Vector<Object>> data = new Vector<>();
		
		GestorBDInitializerFarmacias gestorBD = new GestorBDInitializerFarmacias();
		gestorBD.crearBBDD();
		
		List<Farmacia> farmacias = gestorBD.obtenerDatos();
		if(farmacias == null || farmacias.isEmpty()) {
	        System.out.println("Cargando desde CSV...");
	        List<Farmacia> farmaciasCSV = initFarmacias();
	        System.out.println("Farmacias del CSV: " + farmaciasCSV.size());
	        gestorBD.insertarDatos(farmaciasCSV.toArray(new Farmacia[farmaciasCSV.size()]));
	        farmacias = gestorBD.obtenerDatos();
	        System.out.println("Después de insertar: " + farmacias.size());
	    }
		
		
		for (Farmacia farmacia : farmacias) {
			Vector<Object> farmaciaTemp = new Vector<>();
			farmaciaTemp.add(farmacia.getNombre());
			farmaciaTemp.add(farmacia.getProvincia());
			farmaciaTemp.add("Stock Bajo");
			farmaciaTemp.add("10 pendientes");
			farmaciaTemp.add("Seleccionar");
			data.add(farmaciaTemp);
		}
		return data; 
	}
	
	
	
	
	public static List<Farmacia> initFarmacias() {
		List<Farmacia> farmacias = new ArrayList<>();		
		
		
		try {
			// Abrir el fichero
			File fichero = new File("resources/db/farmacias.csv");
			Scanner sc = new Scanner(fichero);
			
			// Leer el fichero
			while (sc.hasNextLine()) {
			
				String linea = sc.nextLine();
				
				String[] campos = linea.split(",");
				if (campos[0].equalsIgnoreCase("id")) {
				    continue;
				}
				int id = Integer.parseInt(campos[0]);
				String nombre = campos[1];
				String provincia = campos[2];
				
				Farmacia farmacia = new Farmacia(id,nombre,provincia);
				farmacias.add(farmacia);
			}
			
			// Cerrar el fichero
			sc.close();
		} catch (Exception e) {
			System.err.println("Error al cargar datos desde farmacias.csv");
			e.printStackTrace();
		}
		return farmacias;
	}
}
	
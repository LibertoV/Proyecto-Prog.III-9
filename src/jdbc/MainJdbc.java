package jdbc;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import db.Cliente;
import gui.JFrameListaClientes;

public class MainJdbc {

	public static void main(String[] args) {
		GestorBDInitializerCliente gestorBD = new GestorBDInitializerCliente();		
		
		//CREATE DATABASE: Se crea la BBDD
		gestorBD.crearBBDD();    
		//INSERT: Insertar datos en la BBDD		
		List<Cliente> clientes = initClientes();
		gestorBD.insertarDatos(clientes.toArray(new Cliente[clientes.size()]));
		
		//SELECT: Se obtienen datos de la BBDD
		clientes = gestorBD.obtenerDatos();
		printClientes(clientes);
		
		//UPDATE: Se actualiza la password de un cliente
//		String newtlf = "+34 6666666666";
//		gestorBD.actualizarTelefono(clientes.get(0), newtlf);
//		
//		String newNombre = "Maria";
//		gestorBD.actualizarNombre(clientes.get(0), newNombre);
//		
//		String DNI = "5554454a";
//		gestorBD.actualizarDNI(clientes.get(0), DNI);
		//SELECT: Se obtienen datos de la BBDD
		clientes = gestorBD.obtenerDatos();
		printClientes(clientes);
		
		
		gestorBD.borrarCliente(1);
		//DELETE: Se borran datos de la BBDD
		gestorBD.borrarDatos();
		
		//DROP DATABASE: Se borra la BBDD
		gestorBD.borrarBBDD();
	}
	
	private static void printClientes(List<Cliente> clientes) {
		if (!clientes.isEmpty()) {
			clientes.forEach(cliente ->	System.out.format("\n - %s", cliente.toString()));
		}		
	}
	
	public static List<Cliente> initClientes() {
		List<Cliente> clientes = new ArrayList<>();		
		
		
		try {
			// Abrir el fichero
			File fichero = new File("resources/db/clientes.csv");
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
				String dni = campos[2];
				String tlf = campos[3];
				String fecha = campos[4];
				int recetas = Integer.parseInt(campos[5]);
				String email = campos[6];
				String direccion = campos[7];
				
				Cliente cliente = new Cliente(id,nombre,dni,tlf,fecha,recetas,email,direccion);
				clientes.add(cliente);
			}
			
			// Cerrar el fichero
			sc.close();
		} catch (Exception e) {
			System.err.println("Error al cargar datos desde clientes.csv");
		}
		return clientes;
	}
	
 
}
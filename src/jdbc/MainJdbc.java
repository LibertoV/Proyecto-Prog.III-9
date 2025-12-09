package jdbc;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import db.Cliente;
import domain.Farmacia;
import domain.Pedido;
import domain.Producto;
import domain.Trabajador;
import gui.JFrameListaClientes;

public class MainJdbc {

	public static void main(String[] args) {

		GestorBDInitializerCliente gestorClientes = new GestorBDInitializerCliente();
		GestorBDInitializerTrabajadores gestorTrabajadores = new GestorBDInitializerTrabajadores();
		GestorBDInitializerFarmacias gestorFarmacias = new GestorBDInitializerFarmacias(); // Necesario para la FK
		GestorBDInitializerPedido gestorPedidos = new GestorBDInitializerPedido(); // El nuevo gestor

		System.out.println("\n--- 1. INICIANDO CREACIÓN DE BBDD ---");
		gestorFarmacias.crearBBDD();
		gestorClientes.crearBBDD();
		gestorTrabajadores.crearBBDD();
		gestorPedidos.crearBBDD();

		System.out.println("\n--- 2. CARGANDO E INSERTANDO DATOS ---");
		Farmacia farmaciaPrincipal = new Farmacia(1, "Farmacia Central", "Barakaldo");
		gestorFarmacias.insertarDatos(farmaciaPrincipal);

		//List<Trabajador> trabajadores = initTrabajador();
		//gestorTrabajadores.insertarDatos(trabajadores.toArray(new Trabajador[0]));

		//List<Cliente> clientes = initClientes();
		//gestorClientes.insertarDatos(clientes.toArray(new Cliente[0]));

		List<Pedido> pedidos = initPedidos();
		gestorPedidos.insertarDatos(pedidos.toArray(new Pedido[0]));

		System.out.println("\n--- 3. VERIFICACIÓN DE DATOS (SELECT) ---");
		// 4. MOSTRAR DATOS (SELECT)
		// trabajadores = gestorTrabajadores.obtenerDatos();
		// printTrabajadores(trabajadores);

		// clientes = gestorClientes.obtenerDatos();
		// printClientes(clientes);

		List<Pedido> pedidosRecuperados = gestorPedidos.obtenerDatos();
		printPedidos(pedidosRecuperados);

		// 5. LIMPIEZA (Opcional - Comentado para que puedas ver los datos en DB
		// Browser)
		// Si quieres borrar todo al acabar, descomenta las líneas de abajo.
		/*
		 * System.out.println("\n--- 4. LIMPIEZA ---"); gestorPedidos.borrarBBDD(); //
		 * Borrar pedidos primero (por las FK) gestorClientes.borrarCliente(1);
		 * gestorClientes.borrarDatos(); gestorClientes.borrarBBDD();
		 */
	}

	private static void printTrabajadores(List<Trabajador> trabajadores) {
		if (!trabajadores.isEmpty()) {
			trabajadores.forEach(cliente -> System.out.format("\n - %s", cliente.toString()));
		}
	}

	private static void printClientes(List<Cliente> clientes) {
		if (!clientes.isEmpty()) {
			clientes.forEach(cliente -> System.out.format("\n - %s", cliente.toString()));
		}
	}
	
	private static void printPedidos(List<Pedido> pedidos) {
		if (!pedidos.isEmpty()) {
			for (Pedido p : pedidos) {
				System.out.format("Pedido: %s | Fecha: %s | Total: %.2f € | Items: %d\n", 
						p.getId(), p.getFechaOrden(), p.calcularTotal(), p.getProductos().size());
				
				for(Producto prod : p.getProductos()) {
					System.out.format("\t -> %s (x%d) - %.2f €\n", prod.getNombre(), prod.getCantidad(), prod.getPrecioUnitario());
				}
			}
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

				Cliente cliente = new Cliente(id, nombre, dni, tlf, fecha, recetas, email, direccion);
				clientes.add(cliente);
			}

			// Cerrar el fichero
			sc.close();
		} catch (Exception e) {
			System.err.println("Error al cargar datos desde clientes.csv");
		}
		return clientes;
	}

	private static List<Trabajador> initTrabajador() {
		List<Trabajador> trabajadores = new ArrayList<>();

		try {
			// Abrir el fichero
			File fichero = new File("resources/db/trabajadores.csv");
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
				String email = campos[4];
				String direccion = campos[5];
				String puesto = campos[6];
				String nss = campos[7];
				String turno = campos[8];
				Float salario = Float.parseFloat(campos[9]);

				Trabajador trabajador = new Trabajador(id, nombre, dni, tlf, email, direccion, puesto, nss, turno,
						salario);
				trabajadores.add(trabajador);
			}

			// Cerrar el fichero
			sc.close();
		} catch (Exception e) {
			System.err.println("Error al cargar datos desde trabajadores.csv");
		}
		return trabajadores;
	}

	public static List<Pedido> initPedidos() {
	    Map<String, Pedido> mapaPedidos = new HashMap<>();

	    try {
	        File fichero = new File("resources/db/pedidos.csv");
	        Scanner sc = new Scanner(fichero);

	        while (sc.hasNextLine()) {
	            String linea = sc.nextLine().trim();

	            if (linea.isEmpty()) continue;

	            String[] campos = linea.split(",", -1);

	            for (int i = 0; i < campos.length; i++) {
	                campos[i] = campos[i].trim();
	            }

	            if (campos[0].equalsIgnoreCase("id_pedido"))
	                continue;

	            String id = campos[0];
	            String fechaOrd = campos[1];
	            String fechaLleg = campos[2];
	            String proveedor = campos[3];
	            String nombreProd = campos[4];
	            int cantidad = Integer.parseInt(campos[5]);
	            double precio = Double.parseDouble(campos[6]);

	            Pedido p = mapaPedidos.get(id);

	            if (p == null) {
	                p = new Pedido(id, proveedor, Date.valueOf(fechaOrd), Date.valueOf(fechaLleg));
	                mapaPedidos.put(id, p);
	            }

	            Producto prod = new Producto(nombreProd, cantidad, precio);
	            p.agregarProducto(prod);
	        }
	        sc.close();
	    } catch (Exception e) {
	        System.err.println("Error al cargar datos desde pedidos.csv: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return new ArrayList<>(mapaPedidos.values());
	}
}
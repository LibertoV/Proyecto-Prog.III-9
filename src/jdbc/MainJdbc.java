package jdbc;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import db.Cliente;
import domain.Compra;
import domain.Farmacia;
import domain.Pedido;
import domain.Producto;
import domain.Trabajador;
import gui.JFrameListaClientes;
import gui.JFramePrincipal;

public class MainJdbc {

	public static void main(String[] args) {

        GestorBDInitializerCliente gestorClientes = new GestorBDInitializerCliente();
        GestorBDInitializerTrabajadores gestorTrabajadores = new GestorBDInitializerTrabajadores();
        GestorBDInitializerFarmacias gestorFarmacias = new GestorBDInitializerFarmacias();
        GestorBDInitializerPedido gestorPedidos = new GestorBDInitializerPedido();
        GestorBDInitializerProducto gestorProductos = new GestorBDInitializerProducto();
        GestorBDInitializerCompras gestorCompras = new GestorBDInitializerCompras();

        System.out.println("\n--- 0. LIMPIEZA DE BBDD ANTIGUA ---");
        // Borramos en orden inverso para evitar errores de claves foráneas
        gestorPedidos.borrarBBDD();
        gestorClientes.borrarBBDD();
        gestorTrabajadores.borrarBBDD();
        gestorFarmacias.borrarBBDD();
        gestorProductos.borrarBBDD();
        gestorCompras.borrarBBDD(); 
        

        System.out.println("\n--- 1. INICIANDO CREACIÓN DE BBDD ---");
        gestorFarmacias.crearBBDD();
        gestorClientes.crearBBDD();
        gestorTrabajadores.crearBBDD();
        gestorPedidos.crearBBDD();
        gestorProductos.crearBBDD();
        gestorCompras.crearBBDD();

        System.out.println("\n--- 2. CARGANDO E INSERTANDO DATOS ---");

        // 1. FARMACIAS (Cargamos todas desde el CSV)
        List<Farmacia> farmacias = initFarmacias();
        if (farmacias.isEmpty()) {
            System.err.println("⚠️ ALERTA: No se han cargado farmacias. Creando una por defecto para evitar errores.");
            farmacias.add(new Farmacia(1, "Farmacia Default", "Barakaldo"));
        }
        gestorFarmacias.insertarDatos(farmacias.toArray(new Farmacia[0]));

        // 2. TRABAJADORES
        List<Trabajador> trabajadores = initTrabajador();
        gestorTrabajadores.insertarDatos(trabajadores.toArray(new Trabajador[0]));

        // 3. CLIENTES
        List<Cliente> clientes = initClientes();
        gestorClientes.insertarDatos(clientes.toArray(new Cliente[0]));
        
        // 4. PRODUCTOS
        List<Producto> productos = initProductos();
        gestorProductos.insertarDatos(productos.toArray(new Producto[0]));
        
        //4.5 COMPRAS
        List<Compra> compras = initCompras();
        gestorCompras.insertarDatos(compras.toArray(new Compra[0]));
        
        // 5. PEDIDOS (Lo último, para que existan las farmacias primero)
        List<Pedido> pedidos = initPedidos();
        gestorPedidos.insertarDatos(pedidos.toArray(new Pedido[0]));

        System.out.println("\n--- 3. VERIFICACIÓN DE DATOS (SELECT) ---");
        
        System.out.println("--- Farmacias ---");
        List<Farmacia> farmaciasRecuperadas = gestorFarmacias.obtenerDatos();
        for(Farmacia f : farmaciasRecuperadas) System.out.println(" - " + f.getNombre());

        System.out.println("--- Pedidos ---");
        List<Pedido> pedidosRecuperados = gestorPedidos.obtenerDatos();
        printPedidos(pedidosRecuperados);
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
				System.out.format("Pedido: %s | Fecha: %s | Total: %.2f € | Items: %d\n", p.getId(), p.getFechaOrden(),
						p.calcularTotal(), p.getProductos().size());

				for (Producto prod : p.getProductos().keySet()) {
					System.out.format("\t -> %s (x%d) - %.2f €\n", prod.getNombre(), p.getProductos().get(prod),
							prod.getPrecioUnitario());
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

				// Trabajador trabajador = new Trabajador(id, nombre, dni, tlf, email,
				// direccion, puesto, nss, turno,salario);
				// trabajadores.add(trabajador);
			}

			// Cerrar el fichero
			sc.close();
		} catch (Exception e) {
			System.err.println("Error al cargar datos desde trabajadores.csv");
		}
		return trabajadores;
	}
	
	public static List<Farmacia> initFarmacias() {
	    List<Farmacia> farmacias = new ArrayList<>();

	    try {
	        File fichero = new File("resources/db/farmacias.csv"); 
	        Scanner sc = new Scanner(fichero);

	        while (sc.hasNextLine()) {
	            String linea = sc.nextLine().trim();
	            if (linea.isEmpty()) continue;

	            String[] campos = linea.split(",");
	            if (campos[0].equalsIgnoreCase("id")) continue;

	            int id = Integer.parseInt(campos[0].trim());
	            String nombre = campos[1].trim();
	            String provincia = campos[2].trim();

	            Farmacia f = new Farmacia(id, nombre, provincia);
	            farmacias.add(f);
	        }
	        sc.close();
	    } catch (Exception e) {
	        System.err.println("Error al cargar farmacias.csv: " + e.getMessage());
	    }
	    return farmacias;
	}
	public static List<Producto> initProductos(){
		List<Producto> productos = new ArrayList<>();

	    try {
	        File fichero = new File("resources/db/productos.csv"); 
	        Scanner sc = new Scanner(fichero);

	        while (sc.hasNextLine()) {
	            String linea = sc.nextLine().trim();
	            if (linea.isEmpty()) continue;

	            String[] campos = linea.split(",");
	            if (campos[0].equalsIgnoreCase("id")) continue;

	            int id = Integer.parseInt(campos[0].trim());
	            String nombre = campos[1].trim();
	            double precioU = Double.parseDouble(campos[2]);

	            Producto p = new Producto(id, nombre, precioU);
	            productos.add(p);
	        }
	        sc.close();
	    } catch (Exception e) {
	        System.err.println("Error al cargar productos.csv: " + e.getMessage());
	    }
	    return productos;
	}
	public static List<Pedido> initPedidos() {
		Map<String, Pedido> mapaPedidos = new HashMap<>();

		try {
			File fichero = new File("resources/db/pedidos.csv");
			Scanner sc = new Scanner(fichero);

			if (sc.hasNextLine())
				sc.nextLine(); // Saltar cabecera

			while (sc.hasNextLine()) {
				String linea = sc.nextLine().trim();
				if (linea.isEmpty())
					continue;

				String[] campos = linea.split(",", -1);
				if (campos[0].equalsIgnoreCase("id_pedido"))
					continue;

				String id = campos[0].trim();
				String fechaOrd = campos[1].trim();
				String fechaLleg = campos[2].trim();
				String proveedor = campos[3].trim();
				int idProducto = Integer.parseInt(campos[4]);

				int cantidad = Integer.parseInt(campos[5].trim());
				int idFarmacia = Integer.parseInt(campos[6].trim());
				Pedido p = mapaPedidos.get(id);

				if (p == null) {
					Date fechaLlegadaSQL = null;
					if (fechaLleg != null && !fechaLleg.isEmpty() && !fechaLleg.equalsIgnoreCase("null")) {
						try {
							fechaLlegadaSQL = Date.valueOf(fechaLleg);
						} catch (Exception e) {
							fechaLlegadaSQL = null;
						}
					}

					p = new Pedido(id, proveedor, Date.valueOf(fechaOrd), fechaLlegadaSQL, idFarmacia);
					mapaPedidos.put(id, p);
				}
				
		        GestorBDInitializerProducto gestorProductos = new GestorBDInitializerProducto();
				Producto producto = gestorProductos.obtenerProductoPorId(idProducto);
				p.agregarProducto(producto,cantidad);
			}
			sc.close();
		} catch (Exception e) {
			System.err.println("Error al cargar pedidos: " + e.getMessage());
			e.printStackTrace();
		}

		return new ArrayList<>(mapaPedidos.values());
	}
	public static List<Compra> initCompras() {
	    Map<Integer, Compra> mapaCompras = new HashMap<>();
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	    try {
	        File fichero = new File("resources/db/compras.csv");
	        if (!fichero.exists()) {
	            System.err.println("Archivo compras.csv no encontrado.");
	            return new ArrayList<>();
	        }

	        Scanner sc = new Scanner(fichero);

	        if (sc.hasNextLine()) sc.nextLine(); // Saltar cabecera

	        while (sc.hasNextLine()) {
	            String linea = sc.nextLine().trim();
	            if (linea.isEmpty()) continue;

	            String[] campos = linea.split(",");
	            if (campos[0].equalsIgnoreCase("id")) continue;

	            try {
	                // Lectura de campos según estructura: 
	                // [0]ID, [1]ID_Farmacia, [2]ID_Cliente, [3]Fecha, [4]ID_Producto, [5]Cantidad
	                int id = Integer.parseInt(campos[0].trim());
	                int idFarmacia = Integer.parseInt(campos[1].trim());
	                int idCliente = Integer.parseInt(campos[2].trim());
	                String fechaStr = campos[3].trim();
	                int idProducto = Integer.parseInt(campos[4].trim());
	                int cantidad = Integer.parseInt(campos[5].trim());

	                Compra c = mapaCompras.get(id);

	                if (c == null) {
	                    java.util.Date fecha;
	                    try {
	                        fecha = sdf.parse(fechaStr);
	                    } catch (Exception e) {
	                        fecha = new java.util.Date(); // Fecha actual si falla el parseo
	                    }
 
	                    // Compra(id, idFarmacia, idCliente, Date, Map<Integer,Integer>)
	                    c = new Compra(id, idFarmacia, idCliente, fecha, new HashMap<>());
	                    mapaCompras.put(id, c);
	                }

	                c.getMapaProductos().put(idProducto, cantidad);

	            } catch (NumberFormatException nfe) {
	                System.err.println("Error de formato en línea de compra: " + linea);
	            }
	        }
	        sc.close();
	    } catch (Exception e) {
	        System.err.println("Error al cargar compras.csv: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return new ArrayList<>(mapaCompras.values());
	}
}
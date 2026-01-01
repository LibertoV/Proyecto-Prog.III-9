package db;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import domain.Pedido;
import domain.Producto;
import gui.JFramePrincipal;
import jdbc.GestorBDInitializerPedido;
import jdbc.GestorBDInitializerProducto;

public class DataPedidos {
	private static final String IMAGES_PATH = "resources/images/";
	private static final String[] PROVEEDOR_IMGS = {
	        "AbbVieLogo_AbbVie dark blue.png", 
	        "JNJ_Logo_SingleLine_Red_RGB.png",
	        "Lilly-Logo.svg.png",
	        "Logo_Bayer.svg.png",
	        "Logo_Sanofi_(2022).png",
	        "Merck.png",
	        "original.jpg",
	        "roche-logo-blue.png"
	        
	    };
	private static String getProveedorImagenAleatoria() {
        Random random = new Random();
        int index = random.nextInt(PROVEEDOR_IMGS.length);
        return IMAGES_PATH + PROVEEDOR_IMGS[index];
    }
	
	public static Vector<Vector<Object>> cargaPedidos(String path){
		Vector<Vector<Object>> data = new Vector<>();
		
		GestorBDInitializerPedido gestorBD = new GestorBDInitializerPedido();
		gestorBD.crearBBDD();
		
		List<Pedido> pedidos = gestorBD.obtenerDatos();
		
		if(pedidos == null || pedidos.isEmpty()) {
	        System.out.println("Cargando desde CSV...");
	        List<Pedido> pedidosCSV = initPedidos();
	        
	        System.out.println("Pedidos del CSV: " + pedidosCSV.size());
	        
	        gestorBD.borrarBBDD();
	        gestorBD.crearBBDD();
	        
	        
	        gestorBD.insertarDatos(pedidosCSV.toArray(new Pedido[pedidosCSV.size()]));
	        
	        pedidos = gestorBD.obtenerDatos();
	        System.out.println("Después de insertar: " + pedidos.size());
	    }
		
		

		for (Pedido pedido : pedidos) {
			Vector<Object> pedidoTemp = new Vector<>();
			pedidoTemp.add(pedido.getId());
			pedidoTemp.add(pedido.getFechaOrden());
			pedidoTemp.add(pedido.getFechaLlegada());
			pedidoTemp.add(pedido.calcularTotal());
			pedidoTemp.add(getProveedorImagenAleatoria());
			
			data.add(pedidoTemp);
		}
		return data; 
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

}

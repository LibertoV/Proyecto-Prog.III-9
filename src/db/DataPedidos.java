package db;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import domain.Pedido;
import domain.Producto;
import gui.JFramePrincipal;
import jdbc.GestorBDInitializerPedido;

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
		List<Pedido> pedidos = new ArrayList<>();		
		
		
		try {
			// Abrir el fichero
			File fichero = new File("resources/db/pedidos.csv");
			Scanner sc = new Scanner(fichero);
			HashMap<Pedido,ArrayList<Producto>> pedidosMap = new HashMap<Pedido,ArrayList<Producto>>();
			// Leer el fichero
			while (sc.hasNextLine()) {
			
				String linea = sc.nextLine();
				
				String[] campos = linea.split(",");
				
				
				String id = campos[0];
				SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
				Date fecha_orden = formateador.parse(campos[1]);
				Date fecha_llegada = formateador.parse(campos[2]);
				String proveedor = campos[3];
				String nombre = campos[4];
				int cantidad = Integer.parseInt(campos[5]);
				double precio_U = Double.parseDouble(campos[6]);
				int idFarmacia = Integer.parseInt(campos[7]);
				
				Pedido pedido = new Pedido(id,proveedor,fecha_orden,fecha_llegada,idFarmacia);
				Producto producto = new Producto(nombre,cantidad,precio_U);
				
				if (pedidosMap.containsKey(pedido)) {
				    ArrayList<Producto> listaProductos = pedidosMap.get(pedido);
				    listaProductos.add(producto);
				} else {
				    ArrayList<Producto> nuevaListaProductos = new ArrayList<>();
				    nuevaListaProductos.add(producto);
				    pedidosMap.put(pedido, nuevaListaProductos);
				}
				
			}
			
			
			for (Pedido pedido : pedidosMap.keySet()) {
				for (Producto producto : pedidosMap.get(pedido)) {
					pedido.agregarProducto(producto);
				}
				pedidos.add(pedido);
			}
			
			// Cerrar el fichero
			sc.close();
		} catch (Exception e) {
			System.err.println("Error al cargar datos desde pedidos.csv");
			e.printStackTrace();
		}
		return pedidos;
	}

}

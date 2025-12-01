package domain;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Pedido {
	private String id;
	private String proveedor;
	private Date fechaOrden;
	private Date fechaLlegada;
	private ArrayList<Producto> productos;

	public Pedido(String id, String proveedor, Date fechaOrden, Date fechaLlegada) {
		this.id = id;
		this.proveedor = proveedor;
		this.fechaOrden = fechaOrden;
		this.fechaLlegada = fechaLlegada;
		this.productos = new ArrayList<>();
	}

	public void agregarProducto(Producto p) {
		this.productos.add(p);
	}

	public double calcularTotal() {
		double total = 0;
		for (Producto p : productos) {
			total += p.getSubtotal();
		}
		return total;
	}

	// se usa esto para combertir el pedido en un objeto y asi poderlo añadir a la tabla de pedidos
	public Object[] añadirloTabla() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String totalFormateado = String.format("%.2f €", calcularTotal()).replace(",", ".");

		return new Object[] { id, sdf.format(fechaOrden), sdf.format(fechaLlegada), totalFormateado, proveedor, "" };
	}

	public String getId() {
		return id;
	}

	public String getProveedor() {
		return proveedor;
	}
}
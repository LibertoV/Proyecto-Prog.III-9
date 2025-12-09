package domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Pedido {
	private String id;
	private String proveedor;
	private Date fechaOrden;
	private Date fechaLlegada;
	private ArrayList<Producto> productos;
	private double totalImportado = 0.0;

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
        if (!productos.isEmpty()) {
            double total = 0;
            for (Producto p : productos) {
                total += p.getSubtotal();
            }
            return total;
        } else {
            return totalImportado;
        }
    }
    
    public void setTotalImportado(double total) {
        this.totalImportado = total;
    }
    
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

	public Date getFechaOrden() {
		return fechaOrden;
	}

	public Date getFechaLlegada() {
		return fechaLlegada;
	}

	public ArrayList<Producto> getProductos() {
		return productos;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); 
    }
}
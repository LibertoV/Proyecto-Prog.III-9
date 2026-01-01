package domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Pedido {
	private String id;
	private String proveedor;
	private Date fechaOrden;
	private Date fechaLlegada; // esto podria llegar a ser null
	private HashMap<Producto,Integer> productos;
	private int idFarmacia;
	private double totalImportado = 0.0;

	public Pedido(String id, String proveedor, Date fechaOrden, Date fechaLlegada, int idFarmacia) {
		this.id = id;
		this.proveedor = proveedor;
		this.fechaOrden = fechaOrden;
		this.fechaLlegada = fechaLlegada;
		this.idFarmacia = idFarmacia;
		this.productos = new HashMap<>();
	}

	public void agregarProducto(Producto p, int cantidad) {
		productos.put(p, cantidad);
	}

	public double calcularTotal() {
        if (!productos.isEmpty()) {
            double total = 0;
            for (Producto p : productos.keySet()) {
                total += p.getPrecioUnitario()*productos.get(p);
            }
            return total;
        } else {
            return totalImportado;
        }
    }
    
    public Object[] añadirloTabla() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        String fechaLlegadaStr = (fechaLlegada != null) ? sdf.format(fechaLlegada) : "Pendiente";
        String totalFormateado = String.format("%.2f €", calcularTotal());

        return new Object[] { id, sdf.format(fechaOrden), fechaLlegadaStr, totalFormateado, proveedor, "ELIMINAR" };
    }
    
    public void setTotalImportado(double total) {
        this.totalImportado = total;
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

	public HashMap<Producto,Integer> getProductos() {
		return productos;
	}
	
	public int getIdFarmacia() {
		return idFarmacia;
	}

	public void setIdFarmacia(int idFarmacia) {
		this.idFarmacia = idFarmacia;
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
package domain;

import java.util.Date;

public class Venta {
    private int id;
    private int idCompra;
    private int idFarmacia;
    private int idCliente;
    private int idProducto;
    private int cantidad;
    private double precioUnitario;
    private Date fecha;
    
    // Campos adicionales para mostrar (no se guardan en BD, se calculan)
    private String nombreCliente;
    private String nombreProducto;
    
    public Venta() {}
    
    public Venta(int id, int idCompra, int idFarmacia, int idCliente, int idProducto, 
                 int cantidad, double precioUnitario, Date fecha) {
        this.id = id;
        this.idCompra = idCompra;
        this.idFarmacia = idFarmacia;
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.fecha = fecha;
    }
    

    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdCompra() {
		return idCompra;
	}

	public void setIdCompra(int idCompra) {
		this.idCompra = idCompra;
	}

	public int getIdFarmacia() {
		return idFarmacia;
	}

	public void setIdFarmacia(int idFarmacia) {
		this.idFarmacia = idFarmacia;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public int getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public double calcularTotal() {
        return cantidad * precioUnitario;
    }

	@Override
	public String toString() {
		return "Venta [id=" + id + ", idCompra=" + idCompra + ", idFarmacia=" + idFarmacia + ", idCliente=" + idCliente
				+ ", idProducto=" + idProducto + ", cantidad=" + cantidad + ", precioUnitario=" + precioUnitario
				+ ", fecha=" + fecha + ", nombreCliente=" + nombreCliente + ", nombreProducto=" + nombreProducto
				+ ", getId()=" + getId() + ", getIdCompra()=" + getIdCompra() + ", getIdFarmacia()=" + getIdFarmacia()
				+ ", getIdCliente()=" + getIdCliente() + ", getIdProducto()=" + getIdProducto() + ", getCantidad()="
				+ getCantidad() + ", getPrecioUnitario()=" + getPrecioUnitario() + ", getFecha()=" + getFecha()
				+ ", getNombreCliente()=" + getNombreCliente() + ", getNombreProducto()=" + getNombreProducto()
				+ ", calcularTotal()=" + calcularTotal() + "]";
	}
    

}
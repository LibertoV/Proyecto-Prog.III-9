package domain;

public class Producto {
	private String nombre;
	private int cantidad;
	private double precioUnitario;

	public Producto(String nombre, int cantidad, double precioUnitario) {
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
	}

	public double getSubtotal() {
		return cantidad * precioUnitario;
	}

	public String getNombre() {
		return nombre;
	}

	public int getCantidad() {
		return cantidad;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	// esto sirve para añadir las cosas al JDialog cuandoo estamos añadiendo
	// productos a un pedido, a esa tabla
	public Object[] vectorPed() {
		return new Object[] { nombre, cantidad, precioUnitario };
	}
}
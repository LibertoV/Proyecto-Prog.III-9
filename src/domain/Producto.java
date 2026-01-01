package domain;

public class Producto {
	private String nombre;
	private int id;
	private double precioUnitario;

	public Producto(int id, String nombre, double precioUnitario) {
		this.nombre = nombre;
		this.id = id;
		this.precioUnitario = precioUnitario;
	}


	public String getNombre() {
		return nombre;
	}

	public int getId() {
		return id;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	// esto sirve para añadir las cosas al JDialog cuandoo estamos añadiendo
	// productos a un pedido, a esa tabla
	public Object[] vectorPed() {
		return new Object[] { id,nombre, precioUnitario };
	}
}
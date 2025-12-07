package domain;

public class Receta {
	private String nombre;
	private Float precio;
	private String laboratorio;
	
	public Receta(String nombre,Float precio,String laboratorio) {
		this.nombre = nombre;
		this.precio = precio;
		this.laboratorio = laboratorio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Float getPrecio() {
		return precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}

	public String getLaboratorio() {
		return laboratorio;
	}

	public void setLaboratorio(String laboratorio) {
		this.laboratorio = laboratorio;
	}
}

package domain;

public class Farmacia {
	private int id;
	private String nombre;
	private String provincia;
	
	
	
	public Farmacia(int id, String nombre,String provincia) {
		this.id = id;
		this.nombre = nombre;
		this.provincia = provincia;
		}

	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getProvincia() {
		return provincia;
	}



	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Farmacia [id=" + id + ", nombre=" + nombre + ", provincia=" + provincia + "]";
	}
}

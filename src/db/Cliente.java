package db;

public class Cliente {
    private int id;
    private String nombre;
    private String dni;
    private String tlf;
    private String fechaUltimaCompra;
    private Integer recetasPendientes;
    private String email;
    private String direccion;

     public Cliente(int i, String nombre, String dni, String tlf, String fechaUltimaCompra, int recetasPendientes, String email,
			String direccion) {
    	this.id = i;
    	this.nombre = nombre;
        this.dni = dni;
        this.tlf = tlf;
        this.fechaUltimaCompra = fechaUltimaCompra;
        this.recetasPendientes = recetasPendientes;
        this.email = email;
        this.direccion = direccion;
	}



	public int getId() {
		return id;
	}

    
    public void setId(int id) { 
    	this.id = id; 
    }

    
	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getDni() {
		return dni;
	}



	public void setDni(String dni) {
		this.dni = dni;
	}



	public String getTlf() {
		return tlf;
	}



	public void setTlf(String tlf) {
		this.tlf = tlf;
	}



	public String getFechaUltimaCompra() {
		return fechaUltimaCompra;
	}



	public void setFechaUltimaCompra(String fechaUltimaCompra) {
		this.fechaUltimaCompra = fechaUltimaCompra;
	}



	public Integer getRecetasPendientes() {
		return recetasPendientes;
	}



	public void setRecetasPendientes(Integer recetasPendientes) {
		this.recetasPendientes = recetasPendientes;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getDireccion() {
		return direccion;
	}



	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

   
    
    


	@Override
    public String toString() {
        return String.format("%d - %s - %s - %s - %s - %s",
                id, nombre, dni, tlf, fechaUltimaCompra, recetasPendientes, email, direccion);
    }
    
    
}


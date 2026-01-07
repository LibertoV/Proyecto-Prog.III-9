package db;

public class Cliente {
    private int id;
    private String nombre;
    private String dni;
    private String tlf;
    private String fechaUltimaCompra;
    private Integer compras;
    private String email;
    private String direccion;

     public Cliente(int i, String nombre, String dni, String tlf, String fechaUltimaCompra, int compras, String email,
			String direccion) {
    	this.id = i;
    	this.nombre = nombre;
        this.dni = dni;
        this.tlf = tlf;
        this.fechaUltimaCompra = fechaUltimaCompra;
        this.compras = compras;
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



	public Integer getCompras() {
		return compras;
	}



	public void setCompras(Integer compras) {
		this.compras = compras;
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
                id, nombre, dni, tlf, fechaUltimaCompra, compras, email, direccion);
    }
    
    
}


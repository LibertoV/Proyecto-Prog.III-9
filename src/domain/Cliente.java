package domain;

import java.util.Date;

public class Cliente {

	private int id;
	private String nombre;
	private String dni;
	private String telefono;

	private String email;
	private String direccion;
	private String recetasActivas;

	private Date ultimaCompra;
	private int numRecetasPendientes;

	public Cliente() {
	}

	public Cliente(int id, String nombre, String dni, String telefono, String email, String direccion,
			String recetasActivas, Date ultimaCompra, int numRecetasPendientes) {
		this.id = id;
		this.nombre = nombre;
		this.dni = dni;
		this.telefono = telefono;
		this.email = email;
		this.direccion = direccion;
		this.recetasActivas = recetasActivas;
		this.ultimaCompra = ultimaCompra;
		this.numRecetasPendientes = numRecetasPendientes;
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
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

	public String getRecetasActivas() {
		return recetasActivas;
	}

	public void setRecetasActivas(String recetasActivas) {
		this.recetasActivas = recetasActivas;
	}

	public Date getUltimaCompra() {
		return ultimaCompra;
	}

	public void setUltimaCompra(Date ultimaCompra) {
		this.ultimaCompra = ultimaCompra;
	}

	public int getNumRecetasPendientes() {
		return numRecetasPendientes;
	}

	public void setNumRecetasPendientes(int numRecetasPendientes) {
		this.numRecetasPendientes = numRecetasPendientes;
	}

	@Override
	public String toString() {
		return nombre + " (" + dni + ")";
	}
}
package domain;

import java.util.Date;

public class Trabajador {
	private int id;
	private String nombre;
	private String dni;
	private String telefono;

	private String email;
	private String direccion;
	private String puesto;
	private String nss;
	private String turno;
	private String salario;

	
	public Trabajador(int id, String nombre, String dni, String telefono, String email, String direccion,
			String puesto, String nss,String turno, String salario) {
		this.id = id;
		this.nombre = nombre;
		this.dni = dni;
		this.telefono = telefono;
		this.email = email;
		this.direccion = direccion;
		this.puesto = puesto;
		this.nss = nss;
		this.turno = turno;
		this.salario = salario;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getNss() {
		return nss;
	}


	public void setNss(String nss) {
		this.nss = nss;
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

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getSalario() {
		return salario;
	}

	public void setSalario(String salario) {
		this.salario = salario;
	}

	@Override
	public String toString() {
		return nombre + " (" + dni + ")";
	}
}

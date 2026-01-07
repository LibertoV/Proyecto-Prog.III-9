package domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Compra {
	private int id;
	private int idFarmacia;
	private int idCliente;
	private Date fecha;
	private Map<Integer, Integer> mapaProductos;
	public Compra(int id, int idFarmacia, int idCliente, Date fecha, Map<Integer, Integer> mapaProductos) {
		this.id = id;
		this.idFarmacia = idFarmacia;
		this.idCliente = idCliente;
		this.fecha = fecha;
		this.mapaProductos = mapaProductos;
		
	}
	public Compra() {
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Map<Integer, Integer> getMapaProductos() {
		return mapaProductos;
	}
	public void setMapaProductos(Map<Integer, Integer> mapaProductos) {
		this.mapaProductos = mapaProductos;
	}
	
}

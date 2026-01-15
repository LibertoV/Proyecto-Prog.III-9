package jdbc;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import domain.Venta;
//Realizado con ayuda de IAG
public class GestorBDInitializerVentas {
	protected static String DRIVER_NAME;
	protected static String DATABASE_FILE;
	protected static String CONNECTION_STRING;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public GestorBDInitializerVentas() {
		try {
			Properties connectionProperties = new Properties();
			connectionProperties.load(new FileReader("resources/parametros.properties"));
			
			DRIVER_NAME = connectionProperties.getProperty("DRIVER_NAME");
			DATABASE_FILE = connectionProperties.getProperty("DATABASE_FILE");
			CONNECTION_STRING = connectionProperties.getProperty("CONNECTION_STRING") + DATABASE_FILE;
			
			Class.forName(DRIVER_NAME);
		} catch (Exception ex) {
			System.err.format("\n* Error al cargar el driver de BBDD: %s", ex.getMessage());
			ex.printStackTrace();
		}
	}

	public List<Venta> obtenerVentasPorFarmacia(int idFarmacia) {
		List<Venta> ventas = new ArrayList<>();
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			String sql = "SELECT C.ID, C.ID_FARMACIA, C.ID_CLIENTE, C.FECHA, \n"
					   + "L.ID_PRODUCTO, L.CANTIDAD, CLI.NOMBRE, P.NOMBRE, P.PRECIO_UNITARIO \n"
					   + "FROM COMPRA C, LINEA_COMPRA L, CLIENTE CLI, PRODUCTO P \n"
					   + "WHERE C.ID = L.ID_COMPRA \n"
					   + "AND C.ID_CLIENTE = CLI.ID \n"
					   + "AND L.ID_PRODUCTO = P.ID \n"
					   + "AND C.ID_FARMACIA = ? \n"
					   + "ORDER BY C.FECHA DESC;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idFarmacia);
			ResultSet rs = pstmt.executeQuery();
			
			int contadorVenta = 1;
			while (rs.next()) {
				Venta venta = new Venta();
				venta.setId(contadorVenta++);
				venta.setIdCompra(rs.getInt(1));
				venta.setIdFarmacia(rs.getInt(2));
				venta.setIdCliente(rs.getInt(3));
				venta.setIdProducto(rs.getInt(5));
				venta.setCantidad(rs.getInt(6));
				venta.setPrecioUnitario(rs.getDouble(9));
				
				try {
					venta.setFecha(sdf.parse(rs.getString(4)));
				} catch (Exception e) {
					venta.setFecha(new java.util.Date());
				}
				
				venta.setNombreCliente(rs.getString(7));
				venta.setNombreProducto(rs.getString(8));
				ventas.add(venta);
			}
			
			System.out.format("\n- Se han recuperado %d ventas de la farmacia %d", ventas.size(), idFarmacia);
			
			rs.close();
			pstmt.close();
		} catch (Exception ex) {
			System.err.format("\n* Error al obtener ventas de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();
		}
		
		return ventas;
	}

	public List<Venta> obtenerTodasLasVentas() {
		List<Venta> ventas = new ArrayList<>();
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			String sql = "SELECT C.ID, C.ID_FARMACIA, C.ID_CLIENTE, C.FECHA, \n"
					   + "L.ID_PRODUCTO, L.CANTIDAD, CLI.NOMBRE, P.NOMBRE, P.PRECIO_UNITARIO \n"
					   + "FROM COMPRA C, LINEA_COMPRA L, CLIENTE CLI, PRODUCTO P \n"
					   + "WHERE C.ID = L.ID_COMPRA \n"
					   + "AND C.ID_CLIENTE = CLI.ID \n"
					   + "AND L.ID_PRODUCTO = P.ID \n"
					   + "ORDER BY C.FECHA DESC;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			int contadorVenta = 1;
			while (rs.next()) {
				Venta venta = new Venta();
				venta.setId(contadorVenta++);
				venta.setIdCompra(rs.getInt(1));
				venta.setIdFarmacia(rs.getInt(2));
				venta.setIdCliente(rs.getInt(3));
				venta.setIdProducto(rs.getInt(5));
				venta.setCantidad(rs.getInt(6));
				venta.setPrecioUnitario(rs.getDouble(9));
				
				try {
					venta.setFecha(sdf.parse(rs.getString(4)));
				} catch (Exception e) {
					venta.setFecha(new java.util.Date());
				}
				
				venta.setNombreCliente(rs.getString(7));
				venta.setNombreProducto(rs.getString(8));
				ventas.add(venta);
			}
			
			System.out.format("\n- Se han recuperado %d ventas totales", ventas.size());
			
			rs.close();
			pstmt.close();
		} catch (Exception ex) {
			System.err.format("\n* Error al obtener todas las ventas: %s", ex.getMessage());
			ex.printStackTrace();
		}
		
		return ventas;
	}

	public Map<String, Object> obtenerEstadisticasFarmacia(int idFarmacia) {
		Map<String, Object> stats = new HashMap<>();
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			String sql = "SELECT COUNT(DISTINCT C.ID), SUM(L.CANTIDAD), \n"
					   + "SUM(L.CANTIDAD * P.PRECIO_U), COUNT(DISTINCT C.ID_CLIENTE) \n"
					   + "FROM COMPRA C, LINEA_COMPRA L, PRODUCTO P \n"
					   + "WHERE C.ID = L.ID_COMPRA \n"
					   + "AND L.ID_PRODUCTO = P.ID \n"
					   + "AND C.ID_FARMACIA = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idFarmacia);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				stats.put("totalCompras", rs.getInt(1));
				stats.put("totalProductos", rs.getInt(2));
				stats.put("totalIngresos", rs.getDouble(3));
				stats.put("totalClientes", rs.getInt(4));
			}
			
			rs.close();
			pstmt.close();
		} catch (Exception ex) {
			System.err.format("\n* Error al obtener estadísticas: %s", ex.getMessage());
			ex.printStackTrace();
		}
		
		return stats;
	}

	public List<Venta> obtenerVentasPorFechas(int idFarmacia, java.util.Date fechaInicio, java.util.Date fechaFin) {
		List<Venta> ventas = new ArrayList<>();
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			String sql = "SELECT C.ID, C.ID_FARMACIA, C.ID_CLIENTE, C.FECHA, \n"
					   + "L.ID_PRODUCTO, L.CANTIDAD, CLI.NOMBRE, P.NOMBRE, P.PRECIO_UNITARIO \n"
					   + "FROM COMPRA C, LINEA_COMPRA L, CLIENTE CLI, PRODUCTO P \n"
					   + "WHERE C.ID = L.ID_COMPRA \n"
					   + "AND C.ID_CLIENTE = CLI.ID \n"
					   + "AND L.ID_PRODUCTO = P.ID \n"
					   + "AND C.ID_FARMACIA = ? \n"
					   + "AND C.FECHA BETWEEN ? AND ? \n"
					   + "ORDER BY C.FECHA DESC;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idFarmacia);
			pstmt.setString(2, sdf.format(fechaInicio));
			pstmt.setString(3, sdf.format(fechaFin));
			
			ResultSet rs = pstmt.executeQuery();
			
			int contadorVenta = 1;
			while (rs.next()) {
				Venta venta = new Venta();
				venta.setId(contadorVenta++);
				venta.setIdCompra(rs.getInt(1));
				venta.setIdFarmacia(rs.getInt(2));
				venta.setIdCliente(rs.getInt(3));
				venta.setIdProducto(rs.getInt(5));
				venta.setCantidad(rs.getInt(6));
				venta.setPrecioUnitario(rs.getDouble(9));
				
				try {
					venta.setFecha(sdf.parse(rs.getString(4)));
				} catch (Exception e) {
					venta.setFecha(new java.util.Date());
				}
				
				venta.setNombreCliente(rs.getString(7));
				venta.setNombreProducto(rs.getString(8));
				ventas.add(venta);
			}
			
			rs.close();
			pstmt.close();
		} catch (Exception ex) {
			System.err.format("\n* Error al obtener ventas por fechas: %s", ex.getMessage());
			ex.printStackTrace();
		}
		
		return ventas;
	}
}

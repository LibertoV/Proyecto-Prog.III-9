package jdbc;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import db.Cliente;



public class GestorBDInitializerCliente {
	protected static String DRIVER_NAME;
	protected static String DATABASE_FILE;
	protected static String CONNECTION_STRING;
    

	public GestorBDInitializerCliente() {
		try {
			Properties connectionProperties = new Properties();
			connectionProperties.load(new FileReader("resources/parametros.properties"));
			
			DRIVER_NAME = connectionProperties.getProperty("DRIVER_NAME");
			DATABASE_FILE = connectionProperties.getProperty("DATABASE_FILE");
			CONNECTION_STRING = connectionProperties.getProperty("CONNECTION_STRING") + DATABASE_FILE;
			
			//Cargar el diver SQLite
			Class.forName(DRIVER_NAME);
		} catch (Exception ex) {
			System.err.format("\n* Error al cargar el driver de BBDD: %s", ex.getMessage());
			ex.printStackTrace();
		}
	}
		
	public void crearBBDD() {
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {			
	        String sql = "CREATE TABLE IF NOT EXISTS CLIENTE (\n"
	                   + " ID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                   + " NOMBRE TEXT NOT NULL,\n"
	                   + " DNI TEXT NOT NULL,\n"
	                   + " TLF TEXT NOT NULL,\n"
	                   + " FECHA_ULTIMA_COMPRA TEXT NOT NULL,\n"
	                   + " RECETAS_PENDIENTES INT NOT NULL\n"
	                   + ");";
	        
	        PreparedStatement pstmt = con.prepareStatement(sql);
	        
	        if (!pstmt.execute()) {
	        	System.out.println("\n- Se ha creado la tabla Cliente");
	        }
	        
	       
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error al crear la BBDD: %s", ex.getMessage());
			ex.printStackTrace();			
		}
	}
	
	public void borrarBBDD() {
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {			
	        
			String sql = "DROP TABLE IF EXISTS CLIENTE";
			
	        PreparedStatement pstmt = con.prepareStatement(sql);
			
	       
	        if (!pstmt.execute()) {
	        	System.out.println("\n- Se ha borrado la tabla Cliente");
	        }

	        
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error al borrar la BBDD: %s", ex.getMessage());
			ex.printStackTrace();			
		}
		
		try {
			//Se borra el fichero de la BBDD
			Files.delete(Paths.get(DATABASE_FILE));
			System.out.println("\n- Se ha borrado el fichero de la BBDD");
		} catch (Exception ex) {
			System.err.format("\n* Error al borrar el archivo de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}
	}
	
	public void insertarDatos(Cliente... clientes) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se define la plantilla de la sentencia SQL
			String sql = "INSERT INTO CLIENTE (NOMBRE,DNI, TLF,FECHA_ULTIMA_COMPRA, RECETAS_PENDIENTES ) VALUES ( ?, ?,?,?,?);";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			System.out.println("- Insertando clientes...");
			
			//Se recorren los clientes y se insertan uno a uno
			for (Cliente c : clientes) {				
				
				pstmt.setString(1, c.getNombre());
				pstmt.setString(2, c.getDni());
				pstmt.setString(3, c.getTlf());
				pstmt.setString(4, c.getFechaUltimaCompra());
				pstmt.setInt(5, c.getRecetasPendientes());
				
				if (1 == pstmt.executeUpdate()) {					
					System.out.format("\n - Cliente insertado: %s", c.toString());
				} else {
					System.out.format("\n - No se ha insertado el cliente: %s", c.toString());
				}
			}			
		} catch (Exception ex) {
			System.err.format("\n* Error al insertar datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}				
	}
	
	public List<Cliente> obtenerDatos() {
		List<Cliente> clientes = new ArrayList<>();
		
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			String sql = "SELECT * FROM CLIENTE WHERE ID >= ?";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, 0);
			
			//Se ejecuta la sentencia y se obtiene el ResultSet con los resutlados
			ResultSet rs = pstmt.executeQuery();			
			Cliente cliente;
			
			//Se recorre el ResultSet y se crean objetos Cliente
			while (rs.next()) {
				cliente = new Cliente(rs.getInt("ID"), rs.getString("NOMBRE"), rs.getString("DNI"), rs.getString("TLF"), rs.getString("FECHA_ULTIMA_COMPRA"),rs.getInt("RECETAS_PENDIENTES"));
				cliente.setId(rs.getInt("ID"));
				
				//Se inserta cada nuevo cliente en la lista de clientes
				clientes.add(cliente);
			}
			
			System.out.format("\n- Se han recuperado %d clientes...", clientes.size());			
			
			//Se cierra el ResultSet
			rs.close();
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error al obtener datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
		
		return clientes;
	}

	public void actualizarTelefono(Cliente cliente, String newtlf) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE CLIENTE SET TLF = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setString(1, newtlf);
			pstmt.setInt(2, cliente.getId());
			
			int result = pstmt.executeUpdate();
			
			System.out.format("\n- Se ha actulizado %d clientes", result);
			
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error actualizando datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
	}
	
	public void actualizarRecetas(Cliente cliente, Integer newRecetas) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE CLIENTE SET RECETAS_PENDIENTES = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setInt(1, newRecetas);
			pstmt.setInt(2, cliente.getId());
			
			int result = pstmt.executeUpdate();
			
			System.out.format("\n- Se ha actulizado %d clientes", result);
			
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error actualizando datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
	}
	
	public void borrarDatos() {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 Statement stmt = con.createStatement()) {				
			//Se ejecuta la sentencia de borrado de datos
			String sql = "DELETE FROM CLIENTE;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			int result = pstmt.executeUpdate();
			System.out.format("\n- Se han borrado %d clientes", result);
			
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error al borrar datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
	}
}

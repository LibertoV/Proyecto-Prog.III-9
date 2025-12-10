package jdbc;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import db.Cliente;
import domain.Trabajador;

public class GestorBDInitializerTrabajadores {
	protected static String DRIVER_NAME;
	protected static String DATABASE_FILE;
	protected static String CONNECTION_STRING;
    

	public GestorBDInitializerTrabajadores() {
		try {
			Properties connectionProperties = new Properties();
			connectionProperties.load(new FileReader("resources/parametros.properties"));
			
			DRIVER_NAME = connectionProperties.getProperty("DRIVER_NAME");
			DATABASE_FILE = connectionProperties.getProperty("DATABASE_FILE");
			CONNECTION_STRING = connectionProperties.getProperty("CONNECTION_STRING") + DATABASE_FILE;
			
			//Cargar el driver SQLite
			Class.forName(DRIVER_NAME);
		} catch (Exception ex) {
			System.err.format("\n* Error al cargar el driver de BBDD: %s", ex.getMessage());
			ex.printStackTrace();
		}
	}
		
	public void crearBBDD() {
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {			
	        String sql = "CREATE TABLE IF NOT EXISTS TRABAJADOR (\n"
	                   + " ID INTEGER PRIMARY KEY ,\n"
	                   + " NOMBRE TEXT NOT NULL,\n"
	                   + " DNI TEXT NOT NULL,\n"
	                   + " TLF TEXT NOT NULL,\n"
	                   + " EMAIL TEXT NOT NULL,\n"
	                   + " DIRECCION TEXT NOT NULL,\n"
	                   + " PUESTO TEXT NOT NULL,\n"
	                   + " NSS TEXT NOT NULL,\n"
	                   + " TURNO TEXT, \n"
	                   + " SALARIO TEXT NOT NULL\n"
	                   + ");";
	        
	        PreparedStatement pstmt = con.prepareStatement(sql);
	        
	        if (!pstmt.execute()) {
	        	System.out.println("\n- Se ha creado la tabla Trabajador");
	        }
	        
	       
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error al crear la BBDD: %s", ex.getMessage());
			ex.printStackTrace();			
		}
	}
	
	public void borrarBBDD() {
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {			
	        
			String sql = "DROP TABLE IF EXISTS TRABAJADOR";
			
	        PreparedStatement pstmt = con.prepareStatement(sql);
			
	       
	        if (!pstmt.execute()) {
	        	System.out.println("\n- Se ha borrado la tabla Trabajador");
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
	
	public void insertarDatos(Trabajador... trabajadores) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se define la plantilla de la sentencia SQL
			String sql = "INSERT OR IGNORE INTO TRABAJADOR " +
		             "(ID, NOMBRE, DNI, TLF, EMAIL, DIRECCION, PUESTO, NSS, TURNO, SALARIO) " +
		             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			System.out.println("- Insertando trabajadores...");
			
			//Se recorren los clientes y se insertan uno a uno
			for (Trabajador t : trabajadores) {				
				pstmt.setInt(1, t.getId()); 
	            pstmt.setString(2, t.getNombre());
	            pstmt.setString(3, t.getDni());
	            pstmt.setString(4, t.getTelefono());
	            pstmt.setString(5, t.getEmail());
	            pstmt.setString(6, t.getDireccion());
	            pstmt.setString(7, t.getPuesto());
	            pstmt.setString(8, t.getNss());
	            pstmt.setString(9, t.getTurno());
	            pstmt.setString(10, t.getSalario());
	            
				
				if (1 == pstmt.executeUpdate()) {					
					System.out.format("\n - Trabajador insertado: %s", t.toString());
				} else {
					System.out.format("\n - No se ha insertado el trabajador: %s", t.toString());
				}
			}			
		} catch (Exception ex) {
			System.err.format("\n* Error al insertar datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}				
	}
	
	public List<Trabajador> obtenerDatos() {
		List<Trabajador> trabajadores = new ArrayList<>();
		
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			String sql = "SELECT * FROM TRABAJADOR WHERE ID >= ?";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, 0);
			
			//Se ejecuta la sentencia y se obtiene el ResultSet con los resutlados
			ResultSet rs = pstmt.executeQuery();			
			Trabajador trabajador;
			
			//Se recorre el ResultSet y se crean objetos Cliente
			while (rs.next()) {
				trabajador = new Trabajador(rs.getInt("ID"), rs.getString("NOMBRE"), rs.getString("DNI"), rs.getString("TLF"), rs.getString("EMAIL"), rs.getString("DIRECCION"), rs.getString("PUESTO"),rs.getString("NSS"), rs.getString("TURNO"), rs.getString("SALARIO"));
				trabajador.setId(rs.getInt("ID"));
				
				//Se inserta cada nuevo cliente en la lista de clientes
				trabajadores.add(trabajador);
			}
			
			System.out.format("\n- Se han recuperado %d trabajadores...", trabajadores.size());			
			
			//Se cierra el ResultSet
			rs.close();
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error al obtener datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
		
		return trabajadores;
	}
	public void actualizarNombre(Trabajador trabajador, String newNombre) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE TRABAJADOR SET NOMBRE = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setString(1, newNombre);
			pstmt.setInt(2, trabajador.getId());
			
			int result = pstmt.executeUpdate();
			
			System.out.format("\n- Se ha actulizado nombre %d trabajadores", result);
			
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error actualizando datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
	}
	
	public void actualizarDNI(Trabajador trabajador, String newDNI) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE TRABAJADOR SET DNI = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setString(1, newDNI);
			pstmt.setInt(2, trabajador.getId());
			
			int result = pstmt.executeUpdate();
			
			System.out.format("\n- Se ha actulizado DNI %d trabajadores", result);
			
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error actualizando datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
	}

	public void actualizarTelefono(Trabajador trabajador, String newtlf) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE TRABAJADOR SET TLF = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setString(1, newtlf);
			pstmt.setInt(2, trabajador.getId());
			
			int result = pstmt.executeUpdate();
			
			System.out.format("\n- Se ha actulizado %d trabajadores", result);
			
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error actualizando datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
	}
	public void actualizarDireccion(Trabajador trabajador, String newDireccion) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE TRABAJADOR SET DIRECCION = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setString(1, newDireccion);
			pstmt.setInt(2, trabajador.getId());
			
			int result = pstmt.executeUpdate();
			
			System.out.format("\n- Se ha actulizado %d trabajadores", result);
			
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error actualizando datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
	}
	
	public void actualizarEmail(Trabajador trabajador, String newEmail) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE TRABAJADOR SET EMAIL = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setString(1, newEmail);
			pstmt.setInt(2, trabajador.getId());
			
			int result = pstmt.executeUpdate();
			
			System.out.format("\n- Se ha actulizado %d trabajadores", result);
			
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error actualizando datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
	}
	
	public void actualizarNSS(Trabajador trabajador, String newNSS) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE TRABAJADOR SET NSS = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setString(1, newNSS);
			pstmt.setInt(2, trabajador.getId());
			
			int result = pstmt.executeUpdate();
			
			System.out.format("\n- Se ha actulizado %d trabajadores", result);
			
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error actualizando datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
	}
	
	public void actualizarPuesto(Trabajador trabajador, String newPuesto) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE TRABAJADOR SET PUESTO = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setString(1, newPuesto);
			pstmt.setInt(2, trabajador.getId());
			
			int result = pstmt.executeUpdate();
			
			System.out.format("\n- Se ha actulizado %d trabajadores", result);
			
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error actualizando datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
	}
	
	public void actualizarTurno(Trabajador trabajador, String newTurno) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE TRABAJADOR SET TURNO = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setString(1, newTurno);
			pstmt.setInt(2, trabajador.getId());
			
			int result = pstmt.executeUpdate();
			
			System.out.format("\n- Se ha actulizado %d trabajadores", result);
			
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error actualizando datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
	}
	
	public void actualizarSalario(Trabajador trabajador, String newSalario) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE TRABAJADOR SET SALARIO = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setString(1, newSalario);
			pstmt.setInt(2, trabajador.getId());
			
			int result = pstmt.executeUpdate();
			
			System.out.format("\n- Se ha actulizado %d trabajadores", result);
			
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error actualizando datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
	}
	
	public void borrarTrabajador(Integer id) {
		
		 String sql = "DELETE FROM TRABAJADOR WHERE id = ?";
		    
		    try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {
		        
		        pstmt.setInt(1, id);
		        int filasAfectadas = pstmt.executeUpdate();
		        
		        if (filasAfectadas > 0) {
		            System.out.println("✓ Trabajador con ID " + id + " eliminado de la BD");
		        } else {
		            System.out.println("✗ No se encontró trabajador con ID " + id);
		        }
		        
		    } catch (SQLException e) {
		        System.err.println("Error al eliminar trabajador: " + e.getMessage());
		        e.printStackTrace();
		    }
	}
	
	//IAG ADAPTADO
	public int obtenerPrimerIdDisponible() {
	    String sql = "SELECT id FROM TRABAJADOR ORDER BY id";
	    int idDisponible = 1;
	    
	    try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        
	        while (rs.next()) {
	            int idActual = rs.getInt("id");
	            
	            // Si encontramos un hueco, ese es el ID disponible
	            if (idActual != idDisponible) {
	                break;
	            }
	            idDisponible++;
	        }
	        
	        System.out.println("Primer ID disponible: " + idDisponible);
	        return idDisponible;
	        
	    } catch (SQLException e) {
	        System.err.println("Error al obtener ID disponible: " + e.getMessage());
	        // Si hay error, devolver el máximo + 1
	        return obtenerMaximoId() + 1;
	    }
	}
	
	// IAG ADAPTADO
	// Método auxiliar para obtener el ID máximo 
	public int obtenerMaximoId() {
	    String sql = "SELECT MAX(id) as max_id FROM TRABAJADOR";
	    
	    try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        
	        if (rs.next()) {
	            return rs.getInt("max_id");
	        }
	        return 0;
	        
	    } catch (SQLException e) {
	        System.err.println("Error al obtener máximo ID: " + e.getMessage());
	        return 0;
	    }
	}
	
	public void borrarDatos() {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 Statement stmt = con.createStatement()) {				
			//Se ejecuta la sentencia de borrado de datos
			String sql = "DELETE FROM TRABAJADOR;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			int result = pstmt.executeUpdate();
			System.out.format("\n- Se han borrado %d trabajadores", result);
			
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error al borrar datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
	}
}

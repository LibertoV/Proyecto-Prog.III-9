package jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
			
			//Cargar el driver SQLite
			Class.forName(DRIVER_NAME);
		} catch (Exception ex) {
			System.err.format("\n* Error al cargar el driver de BBDD: %s", ex.getMessage());
			ex.printStackTrace();
		}
	}
		
	public void crearBBDD() {
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {			
	        String sql = "CREATE TABLE IF NOT EXISTS CLIENTE (\n"
	                   + " ID INTEGER PRIMARY KEY ,\n"
	                   + " NOMBRE TEXT NOT NULL,\n"
	                   + " DNI TEXT NOT NULL,\n"
	                   + " TLF TEXT NOT NULL,\n"
	                   + " FECHA_ULTIMA_COMPRA TEXT ,\n"
	                   + " COMPRAS INT NOT NULL,\n"
	                   + " EMAIL TEXT NOT NULL,\n"
	                   + " DIRECCION TEXT NOT NULL\n"
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
			String sql = "REPLACE INTO CLIENTE (ID, NOMBRE,DNI, TLF,FECHA_ULTIMA_COMPRA, COMPRAS, EMAIL, DIRECCION ) VALUES ( ?,?,?,?,?,?,?,?);";
			String selCompra = "SELECT MAX(C.FECHA) AS ULTIMA_FECHA, COUNT(*) AS TOTAL_CANTIDAD " +
                    			"FROM COMPRA C " + 
                    			"LEFT JOIN LINEA_COMPRA L ON C.ID = L.ID_COMPRA " +
                    			"WHERE C.ID_CLIENTE = ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			PreparedStatement pretsmtSel = con.prepareStatement(selCompra);
			System.out.println("- Insertando clientes...");
			
			//Se recorren los clientes y se insertan uno a uno
			for (Cliente c : clientes) {
				
				String fechaMax = "Sin Compras";
				int totalComprado = 0;
				pretsmtSel.setInt(1,c.getId());
				try(ResultSet rs = pretsmtSel.executeQuery()){
					if(rs.next()) {
						String fecha= rs.getString("ULTIMA_FECHA");
						if(fecha != null) {
							fechaMax = fecha;
						}
						totalComprado = rs.getInt("TOTAL_CANTIDAD");
						
				}
				
				}
				pstmt.setInt(1, c.getId()); 
	            pstmt.setString(2, c.getNombre());
	            pstmt.setString(3, c.getDni());
	            pstmt.setString(4, c.getTlf());
	            pstmt.setString(5, fechaMax);
	            pstmt.setInt(6, totalComprado);
	            pstmt.setString(7, c.getEmail());
	            pstmt.setString(8, c.getDireccion());
				
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
	//IAG sin cambios, para la primera vez que se usa
	public List<Cliente> obtenerDatosCSV() {
	    List<Cliente> lista = new ArrayList<>();
	    // Ajusta la ruta a tu carpeta de recursos
	    File archivo = new File("resources/db/clientes.csv"); 

	    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
	        String linea = br.readLine(); // Saltar cabecera
	        while ((linea = br.readLine()) != null) {
	            // Este Regex separa por comas pero ignora las que están dentro de comillas (direcciones)
	            String[] datos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
	            
	            if (datos.length >= 8) {
	                try {
	                    int id = Integer.parseInt(datos[0].trim());
	                    String nombre = datos[1].trim();
	                    String dni = datos[2].trim();
	                    String tlf = datos[3].trim();
	                    // datos[4] y [5] son la fecha y recetas del CSV (las ignoramos porque las calcularemos)
	                    String email = datos[6].trim();
	                    String dir = datos[7].replace("\"", "").trim(); // Quitamos las comillas de la dirección

	                    lista.add(new Cliente(id, nombre, dni, tlf, "N/A", 0, email, dir));
	                } catch (Exception e) {
	                    System.err.println("Error en línea: " + e.getMessage());
	                }
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("No se encontró el CSV: " + e.getMessage());
	    }
	    return lista;
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
				cliente = new Cliente(rs.getInt("ID"), rs.getString("NOMBRE"), rs.getString("DNI"), rs.getString("TLF"), rs.getString("FECHA_ULTIMA_COMPRA"),rs.getInt("COMPRAS"), rs.getString("EMAIL"), rs.getString("DIRECCION"));
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
	public void actualizarNombre(Cliente cliente, String newNombre) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE CLIENTE SET NOMBRE = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setString(1, newNombre);
			pstmt.setInt(2, cliente.getId());
			
			int result = pstmt.executeUpdate();
			
			System.out.format("\n- Se ha actulizado nombre %d clientes", result);
			
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error actualizando datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
	}
	
	public void actualizarDNI(Cliente cliente, String newDNI) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE CLIENTE SET DNI = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setString(1, newDNI);
			pstmt.setInt(2, cliente.getId());
			
			int result = pstmt.executeUpdate();
			
			System.out.format("\n- Se ha actulizado DNI %d clientes", result);
			
	        //Es necesario cerrar el PreparedStatement
	        pstmt.close();		
		} catch (Exception ex) {
			System.err.format("\n* Error actualizando datos de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();						
		}		
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
	public void actualizarDireccion(Cliente cliente, String newDireccion) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE CLIENTE SET DIRECCION = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setString(1, newDireccion);
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
	
	public void actualizarEmail(Cliente cliente, String newEmail) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE CLIENTE SET EMAIL = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setString(1, newEmail);
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
	
	public void actualizarRecetas(Cliente cliente, Integer newCompra) {
		//Se abre la conexión y se obtiene el Statement
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			//Se ejecuta la sentencia de borrado de datos
			String sql = "UPDATE CLIENTE SET COMPRAS = ? WHERE ID = ?;";
			
			PreparedStatement pstmt = con.prepareStatement(sql);	
			pstmt.setInt(1, newCompra);
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
	
	public void borrarCliente(Integer id) {
		
		 String sql = "DELETE FROM CLIENTE WHERE id = ?";
		    
		    try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {
		        
		        pstmt.setInt(1, id);
		        int filasAfectadas = pstmt.executeUpdate();
		        
		        if (filasAfectadas > 0) {
		            System.out.println("✓ Cliente con ID " + id + " eliminado de la BD");
		        } else {
		            System.out.println("✗ No se encontró cliente con ID " + id);
		        }
		        
		    } catch (SQLException e) {
		        System.err.println("Error al eliminar cliente: " + e.getMessage());
		        e.printStackTrace();
		    }
	}
	//IAG sin cambios
	public void actualizarResumenCompra(int idCliente, String fechaHoy) {
	    // Esta consulta calcula los nuevos valores basados en la tabla COMPRA
	    String sql = "UPDATE CLIENTE SET " +
                "compras = compras + 1, " +
                "fecha_ultima_compra = ? " +
                "WHERE id = ?";
   
   try (java.sql.Connection con = DriverManager.getConnection(CONNECTION_STRING);
        java.sql.PreparedStatement pstmt = con.prepareStatement(sql)) {
       pstmt.setString(1,fechaHoy );
       pstmt.setInt(2, idCliente);
       pstmt.executeUpdate();
   } catch (Exception e) {
       e.printStackTrace();
   }
}
	//IAG
	public int obtenerPrimerIdDisponible() {
	    String sql = "SELECT id FROM CLIENTE ORDER BY id";
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
	
	// IAG 
	//SIN CAMBIOS
	// Método auxiliar para obtener el ID máximo 
	public int obtenerMaximoId() {
	    String sql = "SELECT MAX(id) as max_id FROM CLIENTE";
	    
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

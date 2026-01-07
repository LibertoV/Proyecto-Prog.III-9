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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import domain.Compra;

public class GestorBDInitializerCompras {
    protected static String DRIVER_NAME;
    protected static String DATABASE_FILE;
    protected static String CONNECTION_STRING;
    
    // Formato para guardar la fecha en texto en la BD
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public GestorBDInitializerCompras() {
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
        
    public void crearBBDD() {
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {            
            
            // 1. Tabla Principal: COMPRA
            String sqlCompra = "CREATE TABLE IF NOT EXISTS COMPRA (\n"
                             + " ID INTEGER PRIMARY KEY,\n"
                             + " ID_FARMACIA INTEGER NOT NULL,\n"
                             + " ID_CLIENTE INTEGER NOT NULL,\n"
                             + " FECHA TEXT NOT NULL\n"
                             + ");";
            
            // 2. Tabla Secundaria: LINEA_COMPRA (Relaciona Compra con Productos y cantidad)
            String sqlLinea = "CREATE TABLE IF NOT EXISTS LINEA_COMPRA (\n"
                            + " ID_COMPRA INTEGER,\n"
                            + " ID_PRODUCTO INTEGER,\n"
                            + " CANTIDAD INTEGER,\n"
                            + " PRIMARY KEY (ID_COMPRA, ID_PRODUCTO),\n"
                            + " FOREIGN KEY(ID_COMPRA) REFERENCES COMPRA(ID)\n"
                            + ");";
        
            Statement stmt = con.createStatement();
            stmt.execute(sqlCompra);
            stmt.execute(sqlLinea);
            
            System.out.println("\n- Se han creado las tablas de Compras y Lineas de Compra");
            stmt.close();       
        } catch (Exception ex) {
            System.err.format("\n* Error al crear la BBDD: %s", ex.getMessage());
            ex.printStackTrace();            
        }
    }
    
    public void borrarBBDD() {
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {            
            Statement stmt = con.createStatement();
            
            // Borramos primero las lineas por la restricción de clave foránea
            stmt.execute("DROP TABLE IF EXISTS LINEA_COMPRA");
            stmt.execute("DROP TABLE IF EXISTS COMPRA");
            
            System.out.println("\n- Se han borrado las tablas de Compras");
            stmt.close();       
        } catch (Exception ex) {
            System.err.format("\n* Error al borrar la BBDD: %s", ex.getMessage());
            ex.printStackTrace();            
        }
        
        try {
            Files.deleteIfExists(Paths.get(DATABASE_FILE));
            System.out.println("\n- Se ha borrado el fichero de la BBDD");
        } catch (Exception ex) {
            System.err.format("\n* Error al borrar el archivo de la BBDD: %s", ex.getMessage());
            ex.printStackTrace();                        
        }
    }
    
    public void insertarDatos(Compra... compras) {
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
            // Desactivamos auto-commit para asegurar que se guarda la compra Y sus productos, o nada.
            con.setAutoCommit(false); 
            for (Compra c : compras) {
            	if (compras.length != 1) break;
            	int nuevoId = 1; // Por defecto será 1 si la tabla está vacía
                try (Statement stmt = con.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT MAX(ID) FROM COMPRA")) {
                    if (rs.next()) {
                        // Si el max es 50, el nuevo será 51. 
                        // Si es null (tabla vacía), se queda en 1.
                        nuevoId = rs.getInt(1) + 1; 
                    }
                }
                
                // Asignamos ese ID calculado a tu objeto Compra
                c.setId(nuevoId);
            }
            String sqlCompra = "INSERT OR IGNORE INTO COMPRA (ID, ID_FARMACIA, ID_CLIENTE, FECHA) VALUES (?, ?, ?, ?);";
            String sqlLinea = "INSERT INTO LINEA_COMPRA (ID_COMPRA, ID_PRODUCTO, CANTIDAD) VALUES (?, ?, ?);";

            PreparedStatement pstmtCompra = con.prepareStatement(sqlCompra);
            PreparedStatement pstmtLinea = con.prepareStatement(sqlLinea);
            
            System.out.println("- Insertando compras...");
            
            for (Compra c : compras) {                
                // 1. Insertar Cabecera de Compra
                pstmtCompra.setInt(1, c.getId()); 
                pstmtCompra.setInt(2, c.getIdFarmacia());
                pstmtCompra.setInt(3, c.getIdCliente());
                // Guardamos la fecha como texto formateado
                pstmtCompra.setString(4, sdf.format(c.getFecha())); 
                
                int filas = pstmtCompra.executeUpdate();
                
                if (filas > 0) {
                    // 2. Insertar Productos de la Compra
                    // Asumo que getProductos() devuelve un Map<Integer, Integer> (ID_Producto, Cantidad)
                    Map<Integer, Integer> productos = c.getMapaProductos();
                    
                    if (productos != null) {
                        for (Map.Entry<Integer, Integer> entry : productos.entrySet()) {
                            pstmtLinea.setInt(1, c.getId());        // ID de la compra recien insertada
                            pstmtLinea.setInt(2, entry.getKey());   // ID del producto
                            pstmtLinea.setInt(3, entry.getValue()); // Cantidad
                            pstmtLinea.addBatch(); // Añadir al lote
                        }
                        pstmtLinea.executeBatch(); // Ejecutar lote de productos
                    }
                    System.out.format("\n - Compra insertada ID: %d", c.getId());
                } else {
                    System.out.format("\n - No se ha insertado la compra ID: %d (Posible duplicado)", c.getId());
                }
            }
            
            con.commit(); // Confirmar cambios
            con.setAutoCommit(true); // Restaurar estado
            
            pstmtCompra.close();
            pstmtLinea.close();
            
        } catch (Exception ex) {
            System.err.format("\n* Error al insertar datos de la BBDD: %s", ex.getMessage());
            ex.printStackTrace();                        
        }                
    }
    
    public List<Compra> obtenerDatos() {
        List<Compra> compras = new ArrayList<>();
        
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
            // 1. Obtenemos las compras
            String sql = "SELECT * FROM COMPRA";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();            
            
            while (rs.next()) {
                int idCompra = rs.getInt("ID");
                int idFarmacia = rs.getInt("ID_FARMACIA");
                int idCliente = rs.getInt("ID_CLIENTE");
                String fechaStr = rs.getString("FECHA");
                
                Date fecha = null;
                try {
                    fecha = sdf.parse(fechaStr);
                } catch (Exception e) {
                    fecha = new Date(); // Fallback si falla el parseo
                }

                // 2. Para cada compra, obtenemos sus productos (Subconsulta)
                Map<Integer, Integer> mapaProductos = obtenerProductosDeCompra(con, idCompra);

                // Asumo un constructor: Compra(id, idFarmacia, idCliente, fecha, mapProductos)
                Compra compra = new Compra(idCompra, idFarmacia, idCliente, fecha, mapaProductos);
                
                compras.add(compra);
            }
            
            System.out.format("\n- Se han recuperado %d compras...", compras.size());            
            rs.close();
            pstmt.close();        
        } catch (Exception ex) {
            System.err.format("\n* Error al obtener datos de la BBDD: %s", ex.getMessage());
            ex.printStackTrace();                        
        }        
        
        return compras;
    }
    
    // Método auxiliar privado para recuperar los productos de una compra
    private Map<Integer, Integer> obtenerProductosDeCompra(Connection con, int idCompra) throws SQLException {
        Map<Integer, Integer> productos = new HashMap<>();
        String sql = "SELECT ID_PRODUCTO, CANTIDAD FROM LINEA_COMPRA WHERE ID_COMPRA = ?";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, idCompra);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                productos.put(rs.getInt("ID_PRODUCTO"), rs.getInt("CANTIDAD"));
            }
            rs.close();
        }
        return productos;
    }

    // --- MÉTODOS DE ACTUALIZACIÓN (Solo datos cabecera) ---

    public void actualizarFecha(Compra compra, Date nuevaFecha) {
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
            String sql = "UPDATE COMPRA SET FECHA = ? WHERE ID = ?;";
            PreparedStatement pstmt = con.prepareStatement(sql);    
            pstmt.setString(1, sdf.format(nuevaFecha));
            pstmt.setInt(2, compra.getId());
            
            int result = pstmt.executeUpdate();
            System.out.format("\n- Se ha actualizado fecha en %d compras", result);
            pstmt.close();        
        } catch (Exception ex) {
            System.err.format("\n* Error actualizando fecha: %s", ex.getMessage());
            ex.printStackTrace();                        
        }        
    }
    
    public void actualizarCliente(Compra compra, int nuevoIdCliente) {
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
            String sql = "UPDATE COMPRA SET ID_CLIENTE = ? WHERE ID = ?;";
            PreparedStatement pstmt = con.prepareStatement(sql);    
            pstmt.setInt(1, nuevoIdCliente);
            pstmt.setInt(2, compra.getId());
            
            int result = pstmt.executeUpdate();
            System.out.format("\n- Se ha actualizado cliente en %d compras", result);
            pstmt.close();        
        } catch (Exception ex) {
            System.err.format("\n* Error actualizando cliente: %s", ex.getMessage());
            ex.printStackTrace();                        
        }        
    }

    public void borrarCompra(Integer id) {
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING)) {
            // Importante: Borrar primero las líneas de producto asociadas
            String sqlLineas = "DELETE FROM LINEA_COMPRA WHERE ID_COMPRA = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlLineas)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }

            // Luego borrar la cabecera
            String sqlCompra = "DELETE FROM COMPRA WHERE ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlCompra)) {
                pstmt.setInt(1, id);
                int filasAfectadas = pstmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    System.out.println("✓ Compra con ID " + id + " eliminada de la BD");
                } else {
                    System.out.println("✗ No se encontró compra con ID " + id);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar compra: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public int obtenerPrimerIdDisponible() {
        String sql = "SELECT ID FROM COMPRA ORDER BY ID";
        int idDisponible = 1;
        
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int idActual = rs.getInt("ID");
                if (idActual != idDisponible) {
                    break;
                }
                idDisponible++;
            }
            
            System.out.println("Primer ID disponible: " + idDisponible);
            return idDisponible;
            
        } catch (SQLException e) {
            System.err.println("Error al obtener ID disponible: " + e.getMessage());
            return obtenerMaximoId() + 1;
        }
    }
    
    public int obtenerMaximoId() {
        String sql = "SELECT MAX(ID) as max_id FROM COMPRA";
        
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
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             Statement stmt = con.createStatement()) {                
            
            stmt.executeUpdate("DELETE FROM LINEA_COMPRA;");
            int result = stmt.executeUpdate("DELETE FROM COMPRA;");
            
            System.out.format("\n- Se han borrado %d compras", result);
      
        } catch (Exception ex) {
            System.err.format("\n* Error al borrar datos de la BBDD: %s", ex.getMessage());
            ex.printStackTrace();                        
        }        
    }
}
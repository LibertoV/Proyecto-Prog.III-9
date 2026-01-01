package jdbc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import domain.Pedido;
import domain.Producto;
import gui.JFramePrincipal;

public class GestorBDInitializerProducto {
	protected static String DRIVER_NAME;
    protected static String DATABASE_FILE;
    protected static String CONNECTION_STRING;

    public GestorBDInitializerProducto() {
        try {
            Properties props = new Properties();
            props.load(new FileReader("resources/parametros.properties"));

            DRIVER_NAME = props.getProperty("DRIVER_NAME");
            DATABASE_FILE = props.getProperty("DATABASE_FILE");
            CONNECTION_STRING = props.getProperty("CONNECTION_STRING") + DATABASE_FILE;

            Class.forName(DRIVER_NAME);
        } catch (Exception e) {
            System.err.println("Error cargando configuración o driver");
            e.printStackTrace();
        }
    }
    public void crearBBDD() {
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
            con.createStatement().execute("PRAGMA foreign_keys = ON");

            String sqlProducto =
                    "CREATE TABLE IF NOT EXISTS PRODUCTO (\n"
                  + " ID INTEGER PRIMARY KEY,\n"
                  + " NOMBRE TEXT NOT NULL UNIQUE,\n"
                  + " PRECIO_UNITARIO REAL NOT NULL\n"
                  + ");";

            PreparedStatement pstmt = con.prepareStatement(sqlProducto);
            if (!pstmt.execute())
                System.out.println("\n- Tabla PRODUCTO creada/verificada.");
            pstmt.close();

        } catch (Exception ex) {
            System.err.format("\n* Error al crear la BBDD: %s", ex.getMessage());
            ex.printStackTrace();
        }
    }
    public void borrarBBDD() {
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
            String sql = "DROP TABLE IF EXISTS PRODUCTO";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.execute();
            pstmt.close();

            System.out.println("- Tabla PRODUCTO borrada.");

        } catch (Exception ex) {
            System.err.format("\n* Error al borrar la BBDD: %s", ex.getMessage());
            ex.printStackTrace();
        }
    }
    public void insertarDatos(Producto... productos) {
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
            con.createStatement().execute("PRAGMA foreign_keys = ON");
			con.setAutoCommit(false);

            String sqlInsert =
                    "INSERT INTO PRODUCTO (ID,NOMBRE, PRECIO_UNITARIO) VALUES (?, ?, ?);";

            PreparedStatement pst = con.prepareStatement(sqlInsert);
            
            for (Producto producto : productos) {
            	pst.setInt(1, producto.getId());
            	pst.setString(2, producto.getNombre());
                pst.setDouble(3, producto.getPrecioUnitario());
                pst.executeUpdate();
            }

            con.commit();
			System.out.println("\n- Datos insertados correctamente.");

			pst.close();
			
        } catch (Exception ex) {
            System.err.format("\n* Error al insertar: %s", ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public List<Producto> obtenerDatos() {
		List<Producto> productos = new ArrayList<>();

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			String sqlProducto = "SELECT * FROM PRODUCTO";
			PreparedStatement pstProducto = con.prepareStatement(sqlProducto);


			ResultSet rs = pstProducto.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID");
				String nombre = rs.getString("NOMBRE");
				Double precioU = rs.getDouble("PRECIO_UNITARIO");


				Producto p = new Producto(id, nombre, precioU);
				productos.add(p);
			}

			System.out.format("\n- Se han recuperado %d productos", productos.size());

			rs.close();
			pstProducto.close();

		} catch (Exception ex) {
			System.err.format("\n* Error al obtener datos: %s", ex.getMessage());
			ex.printStackTrace();
		}
		return productos;
	}
    
    
    public Producto obtenerProductoPorId(int id) {
        Producto producto = null;

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
            // 1. Preparamos la consulta SQL con un placeholder (?)
            String sql = "SELECT * FROM PRODUCTO WHERE ID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            // 2. Asignamos el valor del ID al parámetro
            pst.setInt(1, id);

            // 3. Ejecutamos la consulta 
            ResultSet rs = pst.executeQuery();

            // 4. Verificamos si hay resultados (usamos if porque el ID es único)
            if (rs.next()) {
                String nombre = rs.getString("NOMBRE");
                Double precioU = rs.getDouble("PRECIO_UNITARIO");

                // Creamos el objeto con los datos recuperados
                producto = new Producto(id, nombre, precioU);
            }

            // Mensajes de log para depuración
            if (producto != null) {
                System.out.format("\n- Producto recuperado: %s (ID: %d)", producto.getNombre(), id);
            } else {
                System.out.format("\n- No se encontró ningún producto con el ID: %d", id);
            }

            // Cerramos recursos
            rs.close();
            pst.close();

        } catch (Exception ex) {
            System.err.format("\n* Error al buscar producto por ID: %s", ex.getMessage());
            ex.printStackTrace();
        }

        return producto;
    }

}

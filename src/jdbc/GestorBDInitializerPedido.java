package jdbc;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import domain.Pedido;
import domain.Producto;
import gui.JFramePrincipal;

public class GestorBDInitializerPedido {

	protected static String DRIVER_NAME;
	protected static String DATABASE_FILE;
	protected static String CONNECTION_STRING;

	public GestorBDInitializerPedido() {
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
			con.createStatement().execute("PRAGMA foreign_keys = ON");
			String sqlPedido = "CREATE TABLE IF NOT EXISTS PEDIDO (\n" + " ID TEXT PRIMARY KEY,\n"
					+ " FECHA_ORDEN TEXT NOT NULL,\n" + " FECHA_LLEGADA TEXT,\n" + " TOTAL REAL NOT NULL,\n"
					+ " PROVEEDOR TEXT NOT NULL,\n" + " ID_FARMACIA INTEGER,\n"
					+ " FOREIGN KEY (ID_FARMACIA) REFERENCES FARMACIA(ID) ON DELETE CASCADE\n" + ");";

			PreparedStatement pstmt = con.prepareStatement(sqlPedido);
			if (!pstmt.execute())
				System.out.println("\n- Tabla PEDIDO creada/verificada.");
			pstmt.close();

			String sqlLineas = "CREATE TABLE IF NOT EXISTS LINEA_PEDIDO (\n"
					+ " ID_LINEA INTEGER PRIMARY KEY AUTOINCREMENT,\n" + " ID_PEDIDO TEXT NOT NULL,\n"
					+ " NOMBRE_PRODUCTO TEXT NOT NULL,\n" + " CANTIDAD INTEGER NOT NULL,\n"
					+ " PRECIO_UNITARIO REAL NOT NULL,\n"
					+ " FOREIGN KEY (ID_PEDIDO) REFERENCES PEDIDO(ID) ON DELETE CASCADE\n" + ");";

			PreparedStatement pstmtLineas = con.prepareStatement(sqlLineas);
			if (!pstmtLineas.execute())
				System.out.println("\n- Tabla LINEA_PEDIDO creada/verificada.");
			pstmtLineas.close();

		} catch (Exception ex) {
			System.err.format("\n* Error al crear la BBDD: %s", ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void borrarBBDD() {
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			con.createStatement().execute("PRAGMA foreign_keys = ON");
			String sql1 = "DROP TABLE IF EXISTS LINEA_PEDIDO";
			String sql2 = "DROP TABLE IF EXISTS PEDIDO";

			PreparedStatement pstmt1 = con.prepareStatement(sql1);
			pstmt1.execute();
			pstmt1.close();

			PreparedStatement pstmt2 = con.prepareStatement(sql2);
			pstmt2.execute();
			pstmt2.close();

			System.out.println("- Tablas PEDIDO y LINEA_PEDIDO borradas.");
		} catch (Exception ex) {
			System.err.format("\n* Error al borrar la BBDD: %s", ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void insertarDatos(Pedido... pedidos) {
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			con.createStatement().execute("PRAGMA foreign_keys = ON");
			con.setAutoCommit(false);

			String sqlPedido = "INSERT INTO PEDIDO (ID, FECHA_ORDEN, FECHA_LLEGADA, TOTAL, PROVEEDOR, ID_FARMACIA) VALUES (?,?,?,?,?,?);";
			String sqlLinea = "INSERT INTO LINEA_PEDIDO (ID_PEDIDO, NOMBRE_PRODUCTO, CANTIDAD, PRECIO_UNITARIO) VALUES (?,?,?,?);";

			PreparedStatement pstPedido = con.prepareStatement(sqlPedido);
			PreparedStatement pstLinea = con.prepareStatement(sqlLinea);

			SimpleDateFormat dbFormatter = new SimpleDateFormat("yyyy-MM-dd");

			System.out.println("\n- Insertando pedidos y sus productos...");

			for (Pedido p : pedidos) {
				pstPedido.setString(1, p.getId());

				pstPedido.setString(2, dbFormatter.format(p.getFechaOrden()));
				if (p.getFechaLlegada() != null) {
					pstPedido.setString(3, dbFormatter.format(p.getFechaLlegada()));
				} else {
					pstPedido.setString(3, null);
				}
				pstPedido.setDouble(4, p.calcularTotal());
				pstPedido.setString(5, p.getProveedor());
				pstPedido.setInt(6, p.getIdFarmacia());
				pstPedido.executeUpdate();

				for (Producto prod : p.getProductos()) {
					pstLinea.setString(1, p.getId());
					pstLinea.setString(2, prod.getNombre());
					pstLinea.setInt(3, prod.getCantidad());
					pstLinea.setDouble(4, prod.getPrecioUnitario());
					pstLinea.executeUpdate();
				}
			}

			con.commit();
			System.out.println("\n- Datos insertados correctamente.");

			pstPedido.close();
			pstLinea.close();

		} catch (Exception ex) {
			System.err.format("\n* Error al insertar: %s", ex.getMessage());
			ex.printStackTrace();
		}
	}

	public List<Pedido> obtenerDatos() {
		List<Pedido> pedidos = new ArrayList<>();

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			con.createStatement().execute("PRAGMA foreign_keys = ON");
			String sqlPedido = "SELECT * FROM PEDIDO WHERE ID_FARMACIA = ?";
			PreparedStatement pstPedido = con.prepareStatement(sqlPedido);
			pstPedido.setInt(1, JFramePrincipal.idFarActual);
			ResultSet rs = pstPedido.executeQuery();

			String sqlProductos = "SELECT * FROM LINEA_PEDIDO WHERE ID_PEDIDO = ?";
			PreparedStatement pstProductos = con.prepareStatement(sqlProductos);

			while (rs.next()) {
				String id = rs.getString("ID");
				String fechaOrd = rs.getString("FECHA_ORDEN");
				String fechaLleg = rs.getString("FECHA_LLEGADA");
				String proveedor = rs.getString("PROVEEDOR");
				int idFarmacia = rs.getInt("ID_FARMACIA");

				Pedido p = new Pedido(id, proveedor, Date.valueOf(fechaOrd), Date.valueOf(fechaLleg), idFarmacia);

				pstProductos.setString(1, id);
				ResultSet rsProd = pstProductos.executeQuery();

				while (rsProd.next()) {
					String nombreProd = rsProd.getString("NOMBRE_PRODUCTO");
					int cantidad = rsProd.getInt("CANTIDAD");
					double precio = rsProd.getDouble("PRECIO_UNITARIO");

					Producto prod = new Producto(nombreProd, cantidad, precio);
					p.agregarProducto(prod);
				}
				rsProd.close();
				pedidos.add(p);
			}

			System.out.format("\n- Se han recuperado %d pedidos completos.", pedidos.size());

			rs.close();
			pstPedido.close();
			pstProductos.close();

		} catch (Exception ex) {
			System.err.format("\n* Error al obtener datos: %s", ex.getMessage());
			ex.printStackTrace();
		}
		return pedidos;
	}

	public void borrarPedido(String idPedido) {
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING)) {
			con.createStatement().execute("PRAGMA foreign_keys = ON");

			String sqlBorrar = "DELETE FROM PEDIDO WHERE ID = ?";
			try (PreparedStatement pstBorrar = con.prepareStatement(sqlBorrar)) {
				pstBorrar.setString(1, idPedido);
				int filasAfectadas = pstBorrar.executeUpdate();

				if (filasAfectadas > 0) {
					System.out.println("- Pedido " + idPedido + " eliminado de la BBDD.");
				} else {
					System.out.println("- No se encontró el pedido con ID: " + idPedido);
				}
			}

		} catch (Exception e) {
			System.err.format("\n* Error al borrar pedido: %s", e.getMessage());

		}

	}
}
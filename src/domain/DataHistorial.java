package domain;

import java.util.Vector;

public class DataHistorial {
	public static Vector<Vector<Object>> cargarHistorial(){
		Vector<Vector<Object>> datos = new Vector<>();

        datos.add(crearFila("001", "2025-10-05", "15"));
        datos.add(crearFila("002", "2025-10-06", "20"));
        datos.add(crearFila("003", "2025-10-07", "10"));
        datos.add(crearFila("004", "2025-10-08", "25"));
        datos.add(crearFila("005", "2025-10-09", "30"));
        datos.add(crearFila("006", "2025-10-10", "15"));
        datos.add(crearFila("007", "2025-10-11", "20"));
        datos.add(crearFila("008", "2025-10-12", "25"));
        datos.add(crearFila("009", "2025-10-13", "10"));
        datos.add(crearFila("010", "2025-10-14", "35"));
        datos.add(crearFila("011", "2025-10-15", "15"));
        datos.add(crearFila("012", "2025-10-16", "20"));
        datos.add(crearFila("013", "2025-10-17", "25"));
        datos.add(crearFila("014", "2025-10-18", "30"));
        datos.add(crearFila("015", "2025-10-19", "15"));
        datos.add(crearFila("016", "2025-10-20", "20"));
        datos.add(crearFila("017", "2025-10-21", "10"));
        datos.add(crearFila("018", "2025-10-22", "25"));
        datos.add(crearFila("019", "2025-10-23", "20"));
        datos.add(crearFila("020", "2025-10-24", "30"));

		
		return datos;
		
	};
	
	public static Vector<Object> crearFila(String id, String fecha, String productos){
		Vector<Object> fila = new Vector<>();
		fila.add(id);
		fila.add(fecha);
		fila.add(productos);
		
		
		return fila;
		
	}
}

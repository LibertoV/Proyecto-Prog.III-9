package domain;

import java.util.Vector;

public class DataPedidos {
	public static Vector<Vector<Object>> cargarPedidos(){
		
	       Vector<Vector<Object>> datos = new Vector<>();

	        datos.add(crearFila("001", "2025-10-01", "2025-10-05", "3", "15"));
	        datos.add(crearFila("002", "2025-10-02", "2025-10-06", "4", "20"));
	        datos.add(crearFila("003", "2025-10-03", "2025-10-07", "2", "10"));
	        datos.add(crearFila("004", "2025-10-04", "2025-10-08", "5", "25"));
	        datos.add(crearFila("005", "2025-10-05", "2025-10-09", "6", "30"));
	        datos.add(crearFila("006", "2025-10-06", "2025-10-10", "3", "15"));
	        datos.add(crearFila("007", "2025-10-07", "2025-10-11", "4", "20"));
	        datos.add(crearFila("008", "2025-10-08", "2025-10-12", "5", "25"));
	        datos.add(crearFila("009", "2025-10-09", "2025-10-13", "2", "10"));
	        datos.add(crearFila("010", "2025-10-10", "2025-10-14", "7", "35"));
	        datos.add(crearFila("011", "2025-10-11", "2025-10-15", "3", "15"));
	        datos.add(crearFila("012", "2025-10-12", "2025-10-16", "4", "20"));
	        datos.add(crearFila("013", "2025-10-13", "2025-10-17", "5", "25"));
	        datos.add(crearFila("014", "2025-10-14", "2025-10-18", "6", "30"));
	        datos.add(crearFila("015", "2025-10-15", "2025-10-19", "3", "15"));
	        datos.add(crearFila("016", "2025-10-16", "2025-10-20", "4", "20"));
	        datos.add(crearFila("017", "2025-10-17", "2025-10-21", "2", "10"));
	        datos.add(crearFila("018", "2025-10-18", "2025-10-22", "5", "25"));
	        datos.add(crearFila("019", "2025-10-19", "2025-10-23", "4", "20"));
	        datos.add(crearFila("020", "2025-10-20", "2025-10-24", "6", "30"));
	        datos.add(crearFila("001", "2025-10-01", "2025-10-05", "3", "15"));
	        datos.add(crearFila("002", "2025-10-02", "2025-10-06", "4", "20"));
	        datos.add(crearFila("003", "2025-10-03", "2025-10-07", "2", "10"));
	        datos.add(crearFila("004", "2025-10-04", "2025-10-08", "5", "25"));
	        datos.add(crearFila("005", "2025-10-05", "2025-10-09", "6", "30"));
	        datos.add(crearFila("006", "2025-10-06", "2025-10-10", "3", "15"));
	        datos.add(crearFila("007", "2025-10-07", "2025-10-11", "4", "20"));
	        datos.add(crearFila("008", "2025-10-08", "2025-10-12", "5", "25"));
	        datos.add(crearFila("009", "2025-10-09", "2025-10-13", "2", "10"));
	        datos.add(crearFila("010", "2025-10-10", "2025-10-14", "7", "35"));
	        datos.add(crearFila("011", "2025-10-11", "2025-10-15", "3", "15"));
	        datos.add(crearFila("012", "2025-10-12", "2025-10-16", "4", "20"));
	        datos.add(crearFila("013", "2025-10-13", "2025-10-17", "5", "25"));
	        datos.add(crearFila("014", "2025-10-14", "2025-10-18", "6", "30"));
	        datos.add(crearFila("015", "2025-10-15", "2025-10-19", "3", "15"));
	        datos.add(crearFila("016", "2025-10-16", "2025-10-20", "4", "20"));
	        datos.add(crearFila("017", "2025-10-17", "2025-10-21", "2", "10"));
	        datos.add(crearFila("018", "2025-10-18", "2025-10-22", "5", "25"));
	        datos.add(crearFila("019", "2025-10-19", "2025-10-23", "4", "20"));
	        datos.add(crearFila("020", "2025-10-20", "2025-10-24", "6", "30"));


		return datos;
		
	}
	
	private static Vector<Object> crearFila(String num, String fOrden, String fLlegada, String nProd, String total) {
        Vector<Object> fila = new Vector<>();
        fila.add(num);
        fila.add(fOrden);
        fila.add(fLlegada);
        fila.add(nProd);
        fila.add(total);
        return fila;
    }


}

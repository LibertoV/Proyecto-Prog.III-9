package domain;

import java.util.Vector;

public class DataPedidos {
	public static Vector<Vector<Object>> cargarPedidos(){
		
	       Vector<Vector<Object>> datos = new Vector<>();

	        datos.add(crearFila("001", "2025-10-01", "2025-10-05", "9003", "15"));
	        datos.add(crearFila("002", "2025-10-02", "2025-10-06", "4023", "20"));
	        datos.add(crearFila("003", "2025-10-03", "2025-10-07", "2322", "10"));
	        datos.add(crearFila("004", "2025-10-04", "2025-10-08", "5232", "25"));
	        datos.add(crearFila("005", "2025-10-05", "2025-10-09", "6123", "30"));
	        datos.add(crearFila("006", "2025-10-06", "2025-10-10", "332", "15"));
	        datos.add(crearFila("007", "2025-10-07", "2025-10-11", "4000", "20"));
	        datos.add(crearFila("008", "2025-10-08", "2025-10-12", "5020", "25"));
	        datos.add(crearFila("009", "2025-10-09", "2025-10-13", "2050", "10"));
	        datos.add(crearFila("010", "2025-10-10", "2025-10-14", "7004", "35"));
	        datos.add(crearFila("011", "2025-10-11", "2025-10-15", "3009", "15"));
	        datos.add(crearFila("012", "2025-10-12", "2025-10-16", "4134", "20"));
	        datos.add(crearFila("013", "2025-10-13", "2025-10-17", "5233", "25"));
	        datos.add(crearFila("014", "2025-10-14", "2025-10-18", "6102", "30"));
	        datos.add(crearFila("015", "2025-10-15", "2025-10-19", "3323", "15"));
	        datos.add(crearFila("016", "2025-10-16", "2025-10-20", "4557", "20"));
	        datos.add(crearFila("017", "2025-10-17", "2025-10-21", "2456", "10"));
	        datos.add(crearFila("018", "2025-10-18", "2025-10-22", "5765", "25"));
	        datos.add(crearFila("019", "2025-10-19", "2025-10-23", "4876", "20"));
	        datos.add(crearFila("020", "2025-10-20", "2025-10-24", "6367", "30"));
	        datos.add(crearFila("001", "2025-10-01", "2025-10-05", "3876", "15"));
	        datos.add(crearFila("002", "2025-10-02", "2025-10-06", "4986", "20"));
	        datos.add(crearFila("003", "2025-10-03", "2025-10-07", "2356", "10"));
	        datos.add(crearFila("004", "2025-10-04", "2025-10-08", "5356", "25"));
	        datos.add(crearFila("005", "2025-10-05", "2025-10-09", "6765", "30"));
	        datos.add(crearFila("006", "2025-10-06", "2025-10-10", "376", "15"));
	        datos.add(crearFila("007", "2025-10-07", "2025-10-11", "4876", "20"));
	        datos.add(crearFila("008", "2025-10-08", "2025-10-12", "5786", "25"));
	        datos.add(crearFila("009", "2025-10-09", "2025-10-13", "2245", "10"));
	        datos.add(crearFila("010", "2025-10-10", "2025-10-14", "7542", "35"));
	        datos.add(crearFila("011", "2025-10-11", "2025-10-15", "3876", "15"));
	        datos.add(crearFila("012", "2025-10-12", "2025-10-16", "4563", "20"));
	        datos.add(crearFila("013", "2025-10-13", "2025-10-17", "5432", "25"));
	        datos.add(crearFila("014", "2025-10-14", "2025-10-18", "6135", "30"));
	        datos.add(crearFila("015", "2025-10-15", "2025-10-19", "3543", "15"));
	        datos.add(crearFila("016", "2025-10-16", "2025-10-20", "4375", "20"));
	        datos.add(crearFila("017", "2025-10-17", "2025-10-21", "2876", "10"));
	        datos.add(crearFila("018", "2025-10-18", "2025-10-22", "5889", "25"));
	        datos.add(crearFila("019", "2025-10-19", "2025-10-23", "4954", "20"));
	        datos.add(crearFila("020", "2025-10-20", "2025-10-24", "6489", "30"));


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

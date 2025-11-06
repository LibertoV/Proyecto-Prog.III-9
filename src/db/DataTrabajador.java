package db;

import java.util.Vector;
//IAG
public class DataTrabajador {

    public static Vector<Vector<Object>> cargarTrabajadores(String path) {
        Vector<Vector<Object>> data = new Vector<>();

        Vector<Object> t1 = new Vector<>();
        t1.add(1);
        t1.add("María Pérez");
        t1.add("78233411A");
        t1.add("678112233");
        t1.add("Farmacéutica titular");
        t1.add("2025-10-29");
        t1.add("Activo");
        data.add(t1);

        Vector<Object> t2 = new Vector<>();
        t2.add(2);
        t2.add("Javier López");
        t2.add("74452009B");
        t2.add("611334455");
        t2.add("Técnico de farmacia");
        t2.add("2025-10-28");
        t2.add("Activo");
        data.add(t2);

        Vector<Object> t3 = new Vector<>();
        t3.add(3);
        t3.add("Lucía Sánchez");
        t3.add("73321987C");
        t3.add("699112200");
        t3.add("Auxiliar de farmacia");
        t3.add("2025-10-26");
        t3.add("De baja médica");
        data.add(t3);

        Vector<Object> t4 = new Vector<>();
        t4.add(4);
        t4.add("Carlos Gómez");
        t4.add("72100987D");
        t4.add("687554433");
        t4.add("Farmacéutico adjunto");
        t4.add("2025-10-30");
        t4.add("Activo");
        data.add(t4);

        Vector<Object> t5 = new Vector<>();
        t5.add(5);
        t5.add("Ana Torres");
        t5.add("70011223E");
        t5.add("620334455");
        t5.add("Encargada de almacén");
        t5.add("2025-10-25");
        t5.add("Activo");
        data.add(t5);

        Vector<Object> t6 = new Vector<>();
        t6.add(6);
        t6.add("Marcos Fernández");
        t6.add("75500422F");
        t6.add("645778899");
        t6.add("Farmacéutico adjunto");
        t6.add("2025-10-27");
        t6.add("Permiso temporal");
        data.add(t6);

        Vector<Object> t7 = new Vector<>();
        t7.add(7);
        t7.add("Elena Navarro");
        t7.add("71300456G");
        t7.add("633221144");
        t7.add("Técnico de farmacia");
        t7.add("2025-10-28");
        t7.add("Activo");
        data.add(t7);

        Vector<Object> t8 = new Vector<>();
        t8.add(8);
        t8.add("David Martín");
        t8.add("73112900H");
        t8.add("601887766");
        t8.add("Repartidor / Logística");
        t8.add("2025-10-30");
        t8.add("Activo");
        data.add(t8);

        return data;
    }
}

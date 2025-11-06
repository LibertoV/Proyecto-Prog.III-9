package db;

import java.util.Vector;
//IAG
public class DataVentas {
	public static Vector<Vector<Object>> cargarVentas(String path) {
        Vector<Vector<Object>> data = new Vector<>();

        Vector<Object> ventaA = new Vector<>();
        ventaA.add("V001");
        ventaA.add("2025-10-01");
        ventaA.add("Ana López");
        ventaA.add("María Gómez");
        ventaA.add("Ibuprofeno 600mg");
        ventaA.add(1);
        ventaA.add("4,50€");
        data.add(ventaA);

        Vector<Object> ventaB = new Vector<>();
        ventaB.add("V002");
        ventaB.add("2025-10-02");
        ventaB.add("Juan García");
        ventaB.add("Carlos Pérez");
        ventaB.add("Paracetamol 1g");
        ventaB.add(2);
        ventaB.add("7,00€");
        data.add(ventaB);

        Vector<Object> ventaC = new Vector<>();
        ventaC.add("V003");
        ventaC.add("2025-10-03");
        ventaC.add("Marta Ruiz");
        ventaC.add("Laura Sánchez");
        ventaC.add("Omeprazol 20mg");
        ventaC.add(1);
        ventaC.add("9,80€");
        data.add(ventaC);

        Vector<Object> ventaD = new Vector<>();
        ventaD.add("V004");
        ventaD.add("2025-10-04");
        ventaD.add("Carlos Ortega");
        ventaD.add("María Gómez");
        ventaD.add("Gelocatil");
        ventaD.add(3);
        ventaD.add("12,90€");
        data.add(ventaD);

        Vector<Object> ventaE = new Vector<>();
        ventaE.add("V005");
        ventaE.add("2025-10-05");
        ventaE.add("Laura Jiménez");
        ventaE.add("Carlos Pérez");
        ventaE.add("Amoxicilina 500mg");
        ventaE.add(1);
        ventaE.add("6,70€");
        data.add(ventaE);

        Vector<Object> ventaF = new Vector<>();
        ventaF.add("V006");
        ventaF.add("2025-10-06");
        ventaF.add("Cliente Anónimo");
        ventaF.add("Laura Sánchez");
        ventaF.add("Vitamina C");
        ventaF.add(2);
        ventaF.add("11,50€");
        data.add(ventaF);

        Vector<Object> ventaG = new Vector<>();
        ventaG.add("V007");
        ventaG.add("2025-10-07");
        ventaG.add("Pedro Torres");
        ventaG.add("María Gómez");
        ventaG.add("Enantyum 25mg");
        ventaG.add(1);
        ventaG.add("5,40€");
        data.add(ventaG);

        Vector<Object> ventaH = new Vector<>();
        ventaH.add("V008");
        ventaH.add("2025-10-08");
        ventaH.add("Claudia Pérez");
        ventaH.add("Carlos Pérez");
        ventaH.add("Suero Oral");
        ventaH.add(4);
        ventaH.add("18,60€");
        data.add(ventaH);

        Vector<Object> ventaI = new Vector<>();
        ventaI.add("V009");
        ventaI.add("2025-10-09");
        ventaI.add("Antonio Ros");
        ventaI.add("Laura Sánchez");
        ventaI.add("Voltadol Forte");
        ventaI.add(1);
        ventaI.add("13,20€");
        data.add(ventaI);

        Vector<Object> ventaJ = new Vector<>();
        ventaJ.add("V010");
        ventaJ.add("2025-10-10");
        ventaJ.add("Cliente Anónimo");
        ventaJ.add("María Gómez");
        ventaJ.add("Termómetro Digital");
        ventaJ.add(1);
        ventaJ.add("8,90€");
        data.add(ventaJ);

        return data;
    }
}

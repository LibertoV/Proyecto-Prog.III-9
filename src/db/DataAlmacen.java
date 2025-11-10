package db;

import java.util.Random;
import java.util.Vector;

public class DataAlmacen {
	private static final String IMAGES_PATH = "resources/images/";
	private static final String[] PROVEEDOR_IMGS = {
	        "AbbVieLogo_AbbVie dark blue.png", 
	        "JNJ_Logo_SingleLine_Red_RGB.png",
	        "Lilly-Logo.svg.png",
	        "Logo_Bayer.svg.png",
	        "Logo_Sanofi_(2022).png",
	        "Merck.png",
	        "original.jpg",
	        "roche-logo-blue.png"
	        
	    };
	private static String getProveedorImagenAleatoria() {
        Random random = new Random();
        int index = random.nextInt(PROVEEDOR_IMGS.length);
        return IMAGES_PATH + PROVEEDOR_IMGS[index];
    }
    public static Vector<Vector<Object>> cargarAlmacen() {
        Vector<Vector<Object>> data = new Vector<>();

        Vector<Object> med1 = new Vector<>();
        med1.add("MED-001");
        med1.add("Paracetamol 500mg");
        med1.add("Analgésico");
        med1.add(250);
        med1.add(2.50);
        med1.add(getProveedorImagenAleatoria());
        data.add(med1);

        Vector<Object> med2 = new Vector<>();
        med2.add("MED-002");
        med2.add("Ibuprofeno 400mg");
        med2.add("Antiinflamatorio");
        med2.add(180);
        med2.add(3.20);
        med2.add(getProveedorImagenAleatoria());
        data.add(med2);

        Vector<Object> med3 = new Vector<>();
        med3.add("MED-003");
        med3.add("Amoxicilina 500mg");
        med3.add("Antibiótico");
        med3.add(120);
        med3.add(4.80);
        med3.add(getProveedorImagenAleatoria());
        data.add(med3);

        Vector<Object> med4 = new Vector<>();
        med4.add("MED-004");
        med4.add("Loratadina 10mg");
        med4.add("Antihistamínico");
        med4.add(200);
        med4.add(3.00);
        med4.add(getProveedorImagenAleatoria());
        data.add(med4);

        Vector<Object> med5 = new Vector<>();
        med5.add("MED-005");
        med5.add("Omeprazol 20mg");
        med5.add("Gastroprotector");
        med5.add(300);
        med5.add(2.90);
        med5.add(getProveedorImagenAleatoria());
        data.add(med5);

        Vector<Object> med6 = new Vector<>();
        med6.add("MED-006");
        med6.add("Metformina 850mg");
        med6.add("Antidiabético");
        med6.add(150);
        med6.add(5.10);
        med6.add(getProveedorImagenAleatoria());
        data.add(med6);

        Vector<Object> med7 = new Vector<>();
        med7.add("MED-007");
        med7.add("Losartán 50mg");
        med7.add("Antihipertensivo");
        med7.add(90);
        med7.add(6.25);
        med7.add(getProveedorImagenAleatoria());
        data.add(med7);

        Vector<Object> med8 = new Vector<>();
        med8.add("MED-008");
        med8.add("Atorvastatina 20mg");
        med8.add("Hipolipemiante");
        med8.add(130);
        med8.add(7.40);
        med8.add(getProveedorImagenAleatoria());
        data.add(med8);

        Vector<Object> med9 = new Vector<>();
        med9.add("MED-009");
        med9.add("Salbutamol Inhalador 100mcg");
        med9.add("Broncodilatador");
        med9.add(75);
        med9.add(9.80);
        med9.add(getProveedorImagenAleatoria());
        data.add(med9);

        Vector<Object> med10 = new Vector<>();
        med10.add("MED-010");
        med10.add("Clopidogrel 75mg");
        med10.add("Antiplaquetario");
        med10.add(60);
        med10.add(8.60);
        med10.add(getProveedorImagenAleatoria());
        data.add(med10);

        Vector<Object> med11 = new Vector<>();
        med11.add("MED-011");
        med11.add("Enalapril 10mg");
        med11.add("Antihipertensivo");
        med11.add(140);
        med11.add(4.10);
        med11.add(getProveedorImagenAleatoria());
        data.add(med11);

        Vector<Object> med12 = new Vector<>();
        med12.add("MED-012");
        med12.add("Cetirizina 10mg");
        med12.add("Antialérgico");
        med12.add(220);
        med12.add(3.15);
        med12.add(getProveedorImagenAleatoria());
        data.add(med12);

        Vector<Object> med13 = new Vector<>();
        med13.add("MED-013");
        med13.add("Dexametasona 4mg");
        med13.add("Corticoide");
        med13.add(110);
        med13.add(5.60);
        med13.add(getProveedorImagenAleatoria());
        data.add(med13);

        Vector<Object> med14 = new Vector<>();
        med14.add("MED-014");
        med14.add("Azitromicina 500mg");
        med14.add("Antibiótico");
        med14.add(95);
        med14.add(6.80);
        med14.add(getProveedorImagenAleatoria());
        data.add(med14);

        Vector<Object> med15 = new Vector<>();
        med15.add("MED-015");
        med15.add("Furosemida 40mg");
        med15.add("Diurético");
        med15.add(160);
        med15.add(3.75);
        med15.add(getProveedorImagenAleatoria());
        data.add(med15);

        Vector<Object> med16 = new Vector<>();
        med16.add("MED-016");
        med16.add("Insulina Glargina 100UI/ml");
        med16.add("Antidiabético");
        med16.add(80);
        med16.add(22.50);
        med16.add(getProveedorImagenAleatoria());
        data.add(med16);

        Vector<Object> med17 = new Vector<>();
        med17.add("MED-017");
        med17.add("Ranitidina 150mg");
        med17.add("Gastroprotector");
        med17.add(190);
        med17.add(2.80);
        med17.add(getProveedorImagenAleatoria());
        data.add(med17);

        Vector<Object> med18 = new Vector<>();
        med18.add("MED-018");
        med18.add("Prednisona 5mg");
        med18.add("Corticoide");
        med18.add(130);
        med18.add(3.90);
        med18.add(getProveedorImagenAleatoria());
        data.add(med18);

        Vector<Object> med19 = new Vector<>();
        med19.add("MED-019");
        med19.add("Amiodarona 200mg");
        med19.add("Antiarrítmico");
        med19.add(70);
        med19.add(8.30);
        med19.add(getProveedorImagenAleatoria());
        data.add(med19);

        Vector<Object> med20 = new Vector<>();
        med20.add("MED-020");
        med20.add("Simvastatina 20mg");
        med20.add("Hipolipemiante");
        med20.add(145);
        med20.add(6.10);
        med20.add(getProveedorImagenAleatoria());
        data.add(med20);

        Vector<Object> med21 = new Vector<>();
        med21.add("MED-021");
        med21.add("Levotiroxina 50mcg");
        med21.add("Hormonal");
        med21.add(125);
        med21.add(4.50);
        med21.add(getProveedorImagenAleatoria());
        data.add(med21);

        Vector<Object> med22 = new Vector<>();
        med22.add("MED-022");
        med22.add("Diclofenaco 50mg");
        med22.add("Antiinflamatorio");
        med22.add(260);
        med22.add(3.40);
        med22.add(getProveedorImagenAleatoria());
        data.add(med22);

        Vector<Object> med23 = new Vector<>();
        med23.add("MED-023");
        med23.add("Ciprofloxacino 500mg");
        med23.add("Antibiótico");
        med23.add(100);
        med23.add(5.60);
        med23.add(getProveedorImagenAleatoria());
        data.add(med23);

        Vector<Object> med24 = new Vector<>();
        med24.add("MED-024");
        med24.add("Montelukast 10mg");
        med24.add("Antiasmático");
        med24.add(85);
        med24.add(7.90);
        med24.add(getProveedorImagenAleatoria());
        data.add(med24);

        Vector<Object> med25 = new Vector<>();
        med25.add("MED-025");
        med25.add("Diazepam 5mg");
        med25.add("Ansiolítico");
        med25.add(60);
        med25.add(4.75);
        med25.add(getProveedorImagenAleatoria());
        data.add(med25);

        Vector<Object> med26 = new Vector<>();
        med26.add("MED-026");
        med26.add("Sertralina 50mg");
        med26.add("Antidepresivo");
        med26.add(90);
        med26.add(8.20);
        med26.add(getProveedorImagenAleatoria());
        data.add(med26);

        Vector<Object> med27 = new Vector<>();
        med27.add("MED-027");
        med27.add("Quetiapina 100mg");
        med27.add("Antipsicótico");
        med27.add(70);
        med27.add(9.40);
        med27.add(getProveedorImagenAleatoria());
        data.add(med27);

        Vector<Object> med28 = new Vector<>();
        med28.add("MED-028");
        med28.add("Cetoprofeno 100mg");
        med28.add("Antiinflamatorio");
        med28.add(110);
        med28.add(4.60);
        med28.add(getProveedorImagenAleatoria());
        data.add(med28);

        Vector<Object> med29 = new Vector<>();
        med29.add("MED-029");
        med29.add("Amoxicilina + Clavulánico 875/125mg");
        med29.add("Antibiótico");
        med29.add(75);
        med29.add(9.10);
        med29.add(getProveedorImagenAleatoria());
        data.add(med29);

        Vector<Object> med30 = new Vector<>();
        med30.add("MED-030");
        med30.add("Warfarina 5mg");
        med30.add("Anticoagulante");
        med30.add(55);
        med30.add(7.80);
        med30.add(getProveedorImagenAleatoria());
        data.add(med30);

        Vector<Object> med31 = new Vector<>();
        med31.add("MED-031");
        med31.add("Aspirina 100mg");
        med31.add("Antiplaquetario");
        med31.add(320);
        med31.add(2.10);
        med31.add(getProveedorImagenAleatoria());
        data.add(med31);

        Vector<Object> med32 = new Vector<>();
        med32.add("MED-032");
        med32.add("Carvedilol 12.5mg");
        med32.add("Betabloqueante");
        med32.add(95);
        med32.add(5.75);
        med32.add(getProveedorImagenAleatoria());
        data.add(med32);

        Vector<Object> med33 = new Vector<>();
        med33.add("MED-033");
        med33.add("Clonazepam 0.5mg");
        med33.add("Ansiolítico");
        med33.add(65);
        med33.add(6.20);
        med33.add(getProveedorImagenAleatoria());
        data.add(med33);

        Vector<Object> med34 = new Vector<>();
        med34.add("MED-034");
        med34.add("Escitalopram 10mg");
        med34.add("Antidepresivo");
        med34.add(80);
        med34.add(7.10);
        med34.add(getProveedorImagenAleatoria());
        data.add(med34);

        Vector<Object> med35 = new Vector<>();
        med35.add("MED-035");
        med35.add("Pantoprazol 40mg");
        med35.add("Gastroprotector");
        med35.add(210);
        med35.add(3.80);
        med35.add(getProveedorImagenAleatoria());
        data.add(med35);

        Vector<Object> med36 = new Vector<>();
        med36.add("MED-036");
        med36.add("Lisinopril 10mg");
        med36.add("Antihipertensivo");
        med36.add(170);
        med36.add(4.00);
        med36.add(getProveedorImagenAleatoria());
        data.add(med36);

        Vector<Object> med37 = new Vector<>();
        med37.add("MED-037");
        med37.add("Amlodipino 5mg");
        med37.add("Antihipertensivo");
        med37.add(160);
        med37.add(3.95);
        med37.add(getProveedorImagenAleatoria());
        data.add(med37);

        Vector<Object> med38 = new Vector<>();
        med38.add("MED-038");
        med38.add("Insulina Lispro 100UI/ml");
        med38.add("Antidiabético");
        med38.add(60);
        med38.add(23.40);
        med38.add(getProveedorImagenAleatoria());
        data.add(med38);

        Vector<Object> med39 = new Vector<>();
        med39.add("MED-039");
        med39.add("Ketorolaco 10mg");
        med39.add("Analgésico");
        med39.add(85);
        med39.add(4.30);
        med39.add(getProveedorImagenAleatoria());
        data.add(med39);

        Vector<Object> med40 = new Vector<>();
        med40.add("MED-040");
        med40.add("Gabapentina 300mg");
        med40.add("Neuromodulador");
        med40.add(70);
        med40.add(9.60);
        med40.add(getProveedorImagenAleatoria());
        data.add(med40);

        Vector<Object> med41 = new Vector<>();
        med41.add("MED-041");
        med41.add("Fluoxetina 20mg");
        med41.add("Antidepresivo");
        med41.add(95);
        med41.add(5.25);
        med41.add(getProveedorImagenAleatoria());
        data.add(med41);

        Vector<Object> med42 = new Vector<>();
        med42.add("MED-042");
        med42.add("Azatioprina 50mg");
        med42.add("Inmunosupresor");
        med42.add(55);
        med42.add(11.90);
        med42.add(getProveedorImagenAleatoria());
        data.add(med42);

        Vector<Object> med43 = new Vector<>();
        med43.add("MED-043");
        med43.add("Claritromicina 500mg");
        med43.add("Antibiótico");
        med43.add(85);
        med43.add(6.90);
        med43.add(getProveedorImagenAleatoria());
        data.add(med43);

        Vector<Object> med44 = new Vector<>();
        med44.add("MED-044");
        med44.add("Esomeprazol 20mg");
        med44.add("Gastroprotector");
        med44.add(190);
        med44.add(3.60);
        med44.add(getProveedorImagenAleatoria());
        data.add(med44);

        Vector<Object> med45 = new Vector<>();
        med45.add("MED-045");
        med45.add("Clorfenamina 4mg");
        med45.add("Antihistamínico");
        med45.add(210);
        med45.add(2.70);
        med45.add(getProveedorImagenAleatoria());
        data.add(med45);

        Vector<Object> med46 = new Vector<>();
        med46.add("MED-046");
        med46.add("Insulina NPH 100UI/ml");
        med46.add("Antidiabético");
        med46.add(75);
        med46.add(21.50);
        med46.add(getProveedorImagenAleatoria());
        data.add(med46);

        Vector<Object> med47 = new Vector<>();
        med47.add("MED-047");
        med47.add("Morfina 10mg/ml");
        med47.add("Analgesia mayor");
        med47.add(30);
        med47.add(18.20);
        med47.add(getProveedorImagenAleatoria());
        data.add(med47);

        Vector<Object> med48 = new Vector<>();
        med48.add("MED-048");
        med48.add("Haloperidol 5mg");
        med48.add("Antipsicótico");
        med48.add(65);
        med48.add(7.70);
        med48.add(getProveedorImagenAleatoria());
        data.add(med48);

        Vector<Object> med49 = new Vector<>();
        med49.add("MED-049");
        med49.add("Metronidazol 500mg");
        med49.add("Antimicrobiano");
        med49.add(95);
        med49.add(5.00);
        med49.add(getProveedorImagenAleatoria());
        data.add(med49);

        Vector<Object> med50 = new Vector<>();
        med50.add("MED-050");
        med50.add("Fentanilo Parche 25mcg/h");
        med50.add("Analgesia mayor");
        med50.add(40);
        med50.add(19.90);
        med50.add(getProveedorImagenAleatoria());
        data.add(med50);

        return data;
    }
}

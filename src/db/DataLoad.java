package db;
//IAG
import java.util.Vector;

public class DataLoad {
	public static Vector<Vector<Object>> cargaFarmacia(String path){
		Vector<Vector<Object>> data = new Vector<>();
		
		//Place holder hasta añadir la base de datos
		Vector<Object> farmaciaA = new Vector<>();
		farmaciaA.add("Farmacia Central");
		farmaciaA.add("Madrid");
		farmaciaA.add("Stock Bajo");
		farmaciaA.add("10 pendientes");
		farmaciaA.add("Seleccionar");
		data.add(farmaciaA);
		
		Vector<Object> farmaciaB = new Vector<>();
		farmaciaB.add("Farmacia Del Sur");
		farmaciaB.add("Sevilla");
		farmaciaB.add("Stock Óptimo");
		farmaciaB.add("0 pendientes");
		farmaciaB.add("Seleccionar");
		data.add(farmaciaB);
		
		Vector<Object> farmaciaC = new Vector<>();
		farmaciaC.add("Farmacia Norte");
		farmaciaC.add("Bilbao");
		farmaciaC.add("Stock Medio");
		farmaciaC.add("5 pendientes");
		farmaciaC.add("Seleccionar");
		data.add(farmaciaC);
		
		Vector<Object> farmaciaD = new Vector<>();
		farmaciaD.add("Farmacia Noroeste");
		farmaciaD.add("Coruña");
		farmaciaD.add("Stock Medio");
		farmaciaD.add("2 pendientes");
		farmaciaD.add("Seleccionar");
		data.add(farmaciaD);
		
		Vector<Object> farmaciaE = new Vector<>();
        farmaciaE.add("Farmacia del Este");
        farmaciaE.add("Valencia");
        farmaciaE.add("Stock Óptimo");
        farmaciaE.add("1 pendiente");
        farmaciaE.add("Seleccionar");
        data.add(farmaciaE);

        Vector<Object> farmaciaF = new Vector<>();
        farmaciaF.add("Farmacia Gótico");
        farmaciaF.add("Barcelona");
        farmaciaF.add("Stock Bajo");
        farmaciaF.add("22 pendientes");
        farmaciaF.add("Seleccionar");
        data.add(farmaciaF);

        Vector<Object> farmaciaG = new Vector<>();
        farmaciaG.add("Farmacia La Giralda");
        farmaciaG.add("Sevilla");
        farmaciaG.add("Stock Medio");
        farmaciaG.add("0 pendientes");
        farmaciaG.add("Seleccionar");
        data.add(farmaciaG);

        Vector<Object> farmaciaH = new Vector<>();
        farmaciaH.add("Farmacia Pilar");
        farmaciaH.add("Zaragoza");
        farmaciaH.add("Stock Óptimo");
        farmaciaH.add("3 pendientes");
        farmaciaH.add("Seleccionar");
        data.add(farmaciaH);

        Vector<Object> farmaciaI = new Vector<>();
        farmaciaI.add("Farmacia Costa del Sol");
        farmaciaI.add("Málaga");
        farmaciaI.add("Stock Bajo");
        farmaciaI.add("15 pendientes");
        farmaciaI.add("Seleccionar");
        data.add(farmaciaI);

        Vector<Object> farmaciaJ = new Vector<>();
        farmaciaJ.add("Farmacia Insular");
        farmaciaJ.add("Palma de Mallorca");
        farmaciaJ.add("Stock Medio");
        farmaciaJ.add("7 pendientes");
        farmaciaJ.add("Seleccionar");
        data.add(farmaciaJ);

        Vector<Object> farmaciaK = new Vector<>();
        farmaciaK.add("Farmacia Las Canteras");
        farmaciaK.add("Las Palmas");
        farmaciaK.add("Stock Óptimo");
        farmaciaK.add("0 pendientes");
        farmaciaK.add("Seleccionar");
        data.add(farmaciaK);

        Vector<Object> farmaciaL = new Vector<>();
        farmaciaL.add("Farmacia del Casco");
        farmaciaL.add("Bilbao");
        farmaciaL.add("Stock Medio");
        farmaciaL.add("2 pendientes");
        farmaciaL.add("Seleccionar");
        data.add(farmaciaL);

        Vector<Object> farmaciaM = new Vector<>();
        farmaciaM.add("Farmacia Vega");
        farmaciaM.add("Murcia");
        farmaciaM.add("Stock Bajo");
        farmaciaM.add("11 pendientes");
        farmaciaM.add("Seleccionar");
        data.add(farmaciaM);

        Vector<Object> farmaciaN = new Vector<>();
        farmaciaN.add("Farmacia del Centro");
        farmaciaN.add("Valladolid");
        farmaciaN.add("Stock Óptimo");
        farmaciaN.add("1 pendiente");
        farmaciaN.add("Seleccionar");
        data.add(farmaciaN);

        Vector<Object> farmaciaO = new Vector<>();
        farmaciaO.add("Farmacia San Pablo");
        farmaciaO.add("Sevilla");
        farmaciaO.add("Stock Medio");
        farmaciaO.add("6 pendientes");
        farmaciaO.add("Seleccionar");
        data.add(farmaciaO);

        Vector<Object> farmaciaP = new Vector<>();
        farmaciaP.add("Farmacia Atlántico");
        farmaciaP.add("Vigo");
        farmaciaP.add("Stock Óptimo");
        farmaciaP.add("0 pendientes");
        farmaciaP.add("Seleccionar");
        data.add(farmaciaP);

        Vector<Object> farmaciaQ = new Vector<>();
        farmaciaQ.add("Farmacia Universitaria");
        farmaciaQ.add("Alicante");
        farmaciaQ.add("Stock Bajo");
        farmaciaQ.add("18 pendientes");
        farmaciaQ.add("Seleccionar");
        data.add(farmaciaQ);

        Vector<Object> farmaciaR = new Vector<>();
        farmaciaR.add("Farmacia Ebro");
        farmaciaR.add("Zaragoza");
        farmaciaR.add("Stock Medio");
        farmaciaR.add("4 pendientes");
        farmaciaR.add("Seleccionar");
        data.add(farmaciaR);

        Vector<Object> farmaciaS = new Vector<>();
        farmaciaS.add("Farmacia del Puerto");
        farmaciaS.add("Málaga");
        farmaciaS.add("Stock Óptimo");
        farmaciaS.add("1 pendiente");
        farmaciaS.add("Seleccionar");
        data.add(farmaciaS);

        Vector<Object> farmaciaT = new Vector<>();
        farmaciaT.add("Farmacia Rambla");
        farmaciaT.add("Barcelona");
        farmaciaT.add("Stock Medio");
        farmaciaT.add("9 pendientes");
        farmaciaT.add("Seleccionar");
        data.add(farmaciaT);

        Vector<Object> farmaciaU = new Vector<>();
        farmaciaU.add("Farmacia Plaza Mayor");
        farmaciaU.add("Madrid");
        farmaciaU.add("Stock Bajo");
        farmaciaU.add("30 pendientes");
        farmaciaU.add("Seleccionar");
        data.add(farmaciaU);

        Vector<Object> farmaciaV = new Vector<>();
        farmaciaV.add("Farmacia Catedral");
        farmaciaV.add("Granada");
        farmaciaV.add("Stock Óptimo");
        farmaciaV.add("0 pendientes");
        farmaciaV.add("Seleccionar");
        data.add(farmaciaV);

        Vector<Object> farmaciaW = new Vector<>();
        farmaciaW.add("Farmacia San Ginés");
        farmaciaW.add("Murcia");
        farmaciaW.add("Stock Medio");
        farmaciaW.add("3 pendientes");
        farmaciaW.add("Seleccionar");
        data.add(farmaciaW);

        Vector<Object> farmaciaX = new Vector<>();
        farmaciaX.add("Farmacia La Concha");
        farmaciaX.add("San Sebastián");
        farmaciaX.add("Stock Óptimo");
        farmaciaX.add("1 pendiente");
        farmaciaX.add("Seleccionar");
        data.add(farmaciaX);
        
		return data; 
	}
}
	
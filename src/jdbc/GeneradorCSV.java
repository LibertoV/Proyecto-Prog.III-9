package jdbc;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Random;

public class GeneradorCSV {

    public static void main(String[] args) {
        String rutaArchivo = "resources/db/compras.csv";
        int totalLineasDeseadas = 50000;
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            // Escribir cabecera
            pw.println("id,id_farmacia,id_cliente,fecha,id_producto,cantidad");

            Random rand = new Random();
            int lineasActuales = 0;
            int idCompra = 1;

            // Fecha central: 15 de Enero de 2026
            LocalDate fechaCentral = LocalDate.of(2026, 1, 15);

            System.out.println("Generando " + totalLineasDeseadas + " líneas...");

            while (lineasActuales < totalLineasDeseadas) {
                // DATOS DE LA CABECERA DE LA COMPRA (Iguales para todos los productos de esta compra)
                
                // Farmacia 1 a 30
                int idFarmacia = rand.nextInt(30) + 1; 
                
                // Cliente 1 a 3000
                int idCliente = rand.nextInt(3000) + 1; 
                
                // Fecha: Variación de +/- 5 días alrededor del 15/01/2026
                // nextInt(11) da 0..10. Restamos 5 para tener -5..+5
                LocalDate fechaCompra = fechaCentral.plusDays(rand.nextInt(11) - 5);
                
                // ¿Cuántos productos compramos en esta compra? (Entre 1 y 5 productos)
                int numProductosEnEstaCompra = rand.nextInt(5) + 1;

                for (int i = 0; i < numProductosEnEstaCompra; i++) {
                    if (lineasActuales >= totalLineasDeseadas) break;

                    // Producto 1 a 400
                    int idProducto = rand.nextInt(400) + 1;
                    
                    // Cantidad 1 a 10
                    int cantidad = rand.nextInt(5) + 1;

                    // Escribir línea
                    pw.println(idCompra + "," + idFarmacia + "," + idCliente + "," + fechaCompra + "," + idProducto + "," + cantidad);
                    
                    lineasActuales++;
                }
                
                // Siguiente compra
                idCompra++;
            }

            System.out.println("✅ Archivo generado con éxito en: " + rutaArchivo);
            System.out.println("Total líneas de datos: " + lineasActuales);

        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
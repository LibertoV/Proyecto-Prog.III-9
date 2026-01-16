package jdbc;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class GeneradorPedidosComplejo {

    public static void main(String[] args) {
        // --- CONFIGURACIÓN ---
        String rutaArchivo = "resources/db/pedidos.csv";
        
        // Datos específicos que tú definiste
        int cantidadPedidos = 50; 
        String[] proveedores = {"Cinfa", "Bidafarma", "Amazon", "Bayer", "Pfizer", "Cofares"};
        LocalDate fechaCentral = LocalDate.of(2026, 1, 20); // 20 de Enero de 2026
        
        System.out.println("Iniciando generación con PrintWriter en: " + rutaArchivo);
        
        // CAMBIO CLAVE: Usamos PrintWriter en lugar de BufferedWriter
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            
            // 1. Escribir la Cabecera
            pw.println("id_pedido,fecha_orden,fecha_llegada,proveedor,id_producto,cantidad,id_farmacia");

            Random random = new Random();
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // --- BUCLE PRINCIPAL (Por cada PEDIDO) ---
            for (int i = 1; i <= cantidadPedidos; i++) {
                
                // DATOS COMUNES DEL PEDIDO
                String idPedido = String.format("PED-%04d", i); 
                
                // Fecha Orden: 20 Ene 2026 +/- 3 días
                LocalDate fechaOrdenDate = fechaCentral.plusDays(random.nextInt(8) - 3);
                String fechaOrden = fechaOrdenDate.format(formatoFecha);
                
                // Fecha Llegada: Entre 2 y 10 días después
                String fechaLlegada = fechaOrdenDate.plusDays(2 + random.nextInt(9)).format(formatoFecha);
                
                String proveedor = proveedores[random.nextInt(proveedores.length)];
                
                // id_farmacia: Aleatorio entre 1 y 30
                int idFarmacia = 1 + random.nextInt(30); 

                // --- BUCLE SECUNDARIO (PRODUCTOS DENTRO DEL PEDIDO) ---
                // Entre 1 y 6 líneas por pedido
                int lineasPorPedido = 1 + random.nextInt(6); 

                for (int j = 0; j < lineasPorPedido; j++) {
                    
                    // DATOS VARIABLES
                    int idProducto = 1 + random.nextInt(400); 
                    int cantidad = (1 + random.nextInt(20)) * 5; 

                    // Construir la línea CSV usando concatenación simple que funciona bien con PrintWriter
                    // id_pedido, fecha_orden, fecha_llegada, proveedor, id_producto, cantidad, id_farmacia
                    pw.println(idPedido + "," + 
                               fechaOrden + "," + 
                               fechaLlegada + "," + 
                               proveedor + "," + 
                               idProducto + "," + 
                               cantidad + "," + 
                               idFarmacia);
                }
            }

            System.out.println("✅ ¡ÉXITO! Archivo generated correctamente.");
            System.out.println("Se han creado " + cantidadPedidos + " pedidos con tu configuración personalizada.");
            System.out.println("Recuerda hacer Refresh (F5) en la carpeta resources/db.");

        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
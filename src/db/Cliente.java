package db;

public class Cliente {
    private int id;
    private String nombre;
    private String dni;
    private String tlf;
    private String fechaUltimaCompra;
    private Integer recetasPendientes;

    public Cliente(int i, String nombre, String dni, String tlf, String fechaUltimaCompra, Integer recetasPendientes) {
        this.nombre = nombre;
        this.dni = dni;
        this.tlf = tlf;
        this.fechaUltimaCompra = fechaUltimaCompra;
        this.recetasPendientes = recetasPendientes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public String getDni() { return dni; }
    public String getTlf() { return tlf; }
    public String getFechaUltimaCompra() { return fechaUltimaCompra; }
    public Integer getRecetasPendientes() { return recetasPendientes; }

    @Override
    public String toString() {
        return String.format("%d - %s - %s - %s - %s - %s",
                id, nombre, dni, tlf, fechaUltimaCompra, recetasPendientes);
    }
    
    
}


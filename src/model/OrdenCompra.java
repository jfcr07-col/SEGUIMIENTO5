package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase OrdenCompra
 * Atributos:
 *   - cliente: objeto Cliente que realiza la compra
 *   - producto: objeto Producto que se compra
 *   - cantidad: unidades compradas (int)
 *   - fechaHora: fecha y hora en que se crea la orden (LocalDateTime)
 *
 * Metodos agregados:
 *   - formatoFactura: genera la representacion de texto de la orden para la factura
 */
public class OrdenCompra implements Serializable {
    private static final long serialVersionUID = 1L;

    private Cliente cliente;
    private Producto producto;
    private int cantidad;
    private LocalDateTime fechaHora;

    public OrdenCompra(Cliente cliente, Producto producto, int cantidad) {
        this.cliente = cliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.fechaHora = LocalDateTime.now();
    }

    public OrdenCompra() {
        // Constructor vacío
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * Genera el texto de la factura con los datos de la orden
     */
    public String formatoFactura() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("------ FACTURA DE COMPRA ------\n");
        sb.append("Fecha/Hora: ").append(fechaHora.format(fmt)).append("\n");
        sb.append("Cliente: ").append(cliente.getNombre())
          .append(" (ID: ").append(cliente.getId()).append(")\n");
        sb.append("Producto: ").append(producto.getNombre())
          .append(" (ID: ").append(producto.getId()).append(")\n");
        sb.append("Cantidad: ").append(cantidad).append("\n");
        sb.append("Precio unitario: $").append(producto.getPrecio()).append("\n");
        sb.append("TOTAL: $").append(producto.getPrecio() * cantidad).append("\n");
        sb.append("------------------------------\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "OrdenCompra{" +
               "cliente=" + cliente +
               ", producto=" + producto +
               ", cantidad=" + cantidad +
               ", fechaHora=" + fechaHora +
               '}';
    }
}


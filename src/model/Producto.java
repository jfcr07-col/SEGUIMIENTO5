package model;

import java.io.Serializable;

public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(String id, String nombre, double precio, int stock) {
        if (precio < 0) {
            throw new PrecioInvalidoException("El precio no puede ser negativo: " + precio);
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo: " + stock);
        }
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public Producto() {
        // Constructor vacío
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new PrecioInvalidoException("El precio no puede ser negativo: " + precio);
        }
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo: " + stock);
        }
        this.stock = stock;
    }

    /**
     * Descuenta stock segun la cantidad pedida.
     *
     * @param cantidad unidades a descontar
     * @throws StockInsuficienteException si cantidad > stock
     */
    public void descontarStock(int cantidad) throws StockInsuficienteException {
        if (cantidad > stock) {
            throw new StockInsuficienteException(
                "Stock insuficiente. Disponible: " + stock + ", pedido: " + cantidad
            );
        }
        stock -= cantidad;
    }

    @Override
    public String toString() {
        return "Producto{" +
               "id='" + id + '\'' +
               ", nombre='" + nombre + '\'' +
               ", precio=" + precio +
               ", stock=" + stock +
               '}';
    }
}

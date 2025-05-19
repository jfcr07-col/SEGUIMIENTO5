package model;

/**
 * StockInsuficienteException
 *
 * Donde se lanza?
 *   - En el metodo descontarStock de Producto si stock < cantidad pedida.
 *
 * Se controla localmente o se propaga?
 *   - Se propaga hasta Controladora y luego hasta la vista para avisar al usuario.
 *
 * Estrategia de recuperacion:
 *   - Mostrar mensaje al usuario y pedir una cantidad menor o volver al menu.
 */
public class StockInsuficienteException extends Exception {
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}

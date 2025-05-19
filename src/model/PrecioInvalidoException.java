package model;

/**
 * PrecioInvalidoException
 *
 * Donde se lanza?
 *   - En el constructor de Producto si el precio es negativo.
 *
 * Se controla localmente o se propaga?
 *   - Se propaga hasta donde se crea el Producto (por ejemplo, en Controladora o en la vista).
 *
 * Estrategia de recuperacion:
 *   - Mostrar mensaje al usuario y volver a pedir un precio valido.
 */
public class PrecioInvalidoException extends IllegalArgumentException {
    public PrecioInvalidoException(String mensaje) {
        super(mensaje);
    }
}

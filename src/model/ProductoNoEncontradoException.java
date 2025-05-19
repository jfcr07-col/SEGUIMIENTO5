package model;

/**
 * ProductoNoEncontradoException
 *
 * Donde se lanza?
 *   - En el metodo searchProducto de Controladora si no encuentra el producto.
 *
 * Se controla localmente o se propaga?
 *   - Se propaga hasta la capa de vista (Executable) para informar al usuario.
 *
 * Estrategia de recuperacion:
 *   - Mostrar mensaje al usuario y permitir reintentar con otro ID o volver al menu.
 */
public class ProductoNoEncontradoException extends Exception {
    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

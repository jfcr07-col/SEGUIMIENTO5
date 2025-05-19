package ui;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import model.Controladora;
import model.PrecioInvalidoException;
import model.ProductoNoEncontradoException;
import model.StockInsuficienteException;

public class Executable {

    private Scanner reader;
    private Controladora cont;
    private static boolean flag;

    private Executable() {
        reader = new Scanner(System.in);
        cont = new Controladora();

        // Cargar inventario desde el archivo de texto (productos.txt) despues de la carga serializada
        try {
            cont.cargarProductosDesdeTexto();
        } catch (IOException ioe) {
            System.out.println("Advertencia: no se pudo cargar inventario desde texto.");
        }
    }

    public void run(boolean flag) {
        flag = false;
        while (!flag) {
            System.out.println("\n\nBienvenido al menu de GameZone:\n");
            System.out.println("Opciones:");
            System.out.println("1. Registrar producto");
            System.out.println("2. Procesar orden de compra");
            System.out.println("3. Salir");

            int option = -1;
            try {
                option = reader.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("Por favor ingrese un numero valido para la opcion.");
                reader.nextLine();
                continue;
            }
            reader.nextLine();

            switch (option) {
                case 1:
                    registrarProducto();
                    break;
                case 2:
                    procesarOrdenCompra();
                    break;
                case 3:
                    flag = true;
                    System.out.println("Saliendo del programa...");

                    // Antes de salir, guardar inventario en "productos.txt"
                    try {
                        cont.guardarProductosEnTexto();
                    } catch (IOException ioe) {
                        System.out.println("Error al guardar inventario en texto: " + ioe.getMessage());
                    }

                    // Ademas, la serializacion binaria ya la hace Controladora al modificar inventario,
                    // pero por si hubiese cambios pendientes:
                    cont.guardarInventario();

                    reader.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Por favor ingrese una opcion valida (1-3).");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        Executable app = new Executable();
        app.run(flag);
    }

    
     // Pide datos para registrar un nuevo producto y maneja excepciones.
    public void registrarProducto() {
        System.out.println("\n-- REGISTRAR PRODUCTO --");
        try {
            System.out.print("Ingrese el ID (entero) del producto: ");
            int id = reader.nextInt();
            reader.nextLine();

            System.out.print("Ingrese el nombre del producto: ");
            String nombre = reader.nextLine();

            System.out.print("Ingrese el precio del producto: ");
            double precio = reader.nextDouble();
            reader.nextLine();

            System.out.print("Ingrese el stock inicial (entero): ");
            int stock = reader.nextInt();
            reader.nextLine();

            cont.agregarProducto(id, nombre, precio, stock);

        } catch (InputMismatchException ime) {
            System.out.println("Error: tipo de dato invalido.");
            reader.nextLine();
        } catch (PrecioInvalidoException pie) {
            System.out.println("Error: " + pie.getMessage());
            System.out.println("No se registro el producto. El precio debe ser >= 0.");
        } catch (IllegalArgumentException iae) {
            System.out.println("Error: " + iae.getMessage());
            System.out.println("No se registro el producto. El stock debe ser >= 0.");
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    // Pide datos para procesar una orden y maneja excepciones.
    public void procesarOrdenCompra() {
        System.out.println("\n-- PROCESAR ORDEN DE COMPRA --");
        try {
            System.out.print("Ingrese el ID del cliente (entero): ");
            int clienteId = reader.nextInt();
            reader.nextLine();

            System.out.print("Ingrese el nombre del cliente: ");
            String clienteNombre = reader.nextLine();

            System.out.print("Ingrese el ID del producto a comprar: ");
            int idProducto = reader.nextInt();
            reader.nextLine();

            System.out.print("Ingrese la cantidad a comprar: ");
            int cantidad = reader.nextInt();
            reader.nextLine();

            cont.procesarOrden(clienteNombre, clienteId, idProducto, cantidad);

        } catch (InputMismatchException ime) {
            System.out.println("Error: tipo de dato invalido.");
            reader.nextLine();
        } catch (ProductoNoEncontradoException pnfe) {
            System.out.println("Error: " + pnfe.getMessage());
            System.out.println("Verifique el ID del producto.");
        } catch (StockInsuficienteException sie) {
            System.out.println("Error: " + sie.getMessage());
            System.out.println("Verifique la cantidad solicitada.");
        } catch (IOException ioe) {
            System.out.println("Error al generar la factura: " + ioe.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }
}

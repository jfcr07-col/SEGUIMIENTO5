package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class Controladora {

    private List<Producto> productos;

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> lista) {
        this.productos = lista;
    }

    private static final String RUTA_SERIAL = "productos.ser";

    /**
     * Carga el inventario desde "productos.ser". Si no existe o hay error, inicializa lista vacía.
     */
    @SuppressWarnings("unchecked")
    private void cargarInventario() {
        File file = new File(RUTA_SERIAL);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                productos = (List<Producto>) ois.readObject();
                System.out.println("Inventario cargado desde serializado.");
            } catch (Exception e) {
                System.out.println("No se pudo cargar inventario. Lista vacía.");
                productos = new java.util.ArrayList<>();
            }
        } else {
            productos = new java.util.ArrayList<>();
            System.out.println("No existe archivo de inventario. Inventario vacío.");
        }
    }

    /**
     * Guarda la lista de productos en "productos.ser".
     *
     * @throws IOException si ocurre un error 
     */
    public void guardarInventario() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA_SERIAL))) {
        oos.writeObject(productos);
    } catch (IOException e) {
        System.out.println("Error al guardar inventario: " + e.getMessage());
    }
    }   


    public Controladora() {
        cargarInventario();
    }

    /**
     * Agrega un nuevo producto al inventario
     *
     * @param id      ID entero del producto
     * @param nombre  Nombre del producto
     * @param precio  Precio unitario (>= 0)
     * @param stock   Stock inicial (>= 0)
     * @throws PrecioInvalidoException   si precio < 0
     * @throws IllegalArgumentException  si stock < 0
     */
   public void agregarProducto(int id, String nombre, double precio, int stock) {
    String idStr = String.valueOf(id);
    try {
        searchProducto(idStr);
        System.out.println("Ya existe un producto con ID " + id + ". No se agrega.");
        return;
    } catch (ProductoNoEncontradoException ignored) {
        // continuar
    }

    if (precio < 0) throw new PrecioInvalidoException("Precio negativo: " + precio);
    if (stock < 0) throw new IllegalArgumentException("Stock negativo: " + stock);

    Producto p = new Producto(idStr, nombre, precio, stock);
    productos.add(p);
    guardarInventario();
    System.out.println("Producto agregado: " + nombre);
    }

    /**
     * Busca un producto por su ID.
     *
     * @param id identificador del producto 
     * @return el objeto Producto encontrado
     * @throws ProductoNoEncontradoException si no existe un producto con ese id
     */
    public Producto searchProducto(String id) throws ProductoNoEncontradoException {
        for (Producto p : productos) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        throw new ProductoNoEncontradoException("No existe producto con ID " + id);
    }

    /**
     * Descuenta el stock de un producto segun la cantidad pedida.
     *
     * @param producto objeto Producto
     * @param cantidad unidades a descontar
     * @throws StockInsuficienteException si cantidad > stock disponible
     */
    public void descontarStock(Producto producto, int cantidad) throws StockInsuficienteException {
        producto.descontarStock(cantidad);
        guardarInventario();
        System.out.println("Stock actualizado. Quedan " +
                producto.getStock() + " unidades de '" + producto.getNombre() + "'.");
    }

    /**
     * Procesa una orden de compra:
     *   Busca el producto.
     *   Descuenta stock.
     *   Crea Cliente y OrdenCompra.
     *   Genera factura en texto.
     *
     * @param clienteNombre nombre del cliente
     * @param clienteId     ID entero del cliente
     * @param idProducto    ID entero del producto a comprar
     * @param cantidad      cantidad a comprar
     * @throws ProductoNoEncontradoException si no existe el producto
     * @throws StockInsuficienteException    si no hay suficiente stock
     * @throws IOException                   si hay error al escribir la factura
     */
    
    public void procesarOrden(String clienteNombre, int clienteId, int idProducto, int cantidad)
        throws ProductoNoEncontradoException, StockInsuficienteException, IOException {

        String idProdStr = String.valueOf(idProducto);
        Producto p = searchProducto(idProdStr);

        descontarStock(p, cantidad); // guarda inventario internamente

        Cliente c = new Cliente(String.valueOf(clienteId), clienteNombre);
        OrdenCompra orden = new OrdenCompra(c, p, cantidad);

        generarFactura(orden); // <- ESTA LINEA GENERA LA FACTURA
        }

    /**
     * Genera un archivo de texto con la factura de la orden.
     *
     * @param orden objeto OrdenCompra
     * @throws IOException si hay problema al escribir el archivo
     */
    private void generarFactura(OrdenCompra orden) throws IOException {
        String timestamp = orden.getFechaHora()
            .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nombreArchivo = "factura_" + timestamp + ".txt";

        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.println(orden.formatoFactura());
        }

        System.out.println("Factura generada: " + nombreArchivo);
    }


    /**
     * Guarda inventario en texto plano ("productos.txt") 
     *
     * @throws IOException si ocurre un error
     */
    public void guardarProductosEnTexto() throws IOException {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("productos.txt"))) {
        for (Producto p : productos) {
            String nombreSeguro = p.getNombre().replace(";", " ");
            bw.write(p.getId() + ";" + nombreSeguro + ";" + p.getPrecio() + ";" + p.getStock());
            bw.newLine();
             }
        }
    }

    /**
     * Carga inventario desde texto plano ("productos.txt"). Sobrescribe 'productos'.
     *
     * @throws IOException si ocurre un error de 
     */
    public void cargarProductosDesdeTexto() throws IOException {
    File fileTxt = new File("productos.txt");
    List<Producto> temp = new java.util.ArrayList<>();

    if (!fileTxt.exists()) {
        this.productos = temp;
        return;
    }

    try (BufferedReader br = new BufferedReader(new FileReader(fileTxt))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            if (linea.trim().isEmpty()) continue;
            String[] partes = linea.split(";");
            if (partes.length != 4) continue;

            String id = partes[0];
            String nombre = partes[1];
            try {
                double precio = Double.parseDouble(partes[2]);
                int stock = Integer.parseInt(partes[3]);
                Producto p = new Producto(id, nombre, precio, stock);
                temp.add(p);
            } catch (Exception e) {
                // omitir linea con errores
            }
        }
    }

     this.productos = temp;

    }
}

import org.bson.Document;

/**
 * Clase que representa un producto en el minimarket.
 */
public class Productos {
    private String codigo;
    private String nombre;
    private double precio;
    private int cantidad;

    /**
     * Constructor por defecto.
     */
    public Productos() {
    }

    /**
     * Constructor que inicializa un producto con los datos especificados.
     * 
     * @param codigo   El código del producto.
     * @param nombre   El nombre del producto.
     * @param precio   El precio del producto.
     * @param cantidad La cantidad del producto.
     */
    public Productos(String codigo, String nombre, double precio, int cantidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el código del producto.
     * 
     * @return El código del producto.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Establece el código del producto.
     * 
     * @param codigo El código del producto.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene el nombre del producto.
     * 
     * @return El nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     * 
     * @param nombre El nombre del producto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el precio del producto.
     * 
     * @return El precio del producto.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto.
     * 
     * @param precio El precio del producto.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la cantidad del producto.
     * 
     * @return La cantidad del producto.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad del producto.
     * 
     * @param cantidad La cantidad del producto.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}

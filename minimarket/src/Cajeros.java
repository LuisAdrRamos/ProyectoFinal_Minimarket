import org.bson.Document;

/**
 * Clase que representa un cajero en el minimarket.
 */
public class Cajeros {
    private String userC;
    private String passC;

    /**
     * Constructor por defecto.
     */
    public Cajeros() {
    }

    /**
     * Constructor que inicializa un cajero con los datos especificados.
     *
     * @param userC El nombre de usuario del cajero.
     * @param passC La contraseña del cajero.
     */
    public Cajeros(String userC, String passC) {
        this.userC = userC;
        this.passC = passC;
    }

    /**
     * Obtiene el nombre de usuario del cajero.
     *
     * @return El nombre de usuario del cajero.
     */
    public String getUserC() {
        return userC;
    }

    /**
     * Establece el nombre de usuario del cajero.
     *
     * @param userC El nombre de usuario del cajero.
     */
    public void setUserC(String userC) {
        this.userC = userC;
    }

    /**
     * Obtiene la contraseña del cajero.
     *
     * @return La contraseña del cajero.
     */
    public String getPassC() {
        return passC;
    }

    /**
     * Establece la contraseña del cajero.
     *
     * @param passC La contraseña del cajero.
     */
    public void setPassC(String passC) {
        this.passC = passC;
    }
}

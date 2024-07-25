public class Cajeros {
    String userC;
    String passC;

    public Cajeros() {
    }

    public Cajeros(String userC, String passC) {
        this.userC = userC;
        this.passC = passC;
    }

    public String getUserC() {
        return userC;
    }

    public void setUserC(String userC) {
        this.userC = userC;
    }

    public String getPassC() {
        return passC;
    }

    public void setPassC(String passC) {
        this.passC = passC;
    }
}

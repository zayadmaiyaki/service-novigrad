public class Administrateur {
    private String username;
    private String password;

    public Administrateur(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean verifInfos(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

   
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package core.models;

public class User {

    private static int id;
    private String login;
    private String password;
    private String group;

    public static int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return строку с названием группы пользователя
     */
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * @return
     * Возвращает true если пользователь пустой иначе false
     */
    public boolean isEmpty(){
        if (id == 0 && login == null && password == null){
            return true;
        }
        return false;
    }
}

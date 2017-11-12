package core.models;

public enum UserGroup {

    nobody,         //0
    administrator,  //1
    manager,        //2
    master,         //3
    acceptor;       //4

    public String toLowercaseString() {
        String lowercase = name().toLowerCase();
        return lowercase;
    }
}

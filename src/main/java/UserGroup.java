public enum UserGroup {
    Administrator,
    Manager,
    Employee;

    public String toLowercaseString() {
        String lowercase = name().toLowerCase();
        return lowercase;
    }

}

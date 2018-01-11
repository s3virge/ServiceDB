package core.utils;

public class HashtableValues {

    private final String sDbTable;
    private final String sTaxtFieldLabal;

    public String getDbTable() {
        return sDbTable;
    }

    public String getTaxtFieldLabal() {
        return sTaxtFieldLabal;
    }

    public HashtableValues(String sDbTable, String sTaxtFieldLabal) {
        this.sDbTable = sDbTable;
        this.sTaxtFieldLabal = sTaxtFieldLabal;
    }
}

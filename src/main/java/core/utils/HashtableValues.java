package core.utils;

public class HashtableValues {

    private final String sDbTable;
    private final String sDbColumn;
    private final String sTaxtFieldLabal;

    public String getsDbColumn() {
        return sDbColumn;
    }
    public String getDbTable() {
        return sDbTable;
    }

    public String getTaxtFieldLabal() {
        return sTaxtFieldLabal;
    }

    public HashtableValues(String sDbTable, String sDbColumn, String sTaxtFieldLabal) {
        this.sDbTable = sDbTable;
        this.sDbColumn = sDbColumn;
        this.sTaxtFieldLabal = sTaxtFieldLabal;
    }
}

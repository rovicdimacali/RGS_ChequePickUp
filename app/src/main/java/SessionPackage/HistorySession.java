package SessionPackage;

public class HistorySession {
    final String trans, comp, add, stat;

    public HistorySession(String trans, String comp, String add, String stat) {
        this.trans = trans;
        this.comp = comp;
        this.add = add;
        this.stat = stat;
    }

    public String getTrans() {
        return trans;
    }

    public String getComp() {
        return comp;
    }


    public String getAdd() {
        return add;
    }


    public String getStat() {
        return stat;
    }

}

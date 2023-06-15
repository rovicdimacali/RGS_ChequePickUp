package SessionPackage;

public class HistorySession {
    String trans, comp, add, stat;

    public HistorySession(String trans, String comp, String add, String stat) {
        this.trans = trans;
        this.comp = comp;
        this.add = add;
        this.stat = stat;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}

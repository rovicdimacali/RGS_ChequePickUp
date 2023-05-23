package SessionPackage;

public class accountSession {
    String accno, entity;

    public accountSession(String accno, String entity) {
        this.accno = accno;
        this.entity = entity;
    }

    public String getAccno() {
        return accno;
    }

    public void setAccno(String accno) {
        this.accno = accno;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}

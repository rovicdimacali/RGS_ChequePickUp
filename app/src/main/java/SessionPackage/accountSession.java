package SessionPackage;

public class accountSession {
    final String accno, entity;

    public accountSession(String accno, String entity) {
        this.accno = accno;
        this.entity = entity;
    }

    public String getAccno() {
        return accno;
    }

    public String getEntity() {
        return entity;
    }

}

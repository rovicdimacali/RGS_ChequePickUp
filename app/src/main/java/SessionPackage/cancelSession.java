package SessionPackage;

public class cancelSession {
    final String cancel, pointPerson;

    public cancelSession(String cancel, String pointPerson) {
        this.cancel = cancel;
        this.pointPerson = pointPerson;
    }

    public String getCancel() {
        return cancel;
    }

    public String getPointPerson() {
        return pointPerson;
    }

}

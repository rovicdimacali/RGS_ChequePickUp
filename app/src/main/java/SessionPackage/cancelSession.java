package SessionPackage;

public class cancelSession {
    String cancel, pointPerson;

    public cancelSession(String cancel, String pointPerson) {
        this.cancel = cancel;
        this.pointPerson = pointPerson;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public String getPointPerson() {
        return pointPerson;
    }

    public void setPointPerson(String pointPerson) {
        this.pointPerson = pointPerson;
    }
}

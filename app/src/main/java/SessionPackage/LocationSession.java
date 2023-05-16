package SessionPackage;

public class LocationSession {
    String company;
    String person;
    String address;
    String contact;

    public LocationSession(String company, String person, String address, String contact) {
        this.company = company;
        this.person = person;
        this.address = address;
        this.contact = contact;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}

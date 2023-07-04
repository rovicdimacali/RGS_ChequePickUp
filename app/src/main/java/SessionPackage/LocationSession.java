package SessionPackage;

public class LocationSession {
    final String company;
    final String person;
    final  String address;
    final String contact;
    final String code;

    public LocationSession(String company, String person, String address, String contact, String code) {
        this.company = company;
        this.person = person;
        this.address = address;
        this.contact = contact;
        this.code = code;
    }

    public String getCompany() {
        return company;
    }

    public String getPerson() {
        return person;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }


    public String getCode() {
        return code;
    }

}

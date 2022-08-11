package Models;

public class Client {

    private int id;
    private String reference;
    private String name;
    private String address;
    private String activity;
    private String nationalNumber;

    public Client() {
    }

    public Client(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Client(int id, String name, String address, String activity, String nationalNumber) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.activity = activity;
        this.nationalNumber = nationalNumber;
    }

    public Client(int id, String reference, String name, String address, String activity, String nationalNumber) {
        this.id = id;
        this.reference = reference;
        this.name = name;
        this.address = address;
        this.activity = activity;
        this.nationalNumber = nationalNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getNationalNumber() {
        return nationalNumber;
    }

    public void setNationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
    }
}

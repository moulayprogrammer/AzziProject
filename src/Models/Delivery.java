package Models;

public class Delivery {

    private int id;
    private String name;
    private String driverLicence;
    private String trackNumber;
    private String trackNumber2;

    public Delivery() {
    }

    public Delivery(int id, String name, String driverLicence, String trackNumber, String trackNumber2) {
        this.id = id;
        this.name = name;
        this.driverLicence = driverLicence;
        this.trackNumber = trackNumber;
        this.trackNumber2 = trackNumber2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriverLicence() {
        return driverLicence;
    }

    public void setDriverLicence(String driverLicence) {
        this.driverLicence = driverLicence;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getTrackNumber2() {
        return trackNumber2;
    }

    public void setTrackNumber2(String trackNumber2) {
        this.trackNumber2 = trackNumber2;
    }
}

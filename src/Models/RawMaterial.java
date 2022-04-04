package Models;

public class RawMaterial {

    private int id;
    private String name;
    private String reference;
    private int limiteQte;

    public RawMaterial() {
    }

    public RawMaterial(int id, String name, String reference, int limiteQte) {
        this.id = id;
        this.name = name;
        this.reference = reference;
        this.limiteQte = limiteQte;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getLimiteQte() {
        return limiteQte;
    }

    public void setLimiteQte(int limiteQte) {
        this.limiteQte = limiteQte;
    }
}

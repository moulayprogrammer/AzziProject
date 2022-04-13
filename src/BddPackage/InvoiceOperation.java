package BddPackage;

import Models.Invoice;

import java.util.ArrayList;

public class InvoiceOperation extends BDD<Invoice> {

    @Override
    public boolean insert(Invoice o) {
        return false;
    }

    @Override
    public boolean update(Invoice o1, Invoice o2) {
        return false;
    }

    @Override
    public boolean delete(Invoice o) {
        return false;
    }

    @Override
    public boolean isExist(Invoice o) {
        return false;
    }

    @Override
    public ArrayList<Invoice> getAll() {
        return null;
    }
}

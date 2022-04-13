package BddPackage;

import Models.Receipt;

import java.util.ArrayList;

public class ReceiptOperation extends BDD<Receipt> {

    @Override
    public boolean insert(Receipt o) {
        return false;
    }

    @Override
    public boolean update(Receipt o1, Receipt o2) {
        return false;
    }

    @Override
    public boolean delete(Receipt o) {
        return false;
    }

    @Override
    public boolean isExist(Receipt o) {
        return false;
    }

    @Override
    public ArrayList<Receipt> getAll() {
        return null;
    }
}

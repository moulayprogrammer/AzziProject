package BddPackage;

import Models.Provider;

import java.util.ArrayList;

public class ProviderOperation extends BDD<Provider> {

    @Override
    public boolean insert(Provider o) {
        return false;
    }

    @Override
    public boolean update(Provider o1, Provider o2) {
        return false;
    }

    @Override
    public boolean delete(Provider o) {
        return false;
    }

    @Override
    public boolean isExist(Provider o) {
        return false;
    }

    @Override
    public ArrayList<Provider> getAll() {
        return null;
    }
}

package BddPackage;

import Models.Client;

import java.util.ArrayList;

public class ClientOperation extends BDD<Client> {

    @Override
    public boolean insert(Client o) {
        return false;
    }

    @Override
    public boolean update(Client o1, Client o2) {
        return false;
    }

    @Override
    public boolean delete(Client o) {
        return false;
    }

    @Override
    public boolean isExist(Client o) {
        return false;
    }

    @Override
    public ArrayList<Client> getAll() {
        return null;
    }
}

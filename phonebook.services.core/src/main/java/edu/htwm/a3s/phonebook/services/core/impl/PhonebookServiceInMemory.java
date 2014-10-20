package edu.htwm.a3s.phonebook.services.core.impl;

import edu.htwm.a3s.phonebook.services.core.PhoneUser;
import edu.htwm.a3s.phonebook.services.core.PhonebookService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mbenndor on 06.10.14.
 */
public class PhonebookServiceInMemory implements PhonebookService{

    private final Map<Integer, PhoneUser> users;
    private int lastId=0;

    public PhonebookServiceInMemory() {
        this.users = new HashMap<Integer, PhoneUser>();
    }

    @Override
    public PhoneUser createUser(String name) {
        PhoneUser user = new PhoneUser(name, ++lastId);
        users.put(user.getId(),user);
        return user;
    }

    @Override
    public PhoneUser getUserById(int userId) {
        return users.containsKey(userId)?users.get(userId):null;

    }

    @Override
    public List<PhoneUser> fetchAllUsers() {
        return new ArrayList<PhoneUser>(users.values());
    }

    @Override
    public void deleteUser(int userId) {
        users.remove(userId);
    }
}

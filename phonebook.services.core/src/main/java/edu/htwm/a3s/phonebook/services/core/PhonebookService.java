package edu.htwm.a3s.phonebook.services.core;


import java.util.List;

/**
 * A service that manages a phone book.
 *
 * @author std / mbenndor
 *
 */
public interface PhonebookService {

    /**
     * Creates a new {@link edu.htwm.a3s.phonebook.services.core.PhoneUser} and returns the managed Entity.
     *
     * @param name The name of the new {@link edu.htwm.a3s.phonebook.services.core.PhoneUser}.
     *
     * @return The new {@link edu.htwm.a3s.phonebook.services.core.PhoneUser}.
     */
    PhoneUser createUser(String name);

    /**
     * Fetches a {@link edu.htwm.a3s.phonebook.services.core.PhoneUser} by its assigned ID.
     *
     * @param userID The id of the {@link edu.htwm.a3s.phonebook.services.core.PhoneUser}.
     *
     * @return The {@link edu.htwm.a3s.phonebook.services.core.PhoneUser} if one was found, null otherwise.
     */
    PhoneUser getUserById(int userID);

    /**
     * Fetches all {@link edu.htwm.a3s.phonebook.services.core.PhoneUser}.
     *
     * @return All existing {@link edu.htwm.a3s.phonebook.services.core.PhoneUser}s if some exist, an empty
     * collection otherwise.
     */
    List<PhoneUser> fetchAllUsers();

    /**
     * Deletes the {@link edu.htwm.a3s.phonebook.services.core.PhoneUser} with the given id.
     *
     * @param userID The id of the {@link edu.htwm.a3s.phonebook.services.core.PhoneUser} to delete.
     */
    void deleteUser(int userID);
}

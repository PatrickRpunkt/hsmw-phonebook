package edu.htwm.a3s.phonebook.services.core;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PhoneNumberTest extends BaseTest {

    @Test
    public void addSingleNumberToUser() {

        PhoneUser newUser = createRandomUser(phoneService);

        String phoneNumberCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(8);
        PhoneNumber expectedPhoneNumber = new PhoneNumber( phoneNumber, phoneNumberCaption);

        PhoneUser userFetchedFromDB = phoneService.getUserById(newUser.getId());

        assertThat(userFetchedFromDB.getPhoneNumbers().size(), is(0));
        assertThat(expectedPhoneNumber, not(isIn(userFetchedFromDB.getPhoneNumbers())));

        userFetchedFromDB.setNumber(expectedPhoneNumber);

        assertThat(userFetchedFromDB.getPhoneNumbers().size(), is(1));
        assertThat(expectedPhoneNumber, isIn(userFetchedFromDB.getPhoneNumbers()));
    }

    @Test
    public void checkForNotExistingNumberReturnsFalse() {

        String phoneNumberCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(8);
        PhoneNumber newNumber = new PhoneNumber(phoneNumberCaption, phoneNumber);

        PhoneUser expectedUser = createRandomUser(phoneService);
        assertThat(newNumber, not(isIn(expectedUser.getPhoneNumbers())));
    }

    @Test
    public void checkForNumberWithCaption() {

        String phoneNumberCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(8);
        PhoneNumber number = new PhoneNumber(phoneNumber, phoneNumberCaption);

        PhoneUser user = createRandomUser(phoneService);
        user.setNumber(number);

        assertThat(user.getNumber(phoneNumberCaption), notNullValue());

        user.deleteNumber(phoneNumberCaption);

        assertThat(user.getNumber(phoneNumberCaption),nullValue());
    }
}

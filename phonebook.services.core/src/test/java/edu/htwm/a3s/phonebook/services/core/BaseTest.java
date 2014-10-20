package edu.htwm.a3s.phonebook.services.core;

import edu.htwm.a3s.phonebook.services.core.impl.PhonebookServiceInMemory;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;

public abstract class BaseTest {

	PhonebookService phoneService;

    @Before
    public void buildService(){
        phoneService= new PhonebookServiceInMemory();
    }


	public PhoneUser createRandomUser(PhonebookService phoneService) {
		return createUser(phoneService, RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
	}
	
	public PhoneUser createUser(PhonebookService phoneService, String userName) {
		return phoneService.createUser(userName);
	}
}

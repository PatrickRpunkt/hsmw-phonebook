package edu.htwm.a3s.phonebook.services.rest;

import edu.htwm.a3s.phonebook.services.core.PhoneNumber;
import edu.htwm.a3s.phonebook.services.core.PhoneUser;
import edu.htwm.a3s.phonebook.services.core.PhonebookService;
import edu.htwm.a3s.phonebook.services.core.impl.PhonebookServiceInMemory;
import edu.htwm.a3s.phonebook.services.rest.impl.PhonebookResourceImpl;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Diese Klasse testet die Funktionalität der REST-Methoden unabhängig vom 
 * Web-Server (Whitebox-Testing). Das Controller-Objekt phonebookService 
 * wird manuell instanziert und der Instanz von PhonebookResourcImpl
 * übergeben
 *
 * @author adopleb / mbenndor
 */
public class PhonebookResourceImplTest {

	private PhonebookResourceImpl phonebookResource;
	private UriInfo uriInfo;
	private PhonebookService phoneService;
    private String EMPTYSTRING;

    /**
	 * initialize components
	 */
	@Before
	public void prepareResourcesToTest() {
		phoneService = new PhonebookServiceInMemory();
		phonebookResource = new PhonebookResourceImpl();
		phonebookResource.setPhoneService(phoneService);
        EMPTYSTRING = "";
    }

	/**
	 * Hilfsmethode (Mock-Objekt), welche die URI-Informationen bereitstellt 
	 * (Wird sonst vom Web-Server instanziert)
	 */
	@Before
	public void mockUriInfo() {

		uriInfo = mock(UriInfo.class);
		final UriBuilder fromResource = UriBuilder.fromResource(PhonebookResource.class);
		when(uriInfo.getAbsolutePathBuilder()).thenAnswer(new Answer<UriBuilder>() {
			@Override
			public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
				return fromResource;
			}
		});
	}

	/**
	 * Testet, ob sich ein neuer Nutzer anlegt werden kann (Statuscode 201 (created))
	 * Anschließend wird getestet, ob der neu angelegte Nutzer existiert (Statuscode
	 * 200 (OK))
	 * Daraufhin wird geprüft ob der zurückgegebene User identisch mit dem erzeugten ist
	 *
	 * @throws IllegalArgumentException
	 * @throws javax.ws.rs.core.UriBuilderException
	 * @throws java.net.URISyntaxException
	 * @throws java.net.MalformedURLException
	 */
	@Test
	public void createUserSuccess() throws IllegalArgumentException, UriBuilderException, URISyntaxException, MalformedURLException {
		String expectedName = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1);
		Response createUserResponse = phonebookResource.createUser(uriInfo, expectedName);

		PhoneUser createdUser = (PhoneUser) createUserResponse.getEntity();

		// check correct response code 
		assertThat(createUserResponse.getStatus(), is(Status.CREATED.getStatusCode()));

		// get User 
		Response fetchUserResponse = phonebookResource.getUser(createdUser.getId());
		// check correct response code 
		assertThat(fetchUserResponse.getStatus(), is(Status.OK.getStatusCode()));
        // deserialize User
		PhoneUser fetchedUser = (PhoneUser) fetchUserResponse.getEntity();
		assertThat(fetchedUser, is(createdUser));
	}

	/**
	 * Anlegen eines Nutzer ohne Namen
	 */
	@Test
	public void createUserFails() {
		String name = EMPTYSTRING;
		Response createUserResponse = phonebookResource.createUser(uriInfo, name);
        //check correct response code (bad request because of no name)
		assertThat(createUserResponse.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
	}

	/**
	 * GET einen nicht existierenden User
	 */
	@Test
	public void fetchNotExistingUser() {
		Response fetchUserResponse = phonebookResource.getUser(Integer.MAX_VALUE);
        //check correct response code
		assertThat(fetchUserResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
	}



	/**
	 * Methode testet, ob sich eine Telefonnummer für einen Nutzer anlegen und
	 * löschen lässt
	 */
	@Test
	public void createAndDeleteNumber() {
		//CREATE random PhoneUser and PhoneNumber
        PhoneUser randomUser = createRandomUser();
        PhoneNumber randomNumber = createRandomNumber();
		// add Number
		Response addNumberResponse = phonebookResource.addNumberToUser(randomUser.getId(), randomNumber.getNumber(),randomNumber.getCaption());
        //check correct response code (201)
        assertThat(addNumberResponse.getStatus(), is(Status.CREATED.getStatusCode()));

        // GET User
		PhoneUser fetchedUserWithNumber = (PhoneUser) phonebookResource.getUser(randomUser.getId()).getEntity();
        // number with caption exists
        assertThat(fetchedUserWithNumber.containsNumber(randomNumber.getCaption()), is(true));
        // get Number
        PhoneNumber tempNumber = fetchedUserWithNumber.getNumber(randomNumber.getCaption());
        //same number?
        assertThat(tempNumber.getNumber(), is(randomNumber.getNumber()));


        // DELETE PhoneNumber
		Response deleteNumberResponse = phonebookResource.deleteNumber(randomUser.getId(), randomNumber.getCaption());
        //check correct response code (200)
        assertThat(deleteNumberResponse.getStatus(), is(Status.OK.getStatusCode()));

		// get User
		PhoneUser fetchedUserWithoutNumber = (PhoneUser) phonebookResource.getUser(randomUser.getId()).getEntity();
        // check Number is deleted
        assertThat(fetchedUserWithoutNumber.getPhoneNumbers().contains(randomNumber), is(false));
	}


    /**
	 * Nummer einem nicht existierden User hinzufügen
	 */
	@Test
	public void checkAddNumberToNotUser() {
		//remove all data
		this.prepareResourcesToTest();
        //create random data;
		int randomID = RandomUtils.nextInt(10) + 13;
		PhoneNumber phoneNumber = createRandomNumber();
		///add number to not existing user
		Response addNumberResponse = phonebookResource.addNumberToUser(randomID, phoneNumber.getCaption(), phoneNumber.getNumber());
        //check correct response code (404)
        assertThat(addNumberResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
	}

    /**
     * Leere Nummer hinzufügen
     */
    @Test
    public void checkAddEmptyNumber(){
        //add user
        PhoneUser user = createRandomUser();
        //add empty Strings
        Response addNumberResponse = phonebookResource.addNumberToUser(user.getId(), EMPTYSTRING, EMPTYSTRING);
        assertThat(addNumberResponse.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
    }


	/**
	 * Testet, dass deleteNumber 404 zurückgibt, wenn kein User gefunden wurde
	 * bzw. zu einem User die PhoneNumber nicht existiert
	 */
	@Test
	public void checkDeleteNumber() {
		//create random data;
        PhoneUser randomUser = createRandomUser();
        PhoneNumber randomNumber = createRandomNumber();

		//test delete from user without number
		Response deleteNumberResponse = phonebookResource.deleteNumber(randomUser.getId(), randomNumber.getCaption());
		assertThat(deleteNumberResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));

		// delete all
		this.prepareResourcesToTest();

		//test user not found
		deleteNumberResponse = phonebookResource.deleteNumber(randomUser.getId(), randomNumber.getCaption());
		assertThat(deleteNumberResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
	}

	/**
	 * Löschen eines existierenden und eines nicht existierenden Users
	 */
	@Test
	public void checkDeleteUser() {
        // create randomUser
        PhoneUser randomUser = createRandomUser();
        //user deleted?
		Response deleteUserResponse = phonebookResource.deleteUser(randomUser.getId());
		assertThat(deleteUserResponse.getStatus(), is(Status.OK.getStatusCode()));
		assertThat(phoneService.fetchAllUsers().contains(randomUser), is(false));
		//retry delete allready deleted user
		deleteUserResponse = phonebookResource.deleteUser(randomUser.getId());
        //check correct response code (404)
		assertThat(deleteUserResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
	}

    /**
     * Hilfsmethode: erzeugt einen neuen zufälligen Nutzer
     * @return random PhoneUser
     */
    private PhoneUser createRandomUser() {
        return phoneService.createUser(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
    }

    /**
     * Hilfsmethode: erzeugt eine neue zufällige Nummer
     * @return random PhoneNumber
     */
    private PhoneNumber createRandomNumber() {
        String phoneNumberCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(8);
        return new PhoneNumber(phoneNumber, phoneNumberCaption);
    }

}

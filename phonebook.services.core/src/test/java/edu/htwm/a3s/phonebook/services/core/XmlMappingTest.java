/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package edu.htwm.a3s.phonebook.services.core;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
*
* @author adopleb , mbenndor
*
* Testet, ob die JAXB-Annotationen korrekt gesetzt sind und das das XML-Mapping
* fehlerfrei funktioniert
*/
public class XmlMappingTest extends BaseTest {
    private File testfileLoad;
    private File testfileSave;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;
    private String test;

    @Before
    public void prepareXmlMarshalling() throws JAXBException {
        testfileLoad= new File(XmlMappingTest.class.getResource("/anne.xml").getPath());
        testfileSave = new File(XmlMappingTest.class.getResource("/random.xml").getPath());
        /*
         * Code Duplizierung vermeiden
         * (DRY -> http://de.wikipedia.org/wiki/Don%E2%80%99t_repeat_yourself) und
         * aufwändige, immer wieder verwendete Objekte nur einmal erzeugen
         *
         */
        JAXBContext context = JAXBContext.newInstance(PhoneUser.class, PhoneNumber.class);

        marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        unmarshaller = context.createUnmarshaller();
    }

    /**
     * Testet ob eine XML-Datei korrekt angelegt wird
     */
    @Test
    public void createValidXmlFile() throws IOException, JAXBException {

        // Lösche Datei falls schon vorhanden
        if (testfileSave.exists()) {
            testfileSave.delete();
        }

        /*
         * Erzeugt einen Nutzer mit einem zufälligen Namen und einer zufälligen Telefonnummer.
         */
        PhoneUser newUser = createRandomUser(phoneService);
        String phoneNumberCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(8);
        PhoneNumber newNumber = new PhoneNumber(phoneNumberCaption, phoneNumber);
        newUser.getPhoneNumbers().add(newNumber);

        // XML-Datei schreiben
        marshaller.marshal(newUser, new FileWriter(testfileSave));
        // Teste ob Datei existiert und lesbar ist
        assertThat(testfileSave.exists(), is(true));
        assertThat(testfileSave.isFile(), is(true));

        // deserialize
        PhoneUser deserializedUser = (PhoneUser) unmarshaller.unmarshal(testfileSave);

        // check deserialization creates new object
        assertThat(deserializedUser, is(not(sameInstance(newUser))));
        assertThat(deserializedUser.getName(), is(newUser.getName()));
    }

    /**
     * liest aus XML-Datei *
     */
    @Test
    public void loadFromXmlFile() throws IOException, JAXBException {

        String username=testfileLoad.toString();
        PhoneUser loadedUser = (PhoneUser) unmarshaller.unmarshal(new FileReader(testfileLoad));

        Collection phoneNumbers = loadedUser.getPhoneNumbers();

        // prüft, ob die beiden Telefonnummern enthalten sind
        assertThat(phoneNumbers.size(), is(2));
        // prüft, ob der Name erkannt wurde
        assertThat(loadedUser.getName(), is("anne"));
        // prüft, dass die ID stimmt
        assertThat(loadedUser.getId(), is(1));
    }
}

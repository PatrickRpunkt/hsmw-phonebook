package edu.htwm.a3s.phonebook.services.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "user")
public class PhoneUser {

    private String name;
    private int id;
    private List<PhoneNumber> phoneNumbers;

    private PhoneUser(){
        this.phoneNumbers=new ArrayList<PhoneNumber>();
    }

    public PhoneUser(String name,int id,List<PhoneNumber> phoneNumbers){
        this.name=name;
        this.id=id;
        this.phoneNumbers=phoneNumbers;
    }

    public PhoneUser(String name, int id){
        this(name, id, new ArrayList<PhoneNumber>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name="number")
    @XmlElementWrapper(name = "phone-numbers")
    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public void setNumber(PhoneNumber number){
        this.phoneNumbers.add(number);
    }

    public PhoneNumber getNumber(String caption){
        PhoneNumber phoneNumber = getPhoneNumber(caption);
        return phoneNumber;
    }

    public void deleteNumber(String caption){
        PhoneNumber phoneNumber = getPhoneNumber(caption);
        if(phoneNumber!=null){
            phoneNumbers.remove(phoneNumber);
        }
    }

    public boolean containsNumber(String caption){
        boolean result = false;
        if(this.getPhoneNumber(caption)!=null){
            result = true;
        }
        return result;
    }

    private PhoneNumber getPhoneNumber(String caption) {
        PhoneNumber phoneNumber=null;
        for(PhoneNumber number: phoneNumbers){
            if(number.getCaption()==caption){
                phoneNumber=number;
            }
        }
        return phoneNumber;
    }
}

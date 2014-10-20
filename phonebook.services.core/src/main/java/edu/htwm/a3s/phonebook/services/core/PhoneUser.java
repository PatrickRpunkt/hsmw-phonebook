package edu.htwm.a3s.phonebook.services.core;

import java.util.ArrayList;
import java.util.List;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
        PhoneNumber phoneNumber=null;
        for(PhoneNumber number:phoneNumbers){
            if(number.getCaption()==caption){
                phoneNumber=number;
            }
        }
        return phoneNumber;
    }

    public void deleteNumber(String caption){
        PhoneNumber phoneNumber=null;
        for(PhoneNumber number: phoneNumbers){
            if(number.getCaption()==caption){
                phoneNumber=number;
            }
        }
        if(phoneNumber!=null){
            phoneNumbers.remove(phoneNumber);
        }
    }
}

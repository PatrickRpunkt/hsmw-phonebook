package edu.htwm.a3s.phonebook.services.core;

public class PhoneNumber {

    private String number;
    private String caption;

    protected PhoneNumber(){
    }

    public  PhoneNumber(String number, String caption){
        this.number=number;
        this.caption=caption;
    }



    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}

package edu.htwm.a3s.phonebook.services.rest.impl;

import edu.htwm.a3s.phonebook.services.core.PhoneNumber;
import edu.htwm.a3s.phonebook.services.core.PhoneUser;
import edu.htwm.a3s.phonebook.services.core.PhonebookService;
import edu.htwm.a3s.phonebook.services.rest.PhonebookResource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Created by mbenndor on 28.10.14.
 */
public class PhonebookResourceImpl implements PhonebookResource {

    @Context
    private PhonebookService phoneService;

    @Override
    public Response createUser(UriInfo uriInfo, String name) {
        if (name ==null||name.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        PhoneUser newUser = getPhoneService().createUser(name);
        return Response.status(Response.Status.CREATED).entity(newUser).build();
    }

    @Override
    public Response getUser(int userID) {
        PhoneUser userById = phoneService.getUserById(userID);
        if (userById == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(userById).build();
    }

    public PhonebookService getPhoneService() {
        return phoneService;
    }

    public void setPhoneService(PhonebookService phoneService) {
        this.phoneService = phoneService;
    }

    @Override
    public Response deleteUser(int userID) {
        if (phoneService.getUserById(userID)==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        phoneService.deleteUser(userID);
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response addNumberToUser(int userId, String number, String caption) {
        PhoneUser userById = phoneService.getUserById(userId);
        if (userById==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(number==null||caption==null||number.isEmpty()||caption.isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        userById.setNumber(new PhoneNumber(number,caption));
        return Response.status(Response.Status.CREATED).build();
    }

    @Override
    public Response deleteNumber(int userID,String caption) {
        PhoneUser user= phoneService.getUserById(userID);
        if(user==null||!user.containsNumber(caption)){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        user.deleteNumber(caption);
        return Response.status(Response.Status.OK).build();
    }
}


package edu.htwm.a3s.phonebook.services.rest;

import edu.htwm.a3s.phonebook.services.core.PhoneUser;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("users")
public interface PhonebookResource {
    

    /**
     * Creates a new {@link PhoneUser} with the given name.
     *
     * @param uriInfo Injected by JAX-RS. Used for building the correct path to
     * the newly created resource.
     * @param name The name of the user to create.
     *
     * @return 201 CREATED and the path for accessing the new user, an
     * appropriate status code otherwise.
     */
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response createUser(
            @Context UriInfo uriInfo,
            @FormParam("name") String name);
            
    /**
     * Fetches an existing user by its id.
     *
     * @param userId The id of the user to fetch.
     * @return 200 - and the found user if one exists, an appropriate HTTP
     * status code else.
     */
    @GET
    @Path("{userid}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response getUser(@PathParam("userid") int userId);
             
    
    /**
     * Delete an existiting user by its id
     * @param userId The id of the user to delete
     * @return 200 
     */
    @DELETE
    @Path("{userId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response deleteUser(@PathParam("userId") int userId);
     
    
    /**
     * Adds a number an its caption to an existing user
     * @param userID The id of the existing user
     * @param number The number to add
     * @param caption The caption to the number
     * @return 200
     */
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @PUT
    @Path("{userId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response addNumberToUser(
            @PathParam("userId") int userID,
            @QueryParam("number") String number,
            @QueryParam("caption") String caption);


    /**
     * Deletes a number aand its caption froma an existing user
     * @param userId the id of the existing user
     * @param caption the caption of the number which will be deleted
     * @return 200
     */
    @DELETE
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("{userId}" + "/numbers/" + "{caption}")
	Response deleteNumber(
            @PathParam("userId") int userId,
            @PathParam("caption") String caption);
}

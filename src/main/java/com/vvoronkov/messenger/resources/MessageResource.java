package com.vvoronkov.messenger.resources;

import com.vvoronkov.messenger.exception.DataNotFoundException;
import com.vvoronkov.messenger.model.Message;
import com.vvoronkov.messenger.resources.beans.MessageFilterBean;
import com.vvoronkov.messenger.service.MessageService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {

    private MessageService messageService = new MessageService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getJsonMessages(@BeanParam MessageFilterBean messageFilterBean) {
        if (messageFilterBean.getYear() > 0) {
            return messageService.getAllMessagesFroYear(messageFilterBean.getYear());
        }
        if (messageFilterBean.getStart() > 0 && messageFilterBean.getSize() > 0) {
            return messageService.getAllMessagesPaginated(messageFilterBean.getStart(), messageFilterBean.getSize());
        }
        return messageService.getAllMessages();
    }

    @GET
    @Produces(MediaType.TEXT_XML)
    public List<Message> getXmlMessages(@BeanParam MessageFilterBean messageFilterBean) {
        if (messageFilterBean.getYear() > 0) {
            return messageService.getAllMessagesFroYear(messageFilterBean.getYear());
        }
        if (messageFilterBean.getStart() > 0 && messageFilterBean.getSize() > 0) {
            return messageService.getAllMessagesPaginated(messageFilterBean.getStart(), messageFilterBean.getSize());
        }
        return messageService.getAllMessages();
    }

    @POST
    public Response addMessage(Message message, @Context UriInfo uriInfo) {
        Message newMessage = messageService.addMessage(message);
        URI newUri = uriInfo.getBaseUriBuilder().path(String.valueOf(message.getId())).build();
        return Response.created(newUri)
                .entity(newMessage)
                .build();
    }

    @GET
    @Path("/{messageId}")
    public Message getMessage(@PathParam("messageId") long id, @Context UriInfo uriInfo) {
        Message message = messageService.getMessage(id);
        if (message == null) {
            throw new DataNotFoundException("Message with id " + id + " not found");
        }
        message.addLink(getUriForSelf(uriInfo, message), "self");
        message.addLink(getUriForProfile(uriInfo, message), "profile");
        message.addLink(getUriForComments(uriInfo, message), "comments");
        return message;
    }

    private String getUriForComments(UriInfo uriInfo, Message message) {
        return uriInfo.getBaseUriBuilder()
                .path(MessageResource.class)
                .path(MessageResource.class, "getCommentResource")
                .resolveTemplate("messageId", message.getId())
                .build()
                .toString();
    }

    public String getUriForSelf(UriInfo uriInfo, Message message) {
        return uriInfo.getBaseUriBuilder().path(MessageResource.class)
                .path(Long.toString(message.getId()))
                .build()
                .toString();
    }

    public String getUriForProfile(UriInfo uriInfo, Message message) {
        return uriInfo.getBaseUriBuilder()
                .path(ProfileResource.class)
                .path(message.getAuthor())
                .build()
                .toString();
    }


    @PUT
    @Path("/{messageId}")
    public Message updateMessage(@PathParam("messageId") long id, Message message) {
        message.setId(id);
        return messageService.updateMessage(message);
    }

    @DELETE
    @Path("/{messageId}")
    public void deleteMessage(@PathParam("messageId") long id) {
        messageService.removeMessage(id);
    }

    @Path("/{messageId}/comments")
    public CommentResource getCommentResource() {
        return new CommentResource();
    }
}

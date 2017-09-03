package com.vvoronkov.messenger.resources;

import com.vvoronkov.messenger.model.Message;
import com.vvoronkov.messenger.resources.beans.MessageFilterBean;
import com.vvoronkov.messenger.service.MessageService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {

    private MessageService messageService = new MessageService();

    @GET
    public List<Message> getMessages(@BeanParam MessageFilterBean messageFilterBean){
        if (messageFilterBean.getYear() > 0){
            return messageService.getAllMessagesFroYear(messageFilterBean.getYear());
        }
        if (messageFilterBean.getStart() > 0 && messageFilterBean.getSize() > 0){
            return messageService.getAllMessagesPaginated(messageFilterBean.getStart(), messageFilterBean.getSize());
        }
        return messageService.getAllMessages();
    }

    @POST
    public Message addMessage(Message message){
        return messageService.addMessage(message);
    }

    @GET
    @Path("/{messageId}")
    public Message getMessage(@PathParam("messageId") long id){
        return messageService.getMessage(id);
    }


    @PUT
    @Path("/{messageId}")
    public Message updateMessage(@PathParam("messageId") long id, Message message){
        message.setId(id);
        return messageService.updateMessage(message);
    }

    @DELETE
    @Path("/{messageId}")
    public void deleteMessage(@PathParam("messageId") long id){
        messageService.removeMessage(id);
    }

    @Path("/{messageId}/comments")
    public CommentResource getCommentResource(){
        return new CommentResource();
    }
}

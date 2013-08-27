package kanbannow.resources;

import com.yammer.metrics.annotation.Timed;
import kanbannow.api.BacklogItem;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/backlogitems")
@Produces(MediaType.APPLICATION_JSON)
public class BacklogItemResource {


    @GET
    @Timed
    @Path("user/{id}")
    public List<BacklogItem> getBacklogItemsForUser() {
//    public List<BacklogItem> getBacklogItemsForUser(@PathParam("id") int userId) {
        return new ArrayList<BacklogItem>();
    }
}

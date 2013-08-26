package kanbannow.resources;

import com.yammer.metrics.annotation.Timed;
import kanbannow.api.BacklogItem;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/backlogitems")
@Produces(MediaType.APPLICATION_JSON)
public class BacklogItemResource {


    @GET
    @Timed
    public List<BacklogItem> getBacklogItems() {
        return new ArrayList<BacklogItem>();
    }
}

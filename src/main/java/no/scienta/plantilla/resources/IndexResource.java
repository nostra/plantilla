package no.scienta.plantilla.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import no.scienta.plantilla.views.GeneralView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class IndexResource {

    private Logger log = LoggerFactory.getLogger(IndexResource.class);

    @GET
    @Timed
    public GeneralView index(@QueryParam("explain") Optional<String> explain) {
        List<String> names = Arrays.asList("name 1", "name 2");
        return new GeneralView("index.ftl", names);
    }


}

package no.scienta.plantilla;

import no.scienta.plantilla.views.GeneralView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Based on http://gary-rowe.com/agilestack/2012/10/23/how-to-implement-a-runtimeexceptionmapper-for-dropwizard/
 */

@Provider
public class PlantillaExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static Logger log = LoggerFactory.getLogger(PlantillaExceptionMapper.class);

    @Override
    public Response toResponse(RuntimeException runtime) {

        // Build default response
        Response defaultResponse = Response
                                       .serverError()
                                       .entity(new GeneralView("error/500.ftl"))
                                       .build();
        // Check for any specific handling
        if (runtime instanceof WebApplicationException) {

            return handleWebApplicationException(runtime, defaultResponse);
        }

        // Use the default
        log.error("Got exception: "+runtime, runtime);
        return defaultResponse;

    }

    private Response handleWebApplicationException(RuntimeException exception, Response defaultResponse) {
        WebApplicationException webAppException = (WebApplicationException) exception;

        // No logging
        if (webAppException.getResponse().getStatus() == 401) {
            return Response
                       .status(Response.Status.UNAUTHORIZED)
                       .entity(new GeneralView("error/401.ftl"))
                       .build();
        }
        if (webAppException.getResponse().getStatus() == 404) {
            return Response
                       .status(Response.Status.NOT_FOUND)
                       .entity(new GeneralView("error/404.ftl"))
                       .build();
        }

        log.error("Got unhandled exception type: "+exception, exception);

        return defaultResponse;
    }
}


package tw.kewang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("articles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArticleResource {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleResource.class.getSimpleName());

    @GET
    public Response getArticles(@Context HttpHeaders httpHeaders) {
        return Response.ok("{'a':'b'}").build();
    }

    @POST
    public Response createArticle(@Context HttpHeaders httpHeaders, String body) {
        return Response.ok("{'a':'b'}").build();
    }

    @DELETE
    public Response removeArticle(@Context HttpHeaders httpHeaders, String body) {
        return Response.ok("{'a':'b'}").build();
    }

    @PUT
    public Response updateArticle(@Context HttpHeaders httpHeaders) {
        return Response.ok("{'a':'b'}").build();
    }
}
package tw.kewang.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.kewang.caches.GetArticlesETagCache;
import tw.kewang.filters.annotations.Cache;
import tw.kewang.filters.annotations.Cache.KeyType;
import tw.kewang.filters.annotations.MaxAge;
import tw.kewang.resources.requests.GetArticleRequest;
import tw.kewang.resources.responses.GetArticleResponse;
import tw.kewang.resources.responses.ResponseUtils;
import tw.kewang.resources.services.GetArticleService;

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
    @MaxAge(3)
    @Cache(cacheType = GetArticlesETagCache.class, keyType = KeyType.USER_ID)
    public Response getArticles(@Context HttpHeaders httpHeaders, @QueryParam("size") @DefaultValue("20") int size) {
        GetArticleRequest request = new GetArticleRequest();

        request.size = size;

        GetArticleService service = new GetArticleService();

        GetArticleResponse response = service.execute(request);

        return ResponseUtils.ok(response);
    }

    @POST
    public Response createArticle(@Context HttpHeaders httpHeaders, String body) {
        return Response.ok("{'a':'b'}").build();
    }

    @DELETE
    public Response removeArticle(@Context HttpHeaders httpHeaders) {
        return Response.ok("{'a':'b'}").build();
    }

    @PUT
    public Response updateArticle(@Context HttpHeaders httpHeaders, String body) {
        return Response.ok("{'a':'b'}").build();
    }

//    @Override
//    @MaxAge(3)
//    @Cache(cacheType = MeChatroomETagCache.class, keyType = KeyType.USER_ID)
//    public Response getChatrooms(HttpHeaders httpHeaders) {
//        String userId = UserInfoHolder.getUserInfo().getUserID();
//
//        ChatRequest chatRequest = new ChatRequest().setUserID(userId);
//
//        ChatResponse chatResponse = Chatroom.GET_GLOBAL.call(chatRequest);
//
//        if (chatResponse.isSuccess()) {
//            MeChatroomETagCache cache = new MeChatroomETagCache();
//
//            cache.setKey(userId);
//
//            Response res = ResponseUtils.ok(chatResponse.getArgsMap(), cache);
//
//            cache.store();
//
//            return res;
//        } else {
//            return ResponseUtils.badRequestGlobalException();
//        }
//    }
}
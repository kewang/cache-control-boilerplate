package tw.kewang.resources.services;

import tw.kewang.UserInfoHolder;
import tw.kewang.caches.Cacheable;
import tw.kewang.caches.GetArticlesETagCache;
import tw.kewang.resources.requests.CreateArticleRequest;
import tw.kewang.resources.responses.BasicResponse;
import tw.kewang.resources.responses.CreateArticleResponse;

public class CreateArticleService extends BasicService<CreateArticleRequest, CreateArticleResponse> implements Cacheable<GetArticlesETagCache> {
    @Override
    protected BasicResponse processing(CreateArticleRequest request) throws Exception {
        invalidateCache(UserInfoHolder.getUserInfo().userId);

        return new CreateArticleResponse();
    }
}
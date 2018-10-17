package tw.kewang.resources.services;

import tw.kewang.caches.Cacheable;
import tw.kewang.caches.GetArticlesETagCache;
import tw.kewang.resources.requests.DeleteArticleRequest;
import tw.kewang.resources.responses.BasicResponse;
import tw.kewang.resources.responses.DeleteArticleResponse;

public class DeleteArticleService extends BasicService<DeleteArticleRequest, DeleteArticleResponse> implements Cacheable<GetArticlesETagCache> {
    @Override
    protected BasicResponse processing(DeleteArticleRequest request) throws Exception {
        invalidateAllCache();

        return new DeleteArticleResponse();
    }
}
package tw.kewang.resources.services;

import org.apache.commons.lang3.RandomStringUtils;
import tw.kewang.resources.requests.GetArticleRequest;
import tw.kewang.resources.responses.BasicResponse;
import tw.kewang.resources.responses.GetArticleResponse;

public class GetArticleService extends BasicService<GetArticleRequest, GetArticleResponse> {
    @Override
    protected BasicResponse processing(GetArticleRequest request) throws Exception {
        GetArticleResponse response = new GetArticleResponse();

        for (int i = 0; i < request.size; i++) {
            GetArticleResponse.Article article = new GetArticleResponse.Article();

            article.title = RandomStringUtils.random(3, true, true);
            article.description = RandomStringUtils.random(10, true, true);

            response.articles.add(article);
        }

        return response;
    }
}
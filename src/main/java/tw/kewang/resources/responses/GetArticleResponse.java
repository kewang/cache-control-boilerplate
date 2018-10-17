package tw.kewang.resources.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetArticleResponse extends BasicResponse {
    @SerializedName("articles")
    public List<Article> articles = new ArrayList<>();

    public static class Article {
        @SerializedName("title")
        public String title;
        @SerializedName("description")
        public String description;
    }
}
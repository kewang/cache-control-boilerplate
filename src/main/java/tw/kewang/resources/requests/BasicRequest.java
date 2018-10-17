package tw.kewang.resources.requests;

import com.google.gson.Gson;

public abstract class BasicRequest {
    public String toJson() {
        return new Gson().toJson(this);
    }
}
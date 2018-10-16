package tw.kewang.resources;

import com.google.gson.Gson;

public abstract class BasicRequest implements Request {
    public String toJson() {
        return new Gson().toJson(this);
    }
}
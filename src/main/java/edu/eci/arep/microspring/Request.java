package edu.eci.arep.microspring;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private String path;

    private Map<String , String > queryParams;

    public Request(String path, String queryString) {
        this.path = path;
        this.queryParams = new HashMap<>();
        if (queryString != null && !queryString.isEmpty()){
        String[] pairs = queryString.split("&");
        for(String pair : pairs){
            String[] valueKeyStrings = pair.split("=");
            if(valueKeyStrings.length == 2){
                queryParams.put(valueKeyStrings[0], valueKeyStrings[1]);
            }
        }
    }
}


    public String getPath() {
        return path;
    }

    public String getQueryString(String key){ 
        return queryParams.getOrDefault(key, null);
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }
}
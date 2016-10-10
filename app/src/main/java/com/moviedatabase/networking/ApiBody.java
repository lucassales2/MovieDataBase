package com.moviedatabase.networking;

/**
 * Created by lucas on 27/09/16.
 */

public class ApiBody {
    public String api_key;
    public static ApiBody getDefault() {
        ApiBody apiBody = new ApiBody();
        apiBody.api_key = "8bac4611e91e23f84d7c01b1090379ad";
        return apiBody;
    }
}

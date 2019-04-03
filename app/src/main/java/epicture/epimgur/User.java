package epicture.epimgur;

import java.io.Serializable;
import java.util.Map;

public class User implements Serializable {
    public String access_token;
    public String expires_in;
    public String token_type;
    public String refresh_token;
    public String account_username;
    public String account_id;

    public User(Map<String, String> params) {
        access_token = params.get("access_token");
        expires_in = params.get("expires_in");
        token_type = params.get("token_type");
        refresh_token = params.get("refresh_token");
        account_username = params.get("account_username");
        account_id = params.get("account_id");
    }
}

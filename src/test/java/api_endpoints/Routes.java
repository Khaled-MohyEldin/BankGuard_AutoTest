package api_endpoints;

public class Routes {
    //keep only the URLs

    // public so we can use it across all classes
    // static to use it using className without need to create object
    public static String base = "http://localhost:8083/api";

    //User Module
    // the "{username}" is path parameter
    public static String user_post_url       = base + "/users";
    public static String user_getAll_url     = base + "/users";
    public static String user_get_url        = base + "/users/{id}";
    public static String user_getByName_url  = base + "/users/{username}";
    public static String user_update_url     = base + "/users/{id}";
    public static String user_delete_url     = base + "/users/{id}";

    //Account Module
    public static String acc_post_url        = base + "/accounts";
    public static String acc_getAll_url      = base + "/accounts";
    public static String acc_get_url         = base + "accounts/{id}";
    public static String acc_getByUser_url   = base + "/accounts/user/{userId}";
    public static String acc_getByAccNum_url = base + "/accounts/number/{accountNumber}";
    public static String acc_update_url      = base + "/accounts/{id}";
    public static String acc_delete_url      = base + "/accounts/{id}";

    //Transactions Module
    public static String trans_post_url       = base + "/transactions";
    public static String trans_getAll_url     = base + "/transactions";
    public static String trans_get_url        = base + "/transactions/{id}";
    public static String trans_getByAccID_url = base + "/transactions/account/{accountId}";
    public static String trans_getByRef_url   = base + "/transactions/reference/{reference}";
}

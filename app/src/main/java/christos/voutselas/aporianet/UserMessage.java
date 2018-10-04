package christos.voutselas.aporianet;

public class UserMessage
{
    public String user;
    public String uid;
    public String deviceToken;

    public UserMessage()
    {

    }


    public UserMessage(String user, String uid, String deviceToken)
    {
        this.user = user;
        this.uid = uid;
        this.deviceToken = deviceToken;

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}

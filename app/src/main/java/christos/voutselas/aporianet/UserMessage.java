package christos.voutselas.aporianet;

public class UserMessage
{
    public String user;
    public String uid;

    public UserMessage()
    {

    }


    public UserMessage(String user, String uid)
    {
        this.user = user;
        this.uid = uid;

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
}

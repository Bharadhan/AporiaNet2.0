package christos.voutselas.aporianet;

public class FriendlyMessage
{

    public String text;
    private String name;
    private String photoUrl;
    private String subject;
    private String key;
    private Integer potition;
    private String votes;
    public String date;


    public FriendlyMessage()
    {

    }

    public FriendlyMessage(String text, String name, String subject, String photoUrl, String votes, String date)
    {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.subject = subject;
        this.key = key;
        this.potition = potition;
        this.votes = votes;
        this.date = date;

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() { return photoUrl; }

    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public Integer getPotition() {
        return potition;
    }

    public void setPotition(Integer potition) {
        this.potition = potition;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

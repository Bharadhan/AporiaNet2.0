package christos.voutselas.aporianet;

public class FriendlyMessage
{

    public String text;
    private String name;
    private String photoUrl;
    private String subject;

    public FriendlyMessage()
    {

    }

    public FriendlyMessage(String text, String name, String subject, String photoUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.subject = subject;
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
}

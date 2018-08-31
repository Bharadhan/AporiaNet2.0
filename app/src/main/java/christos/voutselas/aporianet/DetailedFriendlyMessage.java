package christos.voutselas.aporianet;

public class DetailedFriendlyMessage {

    public String text;
    private String name;
    private String photoUrl;
    private String subject;
    private String key;
    private String color;
    private String voted;

    public DetailedFriendlyMessage()
    {

    }

    public DetailedFriendlyMessage(String text, String name, String subject, String key, String photoUrl, String color, String voted)
    {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.subject = subject;
        this.key = key;
        this.color = color;
        this.voted = voted;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVoted() {
        return voted;
    }

    public void setVoted(String voted) {
        this.voted = voted;
    }
}

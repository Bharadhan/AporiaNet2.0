package christos.voutselas.aporianet;

public class DetailedFriendlyMessage {

    public String text;
    private String name;
    private String photoUrl;
    private String subject;
    private String key;
    private String userAnswer;
    private String answername;

    public DetailedFriendlyMessage()
    {

    }

    public DetailedFriendlyMessage(String text, String name, String subject, String key, String userAnswer, String answerName, String photoUrl)
    {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.subject = subject;
        this.key = key;
        this.userAnswer = userAnswer;
        this.answername = answerName;
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

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getAnswername() {
        return answername;
    }

    public void setAnswername(String answername) {
        this.answername = answername;
    }
}

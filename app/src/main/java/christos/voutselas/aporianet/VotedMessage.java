package christos.voutselas.aporianet;

public class VotedMessage {

    public String name;

    public VotedMessage()
    {

    }

    public VotedMessage(String name)
    {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

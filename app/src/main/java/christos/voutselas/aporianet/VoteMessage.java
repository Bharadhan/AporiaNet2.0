package christos.voutselas.aporianet;

public class VoteMessage
{
    public String  votesNumbres;

    public VoteMessage()
    {

    }

    public VoteMessage(String votesNumbres)
    {
        this.votesNumbres = votesNumbres;

    }

    public String getVotesNumbres() {
        return votesNumbres;
    }

    public void setVotesNumbres(String votesNumbres) {
        this.votesNumbres = votesNumbres;
    }
}

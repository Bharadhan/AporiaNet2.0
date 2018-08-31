package christos.voutselas.aporianet;

public class VoteMessage
{
    public Integer votesNumbres;

    public VoteMessage()
    {

    }

    public VoteMessage(Integer votesNumbres)
    {
        this.votesNumbres = votesNumbres;

    }

    public Integer getVotesNumbres() {
        return votesNumbres;
    }

    public void setVotesNumbres(Integer votesNumbres) {
        this.votesNumbres = votesNumbres;
    }
}

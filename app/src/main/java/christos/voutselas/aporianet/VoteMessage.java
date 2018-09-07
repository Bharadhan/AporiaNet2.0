package christos.voutselas.aporianet;

public class VoteMessage
{
    public Long  votesNumbres;

    public VoteMessage()
    {

    }

    public VoteMessage(Long votesNumbres)
    {
        this.votesNumbres = votesNumbres;

    }

    public Long getVotesNumbres() {
        return votesNumbres;
    }

    public void setVotesNumbres(Long votesNumbres) {
        this.votesNumbres = votesNumbres;
    }
}

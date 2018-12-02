public class State {
    private Team myTeam;
    private Team opponentsTeam;

    // 0 = Ongoing game
    // 1 = AI wins
    // -1 = Opponent wins
    private int winState;

    // The current Pokemon out on the field on each side
    private Pokemon myLead;
    private Pokemon opponentsLead;

    public State(Team myTeam, Team opponentsTeam, Pokemon myLead, Pokemon opponentsLead){
        this.myTeam = myTeam;
        this.opponentsTeam = opponentsTeam;
        this.myLead = myLead;
        this.opponentsLead = opponentsLead;
        setWinState();
    } // Constructor

    public State(State src){
        this.myTeam = src.myTeam;
        this.opponentsTeam = src.opponentsTeam;
        this.myLead = src.myLead;
        this.opponentsLead = src.opponentsLead;
        this.winState = src.winState;
    } // Copy constructor

    //////////////////////////////////////////////////////////////////////
    //Accessors
    //////////////////////////////////////////////////////////////////////

    public int myPokemonFainted(){ return myTeam.numFainted(); }

    public int opponentsPokemonFainted(){ return opponentsTeam.numFainted(); }

    public int myTeamSize(){ return myTeam.teamSize(); }

    public int opponentsTeamSize(){ return opponentsTeam.teamSize(); }

    public double myPercentageFainted(){ return (double)(myTeam.numFainted()/myTeam.teamSize()); }

    public double opponentsPercentageFainted(){ return (double)(opponentsTeam.numFainted()/opponentsTeam.teamSize()); }

    public int getWinState(){ return winState; }

    public Pokemon getMyLead(){ return myLead; }

    public Pokemon getOpponentsLead(){ return opponentsLead; }

    public Team getMyTeam(){ return myTeam; }

    public Team getOpponentsTeam(){ return opponentsTeam; }

    public void swapMyLead(Pokemon newLead) { myLead = newLead; }

    public void swapOpponentsLead(Pokemon newLead) { opponentsLead = newLead; }

    //////////////////////////////////////////////////////////////////////
    //Mutators
    //////////////////////////////////////////////////////////////////////

    public void setWinState(){
        if(myTeam.allFainted())
            winState = -1;
        else if(opponentsTeam.allFainted())
            winState = 1;
        else
            winState = 0;
    } // setWinState
}

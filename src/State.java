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
        this.myTeam = new Team(src.myTeam);
        this.opponentsTeam = new Team(src.opponentsTeam);
        this.myLead = new Pokemon(src.myLead);
        this.opponentsLead = new Pokemon(src.opponentsLead);
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

    // Return the index that the AI's lead Pokemon is in
    public int myLeadIndex(){
        int index = 0;
        for(int i = 0; i < myTeam.teamSize(); i++){
            if(myTeam.atSlot(i).getSpecies().equals(myLead.getSpecies()))
                index = i;
        } // for

        return index;
    } // myLeadIndex

    // Return the index that the opponent's lead Pokemon is in
    public int opponentLeadIndex(){
        int index = 0;

        for(int i = 0; i < opponentsTeam.teamSize(); i++){
            if(opponentsTeam.atSlot(i).getSpecies().equals(opponentsLead.getSpecies()))
                index = i;
        } // for

        return index;
    } // myLeadIndex

    public Team getMyTeam(){ return myTeam; }

    public Team getOpponentsTeam(){ return opponentsTeam; }

    public void swapMyLead(Pokemon newLead) { myLead = newLead; }

    public void swapOpponentsLead(Pokemon newLead) { opponentsLead = newLead; }

    public boolean allMyPokemonFainted(){
        boolean fainted = false;
        if(myTeam.allFainted())
            fainted = true;

        return fainted;
    } // allMyPokemonFainted

    public boolean allOppsPokemonFainted(){
        boolean fainted = false;
        if(opponentsTeam.allFainted())
            fainted = true;

        return fainted;
    } // allOppsPokemonFainted

    //////////////////////////////////////////////////////////////////////
    //Mutators
    //////////////////////////////////////////////////////////////////////

    public void setMyLead(String newLead){
        // Find the Pokemon in the linked list first
        myLead = myTeam.findPokemon(newLead);
    } // setMyLead

    public void setOpponentsLead(String newLead){
        // Find the Pokemon in the linked list first
        opponentsLead = opponentsTeam.findPokemon(newLead);
    } // setMyLead

    public void setWinState(){
        if(myTeam.allFainted())
            winState = -1;
        else if(opponentsTeam.allFainted())
            winState = 1;
        else
            winState = 0;
    } // setWinState

    public boolean checkFainted(boolean aiTeam, int slot){
        boolean fainted = false;

        if(aiTeam)
            fainted = myTeam.atSlot(slot).isFainted();
        else
            fainted = opponentsTeam.atSlot(slot).isFainted();

        return fainted;
    }
}

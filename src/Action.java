// Represents the actions taken on a turn
// Set the appropriate boolean, Move and String when an action is meant to be taken
// Default on construction is no action

public class Action { // lawsuit
    private boolean aiMoving;
    private boolean opponentMoving;
    private boolean aiSwitching;
    private boolean opponentSwitching;
    private boolean aiFainted;
    private boolean opponentFainted;

    private String aiMoveName;
    private String opponentMoveName;
    private Move aiMove;
    private Move opponentMove;
    private String aiSwitchingName;
    private String opponentSwitchingName;

    public Action(){
        aiMoving = false;
        opponentMoving = false;
        aiSwitching = false;
        opponentSwitching = false;
        aiFainted = false;
        opponentFainted = false;

        aiMove = null;
        opponentMove = null;
        aiSwitchingName = "";
        opponentSwitchingName = "";
    } // Constructor

    public Action(Action src){
        this.aiMoving = src.aiMoving;
        this.opponentMoving = src.opponentMoving;
        this.aiSwitching = src.aiSwitching;
        this.opponentSwitching = src.opponentSwitching;
        this.aiFainted = src.aiFainted;
        this.opponentFainted = src.opponentFainted;

        this.aiMove = new Move(src.aiMove);
        this.opponentMove = new Move(src.opponentMove);
        this.aiSwitchingName = src.aiSwitchingName;
        this.opponentSwitchingName = src.opponentSwitchingName;
    } // Constructor

    /////////////////////////////////////
    // Accessors
    /////////////////////////////////////

    public boolean isAIMoving() {
        return aiMoving;
    }

    public boolean isOpponentMoving() {
        return opponentMoving;
    }

    public boolean isAISwitching() {
        return aiSwitching;
    }

    public boolean isOpponentSwitching() {
        return opponentSwitching;
    }

    public boolean isAIFainted() {
        return aiFainted;
    }

    public boolean isOpponentFainted() {
        return opponentFainted;
    }

    public String getAIMoveName() {
        return aiMoveName;
    }

    public String getOpponentMoveName() {
        return opponentMoveName;
    }

    public Move getAIMove() {
        return aiMove;
    }

    public Move getOpponentMove() {
        return opponentMove;
    }

    public String getAISwitchingName() {
        return aiSwitchingName;
    }

    public String getOpponentSwitchingName() {
        return opponentSwitchingName;
    }

    /////////////////////////////////////
    // Mutators
    /////////////////////////////////////


    public void setAIMove(Move move) {
        aiMoving = true;
        aiMove = move;
    }

    public void setOpponentMove(Move move){
        opponentMoving = true;
        opponentMove = move;
    }

    public void setAISwitch(String pokemonName){
        aiSwitching = true;
        aiSwitchingName = pokemonName;
    }

    public void setOpponentSwitch(String pokemonName){
        opponentSwitching = true;
        opponentSwitchingName = pokemonName;
    }

    public void setAIFainted(){
        aiFainted = true;
    }

    public void setOpponentFainted() {
        opponentFainted = true;
    }

} // Action

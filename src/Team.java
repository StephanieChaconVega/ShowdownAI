import java.util.LinkedList;

public class Team {

    // Holds the Pokemon in the team
    LinkedList<Pokemon> team;

    public Team(Pokemon slot1, Pokemon slot2, Pokemon slot3, Pokemon slot4, Pokemon slot5, Pokemon slot6){

        // Populate the team
        if(slot1 != null)
            team.add(slot1);

        if(slot2 != null)
            team.add(slot2);

        if(slot3 != null)
            team.add(slot3);

        if(slot4 != null)
            team.add(slot4);

        if(slot5 != null)
            team.add(slot5);

        if(slot6 != null)
            team.add(slot6);
    } // Constructor

    public Pokemon atSlot(int index){
        Pokemon found = null;

        if(index < team.size())
            found = team.get(index);

        return found;
    } // atSlot

    public boolean allFainted(){
        boolean fainted = true;

        for(int i = 0; i < team.size(); i++){
            if(!team.get(i).isFainted())
                fainted = false;
        } // for

        return fainted;
    } // allFainted

    public int numFainted(){
        int fainted = 0;

        for(int i = 0; i < team.size(); i++){
            if(!team.get(i).isFainted())
                fainted++;
        } // for

        return fainted;
    } // numFainted

    public int teamSize(){ return team.size(); }

}// Team

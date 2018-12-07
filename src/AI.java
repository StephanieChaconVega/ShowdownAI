import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import org.openqa.selenium.firefox.FirefoxDriver;

import scala.testing.Show;
import seleniumhelper.ShowdownHelper;
import seleniumhelper.ShowdownHelper.TurnEndStatus;
import seleniumhelper.loginterpret.*;
import seleniumhelper.loginterpret.events.TIEvent;

public class AI {

    public static void main(String[] args) throws Exception{
        // The SFirefoxDriver and ShowdownHelper, to view the current state and perform actions
        FirefoxDriver driver = new FirefoxDriver();
        ShowdownHelper showdown = new ShowdownHelper(driver);

        // The AI is given a preset team.
        // This is to keep tests consistent, and avoid coding for too many edge cases
        // More cases will be covered in later implementations
        String myTeamImportable = "Sharpedo (M)  \n" +
                "Ability: Rough Skin  \n" +
                "EVs: 252 Atk / 4 SpD / 252 Spe  \n" +
                "Jolly Nature  \n" +
                "- Crunch  \n" +
                "- Liquidation  \n" +
                "- Poison Fang  \n" +
                "- Ice Fang  \n" +
                "\n" +
                "Celebi  \n" +
                "Ability: Natural Cure  \n" +
                "EVs: 252 SpA / 4 SpD / 252 Spe  \n" +
                "Timid Nature  \n" +
                "IVs: 0 Atk  \n" +
                "- Dazzling Gleam  \n" +
                "- Psychic  \n" +
                "- Energy Ball  \n" +
                "- Shadow Ball  \n" +
                "\n" +
                "Marowak-Alola (M)  \n" +
                "Ability: Rock Head  \n" +
                "EVs: 248 HP / 252 Atk / 8 SpD  \n" +
                "Adamant Nature  \n" +
                "- Fire Punch  \n" +
                "- Iron Head  \n" +
                "- Thunder Punch  \n" +
                "- Shadow Bone  ";

        /////////////////////////////////////
        // Creating Moves, Pokemon and Teams
        /////////////////////////////////////

        // Because this is meant to be a "toy problem" version of a Pokemon battle,
        // there is no database of Pokemon or their moves.
        // As a result, the values will all be hard coded
        // This will be changed in a later implementation

        Move crunch = new Move("Crunch", 24, 80, Type.DARK, Category.PHYSICAL, false, 100, 1);
        Move liquidation = new Move("Liquidation", 16, 85, Type.WATER, Category.PHYSICAL, false, 100, 1);
        Move poisonFang = new Move("Poison Fang", 24, 50, Type.POISON, Category.PHYSICAL, false, 100, 1);
        Move iceFang = new Move("Ice Fang", 24, 65, Type.ICE, Category.PHYSICAL, false, 95, 1);

        Pokemon sharpedo = new Pokemon("Sharpedo", crunch, liquidation, poisonFang, iceFang,
                Type.WATER, Type.DARK, 'M', "");
        sharpedo.setStats(281, 281, 339, 116, 203, 117, 317);

        Move dazzlingGleam = new Move("Dazzling Gleam", 16, 80, Type.FAIRY, Category.SPECIAL, false, 100, 1);
        Move psychic = new Move("Psychic", 16, 90, Type.PSYCHIC, Category.SPECIAL, false, 100, 1);
        Move energyBall = new Move("Energy Ball", 16, 90, Type.GRASS, Category.SPECIAL, false, 100, 1);
        Move shadowBall = new Move("Shadow Ball", 24, 80, Type.GHOST, Category.SPECIAL, false, 100, 1);

        Pokemon celebi = new Pokemon("Celebi", dazzlingGleam, psychic, energyBall, shadowBall,
                Type.PSYCHIC, Type.GRASS, '-', "");
        celebi.setStats(341, 341, 184, 236, 299, 237, 328);

        Move firePunch = new Move("Fire Punch", 24,75, Type.FIRE, Category.PHYSICAL, false, 100, 1);
        Move ironHead = new Move("Iron Head", 24, 80, Type.STEEL, Category.PHYSICAL, false, 100, 1);
        Move thunderPunch = new Move("Thunder Punch" , 24, 75, Type.ELECTRIC, Category.PHYSICAL, false, 100, 1);
        Move shadowBone = new Move("Shadow Bone", 16, 85, Type.GHOST, Category.PHYSICAL, false, 100, 1);

        Pokemon marowak = new Pokemon("Marowak-Alola", firePunch, ironHead, thunderPunch, shadowBone,
                Type.FIRE, Type.GHOST, 'M', "");
        marowak.setStats(323, 323, 284, 256, 122, 198, 126);

        Team myTeam = new Team(sharpedo, celebi, marowak, null, null, null);

        // The opponent should also use a preset team
        // Normally, the AI would not have this much information about the opponent
        // But for the purposes of this assignment, this "cheat sheet" is acceptable

        Move scald = new Move("Scald", 24, 80, Type.WATER, Category.SPECIAL, false, 100, 1);
        // psychic already defined
        Move iceBeam = new Move("Ice Beam",16, 90, Type.ICE, Category.SPECIAL, false, 100, 1);
        // energyBall already defined

        Pokemon manaphy = new Pokemon("Manaphy", scald, psychic, iceBeam, energyBall,
                Type.WATER, null, '-', "");
        manaphy.setStats(341, 341, 184, 236, 299, 237, 328);

        Move gigaDrain = new Move("Giga Drain", 16, 75, Type.GRASS, Category.SPECIAL, true, 100, 1);
        Move sludgeBomb = new Move("Sludge Bomb", 16, 90, Type.POISON, Category.SPECIAL, false, 100, 1);
        // shadowBall already defined
        // dazzlingGleam already defined

        Pokemon roserade = new Pokemon("Roserade", gigaDrain, sludgeBomb, shadowBall, dazzlingGleam,
                Type.GRASS, Type.POISON, 'F', "");
        roserade.setStats(324, 324, 130, 166, 383, 247, 216);

        Move flamethrower = new Move("Flamethrower", 24, 90, Type.FIRE, Category.SPECIAL, false, 100, 1);
        Move darkPulse = new Move("Dark Pulse", 24, 80, Type.DARK, Category.SPECIAL, false, 100, 1);
        // shadowBall already defined
        // psychic already defined

        Pokemon chandelure = new Pokemon("Chandelure", flamethrower, darkPulse, shadowBall, psychic,
                Type.GHOST, Type.FIRE, 'M', "");
        chandelure.setStats(323, 323, 103, 216, 427, 218, 196);

        Team opponentsTeam = new Team(manaphy, roserade, chandelure, null, null, null);

        try {

            State initialState = startBattle(showdown, myTeamImportable, myTeam, opponentsTeam);
            System.out.println("Initial state created successfully"); /////////////////////////////////// Debugging

            selectMove(showdown, initialState.getMyLead().getMoves()[0]); // make sure moves are working

        }catch(Exception e){
            System.out.println("An error has occurred");
        }

    } // Main

    private static State startBattle(ShowdownHelper showdown, String importableTeam, Team myTeam, Team opponentsTeam) throws Exception{
        State initialState = null;

        showdown.open();
        showdown.login("Stephanie's AI", "");
        showdown.createTeam(importableTeam, "AI Team");
        showdown.challengeUser("Ubers", "AI Team", "sarccatto");

        // WAIT FOR BATTLE START
        TurnEndStatus startStatus = showdown.waitForBattleStart();
        if (startStatus == TurnEndStatus.SWITCH) {
            // Choose a random Pokemon to start with (slots 0 - 2)
            //showdown.switchTo(randomInt(new Random(), 0, 2));
            showdown.switchTo(0);// for testing
            showdown.waitForNextTurn(0);
        }

        try{
            String ai = showdown.getUserName();
            String opponent = showdown.getOpponentName();

            System.out.println("A battle between " + ai + " and " + opponent + " has started!");

            // Get the opponent's Pokemon
            String opponentLead = showdown.getCurrentOpponentPokemon(false);
            // Check our Pokemon
            String aiLead = showdown.getCurrentPokemon(false);

            // Find the corresponding Pokemon objects in each team
            Pokemon opponentLeadPokemon = opponentsTeam.findPokemon(opponentLead);
            Pokemon aiLeadPokemon = myTeam.findPokemon(aiLead);

            // Create the initial state
            initialState = new State(myTeam, opponentsTeam, aiLeadPokemon, opponentLeadPokemon);

        }catch(Exception e){
            System.out.println("An error has occurred");
        }

        return initialState;
    } // startBattle

    // The two ints should be within the range 1-6
    // There is a max of 6 possible actions either player can perform at one turn
    // Use moves 1-4, switch to ally 1 or switch to ally 2
    // If one or both of the selected actions are invalid, return null
    public Action generateAction(int aiActionNum, int opponentActionNum){
        Action newAction = new Action();



        return newAction;
    } // generateAction

    public Stack<State> generateAllStates(State initial){
        Stack<State> stateStack = new Stack<>();



        return stateStack;
    } // generateAllStates

    // Simulate the results of given Actions performed on each turn
    // These actions can be using a move or switching out the active Pokemon
    // Create a new state with the result of this turn and return it
    private static State simulateTurn(State initial, Action turnAction){
        State newState = new State(initial);

        // AI is fainted, opponent is not
        if(turnAction.isAIFainted() && !turnAction.isOpponentFainted()){
            newState = simulateSwitch(newState, true, turnAction.getAISwitchingName());
        }

        // Opponent is fainted, AI is not
        else if(turnAction.isOpponentFainted() && !turnAction.isAIFainted()){
            newState = simulateSwitch(newState, false, turnAction.getOpponentSwitchingName());
        }

        // Both sides are fainted
        else if(turnAction.isAIFainted() && turnAction.isOpponentFainted()){
            newState = simulateSwitch(newState, true, turnAction.getAISwitchingName());
            newState = simulateSwitch(newState, false, turnAction.getOpponentSwitchingName());
        }

        // Both sides use a move
        else if(turnAction.isAIMoving() && turnAction.isOpponentMoving()){
            // Find who goes first (is our Pokemon faster?)
            boolean aiFirst = (newState.getMyLead().getSpeed() > newState.getOpponentsLead().getSpeed());
            if(aiFirst){
                // Perform the moves in the correct order
                newState = simulateMove(newState, true, turnAction.getAIMove());
                newState = simulateMove(newState, false, turnAction.getOpponentMove());
            }else{
                newState = simulateMove(newState, false, turnAction.getOpponentMove());
                newState = simulateMove(newState, true, turnAction.getAIMove());
            } // if/else
        }

        // AI uses a move, opponent switches out
        else if(turnAction.isAIMoving() && turnAction.isOpponentSwitching()){
            newState = simulateSwitch(newState, false, turnAction.getOpponentSwitchingName());
            newState = simulateMove(newState, true, turnAction.getAIMove());
        }

        // AI switches out, opponent uses a move
        else if(turnAction.isAISwitching() && turnAction.isOpponentMoving()){
            newState = simulateSwitch(newState, true, turnAction.getAISwitchingName());
            newState = simulateMove(newState, false, turnAction.getOpponentMove());
        }

        // Both sides switch out
        else if(turnAction.isAISwitching() && turnAction.isOpponentSwitching()){
            newState = simulateSwitch(newState, true, turnAction.getAISwitchingName());
            newState = simulateSwitch(newState, false, turnAction.getOpponentSwitchingName());
        }

        return newState;
    } // simulateTurn


    private static State simulateMove(State state, boolean aiMove, Move move){
        if(aiMove){
            if(!state.getOpponentsLead().isFainted()) {
                // AI's lead Pokemon deals damage to opponent's Pokemon
                double damage = state.getOpponentsLead().takeDamage(move, state.getMyLead());

                // Pokemon faints once its HP drops below 0
                if (state.getOpponentsLead().getCurrHP() <= 0)
                    state.getOpponentsLead().faint();

                // Check if this move heals
                if (move.isHealing() && (move.getCategory() == Category.SPECIAL || move.getCategory() == Category.PHYSICAL)) {
                    // if this move also deals damage, it heals by 50% of the damage dealt
                    state.getMyLead().heal(damage / 2);
                } // if
            } // if

            // Regular healing moves are performed if the opponent is fainted or not
            // They heal 50% of max HP
            if(move.isHealing() && move.getCategory() == Category.STATUS)
                state.getMyLead().heal(state.getMyLead().getMaxHP() / 2);

        }else{ // if it's the opponent's move...
            if(!state.getMyLead().isFainted()) {
                // Opponent's lead Pokemon deals damage to AI's Pokemon
                double damage = state.getMyLead().takeDamage(move, state.getOpponentsLead());

                // Pokemon faints once its HP drops below 0
                if (state.getMyLead().getCurrHP() <= 0)
                    state.getMyLead().faint();

                // Check if this move heals
                if (move.isHealing() && (move.getCategory() == Category.SPECIAL || move.getCategory() == Category.PHYSICAL)) {
                    // if this move also deals damage, it heals by 50% of the damage dealt
                    state.getOpponentsLead().heal(damage / 2);
                } // if
            } // if

            // Regular healing moves are performed if the opponent is fainted or not
            // They heal 50% of max HP
            if(move.isHealing() && move.getCategory() == Category.STATUS)
                state.getOpponentsLead().heal(state.getOpponentsLead().getMaxHP() / 2);

        } // if/else

        return state;
    } // simulateMove

    private static State simulateSwitch(State state, boolean aiSwitch, String switchName){
        if(aiSwitch){
            state.setMyLead(switchName);
        }else{
            state.setOpponentsLead(switchName);
        } // if/else

        return state;
    } // simulateSwitch

    private static void selectMove(ShowdownHelper showdown, Move move) throws Exception{
        showdown.doMove(move.getName());
    }


    private static int randomInt(Random random, int low, int high) {
        return random.nextInt(high) + low;
    }

}
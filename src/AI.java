import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.openqa.selenium.firefox.FirefoxDriver;

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

        Move crunch = new Move(24, 80, Type.DARK, Category.PHYSICAL, false, 100, 1);
        Move liquidation = new Move(16, 85, Type.WATER, Category.PHYSICAL, false, 100, 1);
        Move poisonFang = new Move(24, 50, Type.POISON, Category.PHYSICAL, false, 100, 1);
        Move iceFang = new Move(24, 65, Type.ICE, Category.PHYSICAL, false, 95, 1);

        Pokemon sharpedo = new Pokemon("Sharpedo", crunch, liquidation, poisonFang, iceFang,
                Type.WATER, Type.DARK, 'M', "");
        sharpedo.setStats(281, 339, 116, 203, 117, 317);

        Move dazzlingGleam = new Move(16, 80, Type.FAIRY, Category.SPECIAL, false, 100, 1);
        Move psychic = new Move(16, 90, Type.PSYCHIC, Category.SPECIAL, false, 100, 1);
        Move energyBall = new Move(16, 90, Type.GRASS, Category.SPECIAL, false, 100, 1);
        Move shadowBall = new Move(24, 80, Type.GHOST, Category.SPECIAL, false, 100, 1);

        Pokemon celebi = new Pokemon("Celebi", dazzlingGleam, psychic, energyBall, shadowBall,
                Type.PSYCHIC, Type.GRASS, '-', "");
        celebi.setStats(341, 184, 236, 299, 237, 328);

        Move firePunch = new Move(24,75, Type.FIRE, Category.PHYSICAL, false, 100, 1);
        Move ironHead = new Move(24, 80, Type.STEEL, Category.PHYSICAL, false, 100, 1);
        Move thunderPunch = new Move(24, 75, Type.ELECTRIC, Category.PHYSICAL, false, 100, 1);
        Move shadowBone = new Move(16, 85, Type.GHOST, Category.PHYSICAL, false, 100, 1);

        Pokemon marowak = new Pokemon("Marowak-Alola", firePunch, ironHead, thunderPunch, shadowBone,
                Type.FIRE, Type.GHOST, 'M', "");
        marowak.setStats(323, 284, 256, 122, 198, 126);

        Team myTeam = new Team(sharpedo, celebi, marowak, null, null, null);

        // The opponent should also use a preset team
        // Normally, the AI would not have this much information about the opponent
        // But for the purposes of this assignment, this "cheat sheet" is acceptable

        Move scald = new Move(24, 80, Type.WATER, Category.SPECIAL, false, 100, 1);
        // psychic already defined
        Move iceBeam = new Move(16, 90, Type.ICE, Category.SPECIAL, false, 100, 1);
        // energyBall already defined

        Pokemon manaphy = new Pokemon("Manaphy", scald, psychic, iceBeam, energyBall,
                Type.WATER, null, '-', "");
        manaphy.setStats(341, 184, 236, 299, 237, 328);

        Move gigaDrain = new Move(16, 75, Type.GRASS, Category.SPECIAL, true, 100, 1);
        Move sludgeBomb = new Move(16, 90, Type.POISON, Category.SPECIAL, false, 100, 1);
        // shadowBall already defined
        // dazzlingGleam already defined

        Pokemon roserade = new Pokemon("Roserade", gigaDrain, sludgeBomb, shadowBall, dazzlingGleam,
                Type.GRASS, Type.POISON, 'F', "");
        roserade.setStats(324, 130, 166, 383, 247, 216);

        Move flamethrower = new Move(24, 90, Type.FIRE, Category.SPECIAL, false, 100, 1);
        Move darkPulse = new Move(24, 80, Type.DARK, Category.SPECIAL, false, 100, 1);
        // shadowBall already defined
        // psychic already defined

        Pokemon chandelure = new Pokemon("Chandelure", flamethrower, darkPulse, shadowBall, psychic,
                Type.GHOST, Type.FIRE, 'M', "");
        chandelure.setStats(323, 103, 216, 427, 218, 196);

        Team opponentsTeam = new Team(manaphy, roserade, chandelure, null, null, null);

        try {

            State initialState = startBattle(showdown, myTeamImportable, myTeam, opponentsTeam);
            System.out.println("Initial state created successfully"); /////////////////////////////////// Debugging

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
            showdown.switchTo(randomInt(new Random(), 0, 2));
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


    private static int randomInt(Random random, int low, int high) {
        return random.nextInt(high) + low;
    } // randomInt

}
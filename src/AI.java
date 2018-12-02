import scala.testing.Show;
import seleniumhelper.ShowdownHelper;

public class AI {

    public static void main(String[] args) {
        // The ShowdownHelper, to view the current state
        ShowdownHelper showdown;

        // The AI is given a preset team.
        // This is to keep tests consistent, and avoid coding for too many edge cases
        // More cases will be covered in later implementations
        String myTeamImportable = "Mew  \n" +
                "Ability: Synchronize  \n" +
                "EVs: 252 SpA / 4 SpD / 252 Spe  \n" +
                "Timid Nature  \n" +
                "IVs: 0 Atk  \n" +
                "- Dazzling Gleam  \n" +
                "- Psychic  \n" +
                "- Thunderbolt  \n" +
                "- Signal Beam  \n" +
                "\n" +
                "Reshiram  \n" +
                "Ability: Turboblaze  \n" +
                "EVs: 248 HP / 252 SpA / 8 SpD  \n" +
                "Modest Nature  \n" +
                "IVs: 0 Atk  \n" +
                "- Earth Power  \n" +
                "- Hyper Voice  \n" +
                "- Shadow Ball  \n" +
                "- Flamethrower  \n" +
                "\n" +
                "Tapu Koko  \n" +
                "Ability: Electric Surge  \n" +
                "EVs: 252 SpA / 4 SpD / 252 Spe  \n" +
                "Timid Nature  \n" +
                "IVs: 0 Atk  \n" +
                "- Discharge  \n" +
                "- Dazzling Gleam  \n" +
                "- Hyper Voice  \n" +
                "- Roost  ";

        /////////////////////////////////////
        // Creating Moves, Pokemon and Teams
        /////////////////////////////////////

        // Because this is meant to be a "toy problem" version of a Pokemon battle,
        // there is no database of Pokemon or their moves.
        // As a result, the values will all be hard coded
        // This will be changed in a later implementation

        Move dazzlingGleam = new Move(16, 80, Type.FAIRY, Category.SPECIAL, false, 100, 1);
        Move psychic = new Move(16, 90, Type.PSYCHIC, Category.SPECIAL, false, 100, 1);
        Move thunderbolt = new Move(24, 90, Type.ELECTRIC, Category.SPECIAL, false, 100, 1);
        Move signalBeam = new Move(24, 75, Type.BUG, Category.SPECIAL, false, 100, 1);

        Pokemon mew = new Pokemon("Mew", dazzlingGleam, psychic, thunderbolt, signalBeam,
                Type.PSYCHIC, null, '-', "");

        Move earthPower = new Move(16, 90, Type.GROUND, Category.SPECIAL, false, 100, 1);
        Move hyperVoice = new Move(16, 90, Type.NORMAL, Category.SPECIAL, false, 100, 1);
        Move shadowBall = new Move(24, 80, Type.GHOST, Category.SPECIAL, false, 100, 1);
        Move flamethrower = new Move(24, 90, Type.FIRE, Category.SPECIAL, false, 100, 1);

        Pokemon reshiram = new Pokemon("Reshiram", earthPower, hyperVoice, shadowBall, flamethrower,
                Type.DRAGON, Type.FIRE, '-', "");

        Move discharge = new Move(24,80, Type.ELECTRIC, Category.SPECIAL, false,100, 1);
        // dazzlingGleam already defined
        // hyperVoice already defined
        Move roost = new Move(16, 0, Type.FLYING, Category.STATUS, true, 100, 1);

        Pokemon tapuKoko = new Pokemon("Tapu Koko", discharge, dazzlingGleam, hyperVoice, roost,
                Type.ELECTRIC, Type.FAIRY, '-', "");

        Team myTeam = new Team(mew, reshiram, tapuKoko, null, null, null);

        // The opponent should also use a preset team
        // Normally, the AI would not have this much information about the opponent
        // But for the purposes of this assignment, this "cheat sheet" is acceptable

    } // Main
}
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

public class Example {
    public static void main(String[] args) throws Exception {
        //testBattleLogFile();
        testBattle();
        //benchmark();

    }

    public static void testBattle() throws Exception {
        //System.setProperty("webdriver.firefox.profile", "default");
        FirefoxDriver driver = new FirefoxDriver();
        // wait up to 10 seconds for elements to load
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        ShowdownHelper showdown = new ShowdownHelper(driver);
        showdown.open();
        //String[] userPass = loadUserPass();
        //showdown.sleep(60000);
        showdown.login("geniusecttest" + (new Random()).nextInt(100000), "");
        showdown.createTeam("Shuckle @ Rocky Helmet\nTrait: Sturdy\nEVs: 252 SDef / 252 HP / 4 Atk\nCareful Nature\n- Acupressure\n- Power Split\n- Rest\n- Rollout", "t");
        showdown.challengeUser("Ubers", "t", "sarccatto");

        // WAIT FOR BATTLE START
        TurnEndStatus startStatus = showdown.waitForBattleStart();
        if (startStatus == TurnEndStatus.SWITCH) {
            showdown.switchTo(0);
            showdown.waitForNextTurn(0);
        }
        try {
            String SELF = showdown.getUserName();
            String OPP = showdown.getOpponentName();

            System.out.println("My name is " + SELF + ", and I just started a battle.");
            System.out.println("This is my team. There is none like it-");
            List<String> ourTeam = showdown.getTeam(SELF);
            printlist(ourTeam);
            System.out.println();
            System.out.println("My hapless opponent is " + OPP + ", and this is his team; or what I know of it:");
            List<String> team = showdown.getTeam(OPP);
            printlist(team);
            System.out.println();

            System.out.println("Current turn: " + showdown.getBattleLog().getCurrentTurn());
            System.out.println("-Current turn---------------");
            System.out.println(showdown.getBattleLog().getCurrentTurnText());
            System.out.println("-Last turn----------");
            System.out.println(showdown.getBattleLog().getLastTurnText());
            System.out.println("----------------");
            System.out.println("Opponent's Pokemon: " + showdown.getCurrentPokemon(OPP, false));

            System.out.println("Moves:");
            String poke = showdown.getCurrentPokemon(true);
            printlist(showdown.getMoves());
            for (String move : showdown.getMoves()) {
                System.out.println(move + ": " + showdown.getMoveRemainingPP(move) + " PP");
            }
            System.out.println("Gender: '" + showdown.getGender(poke, SELF) + "'");
            System.out.println("Ability: " + showdown.getAbility(poke, SELF));
            System.out.println("Item: " + showdown.getItem(poke, SELF));
            System.out.println("Their Ability: " + showdown.getAbility(showdown.getCurrentPokemon(OPP, true), OPP));

            System.out.println("Format: " + showdown.getBattleLog().getFormat() + "\nClauses: ");
            printlist(showdown.getBattleLog().getClauses());

            TurnEndStatus s = TurnEndStatus.UNKNOWN;
            while (s != TurnEndStatus.WON && s != TurnEndStatus.LOST) {
                if (showdown.getBattleLog().contains("gsquit", false)) {
                    break;
                }
                ourTeam = showdown.getSwitchableTeam();
                String switchingTo = "No one";
                if (ourTeam.size() > 0) {
                    switchingTo = ourTeam.get((new Random()).nextInt(ourTeam.size()));
                    System.out.println("Switching to " + switchingTo);
                    showdown.switchTo(switchingTo, false);
                } else {
                    showdown.doMove(showdown.getUsableMoves().get(0));
                }

                s = showdown.waitForNextTurn(10);
                System.err.println(s);

                System.out.println("Current Pokemon now (should be " + switchingTo + "): " + showdown.getCurrentPokemon(false));
            }
        } finally {
            dumplogfile(showdown);
        }
        showdown.leaveBattle();
    }

    public static String[] loadUserPass() throws FileNotFoundException {
        String[] ret = new String[2];
        Scanner s = new Scanner(new File("bin/account.txt"));
        ret[0] = s.nextLine();
        ret[1] = s.nextLine();
        return ret;
    }

    public static <T> void printlist(List<T> l) {
        for (int i = 0; i < l.size(); ++i) {
            System.out.print(l.get(i).toString());
            if (i != l.size() - 1)
                System.out.print(", ");
        }
        System.out.println();
    }

    public static void dumplogfile(ShowdownHelper showdown) {
        try {
            String url = showdown.getDriver().getCurrentUrl();
            String battleTitle = url.substring(url.lastIndexOf("/") + 1);
            File out = new File(battleTitle + ".log");
            out.createNewFile();
            PrintWriter w = new PrintWriter(out);
            w.write(showdown.getBattleLog().getLogText());
            w.close();
            System.out.println("Dumped log to " + battleTitle + ".log");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void testBattleLogFile() {
        String text = "";
        try {
            Scanner r = new Scanner(new File("battlesample_html.log"));
            while (r.hasNextLine()) {
                text += r.nextLine() + "\n";
            }
        } catch (Exception e) {
            return;
        }
        BattleLog bl = new BattleLog(text);
//      System.out.println(bl.getCurrentPokemonAtTurn("RODAN", 5, false));
//      System.out.println(bl.getCurrentPokemonAtTurn("RODAN", 6, false));
//      System.out.println(bl.getCurrentPokemonAtTurn("Cloak", 6, true));
//      System.out.println(bl.getCurrentPokemonAtTurn("Cloak", 6, false));
//      System.out.println(bl.getCurrentTurn());
//      System.out.println("-------------------");
        TIContext tic = new TIContext();
        tic.foeCurrentPokemon = bl.getCurrentPokemonAtTurn("Cloak", 5, true);
        tic.myCurrentPokemon = bl.getCurrentPokemonAtTurn("RODAN", 5, true);
        TurnInfo ti = new TurnInfo(bl.getTurnHTML(5), tic);

        for(TIEvent event : ti.getEvents()) {
            System.out.println(event);
            System.out.println("-------");
        }
    }

    public static void benchmark() throws Exception {
        FirefoxDriver driver = new FirefoxDriver();
        ShowdownHelper showdown = new ShowdownHelper(driver);
        showdown.open();
        String userName = "geniusecttest" + (new Random()).nextInt(100000);
        showdown.login(userName, "");
        showdown.createTeam("GUI (Goodra) @ Assault Vest\n" +
                "Ability: Sap Sipper  \n" +
                "EVs: 248 HP / 252 SpA / 8 SpD  \n" +
                "Modest Nature  \n" +
                "IVs: 0 Atk  \n" +
                "- Flamethrower  \n" +
                "- Dragon Pulse  \n" +
                "- Ice Beam  \n" +
                "- Sludge Bomb  \n" +
                "\n" +
                "Mienshao @ Life Orb  \n" +
                "Ability: Regenerator  \n" +
                "EVs: 252 Atk / 4 SpD / 252 Spe  \n" +
                "Jolly Nature  \n" +
                "- Acrobatics  \n" +
                "- Drain Punch  \n" +
                "- Rock Slide  \n" +
                "- Fake Out  \n" +
                "\n" +
                "Naboris (Krookodile) (F) @ Darkinium Z  \n" +
                "Ability: Moxie  \n" +
                "EVs: 252 Atk / 4 SpD / 252 Spe  \n" +
                "Jolly Nature  \n" +
                "- Knock Off  \n" +
                "- Earthquake  \n" +
                "- Taunt  \n" +
                "- Stone Edge  \n" +
                "\n" +
                "Rio (Altaria) (M) @ Altarianite  \n" +
                "Ability: Cloud Nine  \n" +
                "EVs: 252 HP / 252 Def / 4 SpD  \n" +
                "Bold Nature  \n" +
                "IVs: 0 Atk  \n" +
                "- Cotton Guard  \n" +
                "- Roost  \n" +
                "- Dragon Pulse  \n" +
                "- Hyper Voice  \n" +
                "\n" +
                "Saria (Celebi) @ Mental Herb  \n" +
                "Ability: Natural Cure  \n" +
                "EVs: 252 HP / 220 SpD / 36 Spe  \n" +
                "Calm Nature  \n" +
                "IVs: 0 Atk  \n" +
                "- Nasty Plot  \n" +
                "- Dazzling Gleam  \n" +
                "- Stealth Rock  \n" +
                "- Giga Drain  \n" +
                "\n" +
                "Ninjask @ Focus Sash  \n" +
                "Ability: Speed Boost  \n" +
                "EVs: 252 Atk / 4 SpD / 252 Spe  \n" +
                "Adamant Nature  \n" +
                "- Night Slash  \n" +
                "- Swords Dance  \n" +
                "- Protect  \n" +
                "- Leech Life  \n" +
                "\n", "t");
        showdown.challengeUser("Ubers", "t", "sarccatto");

        TurnEndStatus startStatus = showdown.waitForBattleStart();
        if (startStatus == TurnEndStatus.SWITCH) {
            showdown.switchTo(0);
            showdown.waitForNextTurn(0);
        }
        long startTime;
        long endTime;
        startTime = System.nanoTime();
        List<String> ourPokes = showdown.getTeam(userName);
        endTime = System.nanoTime();

        System.out.println(showdown.getUserName()); /////////////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println("Loaded Pokemon LIST in " + ((endTime - startTime) / 1000000) + "ms");
        for (int n = 0; n < ourPokes.size(); n++) {
            startTime = System.nanoTime();

            showdown.getMoves(n, true);

            showdown.getPokemonAttributes(n, userName);

            endTime = System.nanoTime();

            System.out.println("Loaded Pokemon " + n + " in " + ((endTime - startTime) / 1000000) + "ms");
        }
    }
}
public class Move {
    // Move details
    private String name;
    private int maxPP;
    private int currPP;
    private double power; // power = 0 if the move is a Status move
    private Type type;
    private Category category;
    private boolean healing;
    private double accuracy;
    // Won't use priority moves in the simple AI, so this will always be set to 1
    private double priority;

    // Chances of secondary effects
    // Might ignore these for this small implementation...
    private double critChance;
    private double burnChance;
    private double paralyzeChance;
    private double freezeChance;
    private double poisonChance;
    private double infatuateChance;
    private double confuseChance;

    public Move(String name, int pp, double power, Type type, Category category, boolean healing, double accuracy, double priority){
        this.name = name;
        maxPP = pp;
        currPP = pp;
        this.power = power;
        this.type = type;
        this.category = category;
        this.healing = healing;
        this.accuracy = accuracy;
        this.priority = priority;

        // Change this if I have time...
        critChance = 0;
        burnChance = 0;
        paralyzeChance = 0;
        freezeChance = 0;
        poisonChance = 0;
        infatuateChance = 0;
        confuseChance = 0;
    } // Constructor


    public Move(Move src){
        name = src.name;
        maxPP = src.maxPP;
        currPP = src.currPP;
        power = src.power;
        type = src.type;
        category = src.category;
        healing = src.healing;
        accuracy = src.accuracy;
        priority = src.priority;

        // Change this if I have time...
        critChance = 0;
        burnChance = 0;
        paralyzeChance = 0;
        freezeChance = 0;
        poisonChance = 0;
        infatuateChance = 0;
        confuseChance = 0;
    } // Copy constructor

    public String getName(){ return name; }

    public int getPP(){ return currPP; }

    public double getPower() {
        return power;
    }

    public Type getType() {
        return type;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isHealing(){ return healing; }

    public void oneUse(){ currPP--; }
} // Move

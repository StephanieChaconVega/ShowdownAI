public class Pokemon {
    private String species;
    private Move move1;
    private Move move2;
    private Move move3;
    private Move move4;
    // Pokemon can have two types.
    // type2 is null if the Pokemon has only one type
    private Type type1;
    private Type type2;
    // 'F' = Female
    // 'M' = Male
    // '-' = Genderless
    private char gender;
    private boolean fainted;
    private Status status;
    private boolean confused;
    private boolean infatuated;
    // Omitting items for the small implementation
    // "" = no item
    private String item;

    // Stats
    private double maxHP;
    private double currHP;
    private double speed;
    private double defense;
    private double specialDefence;
    private double attack;
    private double specialAttack;

    public Pokemon(String species, Move move1, Move move2, Move move3, Move move4, Type type1, Type type2, char gender, String item){
        this.species = species;
        this.move1 = move1;
        this.move2 = move2;
        this.move3 = move3;
        this.move4 = move4;
        this.type1 = type1;
        this.type2 = type2;
        this.gender = gender;
        fainted = false;
        status = null;
        confused = false;
        infatuated = false;
        this.item = item;
    } // Constructor

    //////////////////////////////////////////////////////////////////////
    //Accessors
    //////////////////////////////////////////////////////////////////////

    public String getSpecies(){ return species; }

    public Move[] getMoves(){
        Move moves[] = new Move[4];

        moves[0] = move1;
        moves[1] = move2;
        moves[2] = move3;
        moves[3] = move4;

        return moves;
    }

    public boolean isDualType(){ return (type1 != null && type2 != null); }

    public Type getType1(){ return type1; }

    public Type getType2(){ return type2; }

    public char getGender(){ return gender; }

    public boolean isFainted(){ return fainted; }

    public boolean hasStatus(){ return (status != null); }

    public Status getStatus(){ return status; }

    public boolean isConfused(){ return confused; }

    public boolean isInfatuated(){ return infatuated; }

    public String getItem(){ return item;}

    //Stat accessors

    public double getMaxHP(){ return maxHP; }

    public double getCurrHP(){ return currHP; }

    public double getSpeed(){ return speed; }

    public double getDefense(){ return defense; }

    public double getSpecialDefence(){ return specialDefence; }

    public double getAttack(){ return attack; }

    public double getSpecialAttack(){ return specialAttack; }


    //////////////////////////////////////////////////////////////////////
    //Mutators
    //////////////////////////////////////////////////////////////////////

    public void setStats(double hp, double attack, double defense, double specialAttack, double specialDefence, double speed){
        maxHP = hp;
        currHP = hp;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefence = specialDefence;
        this.speed = speed;
    } // setStats

    public void faint(){ fainted = true; }

    // Statuses might be left out for the small AI
    // Add an attack modifier for burn status if needed...
    public void inflictStatus(Status status){ this.status = status; }

    public void removeStatus(){ this.status = null; }

    public void heal(double recoveredHP){
        currHP += recoveredHP;

        // prevent overhealing
        if(currHP > maxHP)
            currHP = maxHP;

    } // heal

    // Take damage from an opposing Pokemon's attack
    // Calculates damage based on the in-game formula
    // Returns the value of the damage taken
    // https://bulbapedia.bulbagarden.net/wiki/Damage
    public double takeDamage(Move move, int attackingStat, Type oppType1, Type oppType2){
        // The damage formula is as follows:
        // Damage = (((((2*level)/5)+2) * power * (attack stat/defense stat))/50)+2) * modifier
        // Modifier = weather * critical * random * STAB * type effectiveness * other
        // Level is assumed to be 100
        // Weather, critical hits, random multiplier and "other" will be omitted for the purposes of this assignment
        // That leaves us with:
        // Modifier = STAB * type effectiveness
        // STAB: Same Type Attack Bonus

        double damage;

        double STAB = 1;

        if(move.getType() == oppType1 || move.getType() == oppType2)
            STAB = 1.5;

        double effectiveness = calcEffectiveness(move.getType());

        if(effectiveness != 0) {
            double modifier = STAB * effectiveness;

            // First part of the formula simplifies to 42 if level = 100
            damage = 42 * move.getPower();

            if(move.getCategory() == Category.PHYSICAL)
                damage *= (attackingStat/defense);
            else if(move.getCategory() == Category.SPECIAL)
                damage *= (attackingStat/specialDefence);

            damage /= 50;
            damage += 2;
            damage *= modifier;
        }else{
            // Damage is 0 if the Pokemon is immune
            // This is just to avoid unnecessary calculations
            damage = 0;
        } // if else

        // Apply the damage to the HP
        currHP -= damage;

        if(currHP <= 0)
            faint();

        return damage;
    } // takeDamage

    // Type matchup table
    // https://bulbapedia.bulbagarden.net/wiki/Type#Type_effectiveness
    // Check if of a move with Type type has a modified effectiveness on this Pokemon
    // There's probably a better way to do this lol
    public int calcEffectiveness(Type type){
        int effectiveness = 1;

        //////////////////////////
        // Immunities
        //////////////////////////
        if((type == Type.NORMAL || type == Type.FIGHTING) && (type1 == Type.GHOST || type2 == Type.GHOST))
            effectiveness = 0;

        if(type == Type.GHOST && (type1 == Type.NORMAL || type2 == Type.NORMAL))
            effectiveness = 0;

        if(type == Type.ELECTRIC && (type1 == Type.GROUND || type2 == Type.GROUND))
            effectiveness = 0;

        if(type == Type.GROUND && (type1 == Type.FLYING || type2 == Type.FLYING))
            effectiveness = 0;

        if(type == Type.PSYCHIC && (type1 == Type.DARK || type2 == Type.DARK))
            effectiveness = 0;

        if(type == Type.POISON && (type1 == Type.STEEL || type2 == Type.STEEL))
            effectiveness = 0;

        if(type == Type.DRAGON && (type1 == Type.FAIRY || type2 == Type.FAIRY))
            effectiveness = 0;


        //////////////////////////
        // Weaknesses
        //////////////////////////

        // Normal
        if(type == Type.FIGHTING && (type1 == Type.NORMAL || type2 == Type.NORMAL))
            effectiveness *= 2;

        // Fire
        if(type == Type.GROUND && (type1 == Type.FIRE || type2 == Type.FIRE))
            effectiveness *= 2;

        if(type == Type.ROCK && (type1 == Type.FIRE || type2 == Type.FIRE))
            effectiveness *= 2;

        if(type == Type.WATER && (type1 == Type.FIRE || type2 == Type.FIRE))
            effectiveness *= 2;

        // Water
        if(type == Type.ELECTRIC && (type1 == Type.WATER || type2 == Type.WATER))
            effectiveness *= 2;

        if(type == Type.GRASS && (type1 == Type.WATER || type2 == Type.WATER))
            effectiveness *= 2;

        // Electric
        if(type == Type.GROUND && (type1 == Type.ELECTRIC || type2 == Type.ELECTRIC))
            effectiveness *= 2;

        // Grass
        if(type == Type.ICE && (type1 == Type.GRASS || type2 == Type.GRASS))
            effectiveness *= 2;

        if(type == Type.FIRE && (type1 == Type.GRASS || type2 == Type.GRASS))
            effectiveness *= 2;

        if(type == Type.POISON && (type1 == Type.GRASS || type2 == Type.GRASS))
            effectiveness *= 2;

        if(type == Type.FLYING && (type1 == Type.GRASS || type2 == Type.GRASS))
            effectiveness *= 2;

        if(type == Type.BUG && (type1 == Type.GRASS || type2 == Type.GRASS))
            effectiveness *= 2;

        // Ice
        if(type == Type.FIRE && (type1 == Type.ICE || type2 == Type.ICE))
            effectiveness *= 2;

        if(type == Type.FIGHTING && (type1 == Type.ICE || type2 == Type.ICE))
            effectiveness *= 2;

        if(type == Type.ROCK && (type1 == Type.ICE || type2 == Type.ICE))
            effectiveness *= 2;

        if(type == Type.STEEL && (type1 == Type.ICE || type2 == Type.ICE))
            effectiveness *= 2;

        // Fighting
        if(type == Type.FLYING && (type1 == Type.FIGHTING || type2 == Type.FIGHTING))
            effectiveness *= 2;

        if(type == Type.PSYCHIC && (type1 == Type.FIGHTING || type2 == Type.FIGHTING))
            effectiveness *= 2;

        if(type == Type.FAIRY && (type1 == Type.FIGHTING || type2 == Type.FIGHTING))
            effectiveness *= 2;

        // Poison
        if(type == Type.GROUND && (type1 == Type.POISON || type2 == Type.POISON))
            effectiveness *= 2;

        if(type == Type.PSYCHIC && (type1 == Type.POISON || type2 == Type.POISON))
            effectiveness *= 2;

        // Ground
        if(type == Type.WATER && (type1 == Type.GROUND || type2 == Type.GROUND))
            effectiveness *= 2;

        if(type == Type.GRASS && (type1 == Type.GROUND || type2 == Type.GROUND))
            effectiveness *= 2;

        if(type == Type.ICE && (type1 == Type.GROUND || type2 == Type.GROUND))
            effectiveness *= 2;

        // Flying
        if(type == Type.ELECTRIC && (type1 == Type.FLYING || type2 == Type.FLYING))
            effectiveness *= 2;

        if(type == Type.ICE && (type1 == Type.FLYING || type2 == Type.FLYING))
            effectiveness *= 2;

        if(type == Type.ROCK && (type1 == Type.FLYING || type2 == Type.FLYING))
            effectiveness *= 2;

        // Psychic
        if(type == Type.BUG && (type1 == Type.PSYCHIC || type2 == Type.PSYCHIC))
            effectiveness *= 2;

        if(type == Type.GHOST && (type1 == Type.PSYCHIC || type2 == Type.PSYCHIC))
            effectiveness *= 2;

        if(type == Type.DARK && (type1 == Type.PSYCHIC || type2 == Type.PSYCHIC))
            effectiveness *= 2;

        // Bug
        if(type == Type.FIRE && (type1 == Type.BUG || type2 == Type.BUG))
            effectiveness *= 2;

        if(type == Type.FLYING && (type1 == Type.BUG || type2 == Type.BUG))
            effectiveness *= 2;

        if(type == Type.ROCK && (type1 == Type.BUG || type2 == Type.BUG))
            effectiveness *= 2;

        // Rock
        if(type == Type.WATER && (type1 == Type.ROCK || type2 == Type.ROCK))
            effectiveness *= 2;

        if(type == Type.GRASS && (type1 == Type.ROCK || type2 == Type.ROCK))
            effectiveness *= 2;

        if(type == Type.FIGHTING && (type1 == Type.ROCK || type2 == Type.ROCK))
            effectiveness *= 2;

        if(type == Type.GROUND && (type1 == Type.ROCK || type2 == Type.ROCK))
            effectiveness *= 2;

        if(type == Type.STEEL && (type1 == Type.ROCK || type2 == Type.ROCK))
            effectiveness *= 2;

        // Ghost
        if(type == Type.GHOST && (type1 == Type.GHOST || type2 == Type.GHOST))
            effectiveness *= 2;

        if(type == Type.DARK && (type1 == Type.GHOST || type2 == Type.GHOST))
            effectiveness *= 2;

        // Dragon
        if(type == Type.ICE && (type1 == Type.DRAGON || type2 == Type.DRAGON))
            effectiveness *= 2;

        if(type == Type.DRAGON && (type1 == Type.DRAGON || type2 == Type.DRAGON))
            effectiveness *= 2;

        if(type == Type.FAIRY && (type1 == Type.DRAGON || type2 == Type.DRAGON))
            effectiveness *= 2;

        // Dark
        if(type == Type.FIGHTING && (type1 == Type.DARK || type2 == Type.DARK))
            effectiveness *= 2;

        if(type == Type.BUG && (type1 == Type.DARK || type2 == Type.DARK))
            effectiveness *= 2;

        if(type == Type.FAIRY && (type1 == Type.DARK || type2 == Type.DARK))
            effectiveness *= 2;

        // Steel
        if(type == Type.FIRE && (type1 == Type.STEEL || type2 == Type.STEEL))
            effectiveness *= 2;

        if(type == Type.FIGHTING && (type1 == Type.STEEL || type2 == Type.STEEL))
            effectiveness *= 2;

        if(type == Type.GROUND && (type1 == Type.STEEL || type2 == Type.STEEL))
            effectiveness *= 2;

        // Fairy
        if(type == Type.POISON && (type1 == Type.FAIRY || type2 == Type.FAIRY))
            effectiveness *= 2;

        if(type == Type.STEEL && (type1 == Type.FAIRY || type2 == Type.FAIRY))
            effectiveness *= 2;

        //////////////////////////
        // Resistances
        //////////////////////////

        // Fire
        if(type == Type.FIRE && (type1 == Type.FIRE || type2 == Type.FIRE))
            effectiveness *= 0.5;

        if(type == Type.GRASS && (type1 == Type.FIRE || type2 == Type.FIRE))
            effectiveness *= 0.5;

        if(type == Type.ICE && (type1 == Type.FIRE || type2 == Type.FIRE))
            effectiveness *= 0.5;

        if(type == Type.BUG && (type1 == Type.FIRE || type2 == Type.FIRE))
            effectiveness *= 0.5;

        if(type == Type.STEEL && (type1 == Type.FIRE || type2 == Type.FIRE))
            effectiveness *= 0.5;

        if(type == Type.FAIRY && (type1 == Type.FIRE || type2 == Type.FIRE))
            effectiveness *= 0.5;

        // Water
        if(type == Type.FIRE && (type1 == Type.WATER || type2 == Type.WATER))
            effectiveness *= 0.5;

        if(type == Type.WATER && (type1 == Type.WATER || type2 == Type.WATER))
            effectiveness *= 0.5;

        if(type == Type.ICE && (type1 == Type.WATER || type2 == Type.WATER))
            effectiveness *= 0.5;

        if(type == Type.STEEL && (type1 == Type.WATER || type2 == Type.WATER))
            effectiveness *= 0.5;

        // Electric
        if(type == Type.ELECTRIC && (type1 == Type.ELECTRIC || type2 == Type.ELECTRIC))
            effectiveness *= 0.5;

        if(type == Type.FLYING && (type1 == Type.ELECTRIC || type2 == Type.ELECTRIC))
            effectiveness *= 0.5;

        if(type == Type.STEEL && (type1 == Type.ELECTRIC || type2 == Type.ELECTRIC))
            effectiveness *= 0.5;

        // Grass
        if(type == Type.WATER && (type1 == Type.GRASS || type2 == Type.GRASS))
            effectiveness *= 0.5;

        if(type == Type.ELECTRIC && (type1 == Type.GRASS || type2 == Type.GRASS))
            effectiveness *= 0.5;

        if(type == Type.GRASS && (type1 == Type.GRASS || type2 == Type.GRASS))
            effectiveness *= 0.5;

        if(type == Type.GROUND && (type1 == Type.GRASS || type2 == Type.GRASS))
            effectiveness *= 0.5;

        // Ice
        if(type == Type.ICE && (type1 == Type.ICE || type2 == Type.ICE))
            effectiveness *= 0.5;

        // Fighting
        if(type == Type.BUG && (type1 == Type.FIGHTING || type2 == Type.FIGHTING))
            effectiveness *= 0.5;

        if(type == Type.ROCK && (type1 == Type.FIGHTING || type2 == Type.FIGHTING))
            effectiveness *= 0.5;

        // Poison
        if(type == Type.GRASS && (type1 == Type.POISON || type2 == Type.POISON))
            effectiveness *= 0.5;

        if(type == Type.FIGHTING && (type1 == Type.POISON || type2 == Type.POISON))
            effectiveness *= 0.5;

        if(type == Type.POISON && (type1 == Type.POISON || type2 == Type.POISON))
            effectiveness *= 0.5;

        if(type == Type.BUG && (type1 == Type.POISON || type2 == Type.POISON))
            effectiveness *= 0.5;

        if(type == Type.FAIRY && (type1 == Type.POISON || type2 == Type.POISON))
            effectiveness *= 0.5;

        // Ground
        if(type == Type.POISON && (type1 == Type.GROUND || type2 == Type.GROUND))
            effectiveness *= 0.5;

        if(type == Type.ROCK && (type1 == Type.GROUND || type2 == Type.GROUND))
            effectiveness *= 0.5;

        // Flying
        if(type == Type.GRASS && (type1 == Type.FLYING || type2 == Type.FLYING))
            effectiveness *= 0.5;

        if(type == Type.BUG && (type1 == Type.FLYING || type2 == Type.FLYING))
            effectiveness *= 0.5;

        // Psychic
        if(type == Type.FIGHTING && (type1 == Type.PSYCHIC || type2 == Type.PSYCHIC))
            effectiveness *= 0.5;

        if(type == Type.PSYCHIC && (type1 == Type.PSYCHIC || type2 == Type.PSYCHIC))
            effectiveness *= 0.5;

        // Bug
        if(type == Type.GRASS && (type1 == Type.BUG || type2 == Type.BUG))
            effectiveness *= 0.5;

        if(type == Type.FIGHTING && (type1 == Type.BUG || type2 == Type.BUG))
            effectiveness *= 0.5;

        if(type == Type.GROUND && (type1 == Type.BUG || type2 == Type.BUG))
            effectiveness *= 0.5;

        // Rock
        if(type == Type.NORMAL && (type1 == Type.ROCK || type2 == Type.ROCK))
            effectiveness *= 0.5;

        if(type == Type.FIRE && (type1 == Type.ROCK || type2 == Type.ROCK))
            effectiveness *= 0.5;

        if(type == Type.POISON && (type1 == Type.ROCK || type2 == Type.ROCK))
            effectiveness *= 0.5;

        if(type == Type.FLYING && (type1 == Type.ROCK || type2 == Type.ROCK))
            effectiveness *= 0.5;

        // Ghost
        if(type == Type.POISON && (type1 == Type.GHOST || type2 == Type.GHOST))
            effectiveness *= 0.5;

        if(type == Type.BUG && (type1 == Type.GHOST || type2 == Type.GHOST))
            effectiveness *= 0.5;

        // Dragon
        if(type == Type.FIRE && (type1 == Type.DRAGON || type2 == Type.DRAGON))
            effectiveness *= 0.5;

        if(type == Type.WATER && (type1 == Type.DRAGON || type2 == Type.DRAGON))
            effectiveness *= 0.5;

        if(type == Type.ELECTRIC && (type1 == Type.DRAGON || type2 == Type.DRAGON))
            effectiveness *= 0.5;

        if(type == Type.GRASS && (type1 == Type.DRAGON || type2 == Type.DRAGON))
            effectiveness *= 0.5;

        // Dark
        if(type == Type.GHOST && (type1 == Type.DARK || type2 == Type.DARK))
            effectiveness *= 0.5;

        if(type == Type.DARK && (type1 == Type.DARK || type2 == Type.DARK))
            effectiveness *= 0.5;

        // Steel
        if(type == Type.NORMAL && (type1 == Type.STEEL || type2 == Type.STEEL))
            effectiveness *= 0.5;

        if(type == Type.GRASS && (type1 == Type.STEEL || type2 == Type.STEEL))
            effectiveness *= 0.5;

        if(type == Type.ICE && (type1 == Type.STEEL || type2 == Type.STEEL))
            effectiveness *= 0.5;

        if(type == Type.FLYING && (type1 == Type.STEEL || type2 == Type.STEEL))
            effectiveness *= 0.5;

        if(type == Type.PSYCHIC && (type1 == Type.STEEL || type2 == Type.STEEL))
            effectiveness *= 0.5;

        if(type == Type.BUG && (type1 == Type.STEEL || type2 == Type.STEEL))
            effectiveness *= 0.5;

        if(type == Type.ROCK && (type1 == Type.STEEL || type2 == Type.STEEL))
            effectiveness *= 0.5;

        if(type == Type.DRAGON && (type1 == Type.STEEL || type2 == Type.STEEL))
            effectiveness *= 0.5;

        if(type == Type.STEEL && (type1 == Type.STEEL || type2 == Type.STEEL))
            effectiveness *= 0.5;

        if(type == Type.FAIRY && (type1 == Type.STEEL || type2 == Type.STEEL))
            effectiveness *= 0.5;

        // Fairy
        if(type == Type.FIGHTING && (type1 == Type.FAIRY || type2 == Type.FAIRY))
            effectiveness *= 0.5;

        if(type == Type.BUG && (type1 == Type.FAIRY || type2 == Type.FAIRY))
            effectiveness *= 0.5;

        if(type == Type.DARK && (type1 == Type.FAIRY || type2 == Type.FAIRY))
            effectiveness *= 0.5;

        return effectiveness;
    } // calcEffectiveness

    // For a larger implementation, include stat modifiers
}

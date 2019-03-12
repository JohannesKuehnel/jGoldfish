package at.co.kuehnel.jgoldfish;

/*
    Example simulation for a deck consisting only of Lightning Bolts (3 damage to any target).
*/
public class SimulationBolt extends Simulation{
    String BOLT = "Lightning Bolt";
    String LAND = "Mountain";
    
    public SimulationBolt(){
        super();

        add(BOLT, 44);
        add(LAND, 16);
        
        decklist_mb.addAll(library);
        decklist_sb.addAll(sb);
    }

    @Override
    public boolean isWin() {
        if(damage >= 20)
            return true;
        return false;
    }

    @Override
    public boolean isMulligan() {
        cardsToDraw--;
        // less than TWO lands
        if(hand.size() >= 5)
        {
            if(isInHand(LAND) >= 1 && isInHand(BOLT) >= 2)
                return false;
        } else if(hand.size() < 5 && hand.size() > 2)
        {
            if(hand.contains(LAND) && hand.contains(BOLT))
                return false;
        } else if(hand.size() <= 2)
        {
            return false;
        }
        
        debug("======== Take a mulligan ========");
        return true;
    }

    @Override
    public int play() {
        debug("======== Start game ========");
        init();
        do{
            reset();
            draw(cardsToDraw);
        }
        while(isMulligan());
        debug("======== Keeping hand ========");
        while(turn < turnMax)
        {
            debug("======== Turn " + turn + " ========");
            // DRAW PHASE
            if(turn > 1 || !otp)
                draw();
            // DRAW PHASE
            
            // Land to play? Do so
            if(hasLandDropLeft && hand.contains(LAND))
            {
                debug("Playing " + LAND);
                hand.remove(LAND);
                landsInPlay++;
                debug(landsInPlay + " land" + (landsInPlay == 1 ? "" : "s") + " in play.");
                mana++;
                hasLandDropLeft = false;
            }
            // Land to play? Do so
            
            // Play as many bears as possible
            while(playBolt())
            {
                debug("Casting " + BOLT + ".");
                debug(damage + " total damage dealt.");
            }
            // Play as many Bolts as possible

            if(isWin())
                break;
            
            cleanUp();
            turn++;
        }
        
        return turn;
    }
    
    public void init()
    {
        damage = 0;
        hp = 20;
        mana = 0;
        landsInPlay = 0;
        turn = 1;
        cardsToDraw = 7;
        hasLandDropLeft = true;
    }
    
    public void cleanUp()
    {
        mana = landsInPlay;
        hasLandDropLeft = true;
    }

    public boolean hasMana(int amount) {
        return mana >= amount;
    }

    public boolean playBolt() {
        if(hand.contains(BOLT) && useMana(1)) {
            hand.remove(BOLT);
            damage += 3;
            return true;
        } else {
            return false;
        }
    }

    public boolean useMana(int amount) {
        if (amount < 0 || mana < amount) {
            return false;
        }

        mana -= amount;
        return true;
    }
}

package jgoldfish;

import java.util.Iterator;

/*
    Example simulation for Modern deck 'Reanimator'
*/
public class SimulationReanimator extends Simulation{
    String GORYO = "Goryo's Vengeance";
    String GRISELBRAND = "Griselbrand";
    String EMRAKUL = "Emrakul, the Aeons Torn";
    String FURY = "Fury of the Horde";
    String BREACH = "Through the Breach";
    String SSG = "Simian Spirit Guide";
    String LOOT = "Faithless Looting";
    String CHARM = "Izzet Charm";
    String PRISM = "Pentad Prism";
    String SLEIGHT = "Sleight of Hand";
    String SPOILS = "Spoils of the Vault";
    
    public SimulationReanimator(){
        super();

        add(GRISELBRAND, 4);
        add(EMRAKUL, 4);
        add(SSG, 4);
        add(GORYO, 4);
        add(BREACH, 4);
        add(FURY ,4);
        add(LOOT, 4);
        add(CHARM, 4);
        add(PRISM, 4);
        add(SPOILS, 2);
        add(SLEIGHT, 3);
        
        add("Land", 19);
        
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
        //less than TWO lands
        if(hand.size() >= 5)
        {
            if(isInHand("Land") >= 2)
                return false;
        }else if(hand.size() < 5 && hand.size() > 2)
        {
            if(hand.contains("Land"))
                return false;
        }else if(hand.size() <= 2)
        {
            return false;
        }
        
        //System.out.println("======== Take a mulligan ========");
        return true;
    }

    @Override
    public int play() {
        //System.out.println("======== Start game ========");
        init();
        do{
            reset();
            draw(cardsToDraw);
        }
        while(isMulligan());
        //System.out.println("======== Keeping hand ========");
        while(turn < turnMax)
        {
            //System.out.println("======== Turn " + turn + " ========");
            //DRAW PHASE
            hasLandDropLeft = true;
            if(turn > 1 || !otp)
                draw();
            //DRAW PHASE

            activateGriselbrand();
            
            //Land to play? Do so
            if(hasLandDropLeft && hand.contains("Land"))
            {
                hand.remove("Land");
                landsInPlay++;
                mana++;
                hasLandDropLeft = false;
            }
            //Land to play? Do so
            
            //Goryo's Vengeance + Target ready?
            do
            {
                if(hand.contains(GORYO) && mana >= 2) {
                    if(graveyard.contains(EMRAKUL) && (!graveyard.contains(GRISELBRAND) || damage >= 5) && !board.contains(EMRAKUL))
                    {
                        hand.remove(GORYO);
                        graveyard.remove(EMRAKUL);
                        mana -= 2;
                        board.add(EMRAKUL);
                    }
                    else if(graveyard.contains(GRISELBRAND) && !board.contains(GRISELBRAND))
                    {
                        hand.remove(GORYO);
                        graveyard.remove(GRISELBRAND);
                        mana -= 2;
                        board.add(GRISELBRAND);
                    }
                }
            }
            while(activateGriselbrand());
            //Goryo's Vengeance + Target ready?
            
            //Through the Breach
            if(hand.contains(BREACH) && mana >= 5)
            {
                if(hand.contains(EMRAKUL) && (!hand.contains(GRISELBRAND) || damage >= 5))
                {
                    hand.remove(BREACH);
                    hand.remove(EMRAKUL);
                    mana -= 5;
                    board.add(EMRAKUL);
                }
                else if(hand.contains(GRISELBRAND))
                {
                    hand.remove(BREACH);
                    hand.remove(GRISELBRAND);
                    mana -= 5;
                    board.add(GRISELBRAND);
                }
            }
            //Through the Breach
            
            // ATTACK PHASE
            attack();
            // ATTACK PHASE
        
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
    }
    
    public void cleanUp()
    {
        board.clear();
        mana = landsInPlay;
    }

    public boolean activateGriselbrand() {
        if (board.contains(GRISELBRAND) && (hp > 7) && (library.size() > 7)) {
            //use Griselbrand's draw 7
            do {
                draw(7);
                hp -= 7;
            } while ((hp > 7) && (library.size() > 7));
            return true;
        }
        return false;
    }
    
    public void attack(){
        if (board.contains(EMRAKUL)) {
            damage += 15;
        }
        if (board.contains(GRISELBRAND)) {
            damage += 7;
            hp += 7;
        }

        activateGriselbrand();

        //use Fury of the Horde
        while (hand.contains(FURY)) {
            int redCards = 0;
            Iterator<String> it = hand.iterator();
            while (it.hasNext()) {
                String card = (String) it.next();
                if (card.equals(FURY) || card.equals(CHARM) || card.equals(BREACH) || card.equals(SSG) || card.equals(LOOT)) {
                    redCards++;
                }
            }
            if (redCards > 2) {
                hand.remove(FURY);
                if(hand.contains(SSG))
                    hand.remove(SSG);
                else if(hand.contains(CHARM))
                    hand.remove(CHARM);
                else if(hand.contains(FURY))
                    hand.remove(FURY);
                else if(hand.contains(LOOT))
                    hand.remove(LOOT);
                else if(hand.contains(BREACH))
                    hand.remove(BREACH);
                attack();
            }else{
                break;
            }
        }
    }
    
}

/* TODO
 * 
 * SSG as mana source
 * Pentad Prism as mana source
 * use Looting and Charm
 * use Sleight of Hand
 * use Spoils
 * sequencing and looping of casting checks
 */
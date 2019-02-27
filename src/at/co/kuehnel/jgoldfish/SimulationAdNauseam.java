package at.co.kuehnel.jgoldfish;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
    Example simulation for Modern deck 'Ad Nauseam'
*/
public class SimulationAdNauseam extends Simulation{
    String SSG = "Simian Spirit Guide";
    String PRISM = "Pentad Prism";
    String SPOILS = "Spoils of the Vault";
    String STORM = "Lightning Storm";
    String LABMAN = "Laboratory Maniac";
    String ADNAUSEAM = "Ad Nauseam";
    String PACT = "Pact of Negation";
    String GRACE = "Angel's Grace";
    String SERUM = "Serum Visions";
    String SLEIGHT = "Sleight of Hand";
    String UNLIFE = "Phyrexian Unlife";
    String LOTUS = "Lotus Bloom";
    String FAST_LAND = "Fast Land";
    String SCRY_LAND = "Scry Land";
    String LAND = "Land";

    boolean activeAngelsGrace;
    List<SuspendedCard> suspendedCards;
    
    public SimulationAdNauseam(){
        super();

        add(SSG, 4);
        add(PRISM, 4);
        add(SPOILS, 3);
        add(STORM, 1);
        add(LABMAN, 1);
        add(ADNAUSEAM, 4);
        add(PACT, 3);
        add(GRACE, 4);
        add(SERUM, 4);
        add(SLEIGHT, 4);
        add(UNLIFE, 4);
        add(LOTUS, 4);

        // TODO: add FAST_LAND and SCRY_LAND
        // add(FAST_LAND, 4);
        // add(SCRY_LAND, 4);
        add(LAND, 20);
        
        decklist_mb.addAll(library);
        decklist_sb.addAll(sb);
    }

    @Override
    public boolean isWin() {
        // TODO: add all winning lines, including win con check
        if ( hand.contains(ADNAUSEAM) ) {
            if( board.contains(UNLIFE) && availableMana(5) ) {
                return true;
            } else if ( availableMana(6) && hand.contains(GRACE) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMulligan() {
        cardsToDraw--;
        //less than TWO lands
        if(hand.size() >= 5)
        {
            if( hasLands(2) && hasComboPiece() )
                return false;
            if( hasLands(1) && hasComboPiece() && hasFastMana() )
                return false;
        } else if( hand.size() < 5 && hand.size() > 2 )
        {
            if( hand.contains(LAND) && (hasComboPiece() || hasCantrip()) )
                return false;
        } else if(hand.size() <= 2)
        {
            return false;
        }
        
        debug("======== Take a mulligan ========");
        return true;
    }

    private boolean hasComboPiece() {
        return hand.contains(ADNAUSEAM) || hand.contains(GRACE) || hand.contains(UNLIFE);
    }

    private boolean hasFastMana() {
        return hand.contains(LOTUS) || hand.contains(PRISM) || hand.contains(SSG);
    }

    private boolean hasLands(int amount) {
        return isInHand(LAND) >= amount;
    }

    private boolean hasCantrip() {
        return hand.contains(SERUM) || hand.contains(SLEIGHT) || hand.contains(SPOILS);
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
            //UPKEEP PHASE
            removeTimeCounters();
            //UPKEEP PHASE

            //DRAW PHASE
            hasLandDropLeft = true;
            if(turn > 1 || !otp)
                draw();
            //DRAW PHASE
                        
            boolean possiblePlays = true;
            while(possiblePlays)
            {
                possiblePlays = false;

                //Land to play? Do so
                if(hasLandDropLeft && hand.contains(LAND))
                {
                    debug("Playing " + LAND);
                    hand.remove(LAND);
                    landsInPlay++;
                    mana++;
                    hasLandDropLeft = false;
                }
                //Land to play? Do so

                if(isWin())
                    break;

                /* 
                * TODO: sequencing and looping of casting checks
                */

                if(hand.contains(LOTUS)) {
                    hand.remove(LOTUS);
                    debug("Suspending " + LOTUS);
                    SuspendedCard lotus = new SuspendedCard(LOTUS, 3);
                    suspendedCards.add(lotus);
                }

                if(!board.contains(UNLIFE) && cast(UNLIFE, 3)) {
                    possiblePlays = true;
                    continue;
                }

                if(mana >= 2 && cast(PRISM, 2)) { // do not cast Prism with Lotus, SSG or Prism
                    //
                }

                if(cast(SERUM, 1)) {
                    possiblePlays = true;
                }
            }

            if(isWin())
                break;
            if(isLoss())
                return turnMax;
            
            cleanUp();
            turn++;
        }
        
        return turn;
    }

    public boolean cast(String card, int mana) {
        if(hand.contains(card) && availableMana(mana)) {
            debug("Casting " + card);
            hand.remove(card);
            useMana(mana);
            if(card.equals(ADNAUSEAM)) {
                // TODO: check if mana + wincon is available
            } else if (card.equals(UNLIFE)) {
                board.add(UNLIFE);
                debug(board.toString());
            } else if (card.equals(GRACE)) {
                activeAngelsGrace = true;
            } else if (card.equals(PRISM)) {
                board.add(PRISM);
                board.add(PRISM);
                debug(board.toString());
            } else if (card.equals(SERUM)) {
                draw();
                for(int i = 1; i >= 0; i--) {
                    String topCard = library.get(i);
                    if ( hand.contains(ADNAUSEAM) && !board.contains(UNLIFE) && !(topCard.equals(UNLIFE) || topCard.equals(GRACE))
                        || (hand.contains(UNLIFE) || hand.contains(GRACE)) && !topCard.equals(ADNAUSEAM)
                        || (hand.contains(UNLIFE) || hand.contains(GRACE) || board.contains(UNLIFE)) && hand.contains(ADNAUSEAM) && !(topCard.equals(PRISM) || topCard.equals(LAND)) ) {
                        debug("Scrying " + topCard + " to the bottom");
                        library.remove(i);
                        library.add(topCard);
                    } else {
                        debug("Scrying " + topCard + " to the top");
                    }
                }
            } else if (card.equals(SLEIGHT)) {
                // TODO add Sleight of Hands logic
            } else if (card.equals(SPOILS)) {
                // TODO add Spoils of the Vault logic
            }
            return true;
        }
        return false;
    }
    
    public void init()
    {
        damage = 0;
        hp = 20;
        poison = 0;
        mana = 0;
        landsInPlay = 0;
        turn = 1;
        cardsToDraw = 7;
        activeAngelsGrace = false;
        suspendedCards = new ArrayList<SuspendedCard>();
    }

    public void removeTimeCounters() {
        Iterator<SuspendedCard> it = suspendedCards.iterator();
        while (it.hasNext()) {
            SuspendedCard card = it.next();
            if(card.removeTimeCounter() == 0) {
                debug("Casting suspended " + card.getName());
                board.add(card.getName());
                debug(board.toString());
                it.remove();
            }
        }
    }
    
    public void cleanUp()
    {
        mana = landsInPlay;
    }

    public boolean isUnlifeEffectActive() {
        if (board.contains(UNLIFE) || activeAngelsGrace) {
            return true;
        }
        return false;
    }

    public boolean isLoss() {
        // not enough wincons left
        if( poison >= 10 && !activeAngelsGrace
            || hp <= 0 && !isUnlifeEffectActive()
            || turn == turnMax )
            return true;
        return false;
    }

    public boolean availableMana(int amount) {
        int availableMana = 0;
        for (String card : hand) {
            if(card.equals(SSG))
                availableMana++;
        }
        for (String card : board) {
            if(card.equals(PRISM))
                availableMana++;
            if(card.equals(LOTUS))
                availableMana += 3;
        }
        availableMana += mana;
        return availableMana >= amount;
    }

    public boolean useMana(int amount) {
        if (amount <= 0) {
            return false;
        }

        int ssg = 0;
        int prism = 0;
        int lotus = 0;
        for (String card : hand) {
            if(card.equals(SSG))
                ssg++;
        }
        for (String card : board) {
            if(card.equals(PRISM))
                prism++;
            if(card.equals(LOTUS))
                lotus++;
        }
        if((mana + ssg + prism + lotus*3) < amount)
            return false;

        while(mana > 0 && amount > 0) {
            mana--;
            amount--;
        }
        while(prism > 0 && amount > 0) {
            prism--;
            amount--;
            board.remove(PRISM);
        }
        while(lotus > 0 && amount > 0) {
            lotus--;
            mana += amount >= 3 ? 0 : 3 - amount;
            amount -= amount >= 3 ? 3 : amount;
            board.remove(LOTUS);
        }
        // TODO: restrict SSG usage
        while(ssg > 0 && amount > 0) {
            ssg--;
            amount--;
            board.remove(SSG);
        }

        return true;
    }
    

}

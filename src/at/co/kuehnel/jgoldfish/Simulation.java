package at.co.kuehnel.jgoldfish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/*
    This is the base class for the simulation. You probably don't need to edit this.
    If you want to add your own deck simulation, take a look at SimulationReanimator.java
*/
public abstract class Simulation {
    public List<String> hand;
    public List<String> library;
    public List<String> sb;
    public List<String> decklist_mb;
    public List<String> decklist_sb;
    public List<String> graveyard;
    public List<String> board;
    public List<String> exile;
    public boolean DEBUG = false;
    public boolean otp;
    public int turn;
    public int turnMax;
    public int cardsToDraw;
    public int landsInPlay;
    public int mana;
    public boolean hasLandDropLeft;
    public int damage;
    public int hp;
    public int poison;
    
    public Simulation()
    {
        hand = new ArrayList<>();
        library = new ArrayList<>();
        sb = new ArrayList<>();
        decklist_mb = new ArrayList<>();
        decklist_sb = new ArrayList<>();
        graveyard = new ArrayList<>();
        board = new ArrayList<>();
        otp = true;
        turn = 1;
        turnMax = 8;
        cardsToDraw = 7;
        landsInPlay = 0;
        mana = 0;
        hasLandDropLeft = true;
        damage = 0;
        hp = 20;
        poison = 0;
    }

    public void shuffle() {
        Collections.shuffle(library);
    }
    
    public void draw(){
        if(library.isEmpty()) {
            debug("Cannot draw - Out of cards!");
            turn = turnMax;
        }
        else
        {
            String card = library.get(0);
            library.remove(0);
            hand.add(card);
            debug("Draw: " + card + " (" + library.size() + " cards left in library)");
        }
    }
    
    public void search(String cardToDraw){
        if(library.isEmpty() || !library.contains(cardToDraw)) {
            debug("Cannot draw - Out of cards/Card not found!");
        }
        else
        {
            library.remove(cardToDraw);
            hand.add(cardToDraw);
            //debug("Search: " + cardToDraw + " (" + library.size() + " cards left in library)");
        }
    }
    
    public void draw(int amount){
        for(int i = 0; i < amount; i++)
            draw();
    }
    
    public int isInDeck(String card){
        int count = 0;
        Iterator<String> it = library.iterator();
        while(it.hasNext()){
            String tmp = "";
            tmp = (String) it.next();
            if(tmp.equals(card))
                count++;
        }
        return count;
    }
    
    public int isInHand(String card){
        int count = 0;
        Iterator<String> it = hand.iterator();
        while(it.hasNext()){
            String tmp = "";
            tmp = (String) it.next();
            if(tmp.equals(card))
                count++;
        }
        return count;
    }
    
    public void reset(){
        hand.clear();
        library.clear();
        sb.clear();
        graveyard.clear();
        board.clear();
        library.addAll(decklist_mb);
        sb.addAll(decklist_sb);
        shuffle();
    }
    
    public void setHand(List<String> hand){
        //debug("======== SET HAND BEGIN ========");
        reset();
        this.hand.clear();
        Iterator<String> it = hand.iterator();
        while(it.hasNext())
            search((String)it.next());
        //debug("======== SET HAND END ========");
    }
    
    public void add(String card, int amount){
        //debug("Add " + amount + " " + card + " to the deck");
        for(int i = 0; i < amount; i++)
            library.add(card);
    }
    
    public void addSB(String card, int amount){
        //debug("Add " + amount + " " + card + " to the deck");
        for(int i = 0; i < amount; i++)
            sb.add(card);
    }
    
    public double probabilityToDrawCard(String card, int draws, int amount){
        //debug("======== PROBABILITY TO DRAW " + card + " BEGIN ========");
        int total = library.size(); // 60
        int success = isInDeck(card); // 0-4
        int sample = draws; // 7 for opening hand
        int sampleSuccess = amount; // 0 none, 1 exactly one etc.
        double result = hypergeom(total, success, sample, sampleSuccess);
        //debug("======== PROBABILITY TO DRAW " + card + " END ========");
        return result;
    }
    
    public void probabilityToDrawCardPerTurn(String card){
        debug("======== PROBABILITY TO DRAW " + card + " BEGIN ========");
        int total = library.size(); // 60
        int success = isInDeck(card); // 0-4
        int sample = 7; // 7 for opening hand
        int sampleSuccess = 0; // 0 none, 1 exactly one etc.
        for(int i = 0; i < 7; i++)
        {
            double result = 1 - hypergeom(total, success, sample+i, sampleSuccess);
            debug("Turn " + (i+1) + ": " + result*100 + "%");
        }
        debug("======== PROBABILITY TO DRAW " + card + " END ========");
    }
    
    public void probabilityToDrawHand(List<String> hand){
        debug("======== PROBABILITY TO DRAW HAND BEGIN ========"); 
        debug(hand.toString());
        double probability = 1.0;
        HashMap<String, Integer> desiredCards = new HashMap<String, Integer>();
        Iterator<String> it = hand.iterator();
        while(it.hasNext())
        {
            String card = (String) it.next();
            if (desiredCards.get(card) != null) {
                desiredCards.replace(card, desiredCards.get(card) + 1);
            } else {
                desiredCards.put(card, 1);
            }
        }

        it = desiredCards.keySet().iterator();
        while(it.hasNext())
        {
            String card = (String) it.next();
            // debug(desiredCards.get(card) + " " + card + " with " + isInDeck(card) + " copies in deck of size " + library.size());
            double result = hypergeom(library.size(), isInDeck(card), 7, desiredCards.get(card));
            probability *= result;
            // debug(result*100 + " chance");
        }
        debug(probability*100 + "% chance to have this opener");
        debug("======== PROBABILITY TO DRAW HAND END ========");
    }
    
    public double hypergeom(int totalSize, int successSize, int sampleSize, int sampleSuccessSize){
        double result = 0.0;
        result = binomial(successSize, sampleSuccessSize);
        result *= binomial(totalSize-successSize, sampleSize-sampleSuccessSize);
        result /= binomial(totalSize, sampleSize);
        return result;
    }
    
    public double binomial(int n, int k){
        double result = 0;
        if(k == 0)
            return 1;
        else if(k == 1)
            result = binomial(n, n-1);
        else
        {
            result = n;
            for(int i = 2; i <= k; i++){
                double tmp = ( n - ( i - 1) );
                tmp /= i;
                result *= tmp;
            }
        }
        return result;
    }
    
    public void printHand(){
        if(hand.isEmpty())
            debug("======== HAND EMPTY ========");
        else
        {
            Iterator<String> it = hand.iterator();
            debug("======== HAND BEGIN ========");
            while(it.hasNext()){
                String card = "";
                card = (String) it.next();
                debug(card);
            }
            debug("======== HAND END ========");
        }
    }
    
    public void printLibrary(){
        if(library.isEmpty())
            debug("======== DECKLIST EMPTY ========");
        else
        {
            Iterator<String> it = library.iterator();
            debug("======== DECKLIST BEGIN ========");
            while(it.hasNext()){
                String card = "";
                card = (String) it.next();
                debug(card);
            }
            debug("======== DECKLIST END ========");
        }
    }

    public void debug(String msg) {
        if(DEBUG) {
            System.out.println(msg);
        }
    }
    
    public abstract boolean isWin();
    public abstract boolean isMulligan();
    public abstract int play();
}

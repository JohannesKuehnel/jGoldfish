package jgoldfish;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
    public boolean otp;
    public int turn;
    public int turnMax;
    public int cardsToDraw;
    public int landsInPlay;
    public int mana;
    public boolean hasLandDropLeft;
    public int damage;
    public int hp;
    
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
    }
    
    public void draw(){
        if(library.isEmpty())
            System.out.println("Cannot draw - Out of cards!");
        else
        {
            Random generator = new Random(System.currentTimeMillis());
            //int random = (int) (Math.random() * library.size());
            int random = generator.nextInt(library.size());
            //System.out.println(random);
            String card = library.get(random);
            library.remove(random);
            hand.add(card);
            //System.out.println("Draw: " + card + " (" + library.cards.size() + " cards left in library)");
        }
    }
    
    public void draw(String cardToDraw){
        if(library.isEmpty() || !library.contains(cardToDraw))
            System.out.println("Cannot draw - Out of cards/Card not found!");
        else
        {
            library.remove(cardToDraw);
            hand.add(cardToDraw);
            //System.out.println("Draw: " + cardToDraw + " (" + library.cards.size() + " cards left in library)");
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
    }
    
    public void setHand(List<String> hand){
        System.out.println("======== SET HAND BEGIN ========");
        reset();
        this.hand.clear();
        Iterator<String> it = hand.iterator();
        while(it.hasNext())
            draw((String)it.next());
        System.out.println("======== SET HAND END ========");
    }
    
    public void add(String card, int amount){
        //System.out.println("Add " + amount + " " + card + " to the deck");
        for(int i = 0; i < amount; i++)
            library.add(card);
    }
    
    public void addSB(String card, int amount){
        //System.out.println("Add " + amount + " " + card + " to the deck");
        for(int i = 0; i < amount; i++)
            sb.add(card);
    }
    
    public double probabilityToDrawCard(String card, int draws, int amount){
        //System.out.println("======== PROBABILITY TO DRAW " + card + " BEGIN ========");
        int total = library.size(); // 60
        int success = isInDeck(card); // 0-4
        int sample = draws; // 7 for opening hand
        int sampleSuccess = amount; // 0 none, 1 exactly one etc.
        double result = hypergeom(total, success, sample, sampleSuccess);
        //System.out.println("======== PROBABILITY TO DRAW " + card + " END ========");
        return result;
    }
    
    public void probabilityToDrawCardPerTurn(String card){
        System.out.println("======== PROBABILITY TO DRAW " + card + " BEGIN ========");
        int total = library.size(); // 60
        int success = isInDeck(card); // 0-4
        int sample = 7; // 7 for opening hand
        int sampleSuccess = 0; // 0 none, 1 exactly one etc.
        for(int i = 0; i < 7; i++)
        {
            double result = 1 - hypergeom(total, success, sample+i, sampleSuccess);
            System.out.println("Turn " + (i+1) + ": " + result*100 + "%");
        }
        System.out.println("======== PROBABILITY TO DRAW " + card + " END ========");
    }
    
    public void probabilityToDrawHand(List<String> hand){
       System.out.println("======== PROBABILITY TO DRAW HAND BEGIN ========"); 
       System.out.println(hand);
       double probability = 1.0;
       Iterator<String> it = hand.iterator();
       while(it.hasNext())
       {
           String card = (String) it.next();
           probability *= (double) isInDeck(card)/library.size();
           library.remove(card);
       } // Reihenfolge?
       reset();
       System.out.println(probability*100 + "% chance to have this opener");
       System.out.println("======== PROBABILITY TO DRAW HAND END ========");
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
        else if(2*k > n)
            result = binomial(n, n-k);
        else
        {
            result = n-k+1;
            for(int i = 2; i <= k; i++){
                result *= (n-k+i);
                result /= i;
            }
        }
        return result;
    }
    
    public void printHand(){
        if(hand.isEmpty())
            System.out.println("======== HAND EMPTY ========");
        else
        {
            Iterator<String> it = hand.iterator();
            System.out.println("======== HAND BEGIN ========");
            while(it.hasNext()){
                String card = "";
                card = (String) it.next();
                System.out.println(card);
            }
            System.out.println("======== HAND END ========");
        }
    }
    
    public void printLibrary(){
        if(library.isEmpty())
            System.out.println("======== DECKLIST EMPTY ========");
        else
        {
            Iterator<String> it = library.iterator();
            System.out.println("======== DECKLIST BEGIN ========");
            while(it.hasNext()){
                String card = "";
                card = (String) it.next();
                System.out.println(card);
            }
            System.out.println("======== DECKLIST END ========");
        }
    }
    
    public abstract boolean isWin();
    public abstract boolean isMulligan();
    public abstract int play();
}

package at.co.kuehnel.jgoldfish;

import java.lang.reflect.Constructor;

public class JGoldfish {

    static int SIMULATIONS = 1000000;
    static int MAX_TURNS = 10;
    static boolean ON_THE_PLAY = true;
    static String DEFAULT_DECK = "Reanimator";

    public static void main(String[] args) {
        String deck = DEFAULT_DECK;
        if (args.length == 0 || args[0].trim().isEmpty()) {
            System.out.println("No deck specified.");
            System.out.println("Using 'Reanimator (Modern)'.");
        } else {
            deck = args[0].trim();
            System.out.println("Using '" + deck + "'.");
        }
        Simulation test = null;
        String className = Simulation.class.getPackage().getName() + ".Simulation" + deck;
        try {
            Class<?> simulationClass = Class.forName(className);
            Constructor<?> constructor = simulationClass.getConstructor();
            Object instance = constructor.newInstance();
            test = (Simulation) instance;
        } catch (Exception e) {
            System.out.println("Error loading class '" + className + "'. Check your arguments and class names.");
            System.exit(-1);
        }

        if(args.length > 1 && !args[1].trim().isEmpty()) {
            test.otp = args[1].trim().toLowerCase().equals("otp");
        } else {
            test.otp = ON_THE_PLAY;
        }
        System.out.println("On the Play: " + test.otp);

        int simulations = SIMULATIONS;
        try {
            simulations = Integer.parseInt(args[2].trim());
            System.out.println("Simulations: " + simulations);
        } catch (Exception e) {
            System.out.println("No valid number of simulations provided. Using default value: " + SIMULATIONS);
        }

        try {
            test.turnMax = Integer.parseInt(args[3].trim());
            System.out.println("Break on Turn " + test.turnMax);
        } catch (Exception e) {
            System.out.println("No valid turn limit provided. Using default value: " + MAX_TURNS);
            test.turnMax = MAX_TURNS;
        }

        int[] stats = new int[test.turnMax];
        for (int i = 0; i < simulations; i++)
        {
            stats[test.play() - 1] += 1;
        }
        System.out.println("======== STATISTICS ========");
        for (int i = 0; i < test.turnMax; i++)
        {
            System.out.println("Turn " + (i + 1) + ": " + stats[i] + " (" + 100 * ((float)stats[i] / (float)simulations) + "%)");
        }
    }
}

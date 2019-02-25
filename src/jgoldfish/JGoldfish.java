package jgoldfish;

import java.lang.reflect.Constructor;

/**
 *
 * @author h3llsp4wn
 */
public class JGoldfish {

    static int SIMULATIONS = 1000000;
    static int MAX_TURNS = 10;
    static boolean ON_THE_PLAY = false;

    public static void main(String[] args) {
        String deck = "Reanimator";
        if (args.length == 0 || args[0].trim().isEmpty()) {
            System.out.println("No deck specified.");
            System.out.println("Using 'Reanimator (Modern)'.");
        } else {
            deck = args[0].trim();
            System.out.println("Using '" + deck + "'.");
        }
        Simulation test = null;
        String className = "jgoldfish.Simulation" + deck;
        try {
            Class<?> simulationClass = Class.forName(className);
            Constructor<?> constructor = simulationClass.getConstructor();
            Object instance = constructor.newInstance();
            test = (Simulation) instance;
        } catch (Exception e) {
            System.out.println("Error loading class '" + className + "'. Check your arguments and class names.");
            System.exit(-1);
        }
        test.otp = ON_THE_PLAY;
        test.turnMax = MAX_TURNS;
        int[] stats = new int[test.turnMax];
        for (int i = 0; i < SIMULATIONS; i++)
        {
            stats[test.play() - 1] += 1;
        }
        System.out.println("======== STATISTIK ========");
        for (int i = 0; i < test.turnMax; i++)
        {
            System.out.println("Turn " + (i + 1) + ": " + stats[i] + " (" + 100 * ((float)stats[i] / (float)SIMULATIONS) + "%)");
        }
    }
}

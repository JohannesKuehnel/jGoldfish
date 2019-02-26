# jGoldfish
jGoldfish is a Java program to simulate _goldfishing_ [1] of Magic: The Gathering decks. An example file (`SimulationReanimator.java`) for Reanimator (Format: Modern) is provided.

[1] According to [Gamepedia](https://mtg.gamepedia.com/Goldfishing), goldfishing is "the practice of playing without an opponent as in drawing a starting hand and proceeding to continue to play until an opponent who does nothing to stop you from accomplishing the gameplan is defeated".

## Requirements
Running the JAR-File in `dist/` requires the Java Runtime Environemnt (JRE) 8 or later.

If you are compiling the source (`src/`) yourself, you will need the Java Development Kit (JDK) 8 or later.

## Usage
1. Compile the source code (commands are used from project root)
```javac -d bin -sourcepath src src/at/co/kuehnel/jgoldfish/*.java```

2. Run the the code with
```java -cp bin at.co.kuehnel.jgoldfish.JGoldfish```
or optionally with arguments
```java -cp bin at.co.kuehnel.jgoldfish.JGoldfish Reanimator otp 100000 10```

  * Argument 1 (deck) is the name of your simulation class, prefixed with `Simulation`. If you named your class `SimulationReanimator`, the argument should be `Reanimator`. Default is `Reanimator`.
  * Argument 2 (otp) is whether you want to simulate on-the-play (`otp`) or on-the-draw (`otd`) behavior. Default is `otp`.
  * Argument 3 (game limit) is the number of games the program simulates. Default is `1,000,000`.
  * Argument 4 (turn limit) is the maximum number of turns you want a game to be played out. Default is `10`.

The program outputs the number of games won on each turn up to your turn limit. The number shown at the turn limit includes all games which would have ended after this one.

Example Output:
```
C:\Users\h3llsp4wn\Projects\jGoldfish>java -cp bin at.co.kuehnel.jgoldfish.JGoldfish
No deck specified.
Using 'Reanimator (Modern)'.
On the Play: true
No valid number of simulations provided. Using default value: 1000000
No valid turn limit provided. Using default value: 10
======== STATISTICS ========
Turn 1: 0 (0.0%)
Turn 2: 0 (0.0%)
Turn 3: 0 (0.0%)
Turn 4: 0 (0.0%)
Turn 5: 10952 (1.0952%)
Turn 6: 19204 (1.9204%)
Turn 7: 20114 (2.0114%)
Turn 8: 22890 (2.289%)
Turn 9: 23560 (2.356%)
Turn 10: 903280 (90.328%)
```

## Add your own deck

To add your own deck, just copy the file `SimulationReanimator.java` under `src/at/co/kuehnel/jgoldfish/` and name it with the prefix `Simulation` and the suffix `.java`, for example `SimulationBurn.java`.

In the file, change the class and constructor name to `SimulationBurn`. Change the deck list, win condition, mulligan rules etc to your liking.

If you are done, compile your code as described above under *Usage* and run the code with
```java -cp bin at.co.kuehnel.jgoldfish.JGoldfish Burn```

## Extras

In `Simulation.java` you can find some convenience methods to calculate probabilities.

* `probabilityToDrawCard(String card, int draws, int amount)` - Returns the probability of drawing the specific card `card` with `draws` draws and `amount` copies left in the deck.
* `probabilityToDrawCardPerTurn(String card)` - Prints the probabilities of drawing the specific card `card` in a deck for the first seven turns.
* `probabilityToDrawHand(List<String> hand)` - Prints the probability of having a specific opening hand.

## Authors
* **Johannes Kühnel** - https://github.com/JohannesKuehnel

## License
This project is licensed under the AGPL License - see the [LICENSE](LICENSE) file for details
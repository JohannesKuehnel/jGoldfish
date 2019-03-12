# jGoldfish
jGoldfish is a Java program to simulate _goldfishing_ [1] of Magic: The Gathering decks. Example files for decks consisting solely of 2/2 bears (`SimulationBear.java`) and only of Lightning Bolts (`SimulationBear.java`) are available. There are also incomplete example files for Modern Reanimator (`SimulationReanimator.java`) and Ad Nauseam (`SimulationAdNauseam.java`).

[1] According to [Gamepedia](https://mtg.gamepedia.com/Goldfishing), goldfishing is "the practice of playing without an opponent as in drawing a starting hand and proceeding to continue to play until an opponent who does nothing to stop you from accomplishing the gameplan is defeated".

## Requirements
Running the JAR-File in `bin/` requires the Java Runtime Environemnt (JRE) 8 or later.

If you are compiling the source (`src/`) yourself, you will need the Java Development Kit (JDK) 8 or later.

## Usage

### Run the JAR-file
```
java -jar ./bin/jGoldfish.jar
```
or optionally with arguments
```
java -jar ./bin/jGoldfish.jar Bolt otp 100000 10
```

  * Argument 1 (deck) is the name of your simulation class, prefixed with `Simulation`. If you named your class `SimulationBolt`, the argument should be `Bolt`. Default is `Bolt`.
  * Argument 2 (otp) is whether you want to simulate on-the-play (`otp`) or on-the-draw (`otd`) behavior. Default is `otp`.
  * Argument 3 (game limit) is the number of games the program simulates. Default is `1,000,000`.
  * Argument 4 (turn limit) is the maximum number of turns you want a game to be played out. Default is `10`.
  * Argument 5 (debug) enables debug output via the `debug(String msg)` method. You should turn this off, if you use a game limit over 1. Default is `0` (off).

The program outputs the number of games won on each turn up to your turn limit. The number shown at the turn limit includes all games which would have ended after this one.

Example Output:
```
jdk@KRAKEN:/Projects/jGoldfish$ ./run.sh Bolt otp 1000000 10 0
Using 'Bolt'.
On the Play: true
Simulations: 1000000
Break on Turn 10
DEBUG false
======== STATISTICS ========
Turn 1: 0 (0.0%)
Turn 2: 0 (0.0%)
Turn 3: 0 (0.0%)
Turn 4: 476110 (47.611%)
Turn 5: 250272 (25.0272%)
Turn 6: 147820 (14.782%)
Turn 7: 100216 (10.0216%)
Turn 8: 16975 (1.6975%)
Turn 9: 6037 (0.6037%)
Turn 10: 2570 (0.257%)
```

Example Output with Debugging enabled:
```
jdk@KRAKEN:/Projects/jGoldfish$ ./run.sh Bolt otp 1 10 1
Using 'Bolt'.
On the Play: true
Simulations: 1
Break on Turn 10
DEBUG true
======== Start game ========
Draw: Lightning Bolt (59 cards left in library)
Draw: Lightning Bolt (58 cards left in library)
Draw: Lightning Bolt (57 cards left in library)
Draw: Lightning Bolt (56 cards left in library)
Draw: Lightning Bolt (55 cards left in library)
Draw: Lightning Bolt (54 cards left in library)
Draw: Lightning Bolt (53 cards left in library)
======== Take a mulligan ========
Draw: Lightning Bolt (59 cards left in library)
Draw: Lightning Bolt (58 cards left in library)
Draw: Lightning Bolt (57 cards left in library)
Draw: Lightning Bolt (56 cards left in library)
Draw: Lightning Bolt (55 cards left in library)
Draw: Lightning Bolt (54 cards left in library)
======== Take a mulligan ========
Draw: Lightning Bolt (59 cards left in library)
Draw: Lightning Bolt (58 cards left in library)
Draw: Mountain (57 cards left in library)
Draw: Lightning Bolt (56 cards left in library)
Draw: Lightning Bolt (55 cards left in library)
======== Keeping hand ========
======== Turn 1 ========
Playing Mountain
1 land in play.
Casting Lightning Bolt.
3 total damage dealt.
======== Turn 2 ========
Draw: Lightning Bolt (54 cards left in library)
Casting Lightning Bolt.
6 total damage dealt.
======== Turn 3 ========
Draw: Lightning Bolt (53 cards left in library)
Casting Lightning Bolt.
9 total damage dealt.
======== Turn 4 ========
Draw: Lightning Bolt (52 cards left in library)
Casting Lightning Bolt.
12 total damage dealt.
======== Turn 5 ========
Draw: Lightning Bolt (51 cards left in library)
Casting Lightning Bolt.
15 total damage dealt.
======== Turn 6 ========
Draw: Mountain (50 cards left in library)
Playing Mountain
2 lands in play.
Casting Lightning Bolt.
18 total damage dealt.
Casting Lightning Bolt.
21 total damage dealt.
======== STATISTICS ========
Turn 1: 0 (0.0%)
Turn 2: 0 (0.0%)
Turn 3: 0 (0.0%)
Turn 4: 0 (0.0%)
Turn 5: 0 (0.0%)
Turn 6: 1 (100.0%)
Turn 7: 0 (0.0%)
Turn 8: 0 (0.0%)
Turn 9: 0 (0.0%)
Turn 10: 0 (0.0%)
```

### Compile the source code yourself

Just use the shell script `./run.sh` to compile and run the code. Arguments are the same as for the JAR-file.

Alternatively, manually enter the commands:

1. Compile the source code (commands are used from project root)
```
javac -d bin -sourcepath src src/at/co/kuehnel/jgoldfish/*.java
```

2. Run the the code with
```
java -cp bin at.co.kuehnel.jgoldfish.JGoldfish
```
or optionally with arguments
```
java -cp bin at.co.kuehnel.jgoldfish.JGoldfish Reanimator otp 100000 10
```

3. If you want to build a JAR-file yourself, just run `./buildJar.sh`.

## Add your own deck

To add your own deck, just copy one of the provided `SimulationXYZ.java` files under `src/at/co/kuehnel/jgoldfish/` and name it with the prefix `Simulation` and the suffix `.java`, for example `SimulationBurn.java`.

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
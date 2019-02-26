# jGoldfish
jGoldfish is a Java program to simulate _goldfishing_ [1] of Magic: The Gathering decks. An example file for Reanimator (Format: Modern) is provided.

[1] According to [Gamepedia](https://mtg.gamepedia.com/Goldfishing), goldfishing is "the practice of playing without an opponent as in drawing a starting hand and proceeding to continue to play until an opponent who does nothing to stop you from accomplishing the gameplan is defeated".

## Usage
1. Compile the source code
`javac -jgolfish.jar`

2. Run the JAR-File with your parameters (optional)
`java jgoldfish.jar Reanimator otp 100000 10`

  * Argument 1 is the name of your simulation class, prefixed with `Simulation`. If you named your class `SimulationReanimator`, the argument should be `Reanimator`. Default is `Reanimator`.
  * Argument 2 is whether you want to simulate on-the-play (`otp`) or on-the-draw (`otd`) behavior. Default is `otp`.
  * Argument 3 is the number of games the program simulates. Default is `1,000,000`.
  * Argument 4 is the maximum number of turns you want a game to be played out. Default is `10`.

## Extras

In `Simulation.java` you can find some convenience methods to calculate probabilities.

* `probabilityToDrawCard(String card, int draws, int amount)` - Returns the probability of drawing the specific card `card` with `draws` draws and `amount` copies left in the deck.
* `probabilityToDrawCardPerTurn(String card)` - Prints the probabilities of drawing the specific card `card` in a deck for the first seven turns.
* `probabilityToDrawHand(List<String> hand)` - Prints the probability of having a specific opening hand.

## Authors
* **Johannes Kühnel** - https://github.com/JohannesKuehnel

## License
This project is licensed under the AGPL License - see the [LICENSE](LICENSE) file for details
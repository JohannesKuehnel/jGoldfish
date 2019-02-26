package at.co.kuehnel.jgoldfish;

public class SuspendedCard {
  String name;
  int timeCounters;

  public SuspendedCard(String name, int timeCounters) {
    this.name = name;
    this.timeCounters = timeCounters;
  }

  public String getName() {
    return this.name;
  }

  public int removeTimeCounter() {
    return --this.timeCounters;
  }
}
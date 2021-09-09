package exceptions;


public class RobotHasNoHands extends Exception {
    public RobotHasNoHands() { super("Attempting to put item to hands on a robot without hands."); }
}

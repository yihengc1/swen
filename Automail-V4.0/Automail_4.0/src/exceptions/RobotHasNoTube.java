package exceptions;

public class RobotHasNoTube extends Exception {
    public RobotHasNoTube() { super("Attempting to put item to tube on a robot without tube."); }
}

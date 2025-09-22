public class Turn extends Statement {
    Direction direction;

    public Turn(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void interpret(Robot robot, ActivationRecord record) {
        if (direction == Direction.clockwise) {
            robot.turnClockwise();
        } else if (direction == Direction.counterclockwise) {
            robot.turnCounterclockwise();
        }
    }
}

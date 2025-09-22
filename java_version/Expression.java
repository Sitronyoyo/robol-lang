abstract class Expression {
    boolean isLocalVariabel = false;

    abstract int evaluate(Robot robot, ActivationRecord record);
}

public class NumberExp extends Expression {
    int num;

    public NumberExp(int num) {
        this.num = num;
    }

    @Override
    int evaluate(Robot robot, ActivationRecord record) {
        return num;
    }

}

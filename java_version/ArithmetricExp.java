public class ArithmetricExp extends Expression {
    String operator;
    Expression left, right;

    public ArithmetricExp(String operator, Expression left, Expression right) {

        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    int evaluate(Robot robot, ActivationRecord record) {

        int leftValue = left.evaluate(robot, record);
        int rightValue = right.evaluate(robot, record);

        switch (operator) {
            case "+":
                return leftValue + rightValue;
            case "-":
                return leftValue - rightValue;
            case "*":
                return leftValue * rightValue;
            case "/":
                if (rightValue == 0)
                    throw new ArithmeticException("Division by zero");
                return leftValue / rightValue;
            default:
                throw new RuntimeException("Unsupported operator: " + operator);
        }
    }

}

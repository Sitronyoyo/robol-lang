public class BoolExp extends Expression {
    Expression left, right;
    String operator;

    public BoolExp(String operator, Expression left, Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    // 0 is false, anything else is true
    @Override
    int evaluate(Robot robot, ActivationRecord record) {
        int leftValue = left.evaluate(robot, record);
        int rightValue = right.evaluate(robot, record);

        switch (operator) {
            case ">":
                return (leftValue > rightValue) ? 1 : 0; // Returns 1 for true, 0 for false
            case "<":
                return (leftValue < rightValue) ? 1 : 0; // Returns 1 for true, 0 for false
            case "=":
                return (leftValue == rightValue) ? 1 : 0; // Returns 1 for true, 0 for false
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

public class Step extends Statement {

    Expression exp;

    public Step(Expression exp) {
        this.exp = exp;
    }

    @Override
    public void interpret(Robot robot, ActivationRecord record) {

        int numSteps = exp.evaluate(robot, record); // Steps based on evaluation
        robot.step(numSteps);

    }

}

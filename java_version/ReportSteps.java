public class ReportSteps extends Statement {

    @Override
    public void interpret(Robot robot, ActivationRecord record) {
        System.out.println("Total Steps Taken: " + robot.stepsTaken);
    }

}

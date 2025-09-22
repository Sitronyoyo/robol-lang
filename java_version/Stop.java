public class Stop extends Statement {

    @Override
    public void interpret(Robot robot, ActivationRecord record) {
        System.out.println("The result is (" + robot.start.width + ", " + robot.start.height + ")");
    }

}

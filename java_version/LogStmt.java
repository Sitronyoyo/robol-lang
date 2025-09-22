public class LogStmt extends Statement {
    String message;

    public LogStmt(String message) {
        this.message = message;
    }

    @Override
    public void interpret(Robot robot, ActivationRecord record) {
        robot.log(message);
    }

}

import java.util.List;

class Loop extends Statement {
    List<Statement> statements;
    BoolExp condition;

    public Loop(List<Statement> statements, BoolExp condition) {
        this.statements = statements;
        this.condition = condition;
    }

    public void interpret(Robot robot, ActivationRecord record) {
        // write interpreter code here
        while (condition.evaluate(robot, record) != 0) {
            for (Statement sm : statements) {
                sm.interpret(robot, record);
            }
        }

    }
}

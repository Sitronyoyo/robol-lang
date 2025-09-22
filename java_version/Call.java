import java.util.List;

public class Call extends Statement {
    String procName;
    List<Expression> actualParams;

    public Call(String procName, List<Expression> params) {
        this.procName = procName;
        this.actualParams = params;
    }

    @Override
    public void interpret(Robot robot, ActivationRecord record) {
        // find procedure body and run all statements
        ProcDecl proc = robot.findProcedure(procName);
        if (proc != null) {
            proc.execute(robot, actualParams, record);
        } else {
            throw new IllegalArgumentException("Procedure " + procName + " not found.");
        }

    }

}

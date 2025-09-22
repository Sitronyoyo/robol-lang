import java.util.List;

public class ProcDecl {
    private String name;
    private List<String> parameters;
    private List<Statement> body;

    public ProcDecl(String name, List<String> parameters, List<Statement> body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public int getParamSize() {
        return parameters.size();
    }

    public List<String> getParameters() {
        return parameters;
    }

    public List<Statement> getBody() {
        return body;
    }

    public void execute(Robot robot, List<Expression> actualParams, ActivationRecord record) {
        ActivationRecord newRecord = new ActivationRecord();

        // bind
        for (int i = 0; i < parameters.size(); i++) {
            String paramName = parameters.get(i);
            int paramValue = actualParams.get(i).evaluate(robot, record);
            newRecord.setBinding(paramName, paramValue);
            // robot.bindings.put(paramName, paramValue);
        }
        // robot.pushRecord(newRecord);
        robot.stack.push(newRecord);

        // execute statement
        for (Statement statement : body) {
            statement.interpret(robot, newRecord);
        }

        // pop record after calling
        robot.stack.pop();
    }
}

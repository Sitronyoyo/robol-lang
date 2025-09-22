public class Identifier extends Expression {
    String name;

    public Identifier(String idf) {
        this.name = idf; // combination of letters and nums, and startwith letter
    }

    @Override
    int evaluate(Robot robot, ActivationRecord record) {

        if (record != null) {
            if (record.getValue(name) != null) {
                isLocalVariabel = true;
                return record.getValue(name);

            }
        }
        if (robot.bindings.containsKey(name)) {
            return robot.bindings.get(name);
        } else {
            throw new RuntimeException("Variable " + name + " is not defined.");
        }

    }

}

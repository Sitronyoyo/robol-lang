class Assignment extends Statement {
    Identifier idf;
    String operator;

    public Assignment(Identifier idf, String operator) { // operator can be ++ or --
        this.idf = idf;
        this.operator = operator;
    }

    @Override
    public void interpret(Robot robot, ActivationRecord record) {

        try {
            int value = idf.evaluate(robot, record);
            switch (operator) {
                case "++":
                    if (idf.isLocalVariabel) {
                        record.setBinding(idf.name, value + 1);
                    } else {
                        robot.bindings.put(idf.name, value + 1);

                    }

                    break;
                case "--":
                    if (idf.isLocalVariabel) {
                        record.setBinding(idf.name, value - 1);
                    } else {
                        robot.bindings.put(idf.name, value - 1);
                    }
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            // If the variable is not defined, handle the error
            System.out.println(e.getMessage());
        }
    }

}

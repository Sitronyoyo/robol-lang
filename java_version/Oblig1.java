class Oblig1 {
    public static void main(String[] args) {
        TestCode testCode = new TestCode();
        switch (args.length > 0 ? args[0] : "") {
            case "1":
                testCode.runProgram1();
                return;
            case "2":
                testCode.runProgram2();
                return;
            case "3":
                testCode.runProgram3();
                return;
            case "4":
                testCode.runProgram4();
                return;
            case "5":
                testCode.runProgram5();
                return;
            case "6":
                testCode.runProgram6();
                return;
            case "7":
                testCode.runProgram7();
                return;
            case "all":
                testCode.runAll();
                return;
            default:
                System.out.println("USAGE: java Oblig1 1|2|3|4|5|6|7|all");
                return;
        }
    }
}
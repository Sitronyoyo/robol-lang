import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class TestCode {
    // Create the AST based on testing code 1
    // This code is just to help you understand how to create an AST
    void runProgram1() {
        System.out.println("-- Testing Code 1: Simple Example --");
        Grid grid = new Grid(new NumberExp(64), new NumberExp(64));
        HashMap<String, Integer> bindings = new HashMap<>();
        // tart start = new Start(new NumberExp(23), new NumberExp(30));
        List<ProcDecl> procDecls = new ArrayList<>();
        Start start = new Start(23, 30);
        List<Statement> statements = new ArrayList<Statement>();
        statements.add(new Turn(Direction.clockwise));
        statements.add(new Turn(Direction.clockwise));
        statements.add(new Step(new NumberExp(15)));
        statements.add(new Turn(Direction.counterclockwise));
        statements.add(new Step(new NumberExp(15)));
        statements.add(new Turn(Direction.counterclockwise));
        statements.add(new Step(new ArithmetricExp("+", new NumberExp(2), new NumberExp(3))));
        statements.add(new Turn(Direction.counterclockwise));
        statements.add(new Step(new ArithmetricExp("+", new NumberExp(17), new NumberExp(20))));
        statements.add(new Stop());
        Robot robot = new Robot(bindings, procDecls, statements, grid, start);

        Program program = new Program(robot);

        // Run the interpreter
        program.interpret();
        System.out.println();
    }

    // same as runProgram1 but with the AST based on the other example programs
    void runProgram2() {
        System.out.println("-- Testing Code 2: Example with variables -- ");
        Grid grid = new Grid(new NumberExp(64), new NumberExp(64));
        HashMap<String, Integer> bindings = new HashMap<>();
        bindings.put("i", 5);
        bindings.put("j", 3);
        List<ProcDecl> procDecls = new ArrayList<>();
        Start start = new Start(23, 6);
        List<Statement> statements = new ArrayList<Statement>();
        statements.add(new Turn(Direction.counterclockwise));
        statements.add(new Step(new ArithmetricExp("*", new NumberExp(3), new Identifier("i"))));
        statements.add(new Turn(Direction.clockwise));
        statements.add(new Step(new NumberExp(15)));
        statements.add(new Turn(Direction.clockwise));

        // step - - 12 i j
        Expression left = new ArithmetricExp("-", new NumberExp(12), new Identifier("i"));
        Expression steps = new ArithmetricExp("-", left, new Identifier("j"));
        statements.add(new Step(steps));
        statements.add(new Turn(Direction.clockwise));

        // step + (* 2 i) + (* 3 j) 1
        Expression mul2 = new ArithmetricExp("*", new NumberExp(2), new Identifier("i"));
        Expression mul3 = new ArithmetricExp("*", new NumberExp(3), new Identifier("j"));
        Expression plus1 = new ArithmetricExp("+", mul3, new NumberExp(1));
        Expression f = new ArithmetricExp("+", mul2, plus1);

        statements.add(new Step(f));

        statements.add(new Stop());
        Robot robot = new Robot(bindings, procDecls, statements, grid, start);

        Program program = new Program(robot);

        // Run the interpreter
        program.interpret();
        System.out.println();
    }

    void runProgram3() {
        System.out.println("-- Testing Code 3: Example with loop and assignment --");
        Grid grid = new Grid(new NumberExp(64), new NumberExp(64));
        HashMap<String, Integer> bindings = new HashMap<>();
        bindings.put("i", 5);
        bindings.put("j", 4);
        List<ProcDecl> procDecls = new ArrayList<>();
        Start start = new Start(23, 6);
        List<Statement> statements = new ArrayList<Statement>();
        statements.add(new Turn(Direction.counterclockwise));
        statements.add(new Step(new ArithmetricExp("*", new NumberExp(3), new Identifier("i"))));
        statements.add(new Turn(Direction.counterclockwise));
        statements.add(new Step(new NumberExp(15)));
        statements.add(new Turn(Direction.clockwise));
        statements.add(new Turn(Direction.clockwise));
        statements.add(new Step(new NumberExp(4)));
        statements.add(new Turn(Direction.clockwise));

        // while
        List<Statement> loopStatements = new ArrayList<Statement>();
        Identifier j = new Identifier("j");
        loopStatements.add(new Step(j));
        loopStatements.add(new Assignment(j, "--"));

        BoolExp condition = new BoolExp(">", j, new NumberExp(1));

        statements.add(new Loop(loopStatements, condition));
        // stop
        statements.add(new Stop());
        Robot robot = new Robot(bindings, procDecls, statements, grid, start);

        Program program = new Program(robot);

        // Run the interpreter
        program.interpret();
        System.out.println();
    }

    // Testing Code 4: Example with movement over the edge
    void runProgram4() {
        System.out.println("-- Testing Code 4: Example with movement over the edge --");
        Grid grid = new Grid(new NumberExp(64), new NumberExp(64));
        HashMap<String, Integer> bindings = new HashMap<>();
        bindings.put("i", 8);
        List<ProcDecl> procDecls = new ArrayList<>();
        Start start = new Start(1, 1);
        List<Statement> statements = new ArrayList<Statement>();

        // do.. while
        List<Statement> loopStatements = new ArrayList<Statement>();
        Identifier i = new Identifier("i");
        loopStatements.add(new Step(i));
        BoolExp condition = new BoolExp("<", i, new NumberExp(100));
        statements.add(new Loop(loopStatements, condition));

        // stop
        statements.add(new Stop());
        Robot robot = new Robot(bindings, procDecls, statements, grid, start);

        Program program = new Program(robot);

        // Run the interpreter
        program.interpret();
        System.out.println();
    }

    // Testing Code 5: Example with Step Count
    void runProgram5() {
        System.out.println("-- Testing Code 5: Example with simple procedure call --");
        Grid grid = new Grid(new NumberExp(64), new NumberExp(64));

        HashMap<String, Integer> bindings = new HashMap<>();
        bindings.put("x", 1);
        bindings.put("y", 5);

        List<ProcDecl> procDecls = new ArrayList<>();
        // proc 1
        List<String> p1Params = new ArrayList<>();
        p1Params.add("a");
        p1Params.add("b");
        List<Statement> p1Body = new ArrayList<Statement>();
        p1Body.add(new Step(new Identifier("a")));
        p1Body.add(new Turn(Direction.clockwise));
        p1Body.add(new Step(new Identifier("b")));
        procDecls.add(new ProcDecl("p1", p1Params, p1Body));

        Start start = new Start(23, 30);

        List<Statement> statements = new ArrayList<Statement>();

        List<Expression> actualParamers1 = new ArrayList<>();
        actualParamers1.add(new Identifier("x"));
        actualParamers1.add(new Identifier("y"));
        statements.add(new Call("p1", actualParamers1)); // call p1 (x, y)

        statements.add(new Turn(Direction.clockwise));

        List<Expression> actualParamers2 = new ArrayList<>();
        actualParamers2.add(new Identifier("y"));
        actualParamers2.add(new Identifier("x"));
        statements.add(new Call("p1", actualParamers2)); // call p1 (x, y)
        // stop
        statements.add(new Stop());
        Robot robot = new Robot(bindings, procDecls, statements, grid, start);

        Program program = new Program(robot);

        // Run the interpreter
        program.interpret();
        System.out.println();
    }

    void runProgram6() {
        System.out.println("-- Testing Code 6: Example with two procedures --");
        Grid grid = new Grid(new NumberExp(64), new NumberExp(64));

        HashMap<String, Integer> bindings = new HashMap<>();
        bindings.put("x", 3);
        bindings.put("y", 3);

        List<ProcDecl> procDecls = new ArrayList<>();
        // proc 1
        List<String> p1Params = new ArrayList<>();
        p1Params.add("a");
        p1Params.add("b");
        List<Statement> p1Body = new ArrayList<Statement>();
        p1Body.add(new Step(new Identifier("a")));
        p1Body.add(new Step(new Identifier("b")));
        p1Body.add(new Assignment(new Identifier("a"), "--"));
        procDecls.add(new ProcDecl("p1", p1Params, p1Body)); // add p1 to procDecls

        // proc 2
        List<Statement> p2Body = new ArrayList<Statement>();
        p2Body.add(new Step(new Identifier("a")));
        p2Body.add(new Step(new Identifier("b")));
        p2Body.add(new Assignment(new Identifier("b"), "--"));
        procDecls.add(new ProcDecl("p2", p1Params, p2Body)); // add p2 to procDecls

        Start start = new Start(23, 30);

        List<Statement> statements = new ArrayList<Statement>();
        // call p1 (x, y)
        List<Expression> actualParamers1 = new ArrayList<>();
        actualParamers1.add(new Identifier("x"));
        actualParamers1.add(new Identifier("y"));
        statements.add(new Call("p1", actualParamers1)); // add p1 (x, y) to statements

        // call p2(x, y)
        List<Expression> actualParamers2 = new ArrayList<>();
        actualParamers2.add(new Identifier("x"));
        actualParamers2.add(new Identifier("y"));
        statements.add(new Call("p2", actualParamers1)); // add p2 to statements

        statements.add(new Step(new Identifier("x"))); // step x
        statements.add(new Step(new Identifier("y"))); // step y

        // stop
        statements.add(new Stop());

        Robot robot = new Robot(bindings, procDecls, statements, grid, start);

        Program program = new Program(robot);

        // Run the interpreter
        program.interpret();
        System.out.println();

    }

    void runProgram7() {
        System.out.println("-- Testing Code 7: Example with recursive procedure call --");
        Grid grid = new Grid(new NumberExp(64), new NumberExp(64));

        HashMap<String, Integer> bindings = new HashMap<>();
        bindings.put("x", 3);
        bindings.put("y", 3);

        List<ProcDecl> procDecls = new ArrayList<>();
        // proc 1 - params
        List<String> p1Params = new ArrayList<>();
        p1Params.add("a");
        p1Params.add("b");
        // procedure p1 stattements
        List<Statement> p1Body = new ArrayList<Statement>();

        // proc 1 - while in procedure
        List<Statement> loopStatements = new ArrayList<Statement>();
        Identifier a = new Identifier("a");
        Identifier b = new Identifier("b");
        loopStatements.add(new Step(a)); // step a
        loopStatements.add(new Turn(Direction.clockwise)); // turn clockwise
        loopStatements.add(new Step(b)); // step b
        loopStatements.add(new Assignment(a, "--")); // a--
        loopStatements.add(new Assignment(b, "--")); // b --

        List<Expression> actualParams = new ArrayList<>();
        actualParams.add(a);
        actualParams.add(b);
        loopStatements.add(new Call("p1", actualParams)); // call p1 (a, b)
        loopStatements.add(new Step(new Identifier("x"))); // step x

        BoolExp condition = new BoolExp(">", a, new NumberExp(1));

        p1Body.add(new Loop(loopStatements, condition)); // p1Body have only an while-loop

        procDecls.add(new ProcDecl("p1", p1Params, p1Body)); // add p1 to procDecls

        Start start = new Start(23, 30);

        List<Statement> statements = new ArrayList<Statement>();
        // call p1 (x, y)
        List<Expression> actualParamers1 = new ArrayList<>();
        actualParamers1.add(new Identifier("x"));
        actualParamers1.add(new Identifier("y"));
        statements.add(new Call("p1", actualParamers1)); // add p1 (x, y) to statements

        // stop
        statements.add(new Stop());

        Robot robot = new Robot(bindings, procDecls, statements, grid, start);

        Program program = new Program(robot);

        // Run the interpreter
        program.interpret();
        System.out.println();

    }

    void runAll() {
        runProgram1();
        runProgram2();
        runProgram3();
        runProgram4();
        runProgram5();
        runProgram6();
        runProgram7();
    }
}

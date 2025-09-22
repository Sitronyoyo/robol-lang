import java.util.HashMap;
import java.util.List;
import java.util.Stack;

class Robot {
    HashMap<String, Integer> bindings; // gloable variables
    List<Statement> statements;
    List<ProcDecl> procDecls;
    Stack<ActivationRecord> stack = new Stack<ActivationRecord>();
    ActivationRecord global = new ActivationRecord();

    String facing = "east";

    Grid grid;
    Start start;

    int stepsTaken = 0;

    public Robot(HashMap<String, Integer> bindings, List<ProcDecl> procDecls, List<Statement> statements, Grid grid,
            Start start) {
        this.bindings = bindings;
        this.procDecls = procDecls;
        this.statements = statements;
        this.grid = grid;
        this.start = start;

    }

    public void interpret() {
        // interpret each statement in the statement list
        for (Statement statement : statements) {
            // robot may go out of bound, so we need to catch exception
            try {
                statement.interpret(this, null);

            } catch (Exception e) {
                System.out.println(e.getMessage() + "\n");
                return;
            }

        }
    }

    // find procedure by name and parameter size
    public ProcDecl findProcedure(String procName) {
        for (ProcDecl proc : procDecls) {
            if (proc.getName().equals(procName)) {
                return proc;
            }
        }
        return null;
    }

    public void log(String message) {
        switch (message) {
            case "Operation completed":
                System.out.println("Operation completed");
                break;
            case "Starting operation":
                System.out.println("Expected Outputs:\\");
                System.out.println("Starting operation\\");
                break;
            default:
                System.out.println(message + "\\");
                break;
        }
    }

    // Methods to move and rotate the robot
    public void step(int steps) {
        int newX = start.width, newY = start.height;
        switch (facing) {
            case "east":
                newX += steps;
                break;
            case "west":
                newX -= steps;
                break;
            case "south":
                newY -= steps;
                break;
            case "north":
                newY += steps;
                break;
            default:
                break;
        }

        if (!grid.isValidPosition(newX, newY)) {
            throw new RuntimeException("Error: Robot can't move outside the grid");
            // System.out.println("Error: Robot can't move outside the grid");
        } else {
            start.setX(newX);
            start.setY(newY);
            stepsTaken += steps;
        }

    }

    public void turnClockwise() {
        switch (facing) {
            case "east":
                facing = "south";
                break;
            case "south":
                facing = "west";
                break;
            case "west":
                facing = "north";
                break;
            case "north":
                facing = "east";
                break;
            default:
                break;
        }
    }

    public void turnCounterclockwise() {
        switch (facing) {
            case "east":
                facing = "north";
                break;
            case "south":
                facing = "east";
                break;
            case "west":
                facing = "south";
                break;
            case "north":
                facing = "west";
                break;
            default:
                break;
        }
    }

}
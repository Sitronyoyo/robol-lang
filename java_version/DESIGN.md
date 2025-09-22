## How to run code:
> javac *.java

> java Oblig1 1
The result is (13,52)

> java java Oblig1 2
The result is (18,17)

> java Oblig1 3
The result is (12, 12)

> java java Oblig1 4
Error: Robot can't move outside the grid

> java Oblig1 5
The result is (19, 26)

> java Oblig1 6
The result is (41,30)

> java Oblig1 6
The result is (16,30)

>java Oblig1 all
rull all programs


## Key Components
The core components are:

1. Program: Represents the entire ROBOL program. It contains the Robot and interprets the robot's actions.

2. Robot: Manages the robot's state, including its position, direction, and variable bindings. It also holds the 

procedure definitions and a runtime stack for procedure calls.

3. Statements: Represent individual commands (e.g., turn, step, procedure call). Each statement implements the Statement interface, and different types of statements have their own concrete classes.

4. Expressions: Handle numerical and boolean expressions used within statements (e.g., in loops or procedure parameters).

5. ActivationRecord: Represents the procedure's activation record, storing variable bindings and handling the runtime environment for procedure calls.

## Detailed Design
1. Program Class
The Program class holds the Robot and is responsible for initiating the interpretation. It invokes the robot's interpret() method to start the execution of the ROBOL program.

2. Robot Class
The Robot class is central to the interpreter, handling the following tasks:

Storing the robot's state, including its position (x, y) and facing direction.
Managing global variable bindings.
Handling the runtime stack for procedure calls using ActivationRecord.
Interpreting individual statements.

3. Statement(abstract) and Subclasses
The Statement provides a common method interpret(Robot, ActivationRecord) for interpreting individual statements. There are multiple concrete subclasses:

    Turn: Changes the robot's direction (clockwise/counterclockwise).
    Step: Moves the robot forward a specified number of steps.
    Assignment: Updates variable values. We have to check wheather the variabel comes from local or global
    Loop: Executes statements repeatedly based on a condition.
    Call: Executes a procedure with arguments.

4. Expression(abstract) and Subclasses
Expressions handle evaluations within statements. For instance, arithmetic expressions are used in loops and assignments. Expressions can be:

    NumberExp: Represents a constant number.
    Identifier: Represents a variable and fetches its value from the current activation record or global scope.
    BinaryExp: Performs arithmetic or boolean operations.  0 is false, anything else is true
    ArithmeticExp: Performs arithmetic operations.

5. Activation Record and Stack
The ActivationRecord class stores local variable bindings for each procedure call. The Robot maintains a stack of ActivationRecord instances to track active procedures. When a procedure is called, a new record is pushed onto the stack, and it's popped once the procedure returns.

Handling Procedure Calls and Recursion:
When a procedure is called, the interpreter creates a new ActivationRecord and binds the procedure parameters to their corresponding argument values. The statements in the procedure body are then executed in the context of this record. The record is popped from the runtime stack when the procedure exits. The interpreter also supports recursion, as shown in Test Code 7, where p1 calls itself.
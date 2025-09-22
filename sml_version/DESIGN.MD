# How to run code(windows):

>sml Robot.sml p1 
>sml Robot.sml p2 
>sml Robot.sml p3 
>sml Robot.sml p4 
>sml Robot.sml p5 
>sml Robot.sml p6 

# Design details:

## Main Data Types:

    Expressions(exp): Represents various types of expressions (e.g., identifiers, arithmetic operations).
                    function evalExp is used to calculate the int value

            N: Represents a constant number.
            Identifier: Represents a variable and fetches its value from the current activation record or global scope.
            Plus, Minus, Mutiply, LessThan, GreatThan, Equal: Do mathmatical and logic operations
       
    Statements(stmt): Represents actions the robot can perform (e.g., move, turn, stop, and procedure calls).
                    function interpret is used to perform robot's movement
            
            Turn: Changes the robot's direction (clockwise/counterclockwise).
            Step: Moves the robot forward a specified number of steps.
            Assignment: Updates variable values. We have to check wheather the variabel comes from local or global
            Loop: Executes statements repeatedly based on a condition.
            Call: Executes a procedure with arguments.

    Procedures(procdel): Defined using the Proc datatype, which holds the procedure name, parameter list, and the body of the procedure.

## The interpreter maintains two separate environments:
    Global Environment(varList): Holds global variable bindings.
    Local Environment(localList): Used during procedure execution to bind parameters to actual values.


## Runing order:
    1. Go through binding list and make a global list for all bindings
    2. Call interpret for each statement in statements list and see how the robot is moving
    2. If encounter Stop-stmt, print current position and syop procedure. 


# project description 

In this project, you are going to write a small interpreter for a simple language for controlling a robot on a 2-dimensional grid. 
The language is called *ROBOL*, a clever acronym for *ROBOT LANGUAGE*, and its grammar is defined below.

The grid on which the robot can move about is defined by its x and y bounds, for instance: 

![](./images/grid.png)

The robot has a direction which can be changed in 90 degree increments either clockwise or counter-clockwise, it always starts out facing east. 
It can only be moved forwards and will do so in the direction it is facing. 

The grid above is defined by the bound (7, 6), the robot is currently located at position (3, 3) and facing east.
Moving the robot 1 step forward would put it at (4, 3).
Turning it clockwise once and moving it forward (south) 1 step would put it at (3, 2), etc.

## Tasks

Make an interpreter for the *ROBOL* language in your object-oriented language of choice.
The interpreter shall operate on an *abstract syntax tree (AST)* representing a *ROBOL* program.
You **do not** need to write a parser for the language (however, if you want to rise to the challenge, feel free!).
You can design the classes for the AST as you like, but they should provide a somewhat faithful representation of the grammar listed below.
The outermost element `program` from the grammar should be represented by a class `Program` that provides an `interpret` method which will interpret the entire program.

### Languages
- Java
- SML (Starndard ML)


## Requirements
- The interpreter must check that the poor robot does not fall off the edge of the world (i.e., moves beyond the bounds of the grid).
- You can display the state of the program in any form you like during execution, but at minimum, the program should, upon termination, print its state in the form of the current location of the robot.
- There are some test ROBOL programs below. You must check that your implementation returns the correct result after running these programs, and include instructions on how to run their AST representations with your implementation.
- Write a design document that explains how you have implemented the interpreter, and why you have done it in this way.
 Furthermore, the document should explain how to run your program from the Linux command line.
- It should be possible to use the program from the command line like this: `<ProgramName> 1|2|3|...|all`  
  **Example**: A Java program with the main method in a class called *Oblig1*  
  `java Oblig1 2` should run test code 2 and print the results.  
  `java Oblig1 all` should run all the test programs and print the results.  
  There is an implementation of this provided in the program sketch (see separate files for Java and Python versions).
- At a minimum, upon program termination, the current location of the robot should be printed.



![](./images/image--004.png)

## ROBOL Grammar

Words in angle brackets (e.g. `<expression>`) denote non-terminals, while symbols in quotes (e.g. `"+"`) denote terminals in the grammar.

```ebnf
(* a program consists of a robot, and a grid on which it can move around *)
<program> ::= <grid> <robot> ;

(* size of the grid given as a bound for the x axis and the y axis.
   both axes start at 0, <number> is a positive integer. *)
<grid> ::= "size" "(" <number> "*" <number> ")" ;

(* the robot has a list of variable bindings, a list of procedures,
   a starting point and a list of statements that control its movement *)
<robot> ::= <binding>* <proc_decl>* <start> <stmt>* ; 

(* a variable binding consists of a name and an initial value *)
<binding> ::= "let" <identifier> = <number> ;

(* a procedure can have formal parameters parameters, but does not 
   return values; the parameters are not explicitly typed,
   but procedures (and the whole program) are assumed to be type 
   correct; procedure do not have variable bindings, but can use
   variables from the global scope as well as the parameters *)
<proc_decl> :: = "proc" <identifier> "(" <proc_params>? ")" <proc_body> ;
<proc_params> ::=  <identifier> ("," <identifier>)* ;
<proc_body> :: = "{" <stmt>* "}" ;

(* statements control the robot's movement *)
<stmt> ::= <stop> 
         | "turn" <direction> 
         | "step" <exp>
         | <assignment> 
         | <loop> 
         | <call> ;

(* start gives the initial position for the robot *)
<start> ::= "start" "(" <exp> "," <exp> ")" ;
<stop>  ::= "stop" ;

(* change of direction happens in 90 degree increments. 
After turning the robot is facing either north, east, south or west *)
<direction> ::= "clockwise" | "counterclockwise" ;

(* increment or decrement a variable in steps of 1 *)
<assignment> ::= <identifier> "++" | <identifier> "--" ;

(* a loop will keep executing statements as long as a condition is met *)
<loop>  ::= "while" <boolean_exp> "{" <stmt>* "}" ;

(* a procedure call names the procedure to be called, and supplies
   the right number of parameters in the form of 0 to many expressions;
   ROBOL does not support procedure overloads  *)
<call>  ::= "call" <identifier> "(" <actual-params>? ")" ;
<actual-params> ::= <exp> ( "," <exp> )* ;

(* expressions; number is an integer, identifier is a string of
   letters and numbers, starting with a letter *)
<exp> ::= <identifier> 
        | <number> 
        | "(" <exp> ")"
        | <arithmetic_exp> 
        | <boolean_exp> ;

(* boolean expressions should be calculated as integer values. 0 should be 
   interpreted as false, anything else as true *)
<boolean_exp>    ::= <arithmetic_exp> ;
<arithmetic_exp> ::= <binary_op> <exp> <exp> ;
<binary_op>  ::= "+" | "-" | "*" | "<" | ">" | "=" ;

<stmt> ::= ... | "reportSteps" ;

<stmt> ::= ... | "log" <message>
<message> ::= """ [a-zA-Z0-9\s]+ """
```

### Hints and additional information:
- You may assume that expressions are type-correct (so you do not have to implement a type checker). You can assume that no
  one writes programs that tries to add booleans and numbers, for instance.

- You need to implement a runtime stack with activation records for procedure calls. For this you can use a standard stack implementation available in most languages' standard libraries, for instance the `java.util.Stack<T>` class. Each activation record needs a reference to the global environment (the program variables) via a static link, but dynamic links, return pointers, etc, are not needed. Before you start executing a procedure, the new record should be pushed to the stack, and when you exit, you pop it. You probably(?) do not need to implement an environment pointer.

- Parameters to procedures are passed by value. The language has static scope. Procedures cannot be declared within other procedures. Procedures cannot declare their own variables, but can use the parameters and global variables.

- The robot probably needs to have a reference to the grid, and the statements probably need to have a reference to the robot. The robot could hold the runtime stack. These things can be achieved in many ways, choose one that fits with your overall design.


## Example programs:

#### Testing Code 1: Simple Example

```c
size(64*64)
start(23,30)
turn clockwise
turn clockwise
step 15
turn counterclockwise
step 15
turn counterclockwise
step + 2 3
turn counterclockwise
step + 17 20
stop
```

The result is (13,52)

#### Testing Code 2: Example with variables

```c
size(64*64)
let i = 5
let j = 3
start(23,6)
turn counterclockwise
step * 3 i
turn clockwise
step 15       
turn clockwise
step - - 12 i j      
turn clockwise
step + (* 2 i) + (* 3 j) 1
stop
```
The result is (18,17)

#### Testing Code 3: Example with loop and assignment
```c
size(64*64)
let i = 5
let j = 4
start(23,6)
turn counterclockwise
step * 3 i
turn counterclockwise
step 15
turn clockwise
turn clockwise
step 4
turn clockwise
do while > j 1 {
    step j
    j--
} 
stop
```

The result is (12, 12)

#### Testing Code 4: Example with movement over the edge

```c
size(64*64)
let i = 8
start(1,1)
while < i 100 {
    step i
} 
stop
```

The program should stop with an exception saying that the robot fell off the world

#### Testing Code 5: Example with simple procedure call
```c
size(64*64)
let x = 1
let y = 5
proc p1 ( a, b) 
{
    step a
    turn clockwise
    step b
}
start(23,30)
call p1 (x, y)
turn clockwise
call p1 (y, x)
stop
```

The result is (19, 26)

#### Testing Code 6: Example with two procedures
```c
size(64*64)
let x = 3
let y = 3
proc p1 ( a, b) 
{
    step a
    step b
    a--
}
proc p2 ( a, b) 
{
    step a
    step b
    b--
}
start(23,30)
call p1 (x, y)
call p2 (x,y)
step x
step y
stop
```
The result is (41,30)


#### Testing Code 7: Example with recursive procedure call
```c
size(64*64)
let x = 3
let y = 3
proc p1 (a, b) 
{
    while > a 1 {
        step a
        turn clockwise
        step b  
        a --
        b --
        call p1 (a, b)
        step x
    } 
}
start(23,30)
call p1 (x, y)
stop
```
The result is (16,30)



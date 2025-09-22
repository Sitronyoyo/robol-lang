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


## Theory questions

1)
Explain why expressions never need parantheses. Explain why they would need parantheses if <arithmetic_exp> was implemented as follows:
```ebnf
<arithmetic_exp> ::= "(" <arithmetic_op> <args> ")" ;
(* at least 2 arguments, could be more *)
<args> ::= <exp> <exp>+ ;
```

2)
Explain how you could use the arithmetic operators to define the boolean operators "AND" and "OR". You may assume booleans are non-negative numbers.
For an extra challenge, try to define "XOR" using the arithmetic operators. 

answer:
A AND B = A * B
A OR B = min(A + B, 1)
A XOR B = A + B - 2 * A * B



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





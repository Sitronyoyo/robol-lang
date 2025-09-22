# Mandatory Exercise 2 IN3040

Write	an	interpreter	for	the	ROBOL	language	in	a functional language,	with	the	same requirements,	and	the	same	example	programs,	as	for	Mandatory	Exercise	1.

### Approved Languages
- SML
- Haskell

## Deliverables
- The	entire	program and	the	design	document	should	be	placed	in	a	single	`.zip`	file
- The	name	of	the	file	should	be	`IN3040_Mandatory2_<username>.zip`
- The	submission	is	done	through	Devilry:	https://devilry.ifi.uio.no/
- **Deadline**:	*2024-11-01 23:59*

## Program sketch
You	may	use	this	sketch	as	a	starting	point	for	your	implementation.	You	are
allowed	to	change	these	declarations	as	you	see	fit.

```sml
type ident = string;

datatype direction = CW | CCW;
datatype grid = Size of int * int;
datatype exp =
   Identifier of ident
 | N of int
 | BiggerThan of exp * exp
 | LessThan of exp * exp
…

datatype stmt =
    Turn of direction
  | Step of exp
  | Assignment of (ident * bool) (* example: true -> ++ *)
    …

datatype procdecl = Proc of ident * ident list * stmt list;

type binding = ident * int;
datatype robot = Robot of binding list * procdecl list * start * stmt list …

datatype program = Program of grid * robot;

fun evalExp decls (BiggerThan (e1, e2)) =
  let val eval = evalExp decls in
  if eval e1 > eval e2
    then 1
    else 0 end;
…

exception OutOfBounds;

(* Example program skeletons, feel free to adapt: *)
val prog1 = Program (Size (64, 64), Robot ([], [], Start (23,30), [CW, CW, Step (N(15)), CW, ..., Step (Plus ((N 2), (N 3))), ...]));
val prog5 = Program (Size (64, 64), Robot ([("x",1),("y",5)], [Proc ("p1", ["a","b"], [...])], Start (23,30), [...]);
```

The `interpret`-function should either throw an exception, or return the current position of the robot:

```sml
fun interpret ( Program(Size(x, y),
  Robot(vardecls, procdecls, Start(xpos, ypos), statements : stmt list))) : (int * int) = …
```

Use pattern matching on the left-hand side of function declarations for readability where appropriate.

Because	this	is	an	exercise	in	functional	programming,	you	should	not	use	any
assignment statements	in	your	SML	code except `let`-constructs. Do not use SML's built-in [HashTable](https://www.smlnj.org/doc/smlnj-lib/Util/str-HashTable.html). You may use the [Basis Library structures](https://smlfamily.github.io/Basis/overview.html) such as `Option` and `List`.
If you use Haskell, do not use the `do`-notation or _monads_.

Instead, to manage you variables, you should implement an
association list (a	list	of	key-value	pairs)	with	the	following	functions	to operate	on	the	assoc	list:
```sml
type assocList = (string * int) list;
fun lookup (key:string) (list:assocList) : int option = ...
fun add (key:string) (value:int) (list:assocList) : assocList = ...
fun change (key:string) (value:int) (list:assocList) : assocList = map ...
```
### Tips:
`lookup`: use	`foldr` and	the	`option` data	type  
`change`: use	map

The following example illustrates command line argument-handling (replace `main` with picking the right program/all programs.
Make sure to use `exit` at the end of your code to avoid an annoying warning from the interpreter:

```sml
fun main () = case CommandLine.arguments () of
    [] => print ("No args\n")
  | [x] => print ("Got: " ^ x ^ "\n")
  | _ => print ("Why did you give me " ^ Int.toString (length args) ^ " arguments?!\n");

val _ = main ();
val _ = OS.Process.exit(OS.Process.success);
```

## ROBOL Grammar revisited (unchanged from Oblig 1)

Words in angle brackets (e.g. `<expression>`) denotes non-terminals, while symbols in quotes (e.g. `"+"`) denotes terminals in the grammar.

```ebnf
(* a program consists of a robot, and a grid on which it can move around *)
<program> ::= <grid> <robot> ;

(* size of the grid given as a bound for the x axis and the y axis.
   both axes start at 0, <number> is a positive integer. *)
<grid> ::= "size" "(" <number> "*" <number> ")" ;

(* the robot has a list of variable bindings, a starting point, and a
   a set of statements that control its movement *)
<robot> ::= <binding>* <start> <stmt>* ;

(* a variable binding consists of a name and an initial value *)
<binding> ::= "let" <identifier> = <exp> ;

(* statements control the robot's movement *)
<stmt> ::= <stop>
         | "turn" <direction>
         | "step" <exp>
         | <assignment>
         | <loop> ;

(* start gives the initial position for the robot *)
<start> ::= "start" "(" <exp> "," <exp> ")" ;
<stop>  ::= "stop" ;

(* change of direction happens in 90 degree increments.
After turning the robot is facing either north, east, south or west *)
<direction> ::= "clockwise" | "counterclockwise" ;

<assignment> ::= <identifier> "++" | <identifier> "--" ;

(* a loop will keep executing statements until a condition is met *)
<loop>  ::= "do" "{" <stmt>* "}" <while> ;
<while> ::= "while" <boolean_exp> ;

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
```

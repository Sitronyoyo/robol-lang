(*datatypes*)
infix 6 +
infix 6 -
infix 7 *
infix 5 <
infix 5 >
infix 4 =

type ident = string;
datatype direction = CW | CCW;  (* instance: CW or CCW*)

datatype grid = Size of int * int; (* Size(64, 64)*)

datatype exp = 
      Identifier of ident   (* Identifier "x"*)
    | N of int              (* N 5 or N(5)*)
    | Plus of exp * exp     (* Plus (2, 5)*)
    | Minus of exp * exp     (* Minus(6, 2)*)
    | Multiply of exp * exp
    | LessThan of exp * exp
    | GreaterThan of exp * exp
    | Equal of exp * exp;

datatype stmt = 
      Stop
    | Turn of direction  (* Turn(CW) or Turn CW*)
    | Step of exp        (* Step 5 or Step(5)*)
    | Assignment of (exp * bool)   (* true for increment (++), false for decrement (--) *)
    | Loop of stmt list * exp
    | Call of string * exp list;

datatype procdecl = Proc of ident * string list * stmt list;  (* Proc("a", ["a", "b"], [])*)


datatype start = Start of int * int;  (* Start(3,3) *)

datatype binding = Bind of ident * exp;  (* Bind("X", 1), use change func *)

datatype robot = Robot of binding list * procdecl list * start * stmt list;

datatype program = Program of grid * robot;

datatype facing = East|West|South|North;
(* Exception for out-of-bounds errors *)
exception OutOfBounds of string;

(* manage variables *)
type assocList = (string * int) list; (* Type alias for the association list for variable bindings *)

(* if found variabel, return value, else NONE*)
fun lookup (key: string, list: assocList) : int option =
    (* List.find (fn (k, _) => k = key) env |> Option.map snd *)
    foldr (fn ((k, v), acc) => if k = key then SOME v else acc) NONE list;

(* Change function to update the value of an existing variable, return a new list *)
fun change (key: string, value: int, lst: assocList): assocList =
    let
      
        val updatedList = map (fn (k, v) =>
            if k = key then (k, value)  (* if find same key, update *)
            else (k, v)  (* else keep original *)
        ) lst;

        (* check if the key is in list *)
        fun exists k = List.exists (fn (x, _) => x = k) lst;

    in
        if exists key then updatedList  (* if exist, return updateList *)
        else ( (key, value) :: updatedList )  (* else, add (key,value) to list *)
    end;

(* Helper function to get the current facing  *)
fun getFacing(fac, dir)=
  case (fac, dir) of
      (East, CW)  => South
    | (East, CCW) => North
    | (South, CW) => West
    | (South, CCW) => East
    | (West, CW) => North
    | (West, CCW) => South
    | (North, CW) => East
    | (North, CCW) => West;

(* For step: Helper function to move the robot based on the current direction, x,y are current position *)
fun move (x, y, fac, maxX, maxY, steps) =
    let
        val (newX, newY) =
            case fac of
                East => (x + steps, y)
              | South => (x, y - steps)
              | West => (x - steps, y)
              | North => (x, y + steps);
        val _ = print("("^ Int.toString newX ^ ", " ^ Int.toString newY ^ ")\n");
    in
        (* Check if the new position is out of bounds *)
        if newX < 0 orelse newX >= maxX orelse newY < 0 orelse newY >= maxY then
            (print ("Robot moved out of bounds\n");
            raise OutOfBounds ("Robot moved out of bounds" ))
        
        else           
            (newX, newY)  (* Return the valid new position *)
    end

(*function for exp: Evaluate an expression to get its int-value*)
fun evalExp decls (N n) = n
  | evalExp decls (Identifier name) =  (case lookup (name, decls) of
                SOME value => value  
              | NONE => 0)            
  | evalExp decls (Plus (e1, e2)) = evalExp decls e1 + evalExp decls e2
  | evalExp decls (Minus (e1, e2)) = evalExp decls e1 - evalExp decls e2
  | evalExp decls (Multiply (e1, e2)) = evalExp decls e1 * evalExp decls e2
  | evalExp decls (LessThan (e1, e2)) = 
        if evalExp decls e1 < evalExp decls e2 then 1 else 0
  | evalExp decls (GreaterThan (e1, e2)) = 
        if evalExp decls e1 > evalExp decls e2 then 1 else 0
  | evalExp decls (Equal (e1, e2)) = 
        if evalExp decls e1 = evalExp decls e2 then 1 else 0;

(*function for stmt*)
fun interpret (Program(Size(maxX, maxY), Robot(vardecls, procdecls, Start(startX, startY), statements))) =
    let
        val currentX = ref startX;
        val currentY = ref startY;
        val currentFacing = ref East;
        val varList = ref [];
        val localList = ref [];

        fun interpretBinding (Bind(key,e):binding) = 
            let 
                val value = evalExp (!varList) e
                       
            in
                varList := change(key,value, !varList)
            end;
        val _ = List.app interpretBinding vardecls;
        
        (* 打印 assocList 的函数 *)
        fun printAssocList [] = 
            print "[]\n"  (* 如果列表为空，打印空列表 *)
            | printAssocList ((key, value) :: xs) = 
                (print ("(" ^ key ^ ", " ^ Int.toString value ^ ") ");  (* 打印当前元素 *)
                printAssocList xs);  (* 递归打印剩余元素 *)
        val _ = printAssocList (!varList);  (* 输出: 1 2 3 4 5 *)
        
        fun findProc(name: string) : procdecl =
            case List.find (fn Proc(n, _, _) => n = name) procdecls of
                SOME(proc) => proc
                | NONE => raise OutOfBounds ("Procedure " ^ name ^ " not found")
        
        fun interpretStatement (stm:stmt, isLocal:bool) =
            case stm of
                Stop => 
                    print ("Stopped at: (" ^ Int.toString (!currentX) ^ ", " ^ Int.toString (!currentY) ^ ")\n")
              
              | Turn dir => 
                    currentFacing := getFacing (!currentFacing, dir)
               
              | Step e => 
                    let
                        (* val steps = if isLocal then evalExp (!localList) e else evalExp (!varList) e; *)
                        val steps = if isLocal then
                            let 
                                val lv = evalExp (!localList) e
                            in 
                                if lv = 0 then  evalExp (!varList) e else lv
                            end
                        else
                            evalExp (!varList) e;

                        val (newX, newY) = move (!currentX, !currentY, !currentFacing, maxX,maxY,steps)
                       
                    in
                        currentX := newX;
                        currentY := newY
                       
                    end
              | Assignment (Identifier var, inc) => 
                    
                    let
                        val value = if isLocal then evalExp (!localList) (Identifier var) else evalExp (!varList) (Identifier var);
                        (* val value = evalExp (lst) (Identifier var); *)
                        val newValue = if inc then value + 1 else value - 1
                               
                    in
                        if isLocal then localList := change (var, newValue, !localList) else
                            varList := change (var, newValue, !varList)                      
                    end

              | Loop (loopStmts, conditionExp) => 
               
                    let
                        val conditionValue = ref (if isLocal then evalExp (!localList) conditionExp else evalExp (!varList) conditionExp)  
                    in
                       
                        while (!conditionValue) > 0 do 
                            
                        ( List.app(fn stm => interpretStatement(stm, isLocal)) loopStmts;
                           
                        conditionValue := (if isLocal then evalExp (!localList) conditionExp else evalExp (!varList) conditionExp))
                            
                    end
              | Call (procName, actualParams) =>
                    let
                       
                        (* find procedure *)
                        val (Proc(procName, params, body)) = findProc(procName);
                        
                        (* calcuate actual values *)
                        (* val paramValues = List.map (fn exp => evalExp (!varList) exp) actualParams; *)
                        val paramValues = if isLocal then List.map (fn exp => evalExp (!localList) exp) actualParams
                                            else List.map (fn exp => evalExp (!varList) exp) actualParams;
                        (* bind procParams with actualVulues *)
                        val zipped = ListPair.zip(params, paramValues)

                    in
                        (localList := zipped;
                        (* go throuth all statments *)
                        List.app (fn stm => interpretStatement (stm, true)) body;
                        localList := [])

                        
            end


        (* go through list *)
        val _ = List.app (fn stm => interpretStatement (stm, false)) statements
    in
        
        (!currentX,!currentY)  (* return values *)
    end



val prog1 = Program (Size (64, 64), Robot ([], [], Start (23,30), 
            [Turn CW, Turn CW, Step (N 15), Turn CCW, Step (N 15),Turn CCW, Step (Plus (N 2, N 3)), Turn CCW,Step (Plus (N 17, N 20)),Stop]));

val prog2 = Program (Size (64, 64), Robot ([Bind("i",N 5),Bind("j",N 3)], [], Start (23,6), 
            [Turn CCW, Step (Multiply (N 3, Identifier "i")),Turn CW,Step (N 15),
            Turn CW,Step (Minus (Minus (N 12, Identifier "i"), Identifier "j")),Turn CW,
            Step (Plus (Multiply (N 2, Identifier "i"), Plus (Multiply (N 3, Identifier "j"), N 1))),
            Stop]));

val loopStmts3 = [Step (Identifier "j"), Assignment(Identifier "j", false)];
val prog3 = Program (Size (64, 64), Robot ([Bind("i",N 5),Bind("j",N 4)], [], Start (23,6), 
            [Turn CCW, Step (Multiply (N 3, Identifier "i")),Turn CCW, Step (N 15), Turn CW, Turn CW,Step (N 4),Turn CW, 
            Loop(loopStmts3, GreaterThan(Identifier "j", N 1)),Stop]));

val loopStmts4 = [Step (Identifier "i")];
val prog4 = Program (Size (64, 64), Robot ([Bind("i",N 8)], [], Start (1,1), 
            [Loop(loopStmts4, LessThan(Identifier "i", N 100)) ,Stop]));

val procs5 = [Proc("p1", ["a", "b"], [Step (Identifier "a"), Turn CW, Step(Identifier "b")])];
val prog5 = Program (Size (64, 64), Robot ([Bind("x",N 1), Bind("y",N 5)], procs5, Start (23,30), 
            [Call ("p1", [Identifier "x", Identifier "y"]), Turn CW, Call ("p1", [Identifier "y", Identifier "x"]), Stop]));

val procs6 = [Proc("p1", ["a","b"],[Step(Identifier "a"), Step(Identifier "b"), Assignment(Identifier "a", false)]),
     Proc("p2", ["a","b"],[Step(Identifier "a"), Step(Identifier "b"), Assignment(Identifier "b", false)])]

val prog6 = Program (Size (64, 64), Robot ([Bind("x",N 3), Bind("y",N 3)], procs6, Start (23,30), 
            [Call ("p1", [Identifier "x", Identifier "y"]),Call ("p2", [Identifier "x", Identifier "y"]), Step (Identifier "x"),Step (Identifier "y"), Stop]));

(* val loopStmts7 = [Step(Identifier "a"), Turn CW, Step(Identifier "b"), Assignment(Identifier "a", false), Assignment(Identifier "b", false),Call("p1",[Identifier "a", Identifier "b"]), Step(Identifier "x")];
val procs7 = [Proc("p1", ["a", "b"], [Loop (loopStmts7, GreaterThan(Identifier "a", N 1))])];
val prog7 = Program (Size (64, 64), Robot ([Bind("x",N 3), Bind("y",N 3)], procs7, Start (23,30), 
            [Call ("p1", [Identifier "x", Identifier "y"]),Stop])); *)

fun main () = 
    let
        val args = CommandLine.arguments()
    in
        case args of
            [] => print ("No args\n")
          | [x] => 
                (print ("Got: " ^ x ^ "\n");
                case x of
                     "p1" => (interpret(prog1); ())  (*Testing Code 1: (13,52)*)
                   | "p2" => (interpret(prog2); ())  (*Testing Code 2: (18,17)*)
                   | "p3" => (interpret(prog3); ())  (*Testing Code 3: (12,12)*)
                   | "p4" => (interpret(prog4); ())  (*Testing Code 4: fell down*)
                   | "p5" => (interpret(prog5); ())   (*Testing Code 5 : (19,26)*)
                   | "p6" => (interpret(prog6); ())  (*Testing Code 6: (41,30)*)
                   (* | "p7" => (interpret(prog7); ())  Testing Code : (16,30) *)
                   | _ => print ("Invalid argument: " ^ x ^ "\n"))  
          | _ => print ("Why did you give me " ^ Int.toString (length args) ^ " arguments?!\n")
    end
    
val _ = main ();
val _ = OS.Process.exit(OS.Process.success);


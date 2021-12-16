package CFG.stmt;

import CFG.expr.Expr;
import CFG.expr.Int;
import CFG.expr.Var;

public class Assign extends Stmt{
    public Expr value1;
    public Expr value2;
    public Assign(Expr a, Expr b){
        super(null, null,-1);
        value1 = a;
        value2 = b;
    }

    public String getId(){
        if(!(value1 instanceof Var)){
            System.out.println("error-not var in assign");
        }

        return (String) value1.value;
    }

    public int getValue(){
        if(!(value2 instanceof Int)){
            System.out.println("error-not var in assign");
        }

        return (int) value2.value;
    }
}

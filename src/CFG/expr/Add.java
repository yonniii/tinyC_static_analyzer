package CFG.expr;

public class Add extends Expr {
    public Expr value1;
    public Expr value2;

    public Add(Expr a, Expr b){
        value1 = a;
        value2 = b;
    }

}

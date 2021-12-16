package CFG.expr;

public class Lessthan extends Expr{
    Expr value1;
    Expr value2;

    public Lessthan(Expr a, Expr b){
        value1 = a;
        value2 = b;
    }

    @Override
    public Expr get() {
        return null;
    }
}

package CFG.expr;

public class Mult extends Expr{
    public Expr value1;
    public Expr value2;

    public Mult(Expr a, Expr b){
        value1 = a;
        value2 = b;
    }

    @Override
    public Expr get() {
        return null;
    }
}

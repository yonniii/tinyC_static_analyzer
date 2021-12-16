package CFG.stmt;

public class Stmt {
    public Object value1, value2;
    public int label;
    public Stmt(Stmt a, Stmt b, int label){
        value1 = a;
        value2 = b;
        this.label = label;
    }
}

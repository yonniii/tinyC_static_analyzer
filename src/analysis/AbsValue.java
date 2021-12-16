package analysis;

public class AbsValue {
    public AbsValue Add(AbsValue a, AbsValue b){
        if(a instanceof Bot | b instanceof Bot){
            return new IntBot();
        }else if(a instanceof Top | b instanceof Top){
            return new IntTop();
        }else if (a instanceof IntDomain & b instanceof IntDomain){
            IntDomain tmp = new IntDomain();
            return tmp.add(a,b);
        }else{
            return new IntBot();
        }
    }

}



class Bot extends AbsValue{ }

class Top extends AbsValue{ }

class IntDomain extends AbsValue{
    public AbsValue add(AbsValue a, AbsValue b) {
        if(a instanceof Bot | b instanceof Bot){
            return new IntBot();
        }else if(a instanceof Top | b instanceof Top) {
            return new IntTop();
        }

        return this;
    }
}

class IntBot extends IntDomain{ }

class IntTop extends IntDomain{ }
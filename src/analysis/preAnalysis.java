package analysis;

import CFG.expr.*;
import CFG.stmt.Assign;
import CFG.stmt.Stmt;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class preAnalysis {
    Object env = new Object[2][10];
//    Object mem = new Object[2][10];
//    Stmt stmt;

    public void visit(List start_stmt){
        Stmt stmt;
        Stmt now_stmt, stmt2;
        int label;
        Env env = new Env();
        Mem mem;
        Mem mem_updated;
        AbsState state = new AbsState();
        AbsSemantics semantics = new AbsSemantics();
        for(int i = 0;i<start_stmt.size();i++){
            now_stmt = (Stmt) start_stmt.get(i);
            label = i;
            mem = state.find(label);

            varDecl(now_stmt,env, mem);
            Mem tmp_mem = (Mem) mem.clone();
            mem_updated = semantics.apply_stmt(env,tmp_mem,now_stmt);

            state.put(label+1, env, mem_updated);
//            if (varDecl(now_stmt,env, mem)){
//                state.put(label+1,env, mem);
//            }else{
//                mem_updated = semantics.apply_stmt(env,mem,now_stmt);
//                state.put(label+1, env, mem_updated);
//            }

        }

        String string_env = "#ENV: [ " + env.toString() + " ]";
        System.out.println(string_env);
        System.out.println(state);

    }

    public void print_label(AbsState state, int label){
        Mem mem = state.find(label);
        System.out.println(String.format("[%d] : mem-%s", label, mem));
    }

    public boolean varDecl(Stmt stmt,Env env, Mem mem){
        if (!(stmt instanceof Assign)){
            return false;
        }
//        if(!(((Assign)stmt).value2 instanceof Int)){
//            return false;
//        }
        String id = ((Assign) stmt).getId();
        if(env.get(id) == null){
            env.add(id, 0, mem);
            return true;
        }
        return false;
//        int value = ((Assign) stmt).getValue();
//        if (env.get(id) == null){
//            env.add(id, value, mem);
//            return true;
//        }
//        return false;
    }

}

class AbsSemantics {
    public Mem apply_stmt(Env env, Mem mem, Stmt stmt){
        if(stmt instanceof Assign){
            Assign assign = (Assign) stmt;
            int addr = env.get((assign).getId());
            Object addrs = apply_expr(env,mem, assign.value2);
            if(addrs instanceof IntDomain){
                int value = assign.getValue();
//                String id = assign.getId();
//                env.add(id, value, mem);
                mem.update(addr,value);
            }else if(addrs instanceof Integer){
                mem.update(addr, (int) addrs);
            }else if(addrs instanceof Bot){
                mem.update(addr, Integer.MIN_VALUE);
            }
        }

        return mem;
    }

    public Object apply_expr(Env env, Mem mem, Expr expr){

        if(expr instanceof Int){
            return expr.value;
        }else if(expr instanceof Var){
            String id = (String) expr.value;
            if(env.get(id) == null){
                System.out.println("[expr_error] can't use undefined variable");
//                return null;
                return new Bot();
            }
            int addr = env.get(id);
            return mem.find(addr);
        }else if(expr instanceof Add){
            if((apply_expr(env,mem,((Add) expr).value1) instanceof Bot) | (apply_expr(env,mem,((Add) expr).value2) instanceof Bot)){
                return new Bot();
            }
            int e1 = (int) apply_expr(env,mem,((Add) expr).value1);
            int e2= (int) apply_expr(env,mem,((Add) expr).value2);
            return e1+e2;
        }else if(expr instanceof Sub){
            int e1 = (int) apply_expr(env,mem,((Sub) expr).value1);
            int e2= (int) apply_expr(env,mem,((Sub) expr).value2);
            return e1-e2;
        }else if(expr instanceof Mult){
            int e1 = (int) apply_expr(env,mem,((Mult) expr).value1);
            int e2= (int) apply_expr(env,mem,((Mult) expr).value2);
            return e1*e2;
        }

        return mem;
    }
}


class Env{
//    int count = 0;
    public Map<String, Integer> map = new HashMap<>();
    public void add(String a, Integer b, Mem mem){
        int addr;
        if (get(a) != null){
            addr = map.get(a);
            mem.update(addr, b);
        }else{
            addr = mem.add(b);
            if(addr != -1){
                map.put(a,addr);
            }
        }
    }

    public Integer get(String a){
        Integer tmp = map.get(a);
        if(tmp != null){
            return tmp;
        }
        return null;
    }

    public String toString(){
        String result = "";
        for (String key: map.keySet()) {
            result += String.format("%s -> a%d, ", key, map.get(key));
        }
        return result;
    }
}

class Mem{

    public List<Integer> list = new ArrayList<>();
    boolean isMemBot = false;
    boolean isMemTop = false;

    public Object clone(){
        Mem newmem = new Mem();
        for (int i = 0; i < list.size(); i++) {
            newmem.list.add(list.get(i));
        }
        newmem.isMemBot = isMemBot;
        newmem.isMemTop = isMemTop;
        return newmem;
    }

    public int add(int value){
        if(!isMemBot & !isMemTop){
            list.add(value);
            return list.size()-1;
        }
        return -1;
    }

    public String toString(){
        String result = "";
        for (int i = 0; i < list.size()-1; i++) {
            result += String.format("a%d -> %d, ",i,list.get(i));
        }
        result += String.format("a%d -> %d",list.size()-1,list.get(list.size()-1));
        return result;
    }

    public int add(int value, int addr){
        if(!isMemBot & !isMemTop){
//            list.add(value);
            list.add(addr, value);
            return addr;
        }
        return -1;
    }

    public Object find(int idx){
        if(isMemTop){
            return new Top();
        }else if(isMemBot){
            return new Bot();
        }else{
            if(list.size() > idx){
                return list.get(idx);
            }else{
                return new Top();
            }
        }
    }

    public void update(int idx, int value){
        if(list.size() > idx){
            list.set(idx, value);
        }else{
            System.out.println("error");
        }
    }

}

class AbsState{
    public Map<Integer, Pair<Env, Mem>> map = new HashMap<>();

    public AbsState(){
        map.put(0,new Pair(new Env(), new Mem()));
    }
    public void put(int label,Env env, Mem mem){
        map.put(label, new Pair<>(env,mem));
    }

    public void update(int label, Mem mem){
        Env tmp = map.get(label).a;
        map.replace(label, map.get(label), new Pair<>(tmp,mem));
    }

    public Mem find(int label){
        return map.get(label).b;
    }

    public String toString(){
        String result = "";
        for (int i = 0; i < map.size(); i++) {
            result += String.format("line %d : %s\n", i, map.get(i).b.toString());
        }
        return result;
    }
}
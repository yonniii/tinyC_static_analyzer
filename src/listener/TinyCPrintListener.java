package listener;

import CFG.expr.*;
import CFG.stmt.*;
import gened.*;
import main.Main;
import org.antlr.v4.runtime.tree.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class TinyCPrintListener extends tinycBaseListener {
    ParseTreeProperty<Object> newTexts = new ParseTreeProperty<>();
    List<Object> stmts = new ArrayList<>();

    @Override
    public void exitProgram(tinycParser.ProgramContext ctx) {
        String program = "";
        for (int i = 0; i < ctx.getChildCount(); i++) {     // statement +
            program += newTexts.get(ctx.getChild(i))+"\n";
            Main.cfg = (Stmt)newTexts.get(ctx.getChild(i));
        }
//        System.out.println(program);
        newTexts.put(ctx, program);
    }

    private Boolean isIfstmt(tinycParser.StatementContext ctx){
        if (ctx.getChild(0).getText().equals("if"))
            return true;
        return false;
    }

    private Boolean isWhilestmt(tinycParser.StatementContext ctx){
        if (ctx.paren_expr() != null)
            return true;
        return false;
    }

    @Override
    public void exitStatement(tinycParser.StatementContext ctx) {
        Object statement, paren_expr, stmt1, stmt2;
        if (isIfstmt(ctx)){
            paren_expr = newTexts.get(ctx.paren_expr());
            if (ctx.getChildCount() == 3){                              // 'if' paren_expr statement
                stmt1 = newTexts.get(ctx.statement(0));
                stmt2 = "";
                statement = String.format("if %s \n%s",paren_expr, stmt1);
            }else{                                                      // 'if' paren_expr statement 'else' statement
                stmt1 = newTexts.get(ctx.statement(0));
                stmt2 = newTexts.get(ctx.statement(1));
                statement = String.format("if %s \n%s\nelse \n%s",paren_expr, stmt1, stmt2);
            }
        } else if(isWhilestmt(ctx)){
            paren_expr = newTexts.get(ctx.paren_expr());
            stmt1 = newTexts.get(ctx.statement(0));
            if("while".equals(ctx.getChild(0).getText())){                 // 'while' paren_expr statement
                statement = String.format("while %s \n%s",paren_expr, stmt1);
            }else{                                                      // 'do' statement 'while' paren_expr ';'
                statement = String.format("do %swhile \n%s;", stmt1, paren_expr);
            }
        } else if(ctx.expr() != null){                                  // expr ';'
            Object expr = newTexts.get(ctx.expr());
            statement = expr;
//            statement = expr + ";\n";
        } else if(ctx.getChildCount() == 1){                             // ';'
            statement = ";";
        } else{                                                         // '{' statement* '}'
            stmt1 = "";
            for (int i = 0; i < ctx.statement().size(); i++) {
//                stmt1 += newTexts.get(ctx.statement(i));
                stmts.add(newTexts.get(ctx.statement(i)));

            }
            Stmt tmp = new Stmt((Stmt) stmts.get(stmts.size()-1),null,stmts.size()-1);
            Stmt fstmt = tmp;
            for (int i = stmts.size()-2; i > -1 ; i--) {
//                stmt1 += newTexts.get(ctx.statement(i));
                if(stmts.get(i) instanceof Assign){
                    ((Assign) stmts.get(i)).label = i;
                }
                fstmt = new Stmt((Stmt) stmts.get(i),tmp,i);
                tmp = fstmt;
            }
            statement = fstmt;
        }
//        Collections.reverse(stmts);
        Main.cfg_arr = stmts;
        newTexts.put(ctx, statement);
    }

    @Override
    public void exitParen_expr(tinycParser.Paren_exprContext ctx) {
//        String paren_expr, expr;
//        expr = newTexts.get(ctx.expr());
//        paren_expr = String.format("(%s)", expr);
//        newTexts.put(ctx, paren_expr);
    }

    @Override
    public void exitExpr(tinycParser.ExprContext ctx) {
        Object edited, test, id, expr;
        if (ctx.getChildCount() == 1){                  // test.c
            edited = newTexts.get(ctx.test());
        } else{                                         // id_ '=' expr
            id = newTexts.get(ctx.id_());
            expr = newTexts.get(ctx.expr());
            edited = new Assign((Expr) id, (Expr) expr);
        }

        newTexts.put(ctx, edited);
    }

    @Override
    public void exitTest(tinycParser.TestContext ctx) {
        Object edited, sum1, sum2;
        if (ctx.getChildCount() == 1){                  // sum_
            edited = newTexts.get(ctx.sum_(0));
        } else{                                         // sum_ '<' sum_
            sum1 = newTexts.get(ctx.sum_(0));
            sum2 = newTexts.get(ctx.sum_(1));
            edited = new Lessthan((Expr) sum1, (Expr) sum2);
        }

        newTexts.put(ctx, edited);
    }

    @Override
    public void exitSum_(tinycParser.Sum_Context ctx) {
        Object edited, term, sum;
        String op;
        if (ctx.getChildCount() == 1){                  // term
            edited = newTexts.get(ctx.term());
        } else{                                         // sum_ 'op' term
            sum = newTexts.get(ctx.sum_());
            term = newTexts.get(ctx.term());
            op = ctx.getChild(1).getText();
            if(op.equals("+")){
                edited = new Add((Expr) sum, (Expr) term);
            }
//            else{
            else if(op.equals("-")){
                edited = new Sub((Expr) sum, (Expr) term);
            }else{
                edited = new Mult((Expr) sum, (Expr) term);
            }
        }

        newTexts.put(ctx, edited);

    }

    @Override
    public void exitTerm(tinycParser.TermContext ctx) {
        Object term;
        if(ctx.id_() != null){
            term = newTexts.get(ctx.id_());
        } else if(ctx.integer() != null){
            term = newTexts.get(ctx.integer());
        } else{
            term = newTexts.get(ctx.paren_expr());
        }

        newTexts.put(ctx, term);
    }

    @Override
    public void exitId_(tinycParser.Id_Context ctx) {
        newTexts.put(ctx, new Var(ctx.getText()));
    }

    @Override
    public void exitInteger(tinycParser.IntegerContext ctx) {
        int tmp = Integer.parseInt(ctx.getText());
        newTexts.put(ctx, new Int(tmp));
    }

}

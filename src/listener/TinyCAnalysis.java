package listener;

import gened.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.*;



public class TinyCAnalysis extends tinycBaseListener {
    ParseTreeProperty<String> newTexts = new ParseTreeProperty<>();

    @Override
    public void enterProgram(tinycParser.ProgramContext ctx) {
        super.enterProgram(ctx);
    }

    @Override
    public void enterStatement(tinycParser.StatementContext ctx) {
        super.enterStatement(ctx);
    }

    @Override
    public void enterParen_expr(tinycParser.Paren_exprContext ctx) {
        super.enterParen_expr(ctx);
    }

    @Override
    public void enterExpr(tinycParser.ExprContext ctx) {
        super.enterExpr(ctx);
    }

    @Override
    public void enterTest(tinycParser.TestContext ctx) {
        super.enterTest(ctx);
    }

    @Override
    public void enterSum_(tinycParser.Sum_Context ctx) {
        super.enterSum_(ctx);
    }

    @Override
    public void enterTerm(tinycParser.TermContext ctx) {
        super.enterTerm(ctx);
    }

    @Override
    public void enterId_(tinycParser.Id_Context ctx) {
        super.enterId_(ctx);
    }

    @Override
    public void enterInteger(tinycParser.IntegerContext ctx) {
        super.enterInteger(ctx);
    }

}
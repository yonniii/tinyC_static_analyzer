package main;

import CFG.stmt.Stmt;
import analysis.preAnalysis;
import gened.*;
import listener.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.util.List;

public class Main {
    public static Stmt cfg = null;
    public static List cfg_arr = null;

    public static void main(String[] args) throws IOException {
	// write your code here
        CharStream codeCharStream = CharStreams.fromFileName(args[0]);
        tinycLexer lexer = new tinycLexer(codeCharStream);
        CommonTokenStream tokens = new CommonTokenStream( lexer );
        tinycParser parser = new tinycParser( tokens );
        ParseTree tree = parser.program();

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new TinyCPrintListener(), tree);
//        System.out.println(cfg_arr);
        preAnalysis pa = new preAnalysis();
        pa.visit(cfg_arr);
    }
}

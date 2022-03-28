package org.handle;

import lombok.extern.slf4j.Slf4j;
import org.enumUtils.BigDecimalStringUtil;
import org.latexTranslation.LatexUserString;
import org.latexTranslation.VariableIDDynamicTable;
import org.solutions.Solution;
import org.solveableManipulationBehavior.SolveableManipulateBehavior;
import org.solveables.Equation;
import org.solveables.Solveable;
import org.springframework.util.StringUtils;
import org.symbolComponents.CalcNumber;
import org.symbols.Expression;
import org.symbols.Symbol;
import org.symbols.Variable;
@Slf4j
public class SolveEquationsHandler implements IHandler{

    @Override
    public String execute(String fromula) {
        String zdCalDown = fromula.split("=")[0];
        String bdCalDown = fromula.split("=")[1];
        //解方程
        VariableIDDynamicTable manager = new VariableIDDynamicTable();
        Variable v = new Variable(new CalcNumber(1));
        Symbol left = new LatexUserString(zdCalDown).toExpression().toSymbol(manager);
        Symbol right = new LatexUserString(bdCalDown).toExpression().toSymbol(manager);

        Expression leftE, rightE;
        leftE = new Expression(left);
        rightE = new Expression(right);

        Equation eq = new Equation(leftE, rightE, v, new SolveableManipulateBehavior());

        log.info("解方程:{}",eq.printSolveable(manager));
        Solveable finalS = eq.fullSolve();
        Solution s = finalS.reachedSolution();
        String latexResult = s.printLatex(manager);
        log.info("结果:{}",latexResult);
        String result = "";
        if(StringUtils.isEmpty(latexResult)){
            result = "0";
        }else{
            String[] split = latexResult.split("=");
            result = BigDecimalStringUtil.str2BigDecimalKeepDouble(split[1]);
        }
        log.info("结果:{}",result);
        return result;
    }

    @Override
    public IHandler setParams(Object o) {
        return this;
    }
}

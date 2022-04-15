package org.handler;

import lombok.extern.slf4j.Slf4j;
import org.solveables.LatexSolver;

@Slf4j
public class SolvePowEquationsHandler implements IHandler{
    private static LatexSolver solver = new LatexSolver();
    @Override
    public String execute(String fromula) {
        String calResult = solver.solveUserLatex(fromula);
        log.info("解一元二次方程结果:{}",calResult);
        calResult = getGreaterThanZero(calResult);
        Double aDouble = Double.valueOf(calResult);
        String valueUp = String.format("%.2f", aDouble);
        return valueUp;
    }

    @Override
    public IHandler setParams(Object o) {
        return this;
    }

    private String getGreaterThanZero(String calResult){
        if(calResult.equals("0")){
            return calResult;
        }
        //x = 4.9442719 Or x = -12.9442719 获取大于零的结果
        String[] splits = calResult.split("Or");
        for (int i = 0; i < splits.length; i++) {
            String[] splitOne = splits[i].split("=");
            String trimVaule = splitOne[1].trim();
            if(trimVaule.indexOf("-")<0){
                return trimVaule;
            }
        }
        return calResult;
    }
}

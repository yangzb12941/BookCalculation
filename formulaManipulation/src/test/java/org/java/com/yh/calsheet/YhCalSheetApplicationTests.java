package org.java.com.yh.calsheet;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.aviatorPlugins.MathRadiansFunction;
import org.config.JkzhConfigEnum;
import org.config.JkzhGetValueModelEnum;
import org.context.JkzhContext;
import org.entity.Param;
import org.enumUtils.StringUtil;
import org.handle.JkzhFromulaHandle;
import org.handle.JkzhGetValues;
import org.junit.Test;
import org.latexTranslation.LatexUserString;
import org.latexTranslation.VariableIDDynamicTable;
import org.show.JkzhILayout;
import org.solutions.Solution;
import org.solveableManipulationBehavior.SolveableManipulateBehavior;
import org.solveables.Equation;
import org.solveables.Solveable;
import org.symbolComponents.CalcNumber;
import org.symbols.Expression;
import org.symbols.Symbol;
import org.symbols.Variable;
import org.table.JkzhBasicParam;
import org.table.SoilQualityTable;

import java.util.*;

@Slf4j
public class YhCalSheetApplicationTests {
    static {
        //装配角度转换函数
        AviatorEvaluator.addFunction(new MathRadiansFunction());
    }

    /**
     * 计算过程
     */
    @Test
    public void calculation(){

    }

    /**
     * 测试计算过程公式带入数值和计算结果公式带入数值
     */
    @Test
    public void valueFillCal(){
        JkzhFromulaHandle jkzhFromulaHandle = new JkzhFromulaHandle();
        for (JkzhConfigEnum source : JkzhConfigEnum.values()) {
            String[] values = new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13"};
            //①、计算过程代入数值
            String target_1 = jkzhFromulaHandle.expansionEquation(source.getLatexCal(),2,5,5);
            log.info("展开_1:{}",target_1);
            //②、获取当前公式对应的值集合
            //(载堆面地_{5}+度重_{1} \times 度厚_{1}+度重_{2} \times 度厚_{2}+度重_{3} \times 度厚_{3}) \times 数系力压土动主_{3}- 2 \times 力聚内_{3} \times \sqrt{数系力压土动主_{3}}
            String replaceChar_1 = jkzhFromulaHandle.placeholder(target_1);
            log.info("占位_1:{}",replaceChar_1);
            String s_1 = jkzhFromulaHandle.valuesFillCal(replaceChar_1, values);
            log.info("填充_1:{}",s_1);

            //②、计算结果代入数值
            //String target_2 = jkzhFromulaHandle.expansionEquationWithSubscript(source.getCalculate(),5,Boolean.FALSE);
            //log.info("展开_2:{}",target_2);
            //String replaceChar_2 = jkzhFromulaHandle.placeholder(target_2);
            //log.info("占位_2:{}",replaceChar_2);
            //String s_2 = jkzhFromulaHandle.valuesFillCal(replaceChar_2, values);
            //log.info("填充_2:{}",s_2);
        }
    }

    @Test
    public void latexEquation(){
        JkzhFromulaHandle jkzhFromulaHandle = new JkzhFromulaHandle();
        JkzhILayout jkzhLayout = new JkzhILayout();
        //①、解析公式，把公式展开
        for (JkzhConfigEnum source : JkzhConfigEnum.values()) {
            //①、替换成现实的字符
            //LatexCal : 替换，是用于word展示不带参数的计算过程公式
            String replaceChar = jkzhFromulaHandle.replaceLayoutChar(source.getLatexCal(),jkzhLayout);
            log.info("替换:{}",replaceChar);
            //LatexCal : 展开，是用于word展示不带参数的计算过程公式
            String target = jkzhFromulaHandle.expansionEquation(replaceChar,3,3,4);
            log.info("展开:{}",target);
        }
    }
    @Test
    public void getParamTest(){
        String fromula = "(地面堆载_{5}+重度_{1} \\times 厚度_{1}+重度_{2} \\times 厚度_{2}+重度_{3} \\times 厚度_{3}+重度_{4} \\times 厚度_{4}+重度_{5} \\times 厚度_{5}) \\times 主动土压力系数_{5}- 2 \\times 内聚力_{5} \\times \\sqrt{主动土压力系数_{5}}";
        List<Param> params = getParam(fromula);
        log.info("元素:{}",params);
    }

    private List<Param> getParam(String fromula){
        List<Param> params = new ArrayList<Param>();
        ArrayDeque<Character> deque = new ArrayDeque<Character>();
        Boolean isChanged = Boolean.FALSE;
        char[] chars = fromula.toCharArray();
        for(int i = 0; i < chars.length; i++){
            if(StringUtil.isChineseChar(chars[i])){
                isChanged = Boolean.TRUE;
                deque.push(chars[i]);
                continue;
            }
            if (isChanged) {
                deque.push(chars[i]);
                isChanged = Boolean.FALSE;
                //往前跳过n个字符去掉 _{5}格式内容
                int skip = 0;
                do {
                    skip++;
                    deque.push(chars[i+skip]);
                }while (chars[skip+i]!='}');
                i=i+skip;
            }
            if (!deque.isEmpty()) {
                StringBuilder subPart = new StringBuilder();
                do{
                    subPart.append(deque.pollLast());
                }while (!deque.isEmpty());
                Param param = createParam(subPart.toString());
                params.add(param);
            }
        }
        return params;
    }

    private Param createParam(String element){
        Param param = new Param();
        String[] s = element.split("_");
        param.setName(s[0]);
        param.setIndex(s[1].substring(1,s[1].length() - 1));
        return param;
    }
}

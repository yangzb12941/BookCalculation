package org.java.com.yh.calsheet;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.aviatorPlugins.MathRadiansFunction;

@Slf4j
public class YhCalSheetApplicationTests {
    static {
        //装配角度转换函数
        AviatorEvaluator.addFunction(new MathRadiansFunction());
    }

//    /**
//     * 计算过程
//     */
//    @Test
//    public void calculation(){
//        JkzhFromulaHandle jkzhFromulaHandle = new JkzhFromulaHandle();
//        JkzhCalculation jkzhCalculation = new JkzhCalculation(jkzhFromulaHandle);
//        //计算主动土压力
//        jkzhCalculation.zdPressure();
//        //计算被动土压力
//        jkzhCalculation.bdPressure(7.0);
//        //计算土压力零点
//        jkzhCalculation.pressureZero(7.0);
//        //计算主动土压力合力
//        jkzhCalculation.zdResultantEarthPressures();
//        //主动土压力合力作用点位置
//        jkzhCalculation.zdPositionAction();
//        //被动土压力合力及作用点位置
//        jkzhCalculation.bdResultantEarthPressures(7.0);
//        //被动土压力合力作用点位置
//        jkzhCalculation.bdPositionAction(7.0);
//        //支撑处水平力计算
//        jkzhCalculation.calStrutForce(7.0);
//    }
//
//    /**
//     * 测试计算过程公式带入数值和计算结果公式带入数值
//     */
//    @Test
//    public void valueFillCal(){
//        JkzhFromulaHandle jkzhFromulaHandle = new JkzhFromulaHandle();
//        for (JkzhConfigEnum source : JkzhConfigEnum.values()) {
//            String[] values = new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13"};
//            //①、计算过程代入数值
//            String target_1 = jkzhFromulaHandle.expansionEquation(source.getLatexCal(),2,5,5);
//            log.info("展开_1:{}",target_1);
//            //②、获取当前公式对应的值集合
//            //(载堆面地_{5}+度重_{1} \times 度厚_{1}+度重_{2} \times 度厚_{2}+度重_{3} \times 度厚_{3}) \times 数系力压土动主_{3}- 2 \times 力聚内_{3} \times \sqrt{数系力压土动主_{3}}
//            String replaceChar_1 = jkzhFromulaHandle.placeholder(target_1);
//            log.info("占位_1:{}",replaceChar_1);
//            String s_1 = jkzhFromulaHandle.valuesFillCal(replaceChar_1, values);
//            log.info("填充_1:{}",s_1);
//
//            //②、计算结果代入数值
//            //String target_2 = jkzhFromulaHandle.expansionEquationWithSubscript(source.getCalculate(),5,Boolean.FALSE);
//            //log.info("展开_2:{}",target_2);
//            //String replaceChar_2 = jkzhFromulaHandle.placeholder(target_2);
//            //log.info("占位_2:{}",replaceChar_2);
//            //String s_2 = jkzhFromulaHandle.valuesFillCal(replaceChar_2, values);
//            //log.info("填充_2:{}",s_2);
//        }
//    }
//
//    @Test
//    public void latexCalEquation(){
//        JkzhFromulaHandle jkzhFromulaHandle = new JkzhFromulaHandle();
//        JkzhILayout jkzhLayout = new JkzhILayout();
//        //①、解析公式，把公式展开
//        for (JkzhConfigEnum source : JkzhConfigEnum.values()) {
//            //①、替换成现实的字符
//            //LatexCal : 替换，是用于word展示不带参数的计算过程公式
//            String replaceChar = jkzhFromulaHandle.replaceLayoutChar(source.getLatex(),jkzhLayout);
//            log.info("替换:{}",replaceChar);
//            //LatexCal : 展开，是用于word展示不带参数的计算过程公式
//            String target = jkzhFromulaHandle.expansionEquation(replaceChar,3,3,4);
//            log.info("展开:{}",target);
//        }
//    }
//
//
//    @Test
//    public void latexEquation(){
//        JkzhFromulaHandle jkzhFromulaHandle = new JkzhFromulaHandle();
//        JkzhILayout jkzhLayout = new JkzhILayout();
//        //①、解析公式，把公式展开
//        //①、替换成现实的字符
//        //LatexCal : 替换，是用于word展示不带参数的计算过程公式
//        //String replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.主动土压力水土分算.getLatexCal(),jkzhLayout);
//
//        String replaceChar = JkzhConfigEnum.被动土压力.getLatexCal();
//        log.info("替换:{}",replaceChar);
//        //LatexCal : 展开，是用于word展示不带参数的计算过程公式
//        String target = jkzhFromulaHandle.expansionEquation(replaceChar,3,3,4);
//        log.info("展开:{}",target);
//    }
}

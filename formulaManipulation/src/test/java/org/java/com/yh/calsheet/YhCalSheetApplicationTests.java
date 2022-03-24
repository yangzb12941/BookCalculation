package org.java.com.yh.calsheet;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.aviatorPlugins.MathRadiansFunction;
import org.calculation.JkzhCalculation;
import org.config.JkzhConfigEnum;
import org.entity.ElementParam;
import org.enumUtils.StringUtil;
import org.enums.ConditionEnum;
import org.junit.Test;
import org.show.JkzhILayout;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
        JkzhFromulaHandle jkzhFromulaHandle = new JkzhFromulaHandle();
        JkzhCalculation jkzhCalculation = new JkzhCalculation(jkzhFromulaHandle);
        //计算主动土压力
        jkzhCalculation.zdPressure();
        //计算被动土压力
        jkzhCalculation.bdPressure(7.0);
        //计算土压力零点
        jkzhCalculation.pressureZero(7.0);
        //计算主动土压力合力
        jkzhCalculation.zdResultantEarthPressures();
        //主动土压力合力作用点位置
        jkzhCalculation.zdPositionAction();
        //被动土压力合力及作用点位置
        jkzhCalculation.bdResultantEarthPressures(7.0);
        //被动土压力合力作用点位置
        jkzhCalculation.bdPositionAction(7.0);
        //支撑处水平力计算
        jkzhCalculation.calStrutForce(7.0);
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
    public void latexCalEquation(){
        JkzhFromulaHandle jkzhFromulaHandle = new JkzhFromulaHandle();
        JkzhILayout jkzhLayout = new JkzhILayout();
        //①、解析公式，把公式展开
        for (JkzhConfigEnum source : JkzhConfigEnum.values()) {
            //①、替换成现实的字符
            //LatexCal : 替换，是用于word展示不带参数的计算过程公式
            String replaceChar = jkzhFromulaHandle.replaceLayoutChar(source.getLatex(),jkzhLayout);
            log.info("替换:{}",replaceChar);
            //LatexCal : 展开，是用于word展示不带参数的计算过程公式
            String target = jkzhFromulaHandle.expansionEquation(replaceChar,3,3,4);
            log.info("展开:{}",target);
        }
    }


    @Test
    public void latexEquation(){
        JkzhFromulaHandle jkzhFromulaHandle = new JkzhFromulaHandle();
        JkzhILayout jkzhLayout = new JkzhILayout();
        //①、解析公式，把公式展开
        //①、替换成现实的字符
        //LatexCal : 替换，是用于word展示不带参数的计算过程公式
        //String replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.主动土压力水土分算.getLatexCal(),jkzhLayout);

        String replaceChar = JkzhConfigEnum.被动土压力.getLatexCal();
        log.info("替换:{}",replaceChar);
        //LatexCal : 展开，是用于word展示不带参数的计算过程公式
        String target = jkzhFromulaHandle.expansionEquation(replaceChar,3,3,4);
        log.info("展开:{}",target);
    }

    @Test
    public void getParamTest(){
        String fromula = "(地面堆载_{5}+重度_{1} \\times 厚度_{1}+重度_{2} \\times 厚度_{2}+重度_{3} \\times 厚度_{3}+重度_{4} \\times 厚度_{4}+重度_{5} \\times 厚度_{5}) \\times 主动土压力系数_{5}- 2 \\times 内聚力_{5} \\times \\sqrt{主动土压力系数_{5}}";
        List<ElementParam> elementParams = getParam(fromula);
        log.info("元素:{}", elementParams);
    }

    private List<ElementParam> getParam(String fromula){
        List<ElementParam> elementParams = new ArrayList<ElementParam>();
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
                ElementParam elementParam = createParam(subPart.toString());
                elementParams.add(elementParam);
            }
        }
        return elementParams;
    }

    private ElementParam createParam(String element){
        ElementParam elementParam = new ElementParam();
        String[] s = element.split("_");
        elementParam.setName(s[0]);
        elementParam.setIndex(s[1].substring(1,s[1].length() - 1));
        return elementParam;
    }


    @Test
    public void execute() {
        //判断是否存在堆载，有堆载则需要进行堆载计算
        char[] chars = "<③(<①地面堆载+>[重度*厚度+...]<②-([厚度+...]-基坑外水位)*水常量>)*主动土压力系数->2*内聚力*根号主动土压力系数<②+([厚度+...]-基坑外水位)*水常量>".toCharArray();
        boolean isPush = Boolean.FALSE;
        Stack<String> flagStack = new Stack<String>();
        ArrayDeque<Character> deque = new ArrayDeque<Character>();
        StringBuilder tempFromula = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if(chars[i] == '<'){
                if(String.valueOf(chars[i+1]).equals(ConditionEnum.地面堆载.getValue())){
                    isPush = Boolean.TRUE;
                }
                //<③ 入标识栈
                if(isPush){
                    deque.push(chars[i]);
                    String flagS = chars[i] + String.valueOf(chars[i+1]);
                    flagStack.push(flagS);
                    continue;
                }
            }
            if(chars[i] == '>' && isPush){
                if(flagStack.isEmpty()){
                    throw new RuntimeException("< is not match at index "+i);
                }
                String pop = flagStack.pop();
                //表明已经是匹配上了<③...>
                if(("<"+ConditionEnum.地面堆载.getValue()+">").equals(pop+">")){
                    //需要根据条件判断是否需要保留这部分公式
                    if(Boolean.FALSE){
                        StringBuilder sub = new StringBuilder();
                        do{
                            Character character = deque.pollLast();
                            if(String.valueOf(deque.peekLast()).equals(ConditionEnum.地面堆载.getValue()) && character == '<'
                                    || String.valueOf(character).equals(ConditionEnum.地面堆载.getValue())){

                            }else{
                                sub.append(character);
                            }
                        }while (!String.valueOf(deque.peekFirst()).equals(ConditionEnum.地面堆载.getValue()) && !deque.isEmpty());
                        tempFromula.append(sub);
                        isPush = Boolean.FALSE;
                        continue;
                    }else{
                        isPush = Boolean.FALSE;
                        continue;
                    }
                }else{
                    deque.push(chars[i]);
                    continue;
                }
            }
            if(isPush){
                deque.push(chars[i]);
                continue;
            }
            tempFromula.append(chars[i]);
        }
        log.info("展开:{}",tempFromula.toString());
    }

    @Test
    public void execute2() {
        //判断是否存在堆载，有堆载则需要进行堆载计算
        char[] chars = "(<①地面堆载+>[重度*厚度+...]<②-([厚度+...]-[基坑外水位])*水常量>)*主动土压力系数-2*内聚力*根号主动土压力系数<②+([厚度+...]-[基坑外水位])*水常量>".toCharArray();
        boolean isPush = Boolean.FALSE;
        Stack<String> stack = new Stack<String>();
        StringBuilder tempFromula = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if(chars[i] == '['){
                isPush = Boolean.TRUE;
                stack.push(String.valueOf(chars[i]));
                continue;
            }

            if(chars[i] == ']' && isPush){
                if(stack.isEmpty()){
                    isPush = Boolean.FALSE;
                    continue;
                }else{
                    StringBuilder sub = new StringBuilder();
                    do{
                        sub.append(stack.pop());
                    }while (!stack.peek().equals("["));
                    stack.pop();
                    String sExpansion = sub.toString();
                    String s1 = doExpansion(sExpansion,2,2);
                    stack.push(s1);
                    if(stack.isEmpty()){
                        isPush = Boolean.FALSE;
                        continue;
                    }else{
                        continue;
                    }
                }
            }
            if(isPush){
                stack.push(String.valueOf(chars[i]));
                continue;
            }
            tempFromula.append(chars[i]);
        }
        if(!stack.isEmpty()){
            StringBuilder sub = new StringBuilder();
            do{
                sub.append(stack.pop());
            }while (!stack.isEmpty());
            StringBuilder reverse = sub.reverse();
            tempFromula.append(reverse);
        }
        log.info("展开:{}",tempFromula.toString());
    }

    /**
     * 展开字符次数拼接
     * @param subEquation 需展开的字符
     * @param time 展开次数
     * @param beginFloor 第几层开始
     * @return
     */
    private String doExpansion(String subEquation,
                               int time,
                               int beginFloor){
        String substring = subEquation;
        //表明需要扩展
        if(substring.startsWith("...")){
            //获取展开公式之间的连接符 [重度*厚度+...]
            //连接符是 +
            substring = substring.substring(3,substring.length());
            StringBuilder subPart = new StringBuilder();
            for(int i = 0;i< time;i++){
                subPart.append(nToIndex(substring,String.valueOf(beginFloor+i)));
            }
            return subPart.substring(1,subPart.length());
        }else{
            //表明不需要扩展
            return substring;
        }
    }


    /**
     * 把公式中"{}"中是否存在n,若存在则把n替换为具体的数值。
     * @param substring 源字符串
     * @param index 具体数字
     * @return
     */
    private String nToIndex(String substring,String index){
        Stack<String> stack = new Stack<String>();
        for(char ch:substring.toCharArray()){
            stack.push(String.valueOf(ch));
            if (ch == '}') {
                //持续出栈
                StringBuilder subPart = new StringBuilder();
                do{
                    String pop = stack.pop();
                    subPart.append(pop.equals("n")?index:pop);
                }while (!stack.empty() && !stack.peek().equals("{"));
                subPart.append(stack.pop());
                stack.push(subPart.toString());
            }
        }
        StringBuilder subPart = new StringBuilder();
        do{
            subPart.append(stack.pop());
        }while (!stack.empty());
        return subPart.reverse().toString();
    }
}

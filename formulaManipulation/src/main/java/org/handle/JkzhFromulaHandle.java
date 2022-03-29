package org.handle;


import lombok.extern.slf4j.Slf4j;
import org.config.JkzhConfigEnum;
import org.entity.ExpansionParam;
import org.enums.WaterWhichEnum;
import org.fromulaEntity.FromulaEntity;
import org.getValue.JkzhGetValues;
import org.show.ILayout;

@Slf4j
public class JkzhFromulaHandle{

    // FromulaEntity 工厂。用于组装各个处理器，满足某一公式的解析。
    private JkzhFromulaEntityFactory jkzhFromulaEntityFactory;

    public JkzhFromulaHandle(){
        this.jkzhFromulaEntityFactory = JkzhFromulaEntityFactory.getJkzhFromulaEntityFactory();
    }

    /**
     * 土压力计算的数学表达式
     * @param time 展开次数
     * @param beginFloor 第几层开始
     * @param endFloor 第几层开始
     * @param jkzhConfigEnum 被解析的基础公式
     * @param waterWhichEnum 水土合算、水土分算
     * @param jkzhGetValues 获取值的方式
     * @return
     */
    public String soilPressureToCal(int time,
                                    int beginFloor,
                                    int endFloor,
                                    JkzhConfigEnum jkzhConfigEnum,
                                    WaterWhichEnum waterWhichEnum,
                                    JkzhGetValues jkzhGetValues){
        log.info("cal入参:公式{},展开次数:{},当前第{}层开始,第几{}层结束",jkzhConfigEnum,time,beginFloor,endFloor);
        //处理计算结果的公式
        FromulaEntity fromulaEntity = jkzhFromulaEntityFactory.soilPressureToCal(time,beginFloor,endFloor,jkzhGetValues,jkzhConfigEnum, waterWhichEnum);
        String fillingCal = fromulaEntity.compile();
        return fillingCal;
    }

    /**
     * 土压力计算的数学表达式
     * @param time 展开次数
     * @param beginFloor 第几层开始
     * @param endFloor 第几层开始
     * @param jkzhConfigEnum 被解析的基础公式
     * @param waterWhichEnum 水土合算、水土分算
     * @param jkzhGetValues 获取值的方式
     * @return
     */
    public String calSolveEquations(int time,
                                    int beginFloor,
                                    int endFloor,
                                    JkzhConfigEnum jkzhConfigEnum,
                                    WaterWhichEnum waterWhichEnum,
                                    JkzhGetValues jkzhGetValues){
        log.info("cal入参:公式{},展开次数:{},当前第{}层开始,第几{}层结束",jkzhConfigEnum,time,beginFloor,endFloor);
        //处理计算结果的公式
        FromulaEntity fromulaEntity = jkzhFromulaEntityFactory.calSolveEquations(time,beginFloor,endFloor,jkzhGetValues,jkzhConfigEnum, waterWhichEnum);
        String fillingCal = fromulaEntity.compile();
        return fillingCal;
    }

    /**
     * 土压力计算的数学表达式
     * @param time 展开次数
     * @param beginFloor 第几层开始
     * @param endFloor 第几层开始
     * @param jkzhConfigEnum 被解析的基础公式
     * @param waterWhichEnum 水土合算、水土分算
     * @param jkzhGetValues 获取值的方式
     * @return
     */
    public String soilPressureToLatex(int time,
                                      int beginFloor,
                                      int endFloor,
                                      JkzhConfigEnum jkzhConfigEnum,
                                      WaterWhichEnum waterWhichEnum,
                                      JkzhGetValues jkzhGetValues){
        log.info("cal入参:公式{},展开次数:{},当前第{}层开始,第几{}层结束",jkzhConfigEnum,time,beginFloor,endFloor);
        //处理计算结果的公式
        FromulaEntity fromulaEntity = jkzhFromulaEntityFactory.soilPressureToLatex(time,beginFloor,endFloor,jkzhGetValues, jkzhConfigEnum, waterWhichEnum);
        String fillingLatex = fromulaEntity.compile();
        return fillingLatex;
    }

    /**
     * 获取可计算的数学表达式
     * @param leftFromula 左公式
     * @param rightFromula 右公式
     * @return
     */
    public String solveEquationsToCal(String leftFromula,String rightFromula){
        log.info("solveEquationsToCal:左公式{},右公式{}",leftFromula,rightFromula);
        //处理计算结果的公式
        FromulaEntity fromulaEntity = jkzhFromulaEntityFactory.solveEquationsToCal(leftFromula, rightFromula);
        String compile = fromulaEntity.compile();
        return compile;
    }

    /**
     * 获取可计算的数学表达式
     * @param curFloor 当前层
     * @param jkzhConfigEnum 被解析的基础公式
     * @param jkzhGetValues 获取值
     * @return
     */
    public String extendToCal(int curFloor,
                              JkzhConfigEnum jkzhConfigEnum,
                              JkzhGetValues jkzhGetValues){
        log.info("extendToCal入参:公式{},当前层:{}",jkzhConfigEnum,curFloor);
        //处理计算结果的公式
        FromulaEntity fromulaEntity = jkzhFromulaEntityFactory.extendToCal(curFloor,jkzhGetValues, jkzhConfigEnum.getCalculate());
        String compile = fromulaEntity.compile();
        return compile;
    }

    /**
     * 获取可计算的数学表达式
     * @param curFloor 当前层
     * @param fromula 被解析的基础公式
     * @param jkzhGetValues 获取值
     * @return
     */
    public String extendToCal(int curFloor,
                              String fromula,
                              JkzhGetValues jkzhGetValues){
        log.info("extendToCal入参:公式{},当前层:{}",fromula,curFloor);
        //处理计算结果的公式
        FromulaEntity fromulaEntity = jkzhFromulaEntityFactory.extendToCal(curFloor,jkzhGetValues, fromula);
        String compile = fromulaEntity.compile();
        return compile;
    }

    /**
     * 获取可计算的数学表达式
     * @param curFloor 当前层
     * @param jkzhConfigEnum 被解析的基础公式
     * @param jkzhGetValues 获取值
     * @return
     */
    public String extendToLatex(int curFloor,
                                JkzhConfigEnum jkzhConfigEnum,
                                JkzhGetValues jkzhGetValues){
        log.info("extendToLatex入参:公式{},当前层:{}",jkzhConfigEnum,curFloor);
        //处理计算结果的公式
        FromulaEntity fromulaEntity = jkzhFromulaEntityFactory.extendToLatex(curFloor,jkzhGetValues, jkzhConfigEnum.getLatexCal());
        String compile = fromulaEntity.compile();
        return compile;
    }

    /**
     * 获取可计算的数学表达式
     * @param curFloor 当前层
     * @param fromula 被解析的基础公式
     * @param jkzhGetValues 获取值
     * @return
     */
    public String extendToLatex(int curFloor,
                                String fromula,
                                JkzhGetValues jkzhGetValues){
        log.info("extendToLatex入参:公式{},当前层:{}",fromula,curFloor);
        //处理计算结果的公式
        FromulaEntity fromulaEntity = jkzhFromulaEntityFactory.extendToLatex(curFloor,jkzhGetValues, fromula);
        String compile = fromulaEntity.compile();
        return compile;
    }

    /**
     * 获取可计算的数学表达式
     * @param time 展开次数
     * @param beginFloor 第几层开始
     * @param endFloor 第几层开始
     * @param jkzhConfigEnum 被解析的基础公式
     * @param jkzhGetValues 获取值
     * @return
     */
    public String extendToCalN(int time,
                               int beginFloor,
                               int endFloor,
                               JkzhConfigEnum jkzhConfigEnum,
                               JkzhGetValues jkzhGetValues){
        log.info("cal入参:公式{},展开次数:{},当前第{}层开始,第几{}层结束",jkzhConfigEnum,time,beginFloor,endFloor);
        //处理计算结果的公式
        FromulaEntity fromulaEntity = jkzhFromulaEntityFactory.extendToCalN(time,beginFloor,endFloor,jkzhGetValues, jkzhConfigEnum.getCalculate());
        String compile = fromulaEntity.compile();
        return compile;
    }

    /**
     * 获取可计算的数学表达式
     * @param time 展开次数
     * @param beginFloor 第几层开始
     * @param endFloor 第几层开始
     * @param jkzhConfigEnum 被解析的基础公式
     * @param jkzhGetValues 获取值
     * @return
     */
    public String extendToLatexN(int time,
                                 int beginFloor,
                                 int endFloor,
                                 JkzhConfigEnum jkzhConfigEnum,
                                 JkzhGetValues jkzhGetValues){
        log.info("cal入参:公式{},展开次数:{},当前第{}层开始,第几{}层结束",jkzhConfigEnum,time,beginFloor,endFloor);
        //处理计算结果的公式
        FromulaEntity fromulaEntity = jkzhFromulaEntityFactory.extendToLatexN(time,beginFloor,endFloor,jkzhGetValues, jkzhConfigEnum.getLatexCal());
        String compile = fromulaEntity.compile();
        return compile;
    }

    /**
     * @param fromula 公式字符串
     * @param curFloor 当前层
     * @param iLayout 公式展示字符集
     * @return
     */
    public String replaceExtendToCal(String fromula,
                                     int curFloor,
                                     ILayout iLayout){
        log.info("extendToCal入参:公式{},当前层:{}",fromula,curFloor);
        //处理计算结果的公式
        ExpansionParam expansionParam = new ExpansionParam(curFloor,curFloor,curFloor);
        FromulaEntity fromulaEntity = jkzhFromulaEntityFactory.replaceExtendToCal(fromula,expansionParam,iLayout);
        String compile = fromulaEntity.compile();
        return compile;
    }

    /**
     * 固定公式展示处理器：
     * 1、公式替换元素处理器
     * @param fromula 解析公式
     * @param iLayout 公式展示字符集
     * @return
     */
    public String replaceLayoutChar(String fromula,ILayout iLayout){
        log.info("extendToCal入参:公式{},",fromula);
        //处理计算结果的公式
        FromulaEntity fromulaEntity = jkzhFromulaEntityFactory.replaceLayoutChar(fromula,iLayout);
        String compile = fromulaEntity.compile();
        return compile;
    }
}

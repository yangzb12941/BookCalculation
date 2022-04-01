package org.calculation;

import org.config.JkzhConfigEnum;
import org.context.JkzhContext;
import org.element.FormulaElement;
import org.element.SingleFormulaElement;
import org.element.TableElement;
import org.element.TextElement;
import org.handle.JkzhFromulaHandle;
import org.show.JkzhPrefixLayout;

/**
 * 把模板中的固定元素，填进模板元素集合中
 */
public class CreateFixedElementHandle implements ICreateFixedElement{

    private JkzhFromulaHandle jkzhFromulaHandle;
    private JkzhPrefixLayout jkzhPrefixLayout;
    private JkzhContext jkzhContext;

    public CreateFixedElementHandle(JkzhFromulaHandle jkzhFromulaHandle, JkzhPrefixLayout jkzhPrefixLayout, JkzhContext jkzhContext){
        this.jkzhFromulaHandle = jkzhFromulaHandle;
        this.jkzhPrefixLayout = jkzhPrefixLayout;
        this.jkzhContext = jkzhContext;
    }

    @Override
    public void createFixedElement() {
        /**固定计公式begin*/
        String s_1 = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.主动土压力.getLatex());
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("主动土压力计算公式",new FormulaElement(0,jkzhPrefixLayout,"主动土压力计算公式",s_1));
        String s_2 = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.主动土压力系数.getLatex());
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("主动土压力系数计算公式",new FormulaElement(0,jkzhPrefixLayout,"主动土压力系数计算公式",s_2));

        String s_3 = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.被动土压力.getLatex());
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("被动土压力计算公式",new FormulaElement(0,jkzhPrefixLayout,"被动土压力计算公式",s_3));
        String s_4 = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.被动土压力系数.getLatex());
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("被动土压力系数计算公式",new FormulaElement(0,jkzhPrefixLayout,"被动土压力系数计算公式",s_4));

        String s_5 = jkzhFromulaHandle.replaceSubscript(JkzhConfigEnum.支撑轴力.getLatex(),String.valueOf(jkzhContext.getCalTimes()));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("支反力计算公式",new FormulaElement(jkzhContext.getCalTimes(),jkzhPrefixLayout,"支反力计算公式",s_5));
        /**固定计公式end*/

        /**jkzhILayout begin*/
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("土压力强度顶面",new TextElement(0,"土压力强度顶面",jkzhPrefixLayout.getLayoutMap().get("土压力强度顶面")));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("土压力强度底面",new TextElement(0,"土压力强度底面",jkzhPrefixLayout.getLayoutMap().get("土压力强度底面")));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("土层厚度",new TextElement(0,"土层厚度",jkzhPrefixLayout.getLayoutMap().get("土层厚度")));
        /**jkzhILayout end*/

        /**jkzhElementLayout begin*/
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("被动合力至反弯点的距离",new SingleFormulaElement(jkzhContext.getCalTimes(),jkzhPrefixLayout,"被动合力至反弯点的距离",jkzhPrefixLayout.getLayoutMap().get("被动合力至反弯点的距离")));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("各层土的被动合力",new SingleFormulaElement(0,jkzhPrefixLayout,"各层土的被动合力",jkzhPrefixLayout.getLayoutMap().get("各层土的被动合力")));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("轴向支反力",new SingleFormulaElement(jkzhContext.getCalTimes(),jkzhPrefixLayout,"轴向支反力",jkzhPrefixLayout.getLayoutMap().get("轴向支反力")));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("主动合力至反弯点的距离",new SingleFormulaElement(jkzhContext.getCalTimes(),jkzhPrefixLayout,"主动合力至反弯点的距离",jkzhPrefixLayout.getLayoutMap().get("主动合力至反弯点的距离")));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("各层土的主动合力",new SingleFormulaElement(0,jkzhPrefixLayout,"各层土的主动合力",jkzhPrefixLayout.getLayoutMap().get("各层土的主动合力")));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("支点至基坑底面的距离",new SingleFormulaElement(jkzhContext.getCalTimes(),jkzhPrefixLayout,"支点至基坑底面的距离",jkzhPrefixLayout.getLayoutMap().get("支点至基坑底面的距离")));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("基坑底面至反弯点的距离",new SingleFormulaElement(jkzhContext.getCalTimes(),jkzhPrefixLayout,"基坑底面至反弯点的距离",jkzhPrefixLayout.getLayoutMap().get("基坑底面至反弯点的距离")));
        /**jkzhElementLayout end*/

        /**各工况参数 begin*/
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("坑内水位",new TextElement(0,"坑内水位",jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getBDWarterDepth().toString()));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("坑外水位",new TextElement(0,"坑外水位",jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getZDWarterDepth().toString()));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("支撑位置",new TextElement(0,"支撑位置",jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getAxis().toString()));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("均布荷载",new TextElement(0,"均布荷载",jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getSurcharge().toString()));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("基坑挖深",new TextElement(0,"基坑挖深",jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getDepth().toString()));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("第几工况",new TextElement(0,"第几工况",String.valueOf(jkzhContext.getCalTimes())));
        /**各工况参数 end*/
    }
}

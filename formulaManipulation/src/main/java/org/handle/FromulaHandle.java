package org.handle;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.enumUtils.StringUtil;
import org.show.ILayout;

import java.util.ArrayList;
import java.util.Stack;

@Slf4j
public abstract class FromulaHandle{

	/**
	 * 展开Calculate公式,例如：[|地面堆载+|[重度*厚度+...]]*Ka-2*内聚力*√Ka
	 * 通过出栈入栈的方式，展开计算公式。
	 * (地面堆载+重度 \times 厚度+重度 \times 厚度) \times 主动土压力系数- 2 \times 内聚力 \times \sqrt{主动土压力系数}
	 * [、<、入栈
	 * ]、> 出栈
	 * <>部分，需要根据条件判断是否需要包含在计算公式里
	 * []表示展开之后是一个整体
	 * @param equation 需要被展开的公式
	 * @param time 展开次数
	 * @param beginFloor 第几层开始
	 * @param endFloor 第几层开始
	 * @return 返回展开的公式
	 */
	public String expansionEquation(String equation,
									int time,
									int beginFloor,
									int endFloor){
		Stack<Character> stack = new Stack<Character>();
		StringBuilder subEquation = new StringBuilder();
		//保存在[]内各部分被解析的公式，有可能[[]...[]...[]] 这种公式，需要先解析里面各个子部分
		//然后再合成一个整体
		ArrayList<String> subAnalysis = new ArrayList<String>(8);
		boolean isPush = Boolean.FALSE;
		//在对JkzhConfigEnum下 原始 latex、calculate、latexCal 展开之前做特殊处理。
		//比如：在展开[<地面堆载+>[重度*厚度+...]]*主动土压力系数-2*内聚力*math.sqrt(主动土压力系数)
		//的时候，是否在每个中文指标结束后添加_{n}标识:
		//[<地面堆载_{n}+>[重度_{n}*厚度_{n}+...]]*主动土压力系数_{n}-2*内聚力_{n}*math.sqrt(主动土压力系数_{n})
		// 这样再展开后如下：
		// 一般展开后的公式，是通过_n下标，去匹配具体的值代入。而对于不带入参数的公式，是不需要这个特殊处理的。
		equation = preProcessing(equation);
		for(char ch:equation.toCharArray()){
			if(ch == '[' || ch == '<'){
				isPush = Boolean.TRUE;
				stack.push(ch);
				continue;
			}
			if (ch == ']' || ch == '>') {
				stack.push(ch);
				//持续出栈
				StringBuilder subPart = new StringBuilder();
				do{
					subPart.append(stack.pop());
				}while (!stack.empty() && stackConditions(ch,stack.peek()));
				//最后一个元素也出栈
				subPart.append(stack.pop());
				String analysis = subPart.reverse().toString();
				//展开计算公式
				String expansionPart = doExpansion(analysis,time,beginFloor,endFloor);
				subAnalysis.add(expansionPart);
				boolean isPushContinue =  stack.search('[')>0 || stack.search('<')>0;
				if(isPushContinue){
					isPush = Boolean.TRUE;
				}else{
					isPush = Boolean.FALSE;
				}
				continue;
			}
			if(isPush){
				stack.push(ch);
			}else{
				if(!CollectionUtils.isEmpty(subAnalysis)){
					if(time >= 1) {
						subEquation.append('(');
					}
					for (String item : subAnalysis) {
						subEquation.append(item);
					}
					if(time >= 1) {
						subEquation.append(')');
					}
					subAnalysis.clear();
				}
				subEquation.append(ch);
			}
		}
		if(!CollectionUtils.isEmpty(subAnalysis)){
			if(time >= 1) {
				subEquation.append('(');
			}
			for (String item : subAnalysis) {
				subEquation.append(item);
			}
			if(time >= 1) {
				subEquation.append(')');
			}
			subAnalysis.clear();
		}
		return nToIndex(subEquation.toString(),String.valueOf(endFloor));
	}


	/**
	 * 把 (载堆面地_{5}+度重_{1} \times 度厚_{1}+度重_{2} \times 度厚_{2}+度重_{3} \times 度厚_{3}+度重_{4} \times 度厚_{4}+度重_{5} \times 度厚_{5}) \times 数系力压土动主_{5}- 2 \times 力聚内_{5} \times \sqrt{数系力压土动主_{5}}
	 * 中文都需要替换成对应的参数，当前函数是把中文都替换为[1]、[2]、[3]用来匹配参数下标。
	 * @param source
	 * @return
	 */
	public String placeholder(String source){
		StringBuilder subPart = new StringBuilder();
		int curIndex = 0;
		Boolean isChanged = Boolean.FALSE;
		char[] chars = source.toCharArray();
		for(int i = 0; i < chars.length; i++){
			if(StringUtil.isChineseChar(chars[i])){
				isChanged = Boolean.TRUE;
				continue;
			}
			if (isChanged) {
				subPart.append("[").append(curIndex).append("]");
				isChanged = Boolean.FALSE;
				curIndex++;
				//往前跳过n个字符去掉 _{5}格式内容
				int skip = 0;
				do {
					skip++;
				}while (chars[skip+i]!='}');
				i=i+skip;
			}else{
				subPart.append(chars[i]);
			}
		}
		return subPart.toString();
	}

	/**
	 * 公式显示字符替换
	 * @param source 源字符
	 * @return 被替换的字符
	 */
	public String replaceLayoutChar(String source, ILayout iLayout){
		String target = new String(source);
		for(String item: iLayout.getLayoutChar()){
			if(target.indexOf(item)>=0){
				String sOne = iLayout.getLayoutMap().get(item);
				if(sOne.indexOf('\\')>=0){
					String s = retainSpecialChar(sOne);
					target = target.replaceAll(item,s);
				}else{
					target = target.replaceAll(item,sOne);
				}
			}
		}
		return target;
	}

	private String retainSpecialChar(String source){
		StringBuilder target = new StringBuilder();
		for (char item: source.toCharArray()) {
			target.append(item);
		    if(item == '\\'){
				target.append('\\');
			}
		}
		return target.toString();
	}

	/**
	 * 公式参数替换，并且计算。
	 * $([0]+[1] \times [2]+[3] \times [4]+[5] \times [6]+[7] \times [8]+[9] \times [10]) \times [11]- 2 \times [12] \times \sqrt{[13]}$
	 * 例如，这种需要填充多个值的，values 按顺序存放值，然后一个一个代入。
	 * @param source 展开但未填充值的公式表达式
	 * @param values 填充值
	 * @return
	 */
	public String valuesFillCal(String source, String[] values){
		StringBuilder subPart = new StringBuilder();
		int curIndex = 0;
		Boolean isChanged = Boolean.FALSE;
		for(char ch:source.toCharArray()){
			if(ch == '['){
				isChanged = Boolean.TRUE;
				continue;
			}
			if(ch == ']'){
				isChanged = Boolean.FALSE;
				subPart.append(values[curIndex]);
				curIndex++;
				continue;
			}
			if (isChanged) {
				continue;
			}else{
				subPart.append(ch);
			}
		}
		return subPart.toString();
	}

	/**
	 * 给公式中每一个中文字符后面都加一个_{n}
	 * 源:[<地面堆载+>[重度*厚度+...]]*主动土压力系数-2*内聚力*math.sqrt(主动土压力系数)
	 * 目标:[<地面堆载_{n}+>[重度_{n}*厚度_{n}+...]]*主动土压力系数_{n}-2*内聚力_{n}*math.sqrt(主动土压力系数_{n})
	 * @return
	 */
	public abstract String preProcessing(String source);

	/**
	 * 展开公式时，判断出栈条件
	 * @param stackBottom 终止出栈字符
	 * @param curItem 当前字符
	 * @return
	 */
	private Boolean stackConditions(char stackBottom,char curItem){
		return stackBottom == ']' ? curItem != '[':(stackBottom == '>'?curItem != '<':false);
	}

	/**
	 * 展开字符次数拼接
	 * @param subEquation 需展开的字符
	 * @param time 展开次数
	 * @param beginFloor 第几层开始
	 * @param endFloor 第几层开始
	 * @return
	 */
	private String doExpansion(String subEquation,
							   int time,
							   int beginFloor,
							   int endFloor){
		String substring = subEquation.substring(1, subEquation.length() - 1);
		//表明需要扩展
		if(substring.endsWith("...")){
			//获取展开公式之间的连接符 [重度*厚度+...]
			//连接符是 +
			substring = substring.substring(0,substring.length() - 3);
			StringBuilder subPart = new StringBuilder();
			for(int i = 0;i< time;i++){
				subPart.append(nToIndex(substring,String.valueOf(beginFloor+i)));
			}
			return subPart.substring(0,subPart.length() - 1);
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

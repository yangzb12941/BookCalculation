package org.elementHandler;

import com.deepoove.poi.template.IterableTemplate;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.context.AbstractContext;
import org.context.JkzhContext;
import org.element.BaseElement;
import org.element.BlockElement;
import org.enumUtils.ZDEqualsBDKindsEnum;

import java.util.*;

@Slf4j
public class JkzhBlockElementHandler extends BlockElementHandler{

    private static volatile JkzhBlockElementHandler instance;

    public static JkzhBlockElementHandler getInstance(){
        if(instance == null){
            synchronized (JkzhBlockElementHandler.class){
                if (instance == null) {
                    instance = new JkzhBlockElementHandler();
                }
            }
        }
        return instance;
    }

    @Override
    public Object getElementValue(AbstractContext abstractContext, MetaTemplate metaTemplate) {
        IterableTemplate iterableTemplate = (IterableTemplate) metaTemplate;
        JkzhContext jkzhContext = (JkzhContext)abstractContext;
        List<BlockElement> blockElements = null;
        List<Map<String, Object>> mapList = new ArrayList<>(64);
        if(iterableTemplate.getStartMark().getTagName().equals("多工况")){
            Object workingConditions = multipleWorkingConditions(jkzhContext, iterableTemplate);
            return workingConditions;
        }else if(iterableTemplate.getStartMark().getTagName().equals("主动土压力")){
            blockElements = zdPressure((JkzhContext) abstractContext,iterableTemplate);
            List<Map<String, Object>> maps = blockElementToListMap(blockElements);
            mapList.addAll(maps);
        }else if(iterableTemplate.getStartMark().getTagName().equals("被动土压力")){
            blockElements = bdPressure((JkzhContext) abstractContext,iterableTemplate);
            List<Map<String, Object>> maps = blockElementToListMap(blockElements);
            mapList.addAll(maps);
        }else if(iterableTemplate.getStartMark().getTagName().equals("主动土压力合力")){
            blockElements = zdPressureResultant((JkzhContext) abstractContext,iterableTemplate);
            List<Map<String, Object>> maps = blockElementToListMap(blockElements);
            mapList.addAll(maps);
        }else if(iterableTemplate.getStartMark().getTagName().equals("被动土压力合力")){
            blockElements = bdPressureResultant((JkzhContext) abstractContext,iterableTemplate);
            List<Map<String, Object>> maps = blockElementToListMap(blockElements);
            mapList.addAll(maps);
        }else if(iterableTemplate.getStartMark().getTagName().equals("第一种情况")){
            if(jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getZDEqualsBDKinds().equals(ZDEqualsBDKindsEnum.土压力零点第一种情况.getType())){
                blockElements = zDEqualsBD((JkzhContext) abstractContext,iterableTemplate);
                List<Map<String, Object>> maps = blockElementToListMap(blockElements);
                mapList.addAll(maps);
            }
        }else if(iterableTemplate.getStartMark().getTagName().equals("第二种情况")){
            if(jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getZDEqualsBDKinds().equals(ZDEqualsBDKindsEnum.土压力零点第二种情况.getType())){
                blockElements = zDEqualsBD((JkzhContext) abstractContext,iterableTemplate);
                List<Map<String, Object>> maps = blockElementToListMap(blockElements);
                mapList.addAll(maps);
            }
        }else if(iterableTemplate.getStartMark().getTagName().equals("第三种情况")){
            if(jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getZDEqualsBDKinds().equals(ZDEqualsBDKindsEnum.土压力零点第三种情况.getType())){
                blockElements = zDEqualsBD((JkzhContext) abstractContext,iterableTemplate);
                List<Map<String, Object>> maps = blockElementToListMap(blockElements);
                mapList.addAll(maps);
            }
        }else if(iterableTemplate.getStartMark().getTagName().equals("基坑底面以上")){
            blockElements = maxTc((JkzhContext) abstractContext,iterableTemplate);
            List<Map<String, Object>> maps = blockElementToListMap(blockElements);
            mapList.addAll(maps);
        }else if(iterableTemplate.getStartMark().getTagName().equals("基坑底面以下")){
            blockElements = maxTc((JkzhContext) abstractContext,iterableTemplate);
            List<Map<String, Object>> maps = blockElementToListMap(blockElements);
            mapList.addAll(maps);
        }
        return mapList;
    }

    private List<Map<String,Object>> blockElementToListMap(List<BlockElement> blockElements){
        List<Map<String,Object>> list = null;
        if(CollectionUtils.isNotEmpty(blockElements)){
            list = new ArrayList<>(blockElements.size());
            for (BlockElement block : blockElements) {
                list.add(block.getValues());
            }
        }
        return list;
    }

    private List<BlockElement> zdPressure(JkzhContext jkzhContext, IterableTemplate iterableTemplate){
        int allLands = jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getAllLands();
        List<BlockElement> blockElements = creatBlockElement(jkzhContext, iterableTemplate, 1, allLands);
        return blockElements;
    }

    private List<BlockElement> bdPressure(JkzhContext jkzhContext, IterableTemplate iterableTemplate){
        int depthLand = jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtDepthLand();
        int allLands = jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getAllLands();
        List<BlockElement> blockElements = creatBlockElement(jkzhContext,iterableTemplate, depthLand, allLands);
        return blockElements;
    }

    private List<BlockElement> zdPressureResultant (JkzhContext jkzhContext, IterableTemplate iterableTemplate){
        int zoneLand = jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtZoneLand();
        List<BlockElement> blockElements = creatBlockElement(jkzhContext,iterableTemplate, 1, zoneLand);
        return blockElements;
    }

    private List<BlockElement> bdPressureResultant(JkzhContext jkzhContext, IterableTemplate iterableTemplate){
        int zoneLand = jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtZoneLand();
        int atDepthLand = jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtDepthLand();
        List<BlockElement> blockElements = creatBlockElement(jkzhContext,iterableTemplate, atDepthLand, zoneLand);
        return blockElements;
    }

    private List<BlockElement> zDEqualsBD(JkzhContext jkzhContext, IterableTemplate iterableTemplate){
        List<BlockElement> blockElements = creatBlockElement(jkzhContext,iterableTemplate, 1, 1);
        return blockElements;
    }

    private List<BlockElement> creatBlockElement(JkzhContext jkzhContext, IterableTemplate iterableTemplate,Integer startLand,Integer endLand){
        List<BlockElement> blockElements = new ArrayList<BlockElement>(endLand);
        for(int land = startLand;land <= endLand; land++){
            List<BaseElement> baseElements = new ArrayList<BaseElement>(3);
            for(MetaTemplate templates : iterableTemplate.getTemplates()){
                RunTemplate temp = (RunTemplate)templates;
                String key = temp.getTagName() + land;
                BaseElement value = jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).get(key);
                if(Objects.nonNull(value)){
                    baseElements.add(value);
                }else{
                    value = jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).get(temp.getTagName());
                    baseElements.add(value);
                }
            }
            BlockElement blockElement = new BlockElement(land,iterableTemplate.getStartMark().getTagName(),baseElements);
            blockElements.add(blockElement);
        }
        return blockElements;
    }

    private List<BlockElement> creatBlockElement(JkzhContext jkzhContext, IterableTemplate iterableTemplate){
        List<BlockElement> blockElements = new ArrayList<BlockElement>(jkzhContext.getBendingMomentTemplates().size());
        int tcIndex = 1;
        for(HashMap<String, BaseElement> entry : jkzhContext.getBendingMomentTemplates()){
            if(Objects.isNull(entry)){
                continue;
            }
            if(iterableTemplate.getStartMark().getTagName().equals("基坑底面以上") && Objects.nonNull(entry.get("前一支撑"))){
                List<BaseElement> baseElements = new ArrayList<BaseElement>(12);
                for(MetaTemplate templates : iterableTemplate.getTemplates()){
                    RunTemplate temp = (RunTemplate)templates;
                    String key = temp.getTagName();
                    BaseElement value = entry.get(key);
                    if(Objects.nonNull(value)){
                        baseElements.add(value);
                    }else{
                        value = entry.get(temp.getTagName());
                        baseElements.add(value);
                    }
                }
                BlockElement blockElement = new BlockElement(tcIndex,iterableTemplate.getStartMark().getTagName(),baseElements);
                blockElements.add(blockElement);
                tcIndex++;
            }else if(iterableTemplate.getStartMark().getTagName().equals("基坑底面以下") && Objects.nonNull(entry.get("被动土压力上"))){
                List<BaseElement> baseElements = new ArrayList<BaseElement>(18);
                for(MetaTemplate templates : iterableTemplate.getTemplates()){
                    RunTemplate temp = (RunTemplate)templates;
                    String key = temp.getTagName();
                    BaseElement value = entry.get(key);
                    if(Objects.nonNull(value)){
                        baseElements.add(value);
                    }else{
                        value = entry.get(temp.getTagName());
                        baseElements.add(value);
                    }
                }
                BlockElement blockElement = new BlockElement(tcIndex,iterableTemplate.getStartMark().getTagName(),baseElements);
                blockElements.add(blockElement);
                tcIndex++;
            }
        }
        return blockElements;
    }

    /**
     * 多工况模板值匹配
     * @param jkzhContext
     * @param iterableTemplate
     * @return
     */
    private Object multipleWorkingConditions(JkzhContext jkzhContext, IterableTemplate iterableTemplate){
        List<Map<String, Object>> values = new ArrayList<>();
        for (int index =1;index<=jkzhContext.getElementTemplates().size()-1;index++) {
            jkzhContext.setCalTimes(index);
            Map<String, Object> valueMap = new HashMap<String, Object>() {
                {
                    for (MetaTemplate item:iterableTemplate.getTemplates()) {
                        if(item instanceof RunTemplate){
                            String tagName = ((RunTemplate) item).getTagName();
                            log.info("RunTemplate TagName:{}",tagName);
                            put(tagName, ElementHandlerUtils.getElementValue(jkzhContext,item));
                        }else if (item instanceof IterableTemplate){
                            String tagName = ((IterableTemplate)item).getStartMark().getTagName();
                            log.info("IterableTemplate TagName:{}",tagName);
                            put(tagName, ElementHandlerUtils.getElementValue(jkzhContext,item));
                        }
                    }
                }
            };
            values.add(valueMap);
        }
        //多工况模板元素迭代完，重新设置回解析模板非多工况部分填充值
        jkzhContext.setCalTimes(0);
        return values;
    }


    private List<BlockElement> maxTc (JkzhContext jkzhContext, IterableTemplate iterableTemplate){
        List<BlockElement> blockElements = creatBlockElement(jkzhContext,iterableTemplate);
        return blockElements;
    }
}

package org.elementHandler;

import com.deepoove.poi.template.IterableTemplate;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.context.AbstractContext;
import org.context.JkzhContext;
import org.element.BaseElement;
import org.element.BlockElement;
import org.enumUtils.ZDEqualsBDKindsEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public List<Map<String,Object>> getElementValue(AbstractContext abstractContext, MetaTemplate metaTemplate) {
        IterableTemplate iterableTemplate = (IterableTemplate) metaTemplate;
        JkzhContext jkzhContext = (JkzhContext)abstractContext;
        List<BlockElement> blockElements = null;
        if(iterableTemplate.getStartMark().getTagName().equals("主动土压力")){
            blockElements = zdPressure((JkzhContext) abstractContext,iterableTemplate);
        }else if(iterableTemplate.getStartMark().getTagName().equals("被动土压力")){
            blockElements = bdPressure((JkzhContext) abstractContext,iterableTemplate);
        }else if(iterableTemplate.getStartMark().getTagName().equals("主动土压力合力")){
            blockElements = zdPressureResultant((JkzhContext) abstractContext,iterableTemplate);
        }else if(iterableTemplate.getStartMark().getTagName().equals("被动土压力合力")){
            blockElements = bdPressureResultant((JkzhContext) abstractContext,iterableTemplate);
        }else if(iterableTemplate.getStartMark().getTagName().equals("第一种情况")){
            if(jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getZDEqualsBDKinds().equals(ZDEqualsBDKindsEnum.土压力零点第一种情况.getType())){
                blockElements = zDEqualsBD((JkzhContext) abstractContext,iterableTemplate);
            }
        }else if(iterableTemplate.getStartMark().getTagName().equals("第二种情况")){
            if(jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getZDEqualsBDKinds().equals(ZDEqualsBDKindsEnum.土压力零点第二种情况.getType())){
                blockElements = zDEqualsBD((JkzhContext) abstractContext,iterableTemplate);
            }
        }else if(iterableTemplate.getStartMark().getTagName().equals("第三种情况")){
            if(jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getZDEqualsBDKinds().equals(ZDEqualsBDKindsEnum.土压力零点第三种情况.getType())){
                blockElements = zDEqualsBD((JkzhContext) abstractContext,iterableTemplate);
            }
        }
        List<Map<String, Object>> mapList = blockElementToListMap(blockElements);
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
}

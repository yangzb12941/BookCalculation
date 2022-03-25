package org.handle;

import org.show.ILayout;

public class ReplaceLayoutHandler implements IHandler<ILayout>{
    private ILayout iLayout;
    @Override
    public String execute(String fromula) {
        for(String item: iLayout.getLayoutChar()){
            if(fromula.indexOf(item)>=0){
                String sOne = iLayout.getLayoutMap().get(item);
                if(sOne.indexOf('\\')>=0){
                    String s = retainSpecialChar(sOne);
                    fromula = fromula.replaceAll(item,s);
                }else{
                    fromula = fromula.replaceAll(item,sOne);
                }
            }
        }
        return fromula;
    }

    @Override
    public IHandler setParams(ILayout iLayout) {
        this.iLayout = iLayout;
        return this;
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
}

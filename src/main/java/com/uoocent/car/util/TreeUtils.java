package com.uoocent.car.util;

import java.util.Map;

import com.uoocent.car.entity.Organization;

/**   
 * @author mars   
 * @date 2017年3月27日 下午9:39:08 
 */
public class TreeUtils {
    /**
     * 
    * @author mars   
    * @date 2017年3月27日 下午9:39:36 
    * @Description: TODO(设置节点展开) 
    * @param @param perMap
    * @param @param org    设定文件 
    * @return void    返回类型 
    * @throws
     */
	public static void setExpanded(Map<Long, Organization> perMap,Organization org){
		Long parentId = org.getParent();
		Organization orgs = perMap.get(parentId);
    	if(orgs!=null){
    		orgs.setExpanded(true);
    		if(orgs.getParent()!=null){
    			setExpanded(perMap, orgs);
    		}
    	}
    }
}



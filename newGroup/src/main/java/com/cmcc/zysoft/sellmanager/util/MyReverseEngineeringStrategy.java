// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sellmanager.util;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：hibernate生成实体策略
 * <br />版本:1.0.0
 * <br />日期： 2013-1-11 下午7:47:47
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class MyReverseEngineeringStrategy extends
		DelegatingReverseEngineeringStrategy {

	/**
	 * 构造方法
	 * @param delegate
	 */
	public MyReverseEngineeringStrategy(ReverseEngineeringStrategy delegate) {
		super(delegate);
	}
	
	/**
	 * 除去表名前缀
	 */
	 @Override
     public String tableToClassName(TableIdentifier tableIdentifier) {
             String delegateResult = super.tableToClassName(tableIdentifier);
//             System.out.println("delegateResult_before="+delegateResult);
             int index = delegateResult.lastIndexOf('.');
             
             String packageName = delegateResult.substring(0, index + 1);
             delegateResult = delegateResult.substring(index + 1);
//             System.out.println("delegateResult_after="+delegateResult);
        	 String className = delegateResult.substring(getPrefixLength(delegateResult));
             String fullClassName = packageName + className;
             
             return fullClassName;
     }

     /**
      * 可在子类重载的长度
      * @param delegateResult
      * @return
      */
     protected int getPrefixLength(String delegateResult) {
    	 delegateResult = delegateResult.toUpperCase();
    	 if(delegateResult.startsWith("TBB")||delegateResult.startsWith("TBC")){
    		 return 3;
    	 }else{
    		 return 0;
    	 }
     }

}

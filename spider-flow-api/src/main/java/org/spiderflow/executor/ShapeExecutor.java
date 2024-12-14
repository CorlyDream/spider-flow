package org.spiderflow.executor;

import java.util.Map;

import org.spiderflow.context.SpiderContext;
import org.spiderflow.model.Shape;
import org.spiderflow.model.SpiderNode;
/**
 * 执行器接口
 * @author jmxd
 *
 */
public interface ShapeExecutor {


	default Shape shape(){
		return null;
	}
	
	/**
	 * 节点形状
	 * @return 节点形状名称
	 */
	String supportShape();
	
	/**
	 * 执行器具体的功能实现
	 * @param node 当前要执行的爬虫节点
	 * @param context 爬虫上下文
	 * @param variables 节点流程的全部变量的集合
	 * @return 返回当前节点的执行结果。比如： http 执行器，返回 response 对象
	 */
	default Object execute(SpiderNode node, SpiderContext context, Map<String, Object> variables){
		return null;
	}
	
	default boolean allowExecuteNext(SpiderNode node, SpiderContext context, Map<String, Object> variables){
		return true;
	}
	
	default boolean isThread(){
		return true;
	}
}

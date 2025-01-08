package org.spiderflow.core.executor.shape;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spiderflow.context.SpiderContext;
import org.spiderflow.core.Spider;
import org.spiderflow.core.model.SpiderFlow;
import org.spiderflow.core.service.SpiderFlowService;
import org.spiderflow.core.utils.SpiderFlowUtils;
import org.spiderflow.executor.ShapeExecutor;
import org.spiderflow.model.SpiderNode;
import org.spiderflow.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 子流程执行器
 * @author Administrator
 *
 */
@Component
public class ProcessExecutor implements ShapeExecutor{
	

	private static Logger logger = LoggerFactory.getLogger(ProcessExecutor.class);
	
	@Autowired
	private SpiderFlowService spiderFlowService;
	
	@Autowired
	private Spider spider;
	
	@Override
	public Object execute(SpiderNode node, SpiderContext context, Map<String,Object> variables) {
		String flowId = node.getStringJsonValue(Constants.FLOW_ID);
		SpiderFlow spiderFlow = spiderFlowService.getById(flowId);
		if(spiderFlow != null){
			logger.info("执行子流程:{}", spiderFlow.getName());
			SpiderNode root = SpiderFlowUtils.loadXMLFromString(spiderFlow.getXml());
			// 子流程更换根节点。每个流程的全局配置只在当前流程有效
			context.setCurrentRootNode(root);
			context.setCurrentFlowId(flowId);
			spider.fillPersistentCookies(context);
			spider.executeNode(null,root,context,variables);
		}else{
			logger.info("执行子流程:{}失败，找不到该子流程", flowId);
		}
		return null;
	}

	@Override
	public String supportShape() {
		return "process";
	}

}

package org.spiderflow.core.executor.shape;

import java.util.Map;

import org.spiderflow.context.SpiderContext;
import org.spiderflow.executor.ShapeExecutor;
import org.spiderflow.model.SpiderNode;
import org.springframework.stereotype.Component;

/**
 * 开始执行器
 * @author Administrator
 *
 */
@Component
public class StartExecutor implements ShapeExecutor{


	@Override
	public String supportShape() {
		return "start";
	}

}

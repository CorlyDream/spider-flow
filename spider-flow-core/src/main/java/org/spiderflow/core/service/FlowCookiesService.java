package org.spiderflow.core.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.spiderflow.core.mapper.FlowCookiesMapper;
import org.spiderflow.core.model.FlowCookies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlowCookiesService {
    @Autowired
    FlowCookiesMapper flowCookiesMapper;


    public int saveOrUpdate(FlowCookies entity) {
        FlowCookies existCookie = getCookies(entity.getFlowId(), entity.getDomain(), entity.getName());
        if (existCookie != null) {
            entity.setId(existCookie.getId());
            return flowCookiesMapper.updateById(entity);
        }
        return flowCookiesMapper.insert(entity);
    }

    public FlowCookies getCookies(String flowId, String domain, String name) {
        LambdaQueryWrapper<FlowCookies> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(FlowCookies::getFlowId, flowId)
                .eq(FlowCookies::getDomain, domain)
                .eq(FlowCookies::getName, name);
        return flowCookiesMapper.selectOne(queryWrapper);
    }
}

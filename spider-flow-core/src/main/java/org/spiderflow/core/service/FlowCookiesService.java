package org.spiderflow.core.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.spiderflow.core.mapper.FlowCookiesMapper;
import org.spiderflow.core.model.FlowCookies;
import org.spiderflow.model.CookieDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FlowCookiesService {
    @Autowired
    FlowCookiesMapper flowCookiesMapper;

    public int saveCookies(List<CookieDto> cookieDtos, String flowId) {
        int count = 0;
        for (CookieDto cookieDto : cookieDtos) {
            if (cookieDto.getExpiry() == null) {
                // session cookie not save
                continue;
            }
            FlowCookies flowCookie = new FlowCookies();
            flowCookie.setFlowId(flowId);
            flowCookie.setDomain(cookieDto.getDomain());
            flowCookie.setName(cookieDto.getName());
            flowCookie.setValue(cookieDto.getValue());
            flowCookie.setPath(cookieDto.getPath());
            flowCookie.setExpiry(cookieDto.getExpiry());
            count += saveOrUpdate(flowCookie);
        }
        return count;
    }

    public List<FlowCookies> getAllCookies(String flowId) {
        LambdaQueryWrapper<FlowCookies> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(FlowCookies::getFlowId, flowId)
                .ge(FlowCookies::getExpiry, new Date());
        return flowCookiesMapper.selectList(queryWrapper);
    }

    private int saveOrUpdate(FlowCookies entity) {
        FlowCookies existCookie = getCookies(entity.getFlowId(), entity.getDomain(), entity.getName());
        if (existCookie != null) {
            entity.setId(existCookie.getId());
            return flowCookiesMapper.updateById(entity);
        }
        return flowCookiesMapper.insert(entity);
    }

    private FlowCookies getCookies(String flowId, String domain, String name) {
        LambdaQueryWrapper<FlowCookies> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(FlowCookies::getFlowId, flowId)
                .eq(FlowCookies::getDomain, domain)
                .eq(FlowCookies::getName, name);
        return flowCookiesMapper.selectOne(queryWrapper);
    }
}

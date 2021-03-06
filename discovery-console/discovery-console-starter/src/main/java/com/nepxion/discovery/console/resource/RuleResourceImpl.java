package com.nepxion.discovery.console.resource;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.plugin.framework.parser.PluginConfigDeparser;
import com.nepxion.discovery.plugin.framework.parser.PluginConfigParser;

public class RuleResourceImpl implements RuleResource {
    @Autowired
    private ServiceResource serviceResource;

    @Autowired
    private ConfigResource configResource;

    @Autowired
    private PluginConfigParser pluginConfigParser;

    @Autowired
    private PluginConfigDeparser pluginConfigDeparser;

    @Autowired
    private Environment environment;

    @Override
    public void updateRemoteRuleEntity(String serviceId, RuleEntity ruleEntity) throws Exception {
        String group = serviceResource.getGroup(serviceId);

        updateRemoteRuleEntity(group, serviceId, ruleEntity);
    }

    @Override
    public void updateRemoteRuleEntity(String group, String serviceId, RuleEntity ruleEntity) throws Exception {
        String config = fromRuleEntity(ruleEntity);

        configResource.updateRemoteConfig(group, serviceId, config);
    }

    @Override
    public void clearRemoteRuleEntity(String serviceId) throws Exception {
        String group = serviceResource.getGroup(serviceId);

        clearRemoteRuleEntity(group, serviceId);
    }

    @Override
    public void clearRemoteRuleEntity(String group, String serviceId) throws Exception {
        configResource.clearRemoteConfig(group, serviceId);
    }

    @Override
    public RuleEntity getRemoteRuleEntity(String serviceId) throws Exception {
        String group = serviceResource.getGroup(serviceId);

        return getRemoteRuleEntity(group, serviceId);
    }

    @Override
    public RuleEntity getRemoteRuleEntity(String group, String serviceId) throws Exception {
        String config = configResource.getRemoteConfig(group, serviceId);

        return toRuleEntity(config);
    }

    @Override
    public RuleEntity toRuleEntity(String config) {
        return StringUtils.isNotEmpty(config) ? parse(config) : new RuleEntity();
    }

    @Override
    public String fromRuleEntity(RuleEntity ruleEntity) {
        if (ruleEntity != null) {
            return deparse(ruleEntity);
        }

        String configFormat = environment.getProperty(DiscoveryConstant.SPRING_APPLICATION_CONFIG_FORMAT, String.class, DiscoveryConstant.XML_FORMAT);
        if (StringUtils.equals(configFormat, DiscoveryConstant.XML_FORMAT)) {
            return DiscoveryConstant.EMPTY_XML_RULE;
        } else if (StringUtils.equals(configFormat, DiscoveryConstant.JSON_FORMAT)) {
            return DiscoveryConstant.EMPTY_JSON_RULE_SINGLE;
        }

        return StringUtils.EMPTY;
    }

    @Override
    public RuleEntity parse(String config) {
        return pluginConfigParser.parse(config);
    }

    @Override
    public String deparse(RuleEntity ruleEntity) {
        return pluginConfigDeparser.deparse(ruleEntity);
    }
}
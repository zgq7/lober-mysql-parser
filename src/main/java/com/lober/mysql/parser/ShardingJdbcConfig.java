package com.lober.mysql.parser;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * @author liaonanzhou
 * @date 2021/12/13 16:41
 * @description
 **/
public class ShardingJdbcConfig {


    public static DataSource shardingDataSource() throws Exception {
        HashMap<String, DataSource> dataSourceHashMap = new HashMap<>();
        dataSourceHashMap.put("master", getDataSource());

        List<RuleConfiguration> tableRuleConfigurationList = new ArrayList<>();
        tableRuleConfigurationList.add(shardingRuleConfiguration());

        Properties properties = new Properties();
        properties.put("sql-show", true);
        return ShardingSphereDataSourceFactory.createDataSource(dataSourceHashMap, tableRuleConfigurationList, properties);
    }

    /**
     * t_personal_media_task_channel 分表配置
     **/
    public static ShardingRuleConfiguration shardingRuleConfiguration() {
        // 分片逻辑表名称，真实表名称
        ShardingTableRuleConfiguration tableRuleConfiguration = new ShardingTableRuleConfiguration(
                "t_personal_media_task_channel",
                "master.t_personal_media_task_channel_${0..3}");
        // 分片规则
        tableRuleConfiguration.setTableShardingStrategy(
                new StandardShardingStrategyConfiguration("task_id", "pmtcShardingAlgorithm"));
        Properties properties = new Properties();
        properties.setProperty("algorithm-expression", "t_personal_media_task_channel_$->{task_id % 4}");

        // 绑定分片规则、表路由规则
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        shardingRuleConfiguration.getShardingAlgorithms()
                .putIfAbsent("pmtcShardingAlgorithm", new ShardingSphereAlgorithmConfiguration("INLINE", properties));
        shardingRuleConfiguration.getTables().add(tableRuleConfiguration);
        shardingRuleConfiguration.getBindingTableGroups().add("t_personal_media_task_channel");
        return shardingRuleConfiguration;
    }

    private static DataSource getDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://192.168.2.71:3306/db_cbsp_business_service?serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true");
        druidDataSource.setUsername("tuzhan");
        druidDataSource.setPassword("n0cmV*IKv");
        return druidDataSource;
    }


}
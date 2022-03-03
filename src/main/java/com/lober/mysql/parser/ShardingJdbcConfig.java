package com.lober.mysql.parser;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.groovy.util.Maps;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.sharding.algorithm.config.AlgorithmProvidedShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.algorithm.sharding.inline.InlineShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
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
        tableRuleConfigurationList.add(algorithmProvidedShardingRuleConfiguration());

        Properties properties = new Properties();
        properties.put("sql-show", true);
        return ShardingSphereDataSourceFactory.createDataSource("logic_db", dataSourceHashMap, tableRuleConfigurationList, properties);
    }

    public static AlgorithmProvidedShardingRuleConfiguration algorithmProvidedShardingRuleConfiguration() {
        // 分片逻辑表名称，真实表名称
        ShardingTableRuleConfiguration tableRuleConfiguration = new ShardingTableRuleConfiguration(
                "t_personal_media_task_channel",
                "master.t_personal_media_task_channel_${0..7}");
        // 分片规则
        tableRuleConfiguration.setTableShardingStrategy(new StandardShardingStrategyConfiguration("task_id", "pmtc-sharding-algorithm-name"));
        Properties properties = new Properties();
        properties.setProperty("algorithm-expression", "t_personal_media_task_channel_$->{task_id % 8}");
        InlineShardingAlgorithm inlineShardingAlgorithm = new InlineShardingAlgorithm();
        inlineShardingAlgorithm.setProps(properties);
        inlineShardingAlgorithm.init();

        // 分片计算配置
        AlgorithmProvidedShardingRuleConfiguration algorithmProvidedShardingRuleConfiguration = new AlgorithmProvidedShardingRuleConfiguration();
        algorithmProvidedShardingRuleConfiguration.setTables(Collections.singleton(tableRuleConfiguration));
        algorithmProvidedShardingRuleConfiguration.setBindingTableGroups(Collections.singleton("t_personal_media_task_channel"));
        algorithmProvidedShardingRuleConfiguration.setShardingAlgorithms(Maps.of("pmtc-sharding-algorithm-name", inlineShardingAlgorithm));

        return algorithmProvidedShardingRuleConfiguration;
    }

    private static DataSource getDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://192.168.2.71:3306/db_cbsp_business_service?serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true");
        druidDataSource.setUsername("tuzhan");
        druidDataSource.setPassword("n0cmV*IKv");
        return druidDataSource;
    }


}
package com.lober.mysql.parser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import org.junit.Test;

/**
 * @author liaonanzhou
 * @date 2022/3/3 14:30
 * @description
 **/
public class DruidParserTest implements SQLParserTest {

    @Test
    @Override
    public void parser() {
        final SQLStatement sqlStatement = SQLUtils.parseStatements(getSelectRightSQL(), getDBType()).get(0);
        System.out.println(sqlStatement.toString());
    }

}

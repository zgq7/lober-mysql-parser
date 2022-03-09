package com.lober.mysql.parser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowStatement;
import org.junit.Assert;
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
        final SQLStatement sqlStatement = SQLUtils.parseStatements(getSelectRightSQL(), getDBType().toLowerCase()).get(0);
        Assert.assertTrue(sqlStatement instanceof SQLSelectStatement);

        final SQLStatement sqlStatement2 = SQLUtils.parseSingleStatement(getShowSQL(), getDBType().toLowerCase());
        Assert.assertTrue(sqlStatement2 instanceof SQLShowStatement);
        Assert.assertTrue(sqlStatement2 instanceof SQLInsertStatement);
    }

}

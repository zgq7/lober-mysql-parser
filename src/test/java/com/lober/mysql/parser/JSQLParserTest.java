package com.lober.mysql.parser;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.junit.Test;

/**
 * @author liaonanzhou
 * @date 2022/3/3 14:58
 * @description
 **/
public class JSQLParserTest implements SQLParserTest {


    @Test
    @Override
    public void parser() {
        try {
            final Statement statement = CCJSqlParserUtil.parse(getSelectRightSQL());
            System.out.println(statement.toString());
            final Statement statement1 = CCJSqlParserUtil.parse(getShowSQL());
            System.out.println(statement1.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

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
            final Statement statement = CCJSqlParserUtil.parse(getSQL());
            System.out.println(statement.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

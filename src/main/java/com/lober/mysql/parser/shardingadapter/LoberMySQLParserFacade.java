package com.lober.mysql.parser.shardingadapter;

import org.apache.shardingsphere.sql.parser.api.parser.SQLLexer;
import org.apache.shardingsphere.sql.parser.api.parser.SQLParser;
import org.apache.shardingsphere.sql.parser.spi.DatabaseTypedSQLParserFacade;

/**
 * @author liaonanzhou
 * @date 2022/3/3 16:43
 * @description
 **/
public class LoberMySQLParserFacade implements DatabaseTypedSQLParserFacade {
    @Override
    public String getDatabaseType() {
        return null;
    }

    @Override
    public Class<? extends SQLLexer> getLexerClass() {
        return null;
    }

    @Override
    public Class<? extends SQLParser> getParserClass() {
        return null;
    }
}

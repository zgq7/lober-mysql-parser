package com.lober.mysql.parser.shardingadapter;

import org.antlr.v4.runtime.TokenStream;
import org.apache.shardingsphere.sql.parser.api.parser.SQLParser;
import org.apache.shardingsphere.sql.parser.api.visitor.ASTNode;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser;

/**
 * @author liaonanzhou
 * @date 2022/3/3 16:15
 * @description
 **/
public class LoberMysqlParser extends MySQLStatementParser implements SQLParser {

    public LoberMysqlParser(TokenStream input) {
        super(input);
    }

    @Override
    public ASTNode parse() {
        return null;
    }
}

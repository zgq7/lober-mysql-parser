package com.lober.mysql.parser;

import org.antlr.v4.runtime.*;
import org.apache.shardingsphere.sql.parser.api.SQLVisitorEngine;
import org.apache.shardingsphere.sql.parser.api.parser.SQLParser;
import org.apache.shardingsphere.sql.parser.core.ParseASTNode;
import org.apache.shardingsphere.sql.parser.core.ParseContext;
import org.apache.shardingsphere.sql.parser.core.database.parser.DatabaseTypedSQLParserFacadeRegistry;
import org.apache.shardingsphere.sql.parser.spi.DatabaseTypedSQLParserFacade;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;
import org.junit.Test;

import java.nio.CharBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class AntlrParserTest implements SQLParserTest {

    @Test
    @Override
    public void parser() {
        try {
            //1:先构建sharding sql parser
            DatabaseTypedSQLParserFacade sqlParserFacade = DatabaseTypedSQLParserFacadeRegistry.getFacade(getDBType());
            //2:利用antlr4获取char stream
            CodePointBuffer buffer = CodePointBuffer.withChars(CharBuffer.wrap(getSQL().toCharArray()));
            CharStream charStream = CodePointCharStream.fromBuffer(buffer);
            //3:
            Lexer lexer = (Lexer) sqlParserFacade.getLexerClass().getConstructor(CharStream.class).newInstance(charStream);
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            //4:构建具体的sql parser
            SQLParser sqlParser = sqlParserFacade.getParserClass().getConstructor(TokenStream.class).newInstance(tokenStream);
            //5
            ParseASTNode astNode = (ParseASTNode) sqlParser.parse();
            //6
            ParseContext parseContext = new ParseContext(astNode.getRootNode(), astNode.getHiddenTokens());
            //7:构建visitor
            SQLVisitorEngine visitorEngine = new SQLVisitorEngine(getDBType(), "STATEMENT", new Properties());
            //8:
            SQLStatement sqlStatement = visitorEngine.visit(parseContext);
            System.out.println(sqlStatement.toString());

            System.out.println(sqlStatement.getParameterCount());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void execute() {
        Connection connection = null;
        try {
            connection = ShardingJdbcConfig.shardingDataSource().getConnection();
            connection.prepareStatement(getSQL()).execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

    }


}

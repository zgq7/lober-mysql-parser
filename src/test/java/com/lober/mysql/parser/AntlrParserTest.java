package com.lober.mysql.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CodePointBuffer;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.apache.shardingsphere.driver.jdbc.core.connection.ShardingSphereConnection;
import org.apache.shardingsphere.driver.jdbc.core.statement.ShardingSpherePreparedStatement;
import org.apache.shardingsphere.sql.parser.api.SQLVisitorEngine;
import org.apache.shardingsphere.sql.parser.api.parser.SQLParser;
import org.apache.shardingsphere.sql.parser.core.ParseASTNode;
import org.apache.shardingsphere.sql.parser.core.ParseContext;
import org.apache.shardingsphere.sql.parser.core.database.parser.DatabaseTypedSQLParserFacadeRegistry;
import org.apache.shardingsphere.sql.parser.spi.DatabaseTypedSQLParserFacade;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;
import org.junit.Test;

import java.nio.CharBuffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AntlrParserTest implements SQLParserTest {

    @Test
    @Override
    public void parser() {
        try {
            //1:先构建sharding sql parser
            DatabaseTypedSQLParserFacade sqlParserFacade = DatabaseTypedSQLParserFacadeRegistry.getFacade(getDBType());
            //2:利用antlr4获取char stream
            CodePointBuffer buffer = CodePointBuffer.withChars(CharBuffer.wrap(getSelectRightSQL().toCharArray()));
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
        ShardingSphereConnection connection = null;
        try {
            connection = (ShardingSphereConnection) ShardingJdbcConfig.shardingDataSource().getConnection();

            // 事务
            connection.setAutoCommit(false);
            List<Boolean> booleanList = new ArrayList<>();

//            booleanList.add(connection.prepareStatement(getSelectRightSQL()).execute());
//            System.out.println(getSelectRightSQL() + "执行完毕");
//            booleanList.add(connection.prepareStatement(getSelectErrorSQL()).execute());
//            System.out.println(getSelectErrorSQL() + "执行完毕");

//            booleanList.add(connection.prepareStatement(getInsertRightSQL()).executeUpdate() > 0);
//            System.out.println(getInsertRightSQL() + "执行完毕");
            ShardingSpherePreparedStatement ps = (ShardingSpherePreparedStatement) connection.prepareStatement(getInsertErrorSQL());
            ps.setLong(1, 1);
            booleanList.add(ps.executeUpdate() > 0);
            System.out.println(getInsertErrorSQL() + "执行完毕");
            if (booleanList.contains(false)) {
                System.out.println("事务即将回滚");
                connection.rollback();
            } else {
                System.out.println("事务即将提交");
                connection.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    System.out.println("事务即将回滚");
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }


}

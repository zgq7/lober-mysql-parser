package com.lober.mysql.parser;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CodePointBuffer;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.apache.groovy.util.Maps;
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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class AntlrParserTest implements SQLParserTest {

    @Test
    @Override
    public void parser() {
        try {
            //1:先构建sharding sql parser
            DatabaseTypedSQLParserFacade sqlParserFacade = DatabaseTypedSQLParserFacadeRegistry.getFacade(getDBType());
            //2:利用antlr4获取char stream
            CodePointBuffer buffer = CodePointBuffer.withChars(CharBuffer.wrap(getSelectErrorSQL().toCharArray()));
            CharStream charStream = CodePointCharStream.fromBuffer(buffer);
            //3:lexer
            Lexer lexer = (Lexer) sqlParserFacade.getLexerClass().getConstructor(CharStream.class).newInstance(charStream);
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            //4:构建具体的sql parser
            SQLParser sqlParser = sqlParserFacade.getParserClass().getConstructor(TokenStream.class).newInstance(tokenStream);
            //5:parse ast node
            ParseASTNode astNode = (ParseASTNode) sqlParser.parse();
            //6:实例化parser context
            ParseContext parseContext = new ParseContext(astNode.getRootNode(), astNode.getHiddenTokens());
            //7:构建visitor
            SQLVisitorEngine visitorEngine = new SQLVisitorEngine(getDBType(), "STATEMENT", new Properties());
            //8:实例化sql statement
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

//            ShardingSpherePreparedStatement rightSelectPS = (ShardingSpherePreparedStatement) connection.prepareStatement(getSelectRightSQL());
//            booleanList.add(rightSelectPS.execute());
//            log.info(rightSelectPS.getResultSet().toString());
//            ShardingSpherePreparedStatement errorSelectPS = (ShardingSpherePreparedStatement) connection.prepareStatement(getSelectErrorSQL());
//            errorSelectPS.setLong(1, 1);
//            booleanList.add(errorSelectPS.execute());
//            booleanList.add(connection.prepareStatement(getInsertRightSQL()).executeUpdate() > 0);

            ShardingSpherePreparedStatement ps = (ShardingSpherePreparedStatement) connection.prepareStatement(getInsertErrorSQL());
            ps.setLong(1, 1);
            booleanList.add(ps.executeUpdate() > 0);
            parseColumn(ps.getResultSet()).forEach(System.out::println);

            if (booleanList.contains(false)) {
                log.info("事务即将回滚");
                connection.rollback();
            } else {
                log.error("事务即将提交");
                connection.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    log.error("事务即将回滚");
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

    private List<Map<String, Object>> parseColumn(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnSize = metaData.getColumnCount();
        for (int i = 0; i < columnSize; i++) {
            resultList.add(Maps.of(metaData.getCatalogName(i), resultSet.getObject(i)));
        }
        return resultList;
    }

}

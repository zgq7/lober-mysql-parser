package com.lober.mysql.parser;

/**
 * @author liaonanzhou
 * @date 2022/3/3 14:30
 * @description
 **/
public interface SQLParserTest {

    default String getSQL() {
        return "select rank from t_kol ";
    }

    default String getDBType() {
        return "MySQL";
    }


    void parser();

}

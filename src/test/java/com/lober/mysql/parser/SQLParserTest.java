package com.lober.mysql.parser;

/**
 * @author liaonanzhou
 * @date 2022/3/3 14:30
 * @description
 **/
public interface SQLParserTest {

    default String getSelectRightSQL() {
        return "select id from t_personal_media_task ";
    }

    default String getSelectErrorSQL() {
        return "select rank from t_ranking_list_20211222 where id = ? ";
    }

    default String getInsertRightSQL() {
        return "insert into t_personal_media_task(id) select 100678811 ";
    }

    default String getInsertErrorSQL() {
        return "insert into t_personal_media_task_channel(id,task_id) values(100678811 , ?) ";
    }

    default String getShowSQL() {
        return "show tables like 't_xxx' ";
    }

    default String getDBType() {
        return "MySQL";
    }


    void parser();

}

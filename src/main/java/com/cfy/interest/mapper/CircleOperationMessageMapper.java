package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.CircleOperationMessage;
import org.apache.ibatis.annotations.Select;

public interface CircleOperationMessageMapper extends BaseMapper<CircleOperationMessage> {

    @Select("select count(0) from circle_operation_message where type = 2 and c_id = #{cid} and to_days(datetime) = " +
            "to_days(now()) ")
    Integer statisticsJoinToday(Integer cid);


    @Select("select count(0) from circle_operation_message where type = 2 and c_id = #{cid} and TO_DAYS( NOW( )) - " +
            "TO_DAYS(datetime) = 1 ")
    Integer statisticsJoinYesterday(Integer cid);


}

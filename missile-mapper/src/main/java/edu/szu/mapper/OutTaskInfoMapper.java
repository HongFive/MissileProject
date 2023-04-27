package edu.szu.mapper;

import edu.szu.pojo.TaskInfo;
import edu.szu.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OutTaskInfoMapper extends MyMapper<TaskInfo> {
}
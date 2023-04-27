package edu.szu.mapper;

import edu.szu.pojo.AgvInfo;
import edu.szu.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OutAgvInfoMapper extends MyMapper<AgvInfo> {
}
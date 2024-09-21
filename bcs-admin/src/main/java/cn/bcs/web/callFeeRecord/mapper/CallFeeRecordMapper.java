package cn.bcs.web.callFeeRecord.mapper;

import cn.bcs.web.callFeeRecord.domain.CallFeeRecord;
import cn.bcs.web.callFeeRecord.domain.vo.CallFeeRecordVO;
import cn.bcs.web.callFeeRecord.domain.query.RecordQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 话费分成记录Mapper接口
 *
 * @author ruoyi
 * @date 2024-09-17
 */
@Mapper
public interface CallFeeRecordMapper extends BaseMapper<CallFeeRecord> {

    List<CallFeeRecordVO> selectRecordListByType(@Param("query") RecordQuery query);
}

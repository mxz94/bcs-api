package cn.bcs.web.apply.mapper;

import cn.bcs.web.apply.domain.ApplyRecord;
import cn.bcs.web.apply.domain.query.ApplyRecordQuery;
import cn.bcs.web.apply.domain.vo.ApplyRecordVO;
import cn.bcs.web.apply.domain.vo.MonthCallFeeVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 套餐申请记录Mapper接口
 *
 * @author mxz
 * @date 2024-09-14
 */
@Mapper
public interface ApplyRecordMapper extends BaseMapper<ApplyRecord> {

    List<MonthCallFeeVO> selectCallFee();

    List<ApplyRecord> selectMaxApplyRecord();

    List<ApplyRecordVO> listNew(@Param("query") ApplyRecordQuery query);
}

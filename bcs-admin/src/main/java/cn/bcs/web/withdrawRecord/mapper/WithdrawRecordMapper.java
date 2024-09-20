package cn.bcs.web.withdrawRecord.mapper;

import cn.bcs.web.withdrawRecord.domain.WithdrawRecord;
import cn.bcs.web.withdrawRecord.domain.query.WithDrawRecordQuery;
import cn.bcs.web.withdrawRecord.domain.vo.WithdrawRecordVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 提现记录Mapper接口
 *
 * @author ruoyi
 * @date 2024-09-17
 */
@Mapper
public interface WithdrawRecordMapper extends BaseMapper<WithdrawRecord> {

    List<WithdrawRecordVO> selectListNew(@Param("query") WithDrawRecordQuery query);

}

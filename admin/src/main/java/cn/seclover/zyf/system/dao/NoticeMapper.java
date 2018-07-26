package cn.seclover.zyf.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.seclover.zyf.system.model.Notice;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 通知表 Mapper 接口
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    /**
     * 获取通知列表
     */
    List<Map<String, Object>> list(@Param("condition") String condition);

}
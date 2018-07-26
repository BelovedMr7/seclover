package cn.seclover.zyf.system.service;

import com.baomidou.mybatisplus.service.IService;
import cn.seclover.zyf.system.model.Notice;

import java.util.List;
import java.util.Map;

/**
 * 通知表 服务类
 */
public interface INoticeService extends IService<Notice> {

    /**
     * 获取通知列表
     */
    List<Map<String, Object>> list(String condition);
}

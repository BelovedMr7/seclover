package cn.seclover.zyf.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import cn.seclover.zyf.system.dao.NoticeMapper;
import cn.seclover.zyf.system.model.Notice;
import cn.seclover.zyf.system.service.INoticeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 通知表 服务实现类
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    @Override
    public List<Map<String, Object>> list(String condition) {
        return this.baseMapper.list(condition);
    }
}

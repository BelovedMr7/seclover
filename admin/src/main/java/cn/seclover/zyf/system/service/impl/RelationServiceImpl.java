package cn.seclover.zyf.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import cn.seclover.zyf.system.dao.RelationMapper;
import cn.seclover.zyf.system.model.Relation;
import cn.seclover.zyf.system.service.IRelationService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表 服务实现类
 */
@Service
public class RelationServiceImpl extends ServiceImpl<RelationMapper, Relation> implements IRelationService {

}

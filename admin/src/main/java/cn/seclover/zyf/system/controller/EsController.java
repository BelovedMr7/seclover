package cn.seclover.zyf.system.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.Tip;
import cn.seclover.zyf.core.common.annotion.BussinessLog;
import cn.seclover.zyf.core.common.annotion.Permission;
import cn.seclover.zyf.core.common.constant.Const;
import cn.seclover.zyf.core.common.constant.dictmap.UserDict;
import cn.seclover.zyf.core.common.constant.state.ManagerStatus;
import cn.seclover.zyf.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.datascope.DataScope;
import com.stylefeng.guns.core.exception.GunsException;
import cn.seclover.zyf.core.shiro.ShiroKit;
import cn.seclover.zyf.core.shiro.ShiroUser;
import com.stylefeng.guns.core.util.ToolUtil;
import cn.seclover.zyf.system.factory.UserFactory;
import cn.seclover.zyf.system.model.User;
import cn.seclover.zyf.system.service.IUserService;
import cn.seclover.zyf.system.transfer.UserDto;
import cn.seclover.zyf.system.warpper.UserWarpper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 張燿峰
 *
 * @author 孤
 * @date 2018/7/18
 */
@Controller
@RequestMapping("/es")
public class EsController extends BaseController {
    private static String PREFIX = "/system/es/";

    @Autowired
    private IUserService iUserService;

    /**
     * 跳转到ES数据首页
     * @return es.html
     */
    @RequestMapping("")
    public String index(){
        return PREFIX+"es.html";
    }

    /**
     * 跳转到es新增页面
     * @return es_add.html
     */
    @RequestMapping("/es_add")
    @ResponseBody
    public String userAdd(){
        return PREFIX+ "es_add.html";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String name, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) Integer deptid) {
        if (ShiroKit.isAdmin()){
            List<Map<String,Object>> users = iUserService.selectUsers(null,name,beginTime,endTime,deptid);
            return new UserWarpper(users).warp();
        }else{
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            List<Map<String,Object>> list = iUserService.selectUsers(dataScope,name,beginTime,endTime,deptid);
            return new UserWarpper(list).warp();
        }
    }

    @RequestMapping("/add")
    @BussinessLog(value = "新增ES测试数据",key = "account",dict = UserDict.class)
    @Permission
    @ResponseBody
    public Tip userAdd(@Valid UserDto userDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            //请求有错误
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        //判断账号是否重复
        User theUser = iUserService.getByAccount(userDto.getAccount());
        if (theUser!=null){
            ///用户已经存在
            throw new GunsException(BizExceptionEnum.USER_ALREADY_REG);
        }
        //加盐
        userDto.setSalt(ShiroKit.getRandomSalt(5));
        userDto.setPassword(ShiroKit.md5(userDto.getPassword(),userDto.getSalt()));
        //默认状态
        userDto.setStatus(ManagerStatus.OK.getCode());
        userDto.setCreatetime(new Date());
        this.iUserService.insert(UserFactory.createUser(userDto));
        return SUCCESS_TIP;
    }

    @RequestMapping("/delete")
    @BussinessLog(value = "删除ES数据",key = "userId",dict = UserDict.class)
    @Permission
    @ResponseBody
    public Tip delete(@RequestParam Integer userId){
        if (ToolUtil.isEmpty(userId)){
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        if (userId.equals(Const.ADMIN_ID)){
            throw new GunsException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }
        //判断是否有权限
        assertAuth(userId);
        this.iUserService.setStatus(userId,ManagerStatus.DELETED.getCode());
        return SUCCESS_TIP;
    }

    public void assertAuth(Integer userId){
        if (ShiroKit.isAdmin()){
            return;
        }
        //这里就要开始校验
        //拿到可以操作的部门数据范围
        List<Integer> deptDataScope = ShiroKit.getDeptDataScope();
        //获取用户信息,为了拿到他的部门ID
        User user = this.iUserService.selectById(userId);
        Integer deptid = user.getDeptid();
        //如果当前用户操作的部门包含在可以操作的部门中,那么ok
        if (deptDataScope.contains(deptid)){
            return;
        }else {
            throw new GunsException(BizExceptionEnum.NO_PERMITION);
        }
    }

    @RequestMapping("/edit")
    @BussinessLog(value = "修改ES数据",key = "account",dict = UserDict.class)
    @Permission
    @ResponseBody
    public Tip edit(@Valid UserDto userDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        User oldUser = iUserService.selectById(userDto.getId());
        if (ShiroKit.hasRole(Const.ADMIN_NAME)){
            this.iUserService.updateById(UserFactory.editUser(userDto,oldUser));
            return SUCCESS_TIP;
        }else{
            assertAuth(userDto.getId());
            ShiroUser shiroUser = ShiroKit.getUser();
            if (shiroUser.getId().equals(userDto.getId())){
                this.iUserService.updateById(UserFactory.editUser(userDto,oldUser));
                return SUCCESS_TIP;
            }else{
                throw new GunsException(BizExceptionEnum.NO_PERMITION);
            }
        }
    }
}

package cn.seclover.zyf.core.util;

import cn.seclover.zyf.core.common.constant.Const;
import cn.seclover.zyf.config.properties.GunsProperties;
import com.stylefeng.guns.core.node.MenuNode;
import com.stylefeng.guns.core.util.SpringContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * api接口文档显示过滤
 */
public class ApiMenuFilter extends MenuNode {

    public static List<MenuNode> build(List<MenuNode> nodes) {

        //如果关闭了接口文档,则不显示接口文档菜单
        GunsProperties gunsProperties = SpringContextHolder.getBean(GunsProperties.class);
        if (!gunsProperties.getSwaggerOpen()) {
            List<MenuNode> menuNodesCopy = new ArrayList<>();
            for (MenuNode menuNode : nodes) {
                if (Const.API_MENU_NAME.equals(menuNode.getName())) {
                    continue;
                } else {
                    menuNodesCopy.add(menuNode);
                }
            }
            nodes = menuNodesCopy;
        }

        return nodes;
    }
}

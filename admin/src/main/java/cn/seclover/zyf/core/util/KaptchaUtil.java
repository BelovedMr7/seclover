package cn.seclover.zyf.core.util;

import cn.seclover.zyf.config.properties.GunsProperties;
import com.stylefeng.guns.core.util.SpringContextHolder;

/**
 * 验证码工具类
 */
public class KaptchaUtil {

    /**
     * 获取验证码开关
     */
    public static Boolean getKaptchaOnOff() {
        return SpringContextHolder.getBean(GunsProperties.class).getKaptchaOpen();
    }
}
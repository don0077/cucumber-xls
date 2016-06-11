package org.simplestudio.cucumber;

import java.text.NumberFormat;

/**   
* @Title: Util.java 
* @Description: Util
* @author zhengzhq E-Mail:zhengzhq@jingoal.com
* @date 2016年6月11日 下午5:32:57 
*/
public class Util {

    public static final NumberFormat NUMBER_FORMAT = NumberFormat.getPercentInstance();

    public static String formatNum(double number) {
        return NUMBER_FORMAT.format(number);
    }

}

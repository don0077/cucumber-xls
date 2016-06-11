package org.simplestudio.cucumber;

import jxl.Sheet;
import jxl.format.CellFormat;

/**   
* @Title: XlsCellStyle.java 
* @Description: Excel Cell Format,init from template's "other" sheet.
* @author zhengzhq E-Mail:zzq0324@qq.com
* @date 2016年6月11日 下午8:01:07 
*/
public class XlsCellStyle {

    public static CellFormat RESULT_PASS_STYLE = null;
    public static CellFormat RESULT_FAIL_STYLE = null;
    public static CellFormat FEATURE_STYLE     = null;
    public static CellFormat SCENARIO_STYLE    = null;
    public static CellFormat STEP_STYLE        = null;
    public static CellFormat OTHER_STYLE       = null;

    public static void initCellStyle(Sheet sheet) {
        RESULT_PASS_STYLE = sheet.getCell(2, 1).getCellFormat();
        RESULT_FAIL_STYLE = sheet.getCell(2, 2).getCellFormat();
        FEATURE_STYLE = sheet.getCell(2, 4).getCellFormat();
        SCENARIO_STYLE = sheet.getCell(2, 5).getCellFormat();
        STEP_STYLE = sheet.getCell(2, 6).getCellFormat();
        OTHER_STYLE = sheet.getCell(2, 7).getCellFormat();
    }
}

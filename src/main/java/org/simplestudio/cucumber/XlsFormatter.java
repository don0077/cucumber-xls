package org.simplestudio.cucumber;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**   
* @Title: XlsFormatter.java 
* @Description: cucumber运行结果导出成excel,包含详细和汇总两个sheet
* @author zhengzhq E-Mail:zzq0324@qq.com
* @date 2016年6月10日 下午9:23:09 
*/
public class XlsFormatter implements Formatter, Reporter {

    //default report store dir
    private static final String           DEFAULT_REPORT_DIR = "report";
    public static final Pattern           PARAM_PATTERN      = Pattern.compile("\\$\\{(.+)\\}");
    private Logger                        logger             = Logger.getLogger(XlsFormatter.class);
    private static final SimpleDateFormat FILENAME_FORMAT    = new SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss");
    private WritableWorkbook              workBook;
    private int                           startRow           = 7;

    public XlsFormatter() {
        this(DEFAULT_REPORT_DIR);
    }

    public XlsFormatter(String reportStoreDir) {
        try {
            File templateFile = new File(XlsFormatter.class.getClassLoader()
                    .getResource("report_template.xls").getFile());
            Workbook template = Workbook.getWorkbook(templateFile);
            File storeDir = new File(reportStoreDir);
            if (!storeDir.exists()) {
                storeDir.mkdirs();
            }
            workBook = Workbook.createWorkbook(new File(storeDir, getFileName()), template);

            XlsCellStyle.initCellStyle(workBook.getSheet(2));
        } catch (Exception e) {
            logger.error("init ExcelReporter error{} ", e);
        }
    }

    /**
     * get filename format, like：report_2016-06-06-11-11-11.xls
     * @return
     */
    private String getFileName() {
        String fileName = "report_" + FILENAME_FORMAT.format(new Date()) + ".xls";

        return fileName;
    }

    @Override
    public void before(Match match, Result result) {
        // NoOp
    }

    @Override
    public void result(Result result) {
        ReporterContext.callbackResult(result, CucumberElement.Type.STEP);
    }

    @Override
    public void after(Match match, Result result) {

    }

    @Override
    public void match(Match match) {
        // NoOp
    }

    @Override
    public void embedding(String mimeType, byte[] data) {
        // NoOp
    }

    @Override
    public void write(String text) {
        // NoOp
    }

    @Override
    public void syntaxError(String state, String event, List<String> legalEvents, String uri,
            Integer line) {
        logger.error("feature: " + uri + " ,line: " + line + " syntaxError, please check it!");
    }

    @Override
    public void uri(String uri) {
        logger.info("start execute feature: " + uri);
    }

    @Override
    public void feature(Feature feature) {
        ReporterContext.addEle(feature, CucumberElement.Type.FEATURE);
    }

    @Override
    public void scenarioOutline(ScenarioOutline scenarioOutline) {
        // NoOp
    }

    @Override
    public void examples(Examples examples) {
        // NoOp
    }

    @Override
    public void startOfScenarioLifeCycle(Scenario scenario) {
        //NoOp
    }

    @Override
    public void background(Background background) {
        //NoOp
    }

    @Override
    public void scenario(Scenario scenario) {
        ReporterContext.addEle(scenario, CucumberElement.Type.SCENARIO);
    }

    @Override
    public void step(Step step) {
        ReporterContext.addEle(step, CucumberElement.Type.STEP);
    }

    @Override
    public void endOfScenarioLifeCycle(Scenario scenario) {
        //NoOp
    }

    @Override
    public void done() {
        try {
            //generate summary sheet
            generateSummary();

            workBook.write();
            logger.info("finish execute all features and generate report success !");
        } catch (Exception e) {
            logger.error("generate report error{} ", e);
        }
    }

    @Override
    public void close() {
        if (workBook != null) {
            try {
                workBook.close();
            } catch (Exception e) {
                logger.error("close workBook resource error{} ", e);
            }
        }
    }

    @Override
    public void eof() {
        //write feature execute info to xls
        try {
            genFeatureRunInfo();
        } catch (Exception e) {
            logger.error("generate feature run info error{} ", e);
        }
    }

    private void genFeatureRunInfo() throws Exception, WriteException {
        WritableSheet detailSheet = workBook.getSheet(1);
        Map<String, CucumberElement> featureEles = ReporterContext.getFeatureEles();
        if (!featureEles.isEmpty()) {
            Iterator<String> idIterator = featureEles.keySet().iterator();
            while (idIterator.hasNext()) {
                String id = idIterator.next();
                CucumberElement ele = featureEles.get(id);
                detailSheet.insertRow(startRow);
                boolean isFailed = Result.FAILED.equals(ele.getResult().getStatus());

                Label nameLabel = null;
                switch (ele.getType()) {
                    case FEATURE:
                        nameLabel = new Label(1, startRow, ele.getName(),
                                XlsCellStyle.FEATURE_STYLE);
                        detailSheet.addCell(new Label(2, startRow, "", XlsCellStyle.OTHER_STYLE));
                        detailSheet.addCell(new Label(3, startRow, "", XlsCellStyle.OTHER_STYLE));
                        //feature need calculate costtime at this time
                        ele.setCosttime(System.currentTimeMillis() - ele.getStartTime());
                        detailSheet.addCell(new Label(4, startRow, "", XlsCellStyle.OTHER_STYLE));
                        break;
                    case SCENARIO:
                        detailSheet.addCell(new Label(1, startRow, "", XlsCellStyle.OTHER_STYLE));
                        nameLabel = new Label(2, startRow, ele.getName(),
                                XlsCellStyle.SCENARIO_STYLE);
                        detailSheet.addCell(new Label(3, startRow, "", XlsCellStyle.OTHER_STYLE));
                        detailSheet.addCell(new Label(4, startRow, "", XlsCellStyle.OTHER_STYLE));
                        break;
                    case STEP:
                        detailSheet.addCell(new Label(1, startRow, "", XlsCellStyle.OTHER_STYLE));
                        detailSheet.addCell(new Label(2, startRow, "", XlsCellStyle.OTHER_STYLE));
                        nameLabel = new Label(3, startRow, ele.getName(), XlsCellStyle.STEP_STYLE);

                        detailSheet.addCell(new Label(4, startRow, ele.getResult().getStatus(),
                                isFailed ? XlsCellStyle.RESULT_FAIL_STYLE
                                        : XlsCellStyle.RESULT_PASS_STYLE));
                        break;
                }
                detailSheet.addCell(nameLabel);
                String remark = isFailed ? ele.getResult().getErrorMessage() : "";
                detailSheet.addCell(new Label(5, startRow, remark, XlsCellStyle.OTHER_STYLE));
                detailSheet.addCell(new Label(6, startRow, String.valueOf(ele.getCosttime()),
                        XlsCellStyle.OTHER_STYLE));

                startRow++;
            }
        }

        //clear last feature execute info
        ReporterContext.clear();
    }

    private void generateSummary() throws Exception {
        WritableSheet summarySheet = workBook.getSheet(0);
        Map<String, String> summaryInfoMap = new HashMap<String, String>();
        int successFeatures = ReporterContext.getTotalFeatures()
                - ReporterContext.getFailFeatures();

        int successScenarios = ReporterContext.getTotalScenarios()
                - ReporterContext.getFailScenarios();

        int successSteps = ReporterContext.getTotalSteps() - ReporterContext.getFailSteps();

        summaryInfoMap.put("successFeatures", String.valueOf(successFeatures));
        summaryInfoMap.put("failFeatures", String.valueOf(ReporterContext.getFailFeatures()));
        summaryInfoMap.put("totalFeatures", String.valueOf(ReporterContext.getTotalFeatures()));
        summaryInfoMap.put("featureRate",
                Util.formatNum(1.0 * successFeatures / ReporterContext.getTotalFeatures()));

        summaryInfoMap.put("successScenarios", String.valueOf(successScenarios));
        summaryInfoMap.put("failScenarios", String.valueOf(ReporterContext.getFailScenarios()));
        summaryInfoMap.put("totalScenarios", String.valueOf(ReporterContext.getTotalScenarios()));
        summaryInfoMap.put("scenarioRate",
                Util.formatNum(1.0 * successScenarios / ReporterContext.getTotalScenarios()));

        summaryInfoMap.put("successSteps", String.valueOf(successSteps));
        summaryInfoMap.put("failSteps", String.valueOf(ReporterContext.getFailSteps()));
        summaryInfoMap.put("totalSteps", String.valueOf(ReporterContext.getTotalSteps()));
        summaryInfoMap.put("stepRate",
                Util.formatNum(1.0 * successSteps / ReporterContext.getTotalSteps()));

        int rows = summarySheet.getRows();
        int columns = summarySheet.getColumns();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                WritableCell cell = summarySheet.getWritableCell(j, i);
                String contents = cell.getContents();
                String param = extractParam(contents);

                if (param != null) {
                    String value = summaryInfoMap.get(param);
                    if (value != null) {
                        Label label = new Label(j, i, value, cell.getCellFormat());
                        summarySheet.addCell(label);
                    }
                }
            }
        }
    }

    private String extractParam(String content) {
        if (content == null || "".equals(content)) {
            return null;
        }
        Matcher matcher = PARAM_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}

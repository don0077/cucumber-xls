package org.simplestudio.cucumber;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gherkin.formatter.model.BasicStatement;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.TagStatement;

/**   
* @Title: ReporterContext.java 
* @Description: reporter context
* @author zhengzhq E-Mail:zzq0324@qq.com
* @date 2016年6月10日 下午9:51:21 
*/
public class ReporterContext {

    private static int                          totalFeatures          = 0;
    private static int                          failFeatures           = 0;
    private static int                          totalScenarios         = 0;
    private static int                          failScenarios          = 0;
    private static int                          totalSteps             = 0;
    private static int                          failSteps              = 0;

    private static Map<String, CucumberElement> cucumberElesMap        = new LinkedHashMap<String, CucumberElement>();
    private static List<Result>                 scenarioHookResultList = new ArrayList<Result>();
    private static List<String>                 tempIdList             = new ArrayList<String>();
    public static CucumberElement               currentScenario        = null;
    public static CucumberElement               currentFeature         = null;

    public static void addEle(BasicStatement ele, CucumberElement.Type type) {
        CucumberElement cucumberEle = new CucumberElement();
        cucumberEle.setResult(Result.UNDEFINED);
        cucumberEle.setName(ele.getName());
        cucumberEle.setStartTime(System.currentTimeMillis());
        cucumberEle.setType(type);

        String id = "";

        if (type == CucumberElement.Type.STEP) {
            totalSteps++;
            id = currentScenario.getId() + "_" + ele.getLine();
        }
        else {
            id = ((TagStatement) ele).getId();
            if (type == CucumberElement.Type.SCENARIO) {
                currentScenario = cucumberEle;
                totalScenarios++;
            }
            else {
                currentFeature = cucumberEle;
                totalFeatures++;
            }
        }
        cucumberEle.setId(id);
        cucumberElesMap.put(id, cucumberEle);

        //step and scenario will callback result,so add it to tempIdList to process callback
        if (type == CucumberElement.Type.STEP || type == CucumberElement.Type.SCENARIO) {
            tempIdList.add(id);
        }
    }

    public static void callbackResult(Result result, CucumberElement.Type type) {
        if (type == CucumberElement.Type.STEP) {
            scenarioHookResultList.add(result);
            CucumberElement callbackEle = cucumberElesMap
                    .get(tempIdList.get(scenarioHookResultList.size()));
            callbackEle.setCosttime(Math.round(1.0 * result.getDuration() / 1000000));

            if (Result.FAILED.equals(result.getStatus())) {
                failSteps++;
            }
        }
        else {//scenario result
            scenarioHookResultList.add(0, result);
            currentScenario
                    .setCosttime(System.currentTimeMillis() - currentScenario.getStartTime());

            //if one scenario failed,the feature failed too.
            if (Result.FAILED.equals(result.getStatus())) {
                if (!Result.FAILED.equals(currentFeature.getResult().getStatus())) {
                    currentFeature.setResult(new Result(Result.FAILED, null, null));
                    failFeatures++;
                }
                failScenarios++;
            }

            //add scenario execute info to map
            for (int i = 0; i < tempIdList.size(); i++) {
                CucumberElement ele = cucumberElesMap.get(tempIdList.get(i));
                ele.setResult(scenarioHookResultList.get(i));
            }

            //clear temp list
            tempIdList.clear();
            scenarioHookResultList.clear();
        }
    }

    public static void clear() {
        cucumberElesMap.clear();
    }

    public static Map<String, CucumberElement> getFeatureEles() {
        return cucumberElesMap;
    }

    /**
     * @return the totalFeatures
     */
    public static int getTotalFeatures() {
        return totalFeatures;
    }

    /**
     * @return the failFeatures
     */
    public static int getFailFeatures() {
        return failFeatures;
    }

    /**
     * @return the totalScenarios
     */
    public static int getTotalScenarios() {
        return totalScenarios;
    }

    /**
     * @return the failScenarios
     */
    public static int getFailScenarios() {
        return failScenarios;
    }

    /**
     * @return the totalSteps
     */
    public static int getTotalSteps() {
        return totalSteps;
    }

    /**
     * @return the failSteps
     */
    public static int getFailSteps() {
        return failSteps;
    }

}

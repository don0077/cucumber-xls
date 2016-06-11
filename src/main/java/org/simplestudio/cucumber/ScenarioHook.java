package org.simplestudio.cucumber;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import gherkin.formatter.model.Result;

/**   
* @Title: ScenarioHook.java 
* @Description: Scenario Hook,use for monitor scenario result
* @author zhengzhq E-Mail:zzq0324@qq.com
* @date 2016年6月10日 下午10:19:53 
*/
public class ScenarioHook {

    private static final Result RESULT_PASS = new Result(Result.PASSED, 0l, "");
    private static final Result RESULT_FAIL = new Result(Result.FAILED, 0l, "");

    @After
    public void after(Scenario scenario) {
        ReporterContext.callbackResult(scenario.isFailed() ? RESULT_FAIL : RESULT_PASS,
                CucumberElement.Type.SCENARIO);
    }

}

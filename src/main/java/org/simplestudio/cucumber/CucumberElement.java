package org.simplestudio.cucumber;

import gherkin.formatter.model.Result;

/**   
* @Title: CucumberElement.java 
* @Description: CucumberElement
* @author zhengzhq E-Mail:zzq0324@qq.com
* @date 2016年6月11日 上午7:41:55 
*/
public class CucumberElement {
    private String id;
    private String name;
    private Type   type;
    private long   startTime;
    private Result result;
    private long   costtime;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * @return the startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the result
     */
    public Result getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * @return the costtime
     */
    public long getCosttime() {
        return costtime;
    }

    /**
     * @param costtime the costtime to set
     */
    public void setCosttime(long costtime) {
        this.costtime = costtime;
    }

    static enum Type {
        FEATURE,
        SCENARIO,
        STEP;
    }
}

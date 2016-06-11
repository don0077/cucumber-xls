cucumber-xls
==================================
cucumber excel formatter, excel report contains summary and detail sheet.It's convenient to generate excel report with it.

Overview
----------------------------------
The Cucumber Xls Formatter will help you generate excel report. A template is defiened named "report_template.xls".The template consist of two parts: summary and detail
(1)Summary Part: it contains total Features,total Scenarios,total Steps and so on;
![image](https://github.com/zhengzhiqiang/cucumber-xls/blob/master/screenshot/summary.png)
(2)Detail Part: it contains every step's result and costtime.
![image](https://github.com/zhengzhiqiang/cucumber-xls/blob/master/screenshot/detail.png)

By the way,it use jxl to export excel.

Useage
----------------------------------
@RunWith(Cucumber.class)
@CucumberOptions(plugin = "org.simplestudio.cucumber.XlsFormatter")
public void RunCuckes{
...........
}


package com.yanggu.metric_calculate.config.util.excel;

import java.io.Serializable;
import java.util.List;

public class FormExcelModel implements Serializable {

    private String sheetName;
    private List<String> title;
    private List<Object> data;

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }


    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    @Override
    public String toString() {
        return "FormExcelModel{"
                + "sheetName='" + sheetName + '\''
                + ", title=" + title
                + ", data=" + data
                + '}';
    }
}

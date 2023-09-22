package com.yanggu.metric_calculate.config.util.excel;

import java.io.Serializable;
import java.util.List;

public class ListExcelModel implements Serializable {

    private List<String> title;
    private List<List<Object>> data;
    private String sheetName;

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }


    public List<List<Object>> getData() {
        return data;
    }

    public void setData(List<List<Object>> data) {
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
        return "ListExcelModel{"
                + "title=" + title
                + ", data=" + data
                + ", sheetName='" + sheetName + '\''
                + '}';
    }
}

package com.yanggu.metric_calculate.config.test;


import com.yanggu.metric_calculate.config.enums.AccuracyEnum;
import com.yanggu.metric_calculate.config.pojo.dto.*;
import com.yanggu.metric_calculate.core.pojo.metric.RoundAccuracy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@DisplayName("派生指标导入和导出测试")
class DeriveExcelTest {

    @Test
    @Disabled("仅测试使用")
    @DisplayName("导出测试")
    void exportTest() {

    }

    @Test
    @Disabled("仅测试使用")
    @DisplayName("导入测试")
    void importTest() throws Exception {
        InputStream inputStream = this.getClass().getResourceAsStream("/excel/template/dervie_template.xlsx");
        assert inputStream != null;
        Workbook workbook = new XSSFWorkbook(inputStream);
        //int numberOfSheets = workbook.getNumberOfSheets();
        //System.out.println("numberOfSheets = " + numberOfSheets);
        DeriveDto deriveDto = new DeriveDto();
        Sheet sheet = workbook.getSheetAt(1);

        //基本信息
        //派生指标名称
        deriveDto.setName(sheet.getRow(2).getCell(1).getStringCellValue());
        //中文名称
        deriveDto.setDisplayName(sheet.getRow(3).getCell(1).getStringCellValue());
        //描述
        deriveDto.setDescription(sheet.getRow(4).getCell(1).getStringCellValue());
        //宽表名称
        deriveDto.setModelName(sheet.getRow(5).getCell(1).getStringCellValue());
        //是否包含当前笔
        deriveDto.setIncludeCurrent(Boolean.valueOf(sheet.getRow(6).getCell(1).getStringCellValue()));
        //计量单位
        deriveDto.setUnitMeasure(sheet.getRow(7).getCell(1).getStringCellValue());
        //精度类型
        deriveDto.setRoundAccuracyType(AccuracyEnum.valueOf(sheet.getRow(8).getCell(1).getStringCellValue()));
        //精度长度
        deriveDto.setRoundAccuracyLength(Integer.parseInt(sheet.getRow(9).getCell(1).getStringCellValue()));
        //数据类型
        deriveDto.setDataType(((int) sheet.getRow(10).getCell(1).getNumericCellValue()));


        //时间字段
        String modelColumnName = sheet.getRow(13).getCell(1).getStringCellValue();
        String timeFormat = sheet.getRow(14).getCell(1).getStringCellValue();
        ModelTimeColumnDto modelTimeColumnDto = new ModelTimeColumnDto();
        deriveDto.setModelTimeColumn(modelTimeColumnDto);
        modelTimeColumnDto.setModelColumnName(modelColumnName);
        modelTimeColumnDto.setTimeFormat(timeFormat);


        //维度字段列表
        List<ModelDimensionColumnDto> modelDimensionColumnList = new ArrayList<>();
        deriveDto.setModelDimensionColumnList(modelDimensionColumnList);
        int filterIndex = 0;
        ll: for (int i = 18; i < sheet.getPhysicalNumberOfRows(); i++) {
            Cell cell1 = sheet.getRow(i).getCell(0);
            Cell cell2 = sheet.getRow(i).getCell(1);
            List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
            //判断是否为合并单元格
            for (CellRangeAddress mergedRegion : mergedRegions) {
                if (mergedRegion.isInRange(cell1)) {
                    filterIndex = i;
                    break ll;
                }
            }
            ModelDimensionColumnDto modelDimensionColumnDto = new ModelDimensionColumnDto();
            modelDimensionColumnDto.setModelColumnName(cell1.getStringCellValue());
            modelDimensionColumnDto.setDimensionName(cell2.getStringCellValue());
            modelDimensionColumnList.add(modelDimensionColumnDto);
        }

        //前置过滤条件
        AviatorExpressParamDto aviatorExpressParamDto = null;
        //表达式
        String filterExpress = sheet.getRow(filterIndex + 2).getCell(1).getStringCellValue();
        //Aviator函数
        Cell cell = sheet.getRow(filterIndex + 3).getCell(0);
        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        //判断是否为合并单元格
        for (CellRangeAddress mergedRegion : mergedRegions) {
            if (mergedRegion.isInRange(cell)) {
                int firstRow = mergedRegion.getFirstRow();
                int lastRow = mergedRegion.getLastRow();
                for (int i = firstRow + 1; i < lastRow + 1; i++) {
                    Cell cell1 = sheet.getRow(i).getCell(1);
                    if (cell1 == null) {
                        break;
                    }
                    AviatorFunctionInstanceDto aviatorFunctionInstanceDto = new AviatorFunctionInstanceDto();
                    //aviatorFunctionInstanceDto.setParam();
                }
            }
        }
        if (StrUtil.isNotBlank(filterExpress)) {
            aviatorExpressParamDto = new AviatorExpressParamDto();
            aviatorExpressParamDto.setExpress(filterExpress);
        }
        if (aviatorExpressParamDto != null) {
            deriveDto.setFilterExpressParam(aviatorExpressParamDto);
        }


        System.out.println(deriveDto);
    }

}

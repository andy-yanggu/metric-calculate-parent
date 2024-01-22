package com.yanggu.metric_calculate.config.test;


import com.yanggu.metric_calculate.config.enums.AccuracyEnum;
import com.yanggu.metric_calculate.config.enums.AggregateFunctionTypeEnums;
import com.yanggu.metric_calculate.config.enums.TimeUnitEnum;
import com.yanggu.metric_calculate.config.enums.WindowTypeEnum;
import com.yanggu.metric_calculate.config.pojo.dto.*;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.config.enums.AggregateFunctionTypeEnums.*;
import static com.yanggu.metric_calculate.config.enums.WindowTypeEnum.*;

@DisplayName("派生指标导入和导出测试")
class DeriveExcelTest {

    @Test
    @Disabled("仅测试使用")
    @DisplayName("导出测试")
    void exportTest() {

    }

    @Test
    @Disabled("仅测试使用")
    @DisplayName("时间窗口_数值型_度量字段表达式")
    void importTest1() throws Exception {
        InputStream inputStream = this.getClass().getResourceAsStream("/excel/template/时间窗口_数值型_度量字段表达式.xlsx");
        assert inputStream != null;
        Workbook workbook = new XSSFWorkbook(inputStream);
        DeriveDTO deriveDto = new DeriveDTO();
        Sheet sheet = workbook.getSheetAt(1);

        //基本信息
        //派生指标名称
        deriveDto.setName(sheet.getRow(2).getCell(1).getStringCellValue());
        //中文名称
        deriveDto.setDisplayName(sheet.getRow(3).getCell(1).getStringCellValue());
        //描述
        deriveDto.setDescription(sheet.getRow(4).getCell(1).getStringCellValue());
        //宽表名称
        //deriveDto.setModelName(sheet.getRow(5).getCell(1).getStringCellValue());
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
        ModelTimeColumnDTO modelTimeColumnDto = new ModelTimeColumnDTO();
        //deriveDto.setModelTimeColumn(modelTimeColumnDto);
        modelTimeColumnDto.setModelColumnName(modelColumnName);
        modelTimeColumnDto.setTimeFormat(timeFormat);

        //维度字段列表
        List<ModelDimensionColumnDTO> modelDimensionColumnList = new ArrayList<>();
        deriveDto.setModelDimensionColumnList(modelDimensionColumnList);
        int filterIndex = 0;
        ll:
        for (int i = 18; i < sheet.getPhysicalNumberOfRows(); i++) {
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
            ModelDimensionColumnDTO modelDimensionColumnDto = new ModelDimensionColumnDTO();
            //宽表字段名
            modelDimensionColumnDto.setModelColumnName(cell1.getStringCellValue());
            //维度名称
            modelDimensionColumnDto.setDimensionName(cell2.getStringCellValue());
            modelDimensionColumnList.add(modelDimensionColumnDto);
        }

        //前置过滤条件
        AviatorExpressParamDTO aviatorExpressParamDto = null;
        int windowParamIndex = filterIndex + 4 + 2;
        //表达式
        String filterExpress = sheet.getRow(filterIndex + 2).getCell(1).getStringCellValue();
        if (StrUtil.isNotBlank(filterExpress)) {
            aviatorExpressParamDto = new AviatorExpressParamDTO();
            aviatorExpressParamDto.setExpress(filterExpress);
            List<AviatorFunctionInstanceDTO> aviatorFunctionInstanceList = new ArrayList<>();
            aviatorExpressParamDto.setAviatorFunctionInstanceList(aviatorFunctionInstanceList);
            //Aviator函数
            Cell cell = sheet.getRow(filterIndex + 3).getCell(0);
            List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
            //判断是否为合并单元格
            boolean mergedCell = false;
            CellRangeAddress mergedRegion = null;
            for (CellRangeAddress tempMergedRegion : mergedRegions) {
                if (tempMergedRegion.isInRange(cell)) {
                    mergedCell = true;
                    mergedRegion = tempMergedRegion;
                    break;
                }
            }
            //如果是合并单元格
            if (mergedCell) {
                int firstRow = mergedRegion.getFirstRow();
                int lastRow = mergedRegion.getLastRow();
                windowParamIndex = windowParamIndex + lastRow - firstRow;
                for (int i = firstRow + 1; i < lastRow + 1; i++) {
                    Cell cell1 = sheet.getRow(i).getCell(1);
                    if (cell1 == null || StrUtil.isBlank(cell1.getStringCellValue())) {
                        continue;
                    }
                    AviatorFunctionInstanceDTO aviatorFunctionInstanceDto = new AviatorFunctionInstanceDTO();
                    aviatorFunctionInstanceDto.setName(cell1.getStringCellValue());
                }
            } else {
                Cell cell1 = sheet.getRow(filterIndex + 3).getCell(1);
                if (cell1 != null && StrUtil.isNotBlank(cell1.getStringCellValue())) {
                    AviatorFunctionInstanceDTO aviatorFunctionInstanceDto = new AviatorFunctionInstanceDTO();
                    aviatorFunctionInstanceDto.setName(cell1.getStringCellValue());
                    aviatorFunctionInstanceList.add(aviatorFunctionInstanceDto);
                }
            }
            deriveDto.setFilterExpressParam(aviatorExpressParamDto);
        } else {
            //如果表达式为null, 说明没有前置过滤条件
            //直接跳到窗口参数
        }

        //窗口参数
        String windowType = sheet.getRow(windowParamIndex).getCell(1).getStringCellValue();
        WindowTypeEnum windowTypeEnum = WindowTypeEnum.valueOf(windowType);
        WindowParamDTO windowParamDto = new WindowParamDTO();
        deriveDto.setWindowParam(windowParamDto);
        int aggregateFunctionParamIndex = windowParamIndex;
        //滚动时间窗口和滑动时间窗口
        if (windowTypeEnum.equals(TUMBLING_TIME_WINDOW) || SLIDING_TIME_WINDOW.equals(windowTypeEnum)) {
            double numericCellValue = sheet.getRow(windowParamIndex + 1).getCell(1).getNumericCellValue();
            //时间周期
            int duration = Double.valueOf(numericCellValue).intValue();
            windowParamDto.setDuration(duration);
            //时间单位
            String stringCellValue = sheet.getRow(windowParamIndex + 2).getCell(1).getStringCellValue();
            windowParamDto.setTimeUnit(TimeUnitEnum.valueOf(stringCellValue));
            //滑动计数窗口
        } else if (SLIDING_COUNT_WINDOW.equals(windowTypeEnum)) {

            //状态窗口
        } else if (STATUS_WINDOW.equals(windowTypeEnum)) {

            //全局窗口
        } else if (GLOBAL_WINDOW.equals(windowTypeEnum)) {

            //会话窗口
        } else if (SESSION_WINDOW.equals(windowTypeEnum)) {

            //事件窗口
        } else if (EVENT_WINDOW.equals(windowTypeEnum)) {

        } else {
            //报错
        }

        //聚合函数参数
        AggregateFunctionParamDTO aggregateFunctionParamDto = new AggregateFunctionParamDTO();
        //deriveDto.setAggregateFunctionParam(aggregateFunctionParamDto);
        String aggregateFunctionName = sheet.getRow(aggregateFunctionParamIndex).getCell(1).getStringCellValue();
        //根据name换id
        AggregateFunctionEntity aggregateFunction = getAggregateFunctionByName(aggregateFunctionName);
        Integer aggregateFunctionId = aggregateFunction.getId();
        aggregateFunctionParamDto.setAggregateFunctionId(aggregateFunctionId);
        String paramJsonString = sheet.getRow(aggregateFunctionParamIndex + 1).getCell(1).getStringCellValue();
        Map<String, Object> jsonParam = getJsonParam(paramJsonString);
        AggregateFunctionTypeEnums aggregateFunctionType = aggregateFunction.getType();
        //数值型
        if (NUMERICAL.equals(aggregateFunctionType)) {
            BaseUdafParamDTO baseUdafParamDto = new BaseUdafParamDTO();
            baseUdafParamDto.setAggregateFunctionId(aggregateFunctionId);
            baseUdafParamDto.setParam(jsonParam);
            Boolean multiNumber = aggregateFunction.getMultiNumber();
            if (multiNumber) {
                List<AviatorExpressParamDTO> list = new ArrayList<>();
                baseUdafParamDto.setMetricExpressParamList(list);
            } else {
                AviatorExpressParamDTO expressParamDto = createAviatorExpressParamDto();
                baseUdafParamDto.setMetricExpressParam(expressParamDto);
            }
            //集合型
        } else if (COLLECTIVE.equals(aggregateFunctionType)) {
            BaseUdafParamDTO baseUdafParamDto = new BaseUdafParamDTO();
            baseUdafParamDto.setAggregateFunctionId(aggregateFunctionId);
            baseUdafParamDto.setParam(jsonParam);
            Integer keyStrategy = aggregateFunction.getKeyStrategy();
            //去重字段列表
            if (Integer.valueOf(1).equals(keyStrategy)) {

                //排序字段表达式列表
            } else if (Integer.valueOf(2).equals(keyStrategy)) {
                
            } else {

            }
            Integer retainStrategy = aggregateFunction.getRetainStrategy();
            if (Integer.valueOf(1).equals(retainStrategy)) {

            }
            //对象型
        } else if (OBJECTIVE.equals(aggregateFunctionType)) {
            BaseUdafParamDTO baseUdafParamDto = new BaseUdafParamDTO();
            baseUdafParamDto.setAggregateFunctionId(aggregateFunctionId);
            baseUdafParamDto.setParam(jsonParam);
            Integer keyStrategy = aggregateFunction.getKeyStrategy();
            //如果需要比较字段
            if (Integer.valueOf(3).equals(keyStrategy)) {
                List<AviatorExpressParamDTO> objectiveCompareFieldParamList = new ArrayList<>();
                //baseUdafParamDto.setObjectiveCompareFieldParamList(objectiveCompareFieldParamList);
            }
            //如果需要保留字段
            Integer retainStrategy = aggregateFunction.getRetainStrategy();
            if (Integer.valueOf(1).equals(retainStrategy)) {
                AviatorExpressParamDTO expressParamDto = createAviatorExpressParamDto();
                baseUdafParamDto.setMetricExpressParam(expressParamDto);
            }
            //映射型
        } else if (MAP_TYPE.equals(aggregateFunctionType)) {
            MapUdafParamDTO mapUdafParamDto = new MapUdafParamDTO();
            mapUdafParamDto.setAggregateFunctionId(aggregateFunctionId);
            mapUdafParamDto.setParam(jsonParam);
            List<AviatorExpressParamDTO> list = new ArrayList<>();
            mapUdafParamDto.setDistinctFieldParamList(list);
            BaseUdafParamDTO baseUdafParamDto = new BaseUdafParamDTO();
            mapUdafParamDto.setValueAggParam(baseUdafParamDto);
            //混合型
        } else if (MIX.equals(aggregateFunctionType)) {
            MixUdafParamDTO mixUdafParamDto = new MixUdafParamDTO();
            mixUdafParamDto.setAggregateFunctionId(aggregateFunctionId);
            mixUdafParamDto.setParam(jsonParam);
            AviatorExpressParamDTO aviatorExpressParamDto1 = createAviatorExpressParamDto();
            //设置计算表达式
            mixUdafParamDto.setMetricExpressParam(aviatorExpressParamDto1);
            //设置混合参数列表
            List<MixUdafParamItemDTO> mixUdafParamItemList = new ArrayList<>();
            mixUdafParamDto.setMixUdafParamItemList(mixUdafParamItemList);
        }
        System.out.println(deriveDto);
    }

    private AggregateFunctionEntity getAggregateFunctionByName(String aggregateFunctionName) {
        return null;
    }

    private Map<String, Object> getJsonParam(String paramJsonString) {
        if (StrUtil.isBlank(paramJsonString)) {
            return null;
        }
        return JSONUtil.parseObj(paramJsonString);
    }

    private AviatorExpressParamDTO createAviatorExpressParamDto() {
        AviatorExpressParamDTO expressParamDto = new AviatorExpressParamDTO();
        List<AviatorFunctionInstanceDTO> aviatorFunctionInstanceList = new ArrayList<>();
        expressParamDto.setAviatorFunctionInstanceList(aviatorFunctionInstanceList);
        return expressParamDto;
    }

}

package com.yanggu.metric_calculate.config.util.excel;

import com.yanggu.metric_calculate.config.util.excel.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.convert.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelUtil {

    private ExcelUtil() {
    }

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    private static final String FILE_END_XLS = "xls";
    private static final String FILE_END_XLSX = "xlsx";
    private static final String DEFAULT_SHEET_NAME = "Sheet1";
    private static final String DEFAULT_FONT_NAME = "微软雅黑";
    private static final short DEFAULT_FONT_SIZE = 11;
    private static final int DEFAULT_BORDER_SIZE = 1;
    private static final short DEFAULT_ROW_HEIGHT = 300;
    private static final int DEFAULT_CELL_WIDTH = 356 * 20;

    // 列分隔符，用来分隔多个从表
    private static final String LIST_SEPARATOR = "fmZ4MsRRH*wp3#5x+rT^0Z6HtEEyvCjMT9o6t7q4!k+tm^g0D3X&Svo1e_2c%e19I!nKM*v9SnAZHd5OysJl38MmN^Oq23GI8$rZ";

    /**
     * 导出.
     *
     * @param response .
     * @param list     .
     */
    public static <T> void exportFormList(HttpServletResponse response, List<T> list) {
        response.setContentType("application/vnd.openxmlformats-o`fficedocument.spreadsheetml.sheet");
        Class<?> clazz = list.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        //标量表单字段
        List<Field> formFields = Arrays.stream(fields)
                .filter(e -> e.isAnnotationPresent(ExcelExport.class))
                //根据ExcelExport中的sort进行升序排序
                .sorted(Comparator.comparingInt(tempField -> tempField.getAnnotation(ExcelExport.class).sort()))
                .toList();
        //对象字段
        List<Field> objectFields = Arrays.stream(fields)
                .filter(e -> e.isAnnotationPresent(ExcelObject.class))
                .toList();
        //列表字段
        List<Field> listFields = Arrays.stream(fields)
                .filter(e -> e.isAnnotationPresent(ExcelListColumn.class))
                .toList();
        try (OutputStream outputStream = response.getOutputStream(); XSSFWorkbook wb = new XSSFWorkbook()) {
            for (T t : list) {
                //写入的行号索引
                int rowIndex = 0;
                //标量表单字段
                List<String> formTitle = new ArrayList<>();
                List<Object> formData = new ArrayList<>();
                for (Field field : formFields) {
                    field.setAccessible(true);
                    formTitle.add(field.getAnnotation(ExcelExport.class).name());
                    ExcelExport annotation = field.getAnnotation(ExcelExport.class);
                    if (StringUtils.isNotEmpty(annotation.readConverterExp())) {
                        String value = reverseByExp(ConvertUtil.toStr(field.get(t)), annotation.readConverterExp(), annotation.separator());
                        formData.add(value);
                    } else {
                        formData.add(field.get(t));
                    }
                }
                String sheetName = formData.get(0).toString();
                FormExcelModel formExcelModel = new FormExcelModel();
                formExcelModel.setSheetName(sheetName);
                formExcelModel.setTitle(formTitle);
                formExcelModel.setData(formData);
                ExcelUtil.exportFormExcel(formExcelModel, wb, 0);

                rowIndex = rowIndex + formTitle.size();

                //对象字段
                for (Field objectField : objectFields) {
                    ExcelObject annotation = objectField.getAnnotation(ExcelObject.class);
                    List<String> excludeNameArray = Arrays.stream(annotation.excludeNameArray()).toList();
                    objectField.setAccessible(true);
                    Object objectFormData = objectField.get(t);
                    if (objectFormData == null) {
                        continue;
                    }
                    List<Field> objectFormFieldList = Arrays.stream(objectField.getType().getDeclaredFields())
                            .filter(e -> !excludeNameArray.contains(e.getName()))
                            .filter(e -> e.isAnnotationPresent(ExcelExport.class))
                            //根据ExcelExport中的sort进行升序排序
                            .sorted(Comparator.comparingInt(tempField -> tempField.getAnnotation(ExcelExport.class).sort()))
                            .toList();

                    if (CollUtil.isEmpty(objectFormFieldList)) {
                        continue;
                    }

                    FormExcelModel tempFormExcelModel = new FormExcelModel();
                    List<String> objectFormTitle = new ArrayList<>();
                    List<Object> objectFormDataList = new ArrayList<>();
                    tempFormExcelModel.setSheetName(sheetName);
                    tempFormExcelModel.setTitle(objectFormTitle);
                    tempFormExcelModel.setData(objectFormDataList);
                    for (Field objectFormField : objectFormFieldList) {
                        objectFormField.setAccessible(true);
                        //添加标题
                        objectFormTitle.add(objectFormField.getAnnotation(ExcelExport.class).name());
                        //添加数据
                        objectFormDataList.add(objectFormField.get(objectFormData));
                    }
                    String name = annotation.name();
                    ExcelUtil.exportObjectFormExcel(tempFormExcelModel, wb, rowIndex, name);
                    rowIndex = rowIndex + objectFormFieldList.size() + 1;
                }

                //列表字段
                for (Field field : listFields) {
                    field.setAccessible(true);
                    List<String> listTitle = new ArrayList<>();
                    List<List<Object>> listData = new ArrayList<>();
                    //list
                    List<Object> baseEntityList = (List<Object>) field.get(t);
                    if (CollectionUtils.isEmpty(baseEntityList)) {
                        continue;
                    }
                    Class clazz1 = baseEntityList.get(0).getClass();
                    List<Field> fields1 = Arrays.stream(clazz1.getDeclaredFields())
                            .filter(e -> e.isAnnotationPresent(ExcelExport.class))
                            //根据ExcelExport中的sort进行升序排序
                            .sorted(Comparator.comparingInt(tempField -> tempField.getAnnotation(ExcelExport.class).sort()))
                            .toList();
                    for (Field field1 : fields1) {
                        listTitle.add(field1.getAnnotation(ExcelExport.class).name());
                    }
                    for (Object baseEntity1 : baseEntityList) {
                        List<Object> data = new ArrayList<>();
                        for (Field field1 : fields1) {
                            field1.setAccessible(true);
                            if (field1.isAnnotationPresent(ExcelExport.class)) {
                                ExcelExport annotation = field1.getAnnotation(ExcelExport.class);
                                if (StringUtils.isNotEmpty(annotation.readConverterExp())) {
                                    String value = reverseByExp(ConvertUtil.toStr(field1.get(baseEntity1)), annotation.readConverterExp(), annotation.separator());
                                    data.add(value);
                                } else {
                                    data.add(field1.get(baseEntity1));
                                }
                            }
                        }
                        listData.add(data);
                    }
                    ListExcelModel listExcelModel = new ListExcelModel();
                    listExcelModel.setSheetName(sheetName);
                    listExcelModel.setTitle(listTitle);
                    listExcelModel.setData(listData);
                    ExcelUtil.exportListExcel(listExcelModel, wb, rowIndex, field.getAnnotation(ExcelListColumn.class).name());
                    rowIndex = rowIndex + listData.size() + 2;
                }
            }
            wb.write(outputStream);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("导出失败！");
        }
    }

    /**
     * 反向解析值 男=0,女=1,未知=2.
     *
     * @param propertyValue 参数值
     * @param converterExp  翻译注解
     * @param separator     分隔符
     * @return 解析后值
     */
    public static String reverseByExp(String propertyValue, String converterExp, String separator) {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(separator, propertyValue)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[1].equals(value)) {
                        propertyString.append(itemArray[0]).append(separator);
                        break;
                    }
                }
            } else {
                if (itemArray[1].equals(propertyValue)) {
                    return itemArray[0];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * .
     *
     * @param response response
     * @param list     list
     */
    private static void exportList(HttpServletResponse response, List<Object> list) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        try (OutputStream outputStream = response.getOutputStream();
             Workbook workbook = exportWorkBook(list)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * .
     *
     * @param list list
     * @return ret
     */
    private static Workbook exportWorkBook(List<Object> list) {
        Class clazz = list.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        //需要导出的字段
        List<Field> needExportFields = Arrays.stream(fields).filter(field -> field.isAnnotationPresent(ExcelExport.class))
                .collect(Collectors.toList());
        List<String> titleList = needExportFields.stream().map(e -> e.getAnnotation(ExcelExport.class).name())
                .collect(Collectors.toList());
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            Row titleRow = sheet.createRow(0);
            Font titleFont = workbook.createFont();
            titleFont.setFontName(DEFAULT_FONT_NAME);
            titleFont.setBold(true);
            titleFont.setColor(IndexedColors.BLACK.index);
            XSSFCellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);
            for (int i = 0; i < titleList.size(); i++) {
                Cell cell = titleRow.createCell(i);
                cell.setCellValue(titleList.get(i));
                cell.setCellStyle(titleStyle);
            }
            for (int i = 0; i < list.size(); i++) {
                Object baseEntity = list.get(i);
                Row dataRow = sheet.createRow(i + 1);
                for (int j = 0; j < needExportFields.size(); j++) {
                    Field field = needExportFields.get(j);
                    field.setAccessible(true);
                    Cell cell = dataRow.createCell(j);
                    cell.setCellValue(field.get(baseEntity) == null ? null : field.get(baseEntity).toString());
                }
            }
            return workbook;
        } catch (IllegalAccessException e) {
            logger.error("", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * .
     *
     * @param list list
     * @return ret
     */
    public static DepFile exportListWithErrMsg(List list) {
        Class clazz = list.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<String> listTitle = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelExport.class)) {
                listTitle.add(field.getAnnotation(ExcelExport.class).name());
            }
        }
        List<List<Object>> listData = new ArrayList<>();
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             XSSFWorkbook wb = new XSSFWorkbook()) {
            ListExcelModel listExcelModel = new ListExcelModel();
            for (Object baseEntity : list) {
                List<Object> data = new ArrayList<>();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ExcelExport.class)) {
                        field.setAccessible(true);
                        ImportErrDto importErrDto;
                        if (field.get(baseEntity) instanceof ImportErrDto) {
                            importErrDto = (ImportErrDto) field.get(baseEntity);
                        } else {
                            throw new RuntimeException("字段" + field.getName() + "的类型不是ImportErrDto！");
                        }
                        data.add(importErrDto);
                    }
                }
                listData.add(data);
            }
            listExcelModel.setSheetName("sheet");
            listExcelModel.setTitle(listTitle);
            listExcelModel.setData(listData);
            ExcelUtil.exportListExcelWithErrMsg(listExcelModel, wb, 1);
            wb.write(byteArrayOutputStream);
            DepFile depFile = new DepFile();
            depFile.setFile(byteArrayOutputStream.toByteArray());
            return depFile;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 导出list错误信息.
     *
     * @param list list
     */
    public static DepFile exportListWithErrMsg(MultipartFile multipartFile, List list, int dataSheetIndex,
                                               int titleRowIndex) {
        Class clazz = list.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        // List<String> listTitle = new ArrayList<>();
        // for (Field field : fields) {
        //     if (field.isAnnotationPresent(Excel.class)) {
        //         listTitle.add(field.getAnnotation(Excel.class).name());
        //     }
        // }
        List<List<Object>> listData = new ArrayList<>();
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             XSSFWorkbook wb = new XSSFWorkbook(multipartFile.getInputStream())) {
            ListExcelModel listExcelModel = new ListExcelModel();
            boolean errorSheet = false;
            XSSFSheet sheet = wb.getSheetAt(dataSheetIndex);
            for (Object baseEntity : list) {
                List<Object> data = new ArrayList<>();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ExcelExport.class)) {
                        field.setAccessible(true);
                        ImportErrDto importErrDto;
                        if (field.get(baseEntity) instanceof ImportErrDto) {
                            importErrDto = (ImportErrDto) field.get(baseEntity);
                            if (importErrDto.getErrMsg() != null) {
                                errorSheet = true;
                            }
                        } else {
                            throw new RuntimeException("字段" + field.getName() + "的类型不是ImportErrDto！");
                        }
                        data.add(importErrDto);
                    }
                }
                listData.add(data);
            }
            if (errorSheet) {
                sheet.setTabColor(new XSSFColor(new Color(255, 0, 0), new DefaultIndexedColorMap()));
            }
            listExcelModel.setSheetName(sheet.getSheetName());
            // listExcelModel.setTitle(listTitle);
            listExcelModel.setData(listData);
            ExcelUtil.exportListExcelWithErrMsg(listExcelModel, wb, titleRowIndex + 1);
            wb.write(byteArrayOutputStream);
            DepFile depFile = new DepFile();
            depFile.setFile(byteArrayOutputStream.toByteArray());
            return depFile;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 导出list错误信息.
     *
     * @param list list
     */
    public static DepFile exportListWithErrMsg(LinkedHashMap<Object, Map<String, String>> list) {
        if (list.size() == 0) {
            return null;
        }
        Class clazz = list.keySet().stream().findAny().get().getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<String> listTitle = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelExport.class)) {
                listTitle.add(field.getAnnotation(ExcelExport.class).name());
            }
        }
        List<List<Object>> listData = new ArrayList<>();
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             XSSFWorkbook wb = new XSSFWorkbook()) {
            ListExcelModel listExcelModel = new ListExcelModel();
            for (Object baseEntity : list.keySet()) {
                List<Object> data = new ArrayList<>();
                Map<String, String> fieldMsgMap = list.get(baseEntity);
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ExcelExport.class)) {
                        field.setAccessible(true);
                        ImportErrDto importErrDto = new ImportErrDto();
                        importErrDto.setValue(field.get(baseEntity));
                        String name = field.getName();
                        if (fieldMsgMap.containsKey(name)) {
                            importErrDto.setErrMsg(fieldMsgMap.get(name));
                        }
                        data.add(importErrDto);
                    }
                }
                listData.add(data);
            }
            listExcelModel.setSheetName("sheet");
            listExcelModel.setTitle(listTitle);
            listExcelModel.setData(listData);
            ExcelUtil.exportListExcelWithErrMsg(listExcelModel, wb, 0, new XSSFColor());
            wb.write(byteArrayOutputStream);
            DepFile depFile = new DepFile();
            depFile.setFile(byteArrayOutputStream.toByteArray());
            return depFile;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static DepFile exportFormListWithErrMsg(MultipartFile multipartFile, List list) {
        return exportFormListWithErrMsg(multipartFile, list, 0);
    }

    /**
     * 导出formList错误信息.
     *
     * @param multipartFile  原始文件
     * @param dataSheetIndex 数据起始sheet(索引，从0开始计数)
     * @param list           要导出的数据list
     */
    public static DepFile exportFormListWithErrMsg(MultipartFile multipartFile, List list, int dataSheetIndex) {
        Class clazz = list.get(0).getClass();
        //类中定义的全部字段
        Field[] fields = clazz.getDeclaredFields();
        //过滤类中被@Excel标注的字段
        List<Field> formFields = Arrays.stream(fields).filter(e -> e.isAnnotationPresent(ExcelExport.class))
                .collect(Collectors.toList());
        //这些字段的@Excel注解的name就是form的表头
        List<String> formTitle = formFields.stream().map(e -> e.getAnnotation(ExcelExport.class).name())
                .collect(Collectors.toList());
        //过滤类中被@ExcelListColumn标注的第一个字段，只有第一个会被导出为list数据
        List<Field> listFields = Arrays.stream(fields).filter(e -> e.isAnnotationPresent(ExcelListColumn.class))
                .collect(Collectors.toList());
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             XSSFWorkbook wb = new XSSFWorkbook(multipartFile.getInputStream())) {
            for (int i = 0; i < list.size(); i++) {
                //form
                Object baseEntity = list.get(i);
                XSSFSheet sheet = wb.getSheetAt(dataSheetIndex++);
                //该sheet是否有错误
                boolean errorSheet = false;
                String sheetName = sheet.getSheetName();
                List<Object> formData = new ArrayList<>();
                for (Field field : formFields) {
                    field.setAccessible(true);
                    if (field.get(baseEntity) == null) {
                        formData.add(null);
                    } else if (field.get(baseEntity) instanceof ImportErrDto) {
                        ImportErrDto importErrDto = (ImportErrDto) field.get(baseEntity);
                        if (importErrDto.getErrMsg() != null) {
                            errorSheet = true;
                        }
                        formData.add(importErrDto);
                    } else {
                        throw new RuntimeException("字段" + field.getName() + "的类型不是ImportErrDto！");
                    }
                }
                FormExcelModel formExcelModel = new FormExcelModel();
                formExcelModel.setSheetName(sheetName);
                formExcelModel.setTitle(formTitle);
                formExcelModel.setData(formData);
                writeFormExcelWithMsg(formExcelModel, wb, 0);

                // list区域的起始索引，是从校验行开始的
                int listAreaIndex = formFields.size();
                for (Field listField : listFields) {
                    listField.setAccessible(true);
                    List<List<Object>> listData = new ArrayList<>();
                    //这个字段所属类型中被@Excel标注的字段
                    List<Field> listFields1 = Arrays.stream(((Class) ((ParameterizedType) listField.getGenericType())
                            .getActualTypeArguments()[0])
                            .getDeclaredFields()).filter(e -> e.isAnnotationPresent(ExcelExport.class)).collect(Collectors.toList());
                    //这些字段的@Excel注解的name就是list的表头
                    List<String> listTitle = listFields1.stream().map(e -> e.getAnnotation(ExcelExport.class).name())
                            .collect(Collectors.toList());
                    List<Object> listBaseEntities = (List<Object>) listField.get(baseEntity);
                    if (!CollectionUtils.isEmpty(listBaseEntities)) {
                        for (Object listBaseEntity : listBaseEntities) {
                            List<Object> list1 = new ArrayList<>();
                            for (Field field : listFields1) {
                                field.setAccessible(true);
                                ImportErrDto importErrDto;
                                if (field.get(listBaseEntity) instanceof ImportErrDto) {
                                    importErrDto = (ImportErrDto) field.get(listBaseEntity);
                                    if (importErrDto.getErrMsg() != null) {
                                        errorSheet = true;
                                    }
                                } else {
                                    throw new RuntimeException("字段" + field.getName() + "的类型不是ImportErrDto！");
                                }
                                list1.add(importErrDto);
                            }
                            listData.add(list1);
                        }
                    }
                    ListExcelModel listExcelModel = new ListExcelModel();
                    listExcelModel.setSheetName(sheetName);
                    listExcelModel.setTitle(listTitle);
                    listExcelModel.setData(listData);
                    // 往后跳两行，跳过校验头和标题头
                    if (!listData.isEmpty()) {
                        listAreaIndex += 2;
                    }
                    exportListExcelWithErrMsg(listExcelModel, wb, listAreaIndex);
                    listAreaIndex += listData.size();
                }
                if (errorSheet) {
                    sheet.setTabColor(new XSSFColor(new Color(255, 0, 0), new DefaultIndexedColorMap()));
                }
            }
            wb.write(byteArrayOutputStream);
            DepFile depFile = new DepFile();
            depFile.setFile(byteArrayOutputStream.toByteArray());
            return depFile;
        } catch (Exception e) {
            logger.error("", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 导入单个sheet，返回单个对象.
     *
     * @param clazz 类型
     * @param sheet sheet
     * @param <T>   T
     * @return result
     */
    public static <T> T importSheet(Class<T> clazz, Sheet sheet) {
        try {
            T baseEntity = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            List<Field> fieldList = Arrays.stream(fields).filter(e ->
                    e.isAnnotationPresent(ExcelImport.class)).collect(Collectors.toList());
            Field field = Arrays.stream(fields).filter(e ->
                    e.isAnnotationPresent(ExcelListColumn.class)).findFirst().get();
            field.setAccessible(true);
            Class listColumnClazz = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            //要导入的list所属类型的字段列表
            List<Field> fieldList1 = Arrays.stream(listColumnClazz.getDeclaredFields()).filter(e ->
                    e.isAnnotationPresent(ExcelImport.class)).collect(Collectors.toList());

            int lastRowNum = sheet.getLastRowNum();
            //截止到第fieldList.size()行是form数据
            for (int j = 0; j < fieldList.size(); j++) {
                Row row = sheet.getRow(j);
                if (row != null) {
                    for (Field field1 : fieldList) {
                        if (field1.getAnnotation(ExcelImport.class).name()
                                .equals(row.getCell(0).getStringCellValue())) {
                            Cell cell = row.getCell(1);
                            field1.setAccessible(true);
                            handleCellType(baseEntity, field1, cell);
                            break;
                        }
                    }
                }
            }
            //从第fieldList.size() + 1行到lastRowNum行是list数据
            List<Object> baseEntityList1 = new ArrayList<>();
            //list的标题行，用这个行的cellValue跟字段注解的name循环匹配，能匹配的才设置数据
            Row titleRow = sheet.getRow(fieldList.size());
            if (titleRow != null) {
                short lastTitleCellIndex = titleRow.getLastCellNum();
                for (int j = fieldList.size() + 1; j <= lastRowNum; j++) {
                    Object baseEntity1 = listColumnClazz.newInstance();
                    //list数据行，每行是一条数据库记录
                    Row dataRow = sheet.getRow(j);
                    //循环标题行的cell，数据行对应索引的cell就是对象的字段值
                    for (int k = 0; k < lastTitleCellIndex; k++) {
                        Cell cell = dataRow.getCell(k);
                        Field field1 = fieldList1.get(k);
                        field1.setAccessible(true);
                        handleCellType(baseEntity1, field1, cell);
                    }
                    baseEntityList1.add(baseEntity1);
                    field.set(baseEntity, baseEntityList1);
                }
            }
            return baseEntity;
        } catch (Exception e) {
            throw new RuntimeException("import error");
        }


    }

    /**
     * 导入整个excel，返回list.
     *
     * @param clazz         类型
     * @param multipartFile excel
     * @param <T>           T
     * @return result
     */
    public static <T> ImportResult<T> importExcel(Class<T> clazz, MultipartFile multipartFile, String mediaType) {
        mediaType = mediaType.toUpperCase();
        return importFormListExcel(clazz, multipartFile, mediaType);
    }

    /**
     * 导入.
     *
     * @param clazz         .
     * @param multipartFile .
     * @return BaseEntity列表
     */
    private static <T> ImportResult<T> importFormListExcel(Class<T> clazz, MultipartFile multipartFile, String tempMediaType) {
        ImportResult<T> importResult = new ImportResult<>();
        importResult.setSuccess(true);
        final String mediaType = tempMediaType.toUpperCase();
        List<T> baseEntityList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        // 对应sheet中form区
        List<Field> formFields = Arrays.stream(fields).filter(e ->
                e.isAnnotationPresent(ExcelImport.class) && !Arrays.asList(e.getAnnotation(ExcelExport.class).excludes())
                        .contains(mediaType)).collect(Collectors.toList());
        // 对应sheet中list区
        List<Field> listFields = Arrays.stream(fields).filter(e ->
                e.isAnnotationPresent(ExcelListColumn.class)).collect(Collectors.toList());
        try (InputStream inputStream = multipartFile.getInputStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ZipSecureFile.setMinInflateRatio(-1.0d);
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);

            Font errorDataFont = getFont(wb, IndexedColors.RED);
            //有错误信息时的字体
            XSSFCellStyle errorDataStyle = wb.createCellStyle();
            errorDataStyle.setFont(errorDataFont);
            XSSFDataFormat errorDataFormat1 = wb.createDataFormat();
            errorDataStyle.setDataFormat(errorDataFormat1.getFormat("@"));
            setBorder(errorDataStyle, new XSSFColor(new Color(0, 0, 0)));
            int lastSheetNum = wb.getNumberOfSheets();
            Map<String, List<String>> formUniqueValuesMap = new HashMap<>();
            for (int i = 0; i < lastSheetNum; i++) {
                T baseEntity = clazz.newInstance();
                XSSFSheet sheet = wb.getSheetAt(i);
                // 当前sheet是否校验成功，用于标记sheet的颜色
                boolean sheetSuccess = true;
                Drawing<XSSFShape> drawing = sheet.createDrawingPatriarch();
                int lastRowNum = sheet.getLastRowNum();
                //截止到第formFields.size()行是form数据
                for (int j = 0; j < formFields.size(); j++) {
                    Row row = sheet.getRow(j);
                    for (Field field1 : formFields) {
                        if (field1.getAnnotation(ExcelImport.class).name()
                                .equals(row.getCell(0).getStringCellValue())) {
                            Cell cell = row.getCell(1);
                            // 将单元格数据转成String类型
                            DataFormatter dataFormatter = new DataFormatter();
                            String cellValue = dataFormatter.formatCellValue(cell);
                            StringBuilder cellComment = new StringBuilder();
                            if (field1.isAnnotationPresent(ExcelValid.class)) {
                                BaseValid[] valid = field1.getAnnotation(ExcelValid.class).valid();
                                for (BaseValid baseValid : valid) {
                                    if (baseValid.validClass().isEnum() && cell != null) {
                                        //if (EnumsUtils.isNotInclude((Class<? extends Enum<?>>) baseValid.validClass(), cellValue, baseValid.methodName())) {
                                        importResult.setSuccess(false);
                                        sheetSuccess = false;
                                        cellComment.append(baseValid.message());
                                        //}
                                    }
                                    String validMethodName = baseValid.validClass().getName();
                                    if (validMethodName.equals(NotBlank.class.getName()) && cell != null && cellValue.trim().length() == 0) {
                                        importResult.setSuccess(false);
                                        sheetSuccess = false;
                                        cellComment.append(baseValid.message());
                                    }
                                    if (validMethodName.equals(Size.class.getName()) && cell != null && cellValue.length() > baseValid.max()) {
                                        importResult.setSuccess(false);
                                        cellComment.append(baseValid.message());
                                    }
                                    if ((validMethodName.equals(NotNull.class.getName()) && cell == null) ||
                                            (validMethodName.equals(NotNull.class.getName()) && "".equals(cellValue))) {
                                        importResult.setSuccess(false);
                                        sheetSuccess = false;
                                        cellComment.append(baseValid.message());
                                    }
                                    if (validMethodName.equals(NotEmpty.class.getName()) && cell != null && "".equals(cellValue)) {
                                        importResult.setSuccess(false);
                                        sheetSuccess = false;
                                        cellComment.append(baseValid.message());
                                    }
                                    if (validMethodName.equals(ExcelColumnUnique.class.getName())) {
                                        List<String> formUniqueValues = formUniqueValuesMap.computeIfAbsent(field1.getName(), k1 -> new ArrayList<>());
                                        if (cell != null && !"".equals(cellValue)) {
                                            if (formUniqueValues.contains(cellValue)) {
                                                importResult.setSuccess(false);
                                                sheetSuccess = false;
                                                cellComment.append(baseValid.message());
                                            } else {
                                                formUniqueValues.add(cellValue);
                                            }
                                        }
                                    }
                                }
                            }
//                            if ((field1.isAnnotationPresent(NotNull.class) && cell == null) ||
//                                    (field1.isAnnotationPresent(NotNull.class) && cell != null && "".equals(cell.getStringCellValue()))) {
//                                importResult.setSuccess(false);
//                                sheetSuccess = false;
//                                cellComment.append(field1.getAnnotation(NotNull.class).message());
//                            }
//                            if (field1.isAnnotationPresent(NotEmpty.class)
//                                    && cell != null && "".equals(cell.getStringCellValue())) {
//                                importResult.setSuccess(false);
//                                sheetSuccess = false;
//                                cellComment.append(field1.getAnnotation(NotEmpty.class).message());
//                            }
//                            if (field1.isAnnotationPresent(NotBlank.class)
//                                    && cell != null && cell.getStringCellValue().trim().length() == 0) {
//                                importResult.setSuccess(false);
//                                sheetSuccess = false;
//                                cellComment.append(field1.getAnnotation(NotBlank.class).message());
//                            }
//                            if (field1.isAnnotationPresent(EnumValid.class)
//                                    && cell != null && EnumsUtils.isNotInclude(field1.getAnnotation(EnumValid.class).enumClass(), cell.getStringCellValue(), field1.getAnnotation(EnumValid.class).methodName())) {
//                                importResult.setSuccess(false);
//                                sheetSuccess = false;
//                                cellComment.append(field1.getAnnotation(EnumValid.class).message());
//                            }
//                            if (field1.isAnnotationPresent(ExcelColumnUnique.class)) {
//                                List<String> listUniqueValues = listUniqueValuesMap.computeIfAbsent(field1.getName(), k1 -> new ArrayList<>());
//                                if (cell != null && !"".equals(cell.getStringCellValue())) {
//                                    if (listUniqueValues.contains(cell.getStringCellValue())) {
//                                        importResult.setSuccess(false);
//                                        sheetSuccess = false;
//                                        cellComment.append(field1.getAnnotation(ExcelColumnUnique.class).message());
//                                    } else {
//                                        listUniqueValues.add(cell.getStringCellValue());
//                                    }
//                                }
//                            }
//                            if (field1.isAnnotationPresent(Size.class) && cell != null && cell.getStringCellValue().length() > field1.getAnnotation(Size.class).max()) {
//                                importResult.setSuccess(false);
//                                cellComment.append(field1.getAnnotation(Size.class).message());
//                            }
                            if (cell == null) {
                                cell = row.createCell(1);
                            }
                            if (!cellComment.toString().isEmpty()) {
                                cell.setCellComment(null);
                                Comment comment = drawing.createCellComment(new XSSFClientAnchor(0, 0, 0, 0,
                                        1, j, 1 + 2, j + 3));
                                comment.setString(new XSSFRichTextString(cellComment.toString()));
                                cell.setCellComment(comment);
                                cell.setCellStyle(errorDataStyle);
                            }
                            // 解析单元格数据
                            field1.setAccessible(true);
                            handleCellType(baseEntity, field1, cell);
                            break;
                        }
                    }
                }
                //list的标题行，用这个行的cellValue跟字段注解的name循环匹配，能匹配的才设置数据
                Row titleRow = sheet.getRow(formFields.size());
                // 如果list数据标题行为null 保留form数据 跳出本次循环
                if (titleRow == null) {
                    // 标记错误sheet页
                    if (!sheetSuccess) {
                        sheet.setTabColor(new XSSFColor(new Color(255, 0, 0), new DefaultIndexedColorMap()));
                    }
                    baseEntityList.add(baseEntity);
                    continue;
                }

                Map<String, List<String>> listUniqueValuesMap = new HashMap<>();
                // list区域起始index
                Integer listAreaIndex = formFields.size();
                for (Field listField : listFields) {
                    List<Object> baseEntityList1 = new ArrayList<>();
                    listField.setAccessible(true);
                    Class listFieldClazz = (Class) ((ParameterizedType) listField.getGenericType()).getActualTypeArguments()[0];
                    //要导入的list所属类型的字段列表
                    List<Field> listFieldClazzField = Arrays.stream(listFieldClazz.getDeclaredFields()).filter(e ->
                            e.isAnnotationPresent(ExcelImport.class) && !Arrays.asList(e.getAnnotation(ExcelImport.class).excludes())
                                    .contains(mediaType)).collect(Collectors.toList());
                    //list校验行
                    Row checkTitleRow = sheet.getRow(listAreaIndex);
                    if (checkTitleRow == null
                            || checkTitleRow.getCell(0) == null
                            || !LIST_SEPARATOR.equals(checkTitleRow.getCell(0).getStringCellValue())
                            || !listField.getAnnotation(ExcelListColumn.class).name().equals(checkTitleRow.getCell(1).getStringCellValue())) {
                        continue;
                    }
                    listAreaIndex += 2;
                    for (; listAreaIndex <= lastRowNum; listAreaIndex++) {
                        //list数据行
                        Row dataRow = sheet.getRow(listAreaIndex);
                        // 如果数据行第一个单元格出现了列表分隔符，说明该行是校验行，跳出当前循环
                        if (dataRow.getCell(0) != null
                                && LIST_SEPARATOR.equals(dataRow.getCell(0).getStringCellValue())) {
                            break;
                        }
                        Object baseEntity1 = listFieldClazz.newInstance();
                        short lastTitleCellIndex = dataRow.getLastCellNum();
                        //循环标题行的cell，数据行对应索引的cell就是对象的字段值
                        for (int k = 0; k < lastTitleCellIndex; k++) {
                            Cell cell = dataRow.getCell(k);
                            // 将单元格数据转成String类型
                            DataFormatter dataFormatter = new DataFormatter();
                            String cellValue = dataFormatter.formatCellValue(cell);
                            Field field1 = listFieldClazzField.get(k);

                            StringBuilder cellComment = new StringBuilder();
                            if (field1.isAnnotationPresent(ExcelValid.class)) {
                                BaseValid[] valid = field1.getAnnotation(ExcelValid.class).valid();
                                for (BaseValid baseValid : valid) {
                                    if (baseValid.validClass().isEnum() && cell != null) {
                                        //if (EnumUtil.contains(baseValid.validClass(), cellValue, baseValid.methodName())) {
                                        importResult.setSuccess(false);
                                        sheetSuccess = false;
                                        cellComment.append(baseValid.message());
                                        //}
                                    }
                                    String validMethodName = baseValid.validClass().getName();
                                    if (validMethodName.equals(NotBlank.class.getName()) && cell != null && cellValue.trim().length() == 0) {
                                        importResult.setSuccess(false);
                                        sheetSuccess = false;
                                        cellComment.append(baseValid.message());
                                    }
                                    if (validMethodName.equals(Size.class.getName()) && cell != null && cellValue.length() > baseValid.max()) {
                                        importResult.setSuccess(false);
                                        cellComment.append(baseValid.message());
                                    }
                                    if ((validMethodName.equals(NotNull.class.getName()) && cell == null) ||
                                            (validMethodName.equals(NotNull.class.getName()) && "".equals(cellValue))) {
                                        importResult.setSuccess(false);
                                        sheetSuccess = false;
                                        cellComment.append(baseValid.message());
                                    }
                                    if (validMethodName.equals(NotEmpty.class.getName()) && cell != null && "".equals(cellValue)) {
                                        importResult.setSuccess(false);
                                        sheetSuccess = false;
                                        cellComment.append(baseValid.message());
                                    }
                                    if (validMethodName.equals(ExcelColumnUnique.class.getName())) {
                                        List<String> listUniqueValues = listUniqueValuesMap.computeIfAbsent(field1.getName(), k1 -> new ArrayList<>());
                                        if (cell != null && !"".equals(cellValue)) {
                                            if (listUniqueValues.contains(cellValue)) {
                                                importResult.setSuccess(false);
                                                sheetSuccess = false;
                                                cellComment.append(baseValid.message());
                                            } else {
                                                listUniqueValues.add(cellValue);
                                            }
                                        }
                                    }
                                }
                            }
//                            if ((field1.isAnnotationPresent(NotNull.class) && cell == null) ||
//                                    (field1.isAnnotationPresent(NotNull.class) && cell != null && "".equals(cell.getStringCellValue()))) {
//                                importResult.setSuccess(false);
//                                sheetSuccess = false;
//                                cellComment.append(field1.getAnnotation(NotNull.class).message());
//                            }
//                            if (field1.isAnnotationPresent(NotEmpty.class)
//                                    && cell != null && "".equals(cell.getStringCellValue())) {
//                                importResult.setSuccess(false);
//                                sheetSuccess = false;
//                                cellComment.append(field1.getAnnotation(NotEmpty.class).message());
//                            }
//                            if (field1.isAnnotationPresent(NotBlank.class)
//                                    && cell != null && cell.getStringCellValue().trim().length() == 0) {
//                                importResult.setSuccess(false);
//                                sheetSuccess = false;
//                                cellComment.append(field1.getAnnotation(NotBlank.class).message());
//                            }
//                            if (field1.isAnnotationPresent(EnumValid.class)
//                                    && cell != null && EnumsUtils.isNotInclude(field1.getAnnotation(EnumValid.class).enumClass(), cell.getStringCellValue(), field1.getAnnotation(EnumValid.class).methodName())) {
//                                importResult.setSuccess(false);
//                                sheetSuccess = false;
//                                cellComment.append(field1.getAnnotation(EnumValid.class).message());
//                            }
//                            if (field1.isAnnotationPresent(ExcelColumnUnique.class)) {
//                                List<String> formUniqueValues = formUniqueValuesMap.computeIfAbsent(field1.getName(), k1 -> new ArrayList<>());
//                                if (cell != null && !"".equals(cell.getStringCellValue())) {
//                                    if (formUniqueValues.contains(cell.getStringCellValue())) {
//                                        importResult.setSuccess(false);
//                                        sheetSuccess = false;
//                                        cellComment.append(field1.getAnnotation(ExcelColumnUnique.class).message());
//                                    } else {
//                                        formUniqueValues.add(cell.getStringCellValue());
//                                    }
//                                }
//                            }
//                            if (field1.isAnnotationPresent(Size.class) && cell != null && cell.getStringCellValue().length() > field1.getAnnotation(Size.class).max()) {
//                                importResult.setSuccess(false);
//                                cellComment.append(field1.getAnnotation(Size.class).message());
//                            }
                            if (cell == null) {
                                cell = dataRow.createCell(k);
                            }
                            if (!cellComment.toString().isEmpty()) {
                                cell.setCellComment(null);
                                Comment comment = drawing.createCellComment(new XSSFClientAnchor(0, 0, 0, 0,
                                        k, listAreaIndex, k + 2, listAreaIndex + 3));
                                comment.setString(new XSSFRichTextString(cellComment.toString()));
                                cell.setCellComment(comment);
                                cell.setCellStyle(errorDataStyle);
                            }
                            // 解析单元格数据
                            field1.setAccessible(true);
                            handleCellType(baseEntity1, field1, cell);
                        }
                        baseEntityList1.add(baseEntity1);
                        listField.set(baseEntity, baseEntityList1);
                    }
                }

                baseEntityList.add(baseEntity);
                if (!sheetSuccess) {
                    sheet.setTabColor(new XSSFColor(new Color(255, 0, 0), new DefaultIndexedColorMap()));
                }
            }
            if (!importResult.getSuccess()) {
                wb.write(byteArrayOutputStream);
                importResult.setFile(byteArrayOutputStream.toByteArray());
            }
        } catch (IOException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            logger.error("导入失败：", e);
            throw new RuntimeException("导入失败！");
        }
        importResult.setData(baseEntityList);
        return importResult;
    }

    private static <T> ImportResult<T> importListExcel(Class<T> clazz, MultipartFile multipartFile, String tempMediaType) {
        ImportResult<T> importResult = new ImportResult<>();
        importResult.setSuccess(true);
        final String mediaType = tempMediaType.toUpperCase();
        List<T> baseEntityList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields).filter(e ->
                e.isAnnotationPresent(ExcelImport.class) && !Arrays.asList(e.getAnnotation(ExcelImport.class).excludes()).contains(mediaType)).collect(Collectors.toList());
        try (InputStream inputStream = multipartFile.getInputStream()) {
            ZipSecureFile.setMinInflateRatio(-1.0d);
            Workbook wb = new XSSFWorkbook(inputStream);
            Sheet sheet = wb.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            //第一行是标题
            Row titleRow = sheet.getRow(0);
            //截止到第fieldList.size()行是form数据
            for (int i = 1; i <= lastRowNum; i++) {
                T baseEntity = clazz.newInstance();
                Row dataRow = sheet.getRow(i);
                for (int j = 0; j < fieldList.size(); j++) {
                    Field field1 = fieldList.get(j);
                    if (field1.getAnnotation(ExcelImport.class).name()
                            .equals(titleRow.getCell(j).getStringCellValue())) {
                        Cell cell = dataRow.getCell(j);
                        field1.setAccessible(true);
                        handleCellType(baseEntity, field1, cell);
                        //break;
                    }
                }
                baseEntityList.add(baseEntity);
            }
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("导入失败！");
        } catch (InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        importResult.setData(baseEntityList);
        return importResult;
    }

    private static <T> void handleCellType(T baseEntity, Field field1, Cell cell) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (cell == null) {
            field1.set(baseEntity, null);
            return;
        }
        Class clazz = field1.getType();
        CellType cellType = cell.getCellType();
        if (clazz == Integer.class) {
            switch (cellType) {
                case STRING:
                    field1.set(baseEntity, cell.getStringCellValue().isEmpty() ? null : Integer.parseInt(cell.getStringCellValue()));
                    break;
                case NUMERIC:
                    field1.set(baseEntity, Double.valueOf(cell.getNumericCellValue()).intValue());
                    break;
                default:
                    break;
            }
        } else if (clazz == Long.class) {
            switch (cellType) {
                case STRING:
                    field1.set(baseEntity, cell.getStringCellValue().isEmpty() ? null :
                            Long.parseLong(cell.getStringCellValue()));
                    break;
                case NUMERIC:
                    field1.set(baseEntity, Double.valueOf(cell.getNumericCellValue()).longValue());
                    break;
                default:
                    break;
            }
        } else if (clazz == Boolean.class) {
            field1.set(baseEntity, cell.getBooleanCellValue());
        } else if (clazz.isEnum()) {
            String enumDescKey = null;
            String value = cell.getStringCellValue();
            ExcelImport[] imports = field1.getAnnotationsByType(ExcelImport.class);
            for (ExcelImport anImport : imports) {
                enumDescKey = anImport.enumDescKey();
            }

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                Class[] parameterTypes = method.getParameterTypes();
                String name = method.getName();
                if (name.equals(enumDescKey) && parameterTypes[0] == String.class) {
                    Object invoke = method.invoke(clazz, value);
                    field1.set(baseEntity, invoke);
                }
            }

        } else if (clazz == LocalDateTime.class) {
            LocalDateTime localDateTime = cell.getStringCellValue().isEmpty() ? null : LocalDateTime.parse(cell.getStringCellValue());
            field1.set(baseEntity, localDateTime);
        } else {
            switch (cellType) {
                case STRING:
                    field1.set(baseEntity, cell.getStringCellValue().isEmpty() ? null : cell.getStringCellValue());
                    break;
                case NUMERIC:
                    field1.set(baseEntity, String.valueOf(cell.getNumericCellValue()));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 写入list区域的数据.
     *
     * @param listExcelModel model
     * @param workbook       wb
     * @param rowIndex       从哪一行开始写(索引，从0开始计数)
     * @param checkTitle     校验头
     */
    private static void exportListExcel(ListExcelModel listExcelModel, XSSFWorkbook workbook,
                                        int rowIndex, String checkTitle) {
        String sheetName = listExcelModel.getSheetName();
        if (sheetName == null) {
            sheetName = DEFAULT_SHEET_NAME;
        }
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
        //写入校验头
        writeCheckTitlesToExcel(sheet, checkTitle, rowIndex);
        //标题写入第rowIndex行
        writeTitlesToExcel(workbook, sheet, listExcelModel.getTitle(), rowIndex + 1);
        //数据从第rowIndex+1行开始写
        writeRowsToExcel(workbook, sheet, listExcelModel.getData(), rowIndex + 2);
    }

    private static void exportObjectFormExcel(FormExcelModel formExcelModel, XSSFWorkbook workbook,
                                              int rowIndex, String checkTitle) {
        String sheetName = formExcelModel.getSheetName();
        if (sheetName == null) {
            sheetName = DEFAULT_SHEET_NAME;
        }
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
        //写入校验头
        writeCheckTitlesToExcel(sheet, checkTitle, rowIndex);
        //数据从第rowIndex+1行开始写
        writeFormExcel(workbook, sheet, formExcelModel, rowIndex + 1);
    }

    /**
     * 写入list区域的数据(带错误信息).
     *
     * @param listExcelModel listExcelModel
     * @param workbook       workbook
     * @param rowIndex       从哪一行开始写(索引，从0开始计数)
     */
    private static void exportListExcelWithErrMsg(ListExcelModel listExcelModel, XSSFWorkbook workbook,
                                                  int rowIndex) {
        String sheetName = listExcelModel.getSheetName();
        if (sheetName == null) {
            sheetName = DEFAULT_SHEET_NAME;
        }
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
        Drawing<XSSFShape> drawing = sheet.createDrawingPatriarch();
        //标题写入第rowIndex行
        // writeTitlesToExcel(workbook, sheet, listExcelModel.getTitle(), rowIndex);
        //数据从第rowIndex+1行开始写
        writeRowsToExcelWithErrMsg(workbook, sheet, listExcelModel.getData(), rowIndex, drawing);
    }

    /**
     * .
     *
     * @param listExcelModel listExcelModel
     * @param workbook       workbook
     * @param rowIndex       rowIndex
     * @param color          color
     */
    private static void exportListExcelWithErrMsg(ListExcelModel listExcelModel, XSSFWorkbook workbook,
                                                  int rowIndex, XSSFColor color) {
        String sheetName = listExcelModel.getSheetName();
        if (sheetName == null) {
            sheetName = DEFAULT_SHEET_NAME;
        }
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
        sheet.setTabColor(color);
        Drawing<XSSFShape> drawing = sheet.createDrawingPatriarch();
        //标题写入第rowIndex行
        writeTitlesToExcel(workbook, sheet, listExcelModel.getTitle(), rowIndex);
        //数据从第rowIndex+1行开始写
        writeRowsToExcelWithErrMsg(workbook, sheet, listExcelModel.getData(), rowIndex + 1, drawing);
    }

    /**
     * export form excel model.
     *
     * @param formExcelModel model
     * @param workbook       outputStream
     * @throws Exception 异常
     */
    private static void exportFormExcel(FormExcelModel formExcelModel, XSSFWorkbook workbook, int rowIndex) {

        String sheetName = formExcelModel.getSheetName();
        if (null == sheetName) {
            sheetName = DEFAULT_SHEET_NAME;
        }
        XSSFSheet sheet = workbook.createSheet(sheetName);
        writeFormExcel(workbook, sheet, formExcelModel, rowIndex);

    }

    private static void writeFormExcelWithMsg(FormExcelModel formExcelModel, XSSFWorkbook workbook, int rowIndex) {

        String sheetName = formExcelModel.getSheetName();
        if (null == sheetName) {
            sheetName = DEFAULT_SHEET_NAME;
        }
        XSSFSheet sheet = workbook.getSheet(sheetName);
        Drawing<XSSFShape> drawing = sheet.createDrawingPatriarch();

        // Font dataFont = getFont(workbook, IndexedColors.BLACK);
        //
        // XSSFCellStyle dataStyle = workbook.createCellStyle();
        // dataStyle.setFont(dataFont);
        // setBorder(dataStyle, new XSSFColor(new Color(0, 0, 0)));

        int rowsCount = formExcelModel.getTitle() != null ? formExcelModel.getTitle().size() : 0;
        for (int i = 0; i < rowsCount; i++) {
            XSSFRow row = sheet.getRow(rowIndex);
            createRowCell(sheet, row, DEFAULT_ROW_HEIGHT, DEFAULT_CELL_WIDTH, 0,
                    formExcelModel.getTitle().get(i));
            createRowCellWithMsg(workbook, sheet, row, DEFAULT_ROW_HEIGHT, DEFAULT_CELL_WIDTH, 1,
                    (ImportErrDto) formExcelModel.getData().get(i), drawing, rowIndex);
            rowIndex++;
        }

    }

    /**
     * 写入表头.
     *
     * @param wb       工作表
     * @param sheet    sheet 页面
     * @param titles   表头行内容
     * @param rowIndex 要写入的行索引(从0开始计数)
     */
    private static void writeTitlesToExcel(XSSFWorkbook wb, Sheet sheet, List<String> titles, int rowIndex) {
        int colIndex = 0;

        Font titleFont = wb.createFont();
        titleFont.setFontName(DEFAULT_FONT_NAME);
        titleFont.setBold(true);
        titleFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle titleStyle = wb.createCellStyle();
        XSSFDataFormat dataFormat = wb.createDataFormat();
        titleStyle.setDataFormat(dataFormat.getFormat("@"));
        titleStyle.setFillForegroundColor(new XSSFColor(new Color(182, 184, 192)));
        titleStyle.setFont(titleFont);
        setBorder(titleStyle, new XSSFColor(new Color(0, 0, 0)));

        Row titleRow = sheet.createRow(rowIndex);
        for (String field : titles) {
            Cell cell = titleRow.createCell(colIndex);
            cell.setCellValue(field);
            cell.setCellStyle(titleStyle);
            colIndex++;
        }
    }

    private static void writeCheckTitlesToExcel(Sheet sheet, String checkTitle, int rowIndex) {
        Row row = sheet.createRow(rowIndex);
        Cell checkCell0 = row.createCell(0);
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        Comment comment = drawing.createCellComment(new XSSFClientAnchor(0, 0, 0, 0,
                0, rowIndex, 2, rowIndex + 3));
        comment.setString(new XSSFRichTextString("校验数据使用，请勿修改此单元格内容！"));
        checkCell0.setCellComment(comment);
        checkCell0.setCellValue(LIST_SEPARATOR);

        Cell checkCell1 = row.createCell(1);
        Comment comment1 = drawing.createCellComment(new XSSFClientAnchor(0, 0, 0, 0,
                1, rowIndex, 3, rowIndex + 3));
        comment1.setString(new XSSFRichTextString("校验数据使用，请勿修改此单元格内容！"));
        checkCell1.setCellComment(comment1);
        checkCell1.setCellValue(checkTitle);
    }

    /**
     * 写入sheet页行数据.
     *
     * @param wb       工作表
     * @param sheet    sheet 页
     * @param rows     行
     * @param rowIndex 要写入的行索引(从0开始计数)
     */
    private static void writeRowsToExcel(XSSFWorkbook wb, Sheet sheet, List<List<Object>> rows, int rowIndex) {
        Font dataFont = getFont(wb, IndexedColors.BLACK);

        XSSFCellStyle dataStyle = wb.createCellStyle();
        dataStyle.setFont(dataFont);
        XSSFDataFormat dataFormat = wb.createDataFormat();
        dataStyle.setDataFormat(dataFormat.getFormat("@"));
        setBorder(dataStyle, new XSSFColor(new Color(0, 0, 0)));

        if (!CollectionUtils.isEmpty(rows)) {
            for (List<Object> rowData : rows) {
                Row dataRow = sheet.createRow(rowIndex);
                int colIndex = 0;

                for (Object cellData : rowData) {
                    Cell cell = dataRow.createCell(colIndex);
                    if (cellData != null) {
                        cell.setCellValue(cellData.toString());
                    }

                    cell.setCellStyle(dataStyle);
                    colIndex++;
                    sheet.setColumnWidth(colIndex, DEFAULT_CELL_WIDTH);
                }
                rowIndex++;
            }
        }
    }

    /**
     * 写入sheet页行数据(带错误信息).
     *
     * @param wb       工作表
     * @param sheet    sheet 页
     * @param rows     行
     * @param rowIndex 要写入的行索引(从0开始计数)
     * @param drawing  drawing
     */
    private static void writeRowsToExcelWithErrMsg(XSSFWorkbook wb, Sheet sheet, List<List<Object>> rows, int rowIndex,
                                                   Drawing<XSSFShape> drawing) {
        Font dataFont = getFont(wb, IndexedColors.BLACK);
        Font errorDataFont = getFont(wb, IndexedColors.RED);

        XSSFCellStyle dataStyle = wb.createCellStyle();
        dataStyle.setFont(dataFont);
        XSSFDataFormat dataFormat = wb.createDataFormat();
        dataStyle.setDataFormat(dataFormat.getFormat("@"));
        setBorder(dataStyle, new XSSFColor(new Color(0, 0, 0)));
        //有错误信息时的字体
        XSSFCellStyle errorDataStyle = wb.createCellStyle();
        errorDataStyle.setFont(errorDataFont);
        XSSFDataFormat errorDataFormat1 = wb.createDataFormat();
        errorDataStyle.setDataFormat(errorDataFormat1.getFormat("@"));
        setBorder(errorDataStyle, new XSSFColor(new Color(0, 0, 0)));

        if (!CollectionUtils.isEmpty(rows)) {
            for (List<Object> rowData : rows) {
                Row dataRow = sheet.getRow(rowIndex);
                if (dataRow == null) {
                    dataRow = sheet.createRow(rowIndex);
                }
                int colIndex = 0;

                for (Object cellData : rowData) {
                    ImportErrDto importErrDto = (ImportErrDto) cellData;
                    Cell cell = dataRow.getCell(colIndex);
                    if (cell == null) {
                        cell = dataRow.createCell(colIndex);
                    }
                    if (importErrDto != null && importErrDto.getErrMsg() != null) {
                        // if (importErrDto.getValue() != null) {
                        //     cell.setCellValue(importErrDto.getValue().toString());
                        // }
                        cell.setCellComment(null);
                        Comment comment = drawing.createCellComment(new XSSFClientAnchor(0, 0, 0, 0,
                                colIndex, rowIndex, colIndex + 2, rowIndex + 3));
                        comment.setString(new XSSFRichTextString(importErrDto.getErrMsg()));
                        cell.setCellComment(comment);
                        cell.setCellStyle(errorDataStyle);
                        // } else {
                        //     cell.setCellStyle(dataStyle);
                    }

                    colIndex++;
                    // sheet.setColumnWidth(colIndex, DEFAULT_CELL_WIDTH);
                }
                rowIndex++;
            }
        }
    }

    /**
     * 创建excel 表格单元格.
     *
     * @param sheet       sheet
     * @param row         行
     * @param height      高度
     * @param width       宽度
     * @param columnIndex 列索引
     * @param cellValue   单元格值
     */
    private static void createRowCell(XSSFSheet sheet, XSSFRow row, short height, int width,
                                      int columnIndex, Object cellValue) {
        // row.setHeight(height);
        XSSFCell cell = row.getCell(columnIndex);
        if (cell == null) {
            sheet.setColumnWidth(columnIndex, width);
            cell = row.createCell(columnIndex);
            XSSFCellStyle style = getTableHeaderStyle(sheet.getWorkbook());
            cell.setCellStyle(style);
        }
        if (cellValue instanceof Number) {
            cell.setCellValue(Double.parseDouble(cellValue.toString()));
        } else {
            cell.setCellValue(cellValue != null ? cellValue.toString() : null);
        }
    }

    /**
     * .
     */
    private static void createRowCellWithMsg(XSSFWorkbook workbook, XSSFSheet sheet, XSSFRow row, short height,
                                             int width, int columnIndex, ImportErrDto importErrDto, Drawing<XSSFShape> drawing,
                                             int rowIndex) {
        // row.setHeight(height);
        XSSFCell cell = row.getCell(columnIndex);
        // XSSFCell cell = row.createCell(columnIndex);
        try {
            if (importErrDto != null) {
                // if (importErrDto.getValue() != null) {
                //     cell.setCellValue(importErrDto.getValue().toString());
                // }
                if (importErrDto.getErrMsg() != null) {
                    XSSFCellStyle errorDataStyle = workbook.createCellStyle();
                    errorDataStyle.cloneStyleFrom(cell.getCellStyle());
                    Font errorDataFont = getFont(workbook, IndexedColors.RED);
                    errorDataStyle.setFont(errorDataFont);
                    cell.setCellStyle(errorDataStyle);
                    cell.setCellComment(null);
                    Comment comment = drawing.createCellComment(new XSSFClientAnchor(0, 0, 0, 0,
                            columnIndex, rowIndex, columnIndex + 2, rowIndex + 3));
                    comment.setString(new XSSFRichTextString(importErrDto.getErrMsg()));
                    cell.setCellComment(comment);
                }
                // else {
                //     cell.setCellStyle(dataStyle);
                // }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 设置样式.
     *
     * @param workbook 工作表
     * @return ret
     */
    private static XSSFCellStyle getTableHeaderStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(DEFAULT_FONT_SIZE);
        font.setBold(false);
        font.setFontName(DEFAULT_FONT_NAME);
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFDataFormat dataFormat = workbook.createDataFormat();
        style.setDataFormat(dataFormat.getFormat("@"));
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setFont(font);
        style.setWrapText(false);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 写入form excel model 数据.
     *
     * @param wb             工作表
     * @param sheet          sheet页
     * @param formExcelModel 数据实体
     * @param rowIndex       要写入的行索引(从0开始计数)
     */
    private static void writeFormExcel(XSSFWorkbook wb, XSSFSheet sheet, FormExcelModel formExcelModel, int rowIndex) {

        Font dataFont = wb.createFont();
        dataFont.setFontName(DEFAULT_FONT_NAME);
        dataFont.setBold(true);
        dataFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle dataStyle = wb.createCellStyle();
        dataStyle.setFont(dataFont);
        setBorder(dataStyle, new XSSFColor(new Color(0, 0, 0)));

        int rowsCount = 0;
        if (formExcelModel != null) {
            rowsCount = formExcelModel.getTitle() != null ? formExcelModel.getTitle().size() : 0;
        }
        for (int i = 0; i < rowsCount; i++) {
            XSSFRow row = sheet.createRow(rowIndex++);
            createRowCell(sheet, row, DEFAULT_ROW_HEIGHT, DEFAULT_CELL_WIDTH, 0, formExcelModel.getTitle().get(i));
            Object cellValue = formExcelModel.getData() != null ? formExcelModel.getData().get(i) : null;
            createRowCell(sheet, row, DEFAULT_ROW_HEIGHT, DEFAULT_CELL_WIDTH, 1, cellValue);
        }
    }

    private static Font getFont(XSSFWorkbook wb, IndexedColors indexedColors) {
        Font dataFont = wb.createFont();
        dataFont.setFontName(DEFAULT_FONT_NAME);
        dataFont.setColor(indexedColors.index);
        return dataFont;
    }

    private static void setBorder(XSSFCellStyle style, XSSFColor color) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
    }

}

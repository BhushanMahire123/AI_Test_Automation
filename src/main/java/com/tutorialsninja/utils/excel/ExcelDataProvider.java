package com.tutorialsninja.utils.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelDataProvider {

    private static final String BASE_PATH = "src/main/resources/testdata/excel/";

    private ExcelDataProvider() {}

    public static List<Map<String, String>> getSheetData(String fileName, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(BASE_PATH + fileName);
             Workbook wb = new XSSFWorkbook(fis)) {
            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) throw new IllegalArgumentException("Sheet not found: " + sheetName);
            Row header = sheet.getRow(0);
            List<String> headers = new ArrayList<>();
            for (Cell c : header) headers.add(getCellValue(c));
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Map<String, String> rowMap = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowMap.put(headers.get(j), getCellValue(cell));
                }
                data.add(rowMap);
            }
        } catch (IOException e) {
            throw new RuntimeException("Excel read failed: " + fileName, e);
        }
        return data;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:  return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default:      return "";
        }
    }
}

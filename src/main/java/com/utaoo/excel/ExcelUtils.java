package com.utaoo.excel;

import org.apache.poi.xssf.usermodel.XSSFCell;

import java.text.DecimalFormat;

public class ExcelUtils {
    /**
     * excel cell convert to string
     */
    public static String convertCellValueToString(XSSFCell cell) {

        if (null == cell) {
            return null;
        }
        String returnValue = null;
        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_STRING:
                returnValue = cell.getStringCellValue();
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                double numericCellValue = cell.getNumericCellValue();
                boolean isInteger = isIntegerForDouble(numericCellValue);
                if (isInteger) {
                    DecimalFormat df = new DecimalFormat("0");
                    returnValue = df.format(numericCellValue);
                } else {
                    returnValue = Double.toString(numericCellValue);
                }
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                boolean booleanCellValue = cell.getBooleanCellValue();
                returnValue = Boolean.toString(booleanCellValue);
                break;
            case XSSFCell.CELL_TYPE_BLANK:
                break;
            case XSSFCell.CELL_TYPE_FORMULA:
                cell.getCellFormula();
                break;
            case XSSFCell.CELL_TYPE_ERROR: //故障
                break;
            default:
                break;
        }
        return returnValue;
    }
    /**
     * check is integer
     */
    public static boolean isIntegerForDouble(Double num) {
        double eqs = 1e-10; //精度范围
        return num - Math.floor(num) < eqs;
    }
}

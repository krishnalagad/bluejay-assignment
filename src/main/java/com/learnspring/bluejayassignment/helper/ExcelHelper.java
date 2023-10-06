package com.learnspring.bluejayassignment.helper;

import com.learnspring.bluejayassignment.model.Bluejay;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {

    private static Logger logger = LoggerFactory.getLogger(ExcelHelper.class);
    private static String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
//    private static String EXCEL_CONTENT_TYPE = "text/csv";

    // check the type of file
    // if its Excel file then will return true
    // else will return false.
    public static boolean checkFileType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType.equals(EXCEL_CONTENT_TYPE);
    }

    public static List<Bluejay> convertExcelToList(InputStream is) {

        List<Bluejay> list = new ArrayList<>();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheet("Sheet1");

            int rowNumber = 0;
            Iterator<Row> iterator = sheet.iterator();

            while (iterator.hasNext()) {

                Row row = iterator.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cells = row.iterator();

                int cid = 0;
                Bluejay e = new Bluejay();

                while (cells.hasNext()) {

                    Cell cell = cells.next();
                    switch (cid) {

                        case 0:
                            String value = cell.getStringCellValue();
                            if (!value.isBlank() || !value.isEmpty())
                                e.setPositionId(value.trim());
                            else
                                e.setPositionId(null);
                            break;

                        case 1:
                            String value1 = cell.getStringCellValue();
                            if (!value1.isBlank() || !value1.isEmpty())
                                e.setPositionStatus(value1.trim());
                            else
                                e.setPositionStatus(null);
                            break;

                        case 2:
                            if(cell.getCellType() == CellType.BLANK)
                                e.setTime(null);
                            else if (cell.getCellType() == CellType.NUMERIC)
                                e.setTime(String.valueOf(cell.getNumericCellValue()));
                            else if (cell.getCellType() == CellType.STRING)
                                e.setTime(cell.getStringCellValue().trim());
                            break;

                        case 3:
                            if(cell.getCellType() == CellType.BLANK)
                                e.setTimeOut(null);
                            else if (cell.getCellType() == CellType.NUMERIC)
                                e.setTimeOut(String.valueOf(cell.getNumericCellValue()));
                            else if (cell.getCellType() == CellType.STRING)
                                e.setTimeOut(cell.getStringCellValue().trim());
                            break;

                        case 4:
                            String value4 = cell.getStringCellValue();
                            if (!value4.isBlank() || !value4.isEmpty())
                                e.setTimecardHours(value4.trim());
                            else
                                e.setTimecardHours(null);
                            break;

                        case 5:
                            if(cell.getCellType() == CellType.BLANK)
                                e.setPayCycleStartDate(null);
                            else if (cell.getCellType() == CellType.NUMERIC)
                                e.setPayCycleStartDate(String.valueOf(cell.getNumericCellValue()));
                            else if (cell.getCellType() == CellType.STRING)
                                e.setPayCycleStartDate(cell.getStringCellValue().trim());
                            break;

                        case 6:
                            if(cell.getCellType() == CellType.BLANK)
                                e.setPayCycleEndDate(null);
                            if (cell.getCellType() == CellType.NUMERIC)
                                e.setPayCycleEndDate(String.valueOf(cell.getNumericCellValue()));
                            else if (cell.getCellType() == CellType.STRING)
                                e.setPayCycleEndDate(cell.getStringCellValue().trim());
                            break;

                        case 7:
                            if(cell.getCellType() == CellType.BLANK)
                                e.setEmployeeName(null);
                            if (cell.getCellType() == CellType.NUMERIC)
                                e.setEmployeeName(String.valueOf(cell.getNumericCellValue()));
                            else if (cell.getCellType() == CellType.STRING)
                                e.setEmployeeName(cell.getStringCellValue().trim());
                            break;
                        case 8:
                            if(cell.getCellType() == CellType.BLANK)
                                e.setFileNumber(null);
                            if (cell.getCellType() == CellType.NUMERIC)
                                e.setFileNumber(String.valueOf(cell.getNumericCellValue()));
                            else if (cell.getCellType() == CellType.STRING)
                                e.setFileNumber(cell.getStringCellValue().trim());
                            break;

                        default:
                            break;
                    }

                    cid++;
                }
                list.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
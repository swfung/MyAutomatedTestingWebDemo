package utilities;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExcelUtil {
//	private static ArrayList <String> headerList = new ArrayList <String> ();
//    private static ArrayList <String> testdataList = new ArrayList <String> ();

    private static ThreadLocal<CopyOnWriteArrayList<String>> headerList = new ThreadLocal<>();
    private static ThreadLocal<CopyOnWriteArrayList<String>> testdataList = new ThreadLocal<>();
    private static ThreadLocal<CopyOnWriteArrayList<String>> headerListWSheetName = new ThreadLocal<>();
    private static ThreadLocal<CopyOnWriteArrayList<String>> testdataListWSheetName = new ThreadLocal<>();
    private static ThreadLocal<CopyOnWriteArrayList<String>> headerListWSheetNameOfExcelFile = new ThreadLocal<>();
    private static ThreadLocal<CopyOnWriteArrayList<String>> testdataListWSheetNameOfExcelFile = new ThreadLocal<>();
    private static int currentRowNo;


    public static synchronized void readExcel(String scenarioName)throws IOException {
        System.out.println("Read Excecl for case: "+scenarioName);
        XSSFWorkbook excelWBook;
        XSSFSheet excelWSheet;

        DataFormatter dataFormatter = new DataFormatter();

        String path = "resource/BC_Web.xlsx";

        try {
            FileInputStream excelFile = new FileInputStream(path);
            excelWBook = new XSSFWorkbook(excelFile);
            excelWSheet = excelWBook.getSheetAt(0);
            headerList.remove();
            testdataList.remove();

            for (Row row: excelWSheet) {
                String cellValue1stCol = dataFormatter.formatCellValue(row.getCell(0));

                //Setup the array list for header

//              if (row.getRowNum() == 0) {
                if (cellValue1stCol.trim().equals("Scenario")) {
                    for (Cell cell: row) {
                        String cellValue = dataFormatter.formatCellValue(cell);
//                        headerList.add(cellValue);
                        CopyOnWriteArrayList<String> headerListTmp = headerList.get();
                        if(headerListTmp == null){
                            headerListTmp = new CopyOnWriteArrayList<>();
                        }
                        headerListTmp.add(cellValue);
                        headerList.set(headerListTmp);
                    }
                }
                //Setup the array list for test data
                else if (cellValue1stCol.trim().equals(scenarioName.trim())) {
                    currentRowNo = row.getRowNum();
                    for (Cell cell: row) {
                        String cellValue = dataFormatter.formatCellValue(cell);
//                        testdataList.add(cellValue);
                        CopyOnWriteArrayList<String> testDataListTmp = testdataList.get();
                        if(testDataListTmp == null){
                            testDataListTmp = new CopyOnWriteArrayList<>();
                        }
                        testDataListTmp.add(cellValue);
                        testdataList.set(testDataListTmp);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
    
    public static String getValue(String fieldName) {
//    	int ColIndex = headerList.indexOf(fieldName.trim());
    	int ColIndex = headerList.get().indexOf(fieldName.trim());
    	String fieldValue = testdataList.get().get(ColIndex).trim();
//    	String fieldValue = testdataList.get(ColIndex).trim();
        return fieldValue;
    }

    public static synchronized void setValue(String fieldName, String value) {
//        int ColIndex = headerList.indexOf(fieldName.trim());
//        testdataList.set(ColIndex, value);

        CopyOnWriteArrayList<String> testdatatmp = testdataList.get();
        int ColIndex = headerList.get().indexOf(fieldName.trim());
        testdatatmp.set(ColIndex, value);
        testdataList.set(testdatatmp);
    }

    public static synchronized void readExcelWorkSheet(String workSheetName, int rowIndex)throws IOException {
        XSSFWorkbook excelWBook;
        XSSFSheet excelWSheet;

        DataFormatter dataFormatter = new DataFormatter();
        String cellValue = "";

        String path = "resource/BC_Web.xlsx";

        try {
            FileInputStream excelFile = new FileInputStream(path);
            excelWBook = new XSSFWorkbook(excelFile);
            excelWSheet = excelWBook.getSheet(workSheetName);
            headerListWSheetName.remove();
            testdataListWSheetName.remove();

            for (Row row: excelWSheet) {
                //Setup the array list for header
                if (row.getRowNum()==0) {
                    for (Cell cell : row) {
                        if(cell.getCellType() == CellType.FORMULA){
                            switch(cell.getCachedFormulaResultType()) {
                                case NUMERIC:
                                    double cellNum = cell.getNumericCellValue();
                                    cellValue = String.valueOf(cellNum);
                                    break;
                                case STRING:
                                    cellValue = cell.getRichStringCellValue().toString();
                                    break;
                            }
                        }else{
                            cellValue = dataFormatter.formatCellValue(cell);
                        }
                        CopyOnWriteArrayList<String> headerListTmp = headerListWSheetName.get();
                        if (headerListTmp == null) {
                            headerListTmp = new CopyOnWriteArrayList<>();
                        }
                        headerListTmp.add(cellValue);
                        headerListWSheetName.set(headerListTmp);
                    }
                }else if(row.getRowNum()==rowIndex) {
                    //Setup the array list for test data
                    for (Cell cell : row) {
                        if(cell.getCellType() == CellType.FORMULA){
                            switch(cell.getCachedFormulaResultType()) {
                                case NUMERIC:
                                    double cellNum = cell.getNumericCellValue();
                                    cellValue = String.valueOf(cellNum);
                                    break;
                                case STRING:
                                    cellValue = cell.getRichStringCellValue().toString();
                                    break;
                            }
                        }else{
                            cellValue = dataFormatter.formatCellValue(cell);
                        }
                        CopyOnWriteArrayList<String> testDataListTmp = testdataListWSheetName.get();
                        if (testDataListTmp == null) {
                            testDataListTmp = new CopyOnWriteArrayList<>();
                        }
                        testDataListTmp.add(cellValue);
                        testdataListWSheetName.set(testDataListTmp);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getValueWWorkSheetName(String fieldName, String workSheetName, int rowIndex) throws IOException {
        readExcelWorkSheet(workSheetName,rowIndex);
        int ColIndex = headerListWSheetName.get().indexOf(fieldName.trim());
        return testdataListWSheetName.get().get(ColIndex).trim();
    }

    public static synchronized void setValueWWorkSheetName(String fieldName, String value, String workSheetName, int rowIndex) throws IOException {
        readExcelWorkSheet(workSheetName,rowIndex);
        CopyOnWriteArrayList<String> testdatatmp = testdataListWSheetName.get();
        int ColIndex = headerListWSheetName.get().indexOf(fieldName.trim());
        testdatatmp.set(ColIndex, value);
        testdataListWSheetName.set(testdatatmp);
    }

    public static int getRowSize(String workSheetName) throws IOException {
        XSSFWorkbook excelWBook;
        XSSFSheet excelWSheet;
        String path = "resource/BC_Web.xlsx";
        FileInputStream excelFile = new FileInputStream(path);
        excelWBook = new XSSFWorkbook(excelFile);
        excelWSheet = excelWBook.getSheet(workSheetName);
        return excelWSheet.getLastRowNum();
    }

    //functions to get, set, read values for an Excel File
    public static synchronized void readExcelWorkSheetOfExcelFile(String excelFilePath, String sheetName, int rowIndex) throws IOException{

        File excelFile = new File(excelFilePath);
        FileInputStream inputStream = new FileInputStream(excelFile);
        Workbook excelWorkbook = null;

        String fileExtensionName = excelFilePath.substring(excelFilePath.indexOf("."));
        if(fileExtensionName.equals(".xlsx")){
            excelWorkbook = new XSSFWorkbook(inputStream);
        } else if(fileExtensionName.equals(".xls")) {
            excelWorkbook = new HSSFWorkbook(inputStream);
        }

        DataFormatter dataFormatter = new DataFormatter();
        String cellValue = "";

        try {

            Sheet excelSheet = excelWorkbook.getSheet(sheetName);
            headerListWSheetNameOfExcelFile.remove();
            testdataListWSheetNameOfExcelFile.remove();

            for (Row row: excelSheet) {
                //Setup the array list for header
                if (row.getRowNum()==0) {
                    for (Cell cell : row) {
                        if(cell.getCellType() == CellType.FORMULA){
                            switch(cell.getCachedFormulaResultType()) {
                                case NUMERIC:
                                    double cellNum = cell.getNumericCellValue();
                                    cellValue = String.valueOf(cellNum);
                                    break;
                                case STRING:
                                    cellValue = cell.getRichStringCellValue().toString();
                                    break;
                            }
                        }else{
                            cellValue = dataFormatter.formatCellValue(cell);
                        }
                        CopyOnWriteArrayList<String> headerListTmp = headerListWSheetNameOfExcelFile.get();
                        if (headerListTmp == null) {
                            headerListTmp = new CopyOnWriteArrayList<>();
                        }
                        headerListTmp.add(cellValue);
                        headerListWSheetNameOfExcelFile.set(headerListTmp);
                    }
                }else if(row.getRowNum()==rowIndex) {
                    //Setup the array list for test data
                    for (Cell cell : row) {
                        if(cell.getCellType() == CellType.FORMULA){
                            switch(cell.getCachedFormulaResultType()) {
                                case NUMERIC:
                                    double cellNum = cell.getNumericCellValue();
                                    cellValue = String.valueOf(cellNum);
                                    break;
                                case STRING:
                                    cellValue = cell.getRichStringCellValue().toString();
                                    break;
                            }
                        }else{
                            cellValue = dataFormatter.formatCellValue(cell);
                        }
                        CopyOnWriteArrayList<String> testDataListTmp = testdataListWSheetNameOfExcelFile.get();
                        if (testDataListTmp == null) {
                            testDataListTmp = new CopyOnWriteArrayList<>();
                        }
                        testDataListTmp.add(cellValue);
                        testdataListWSheetNameOfExcelFile.set(testDataListTmp);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getColumnValueWWorkSheetNameOfExcelFile(String excelFilePath, int colIndex, String workSheetName, int rowIndex) throws IOException {
        readExcelWorkSheetOfExcelFile(excelFilePath,workSheetName,rowIndex);
        return headerListWSheetNameOfExcelFile.get().get(colIndex).trim();
    }

    public static String getCellValueWWorkSheetNameOfExcelFile(String excelFilePath, int colIndex, String workSheetName, int rowIndex) throws IOException {
        readExcelWorkSheetOfExcelFile(excelFilePath,workSheetName,rowIndex);
        return testdataListWSheetNameOfExcelFile.get().get(colIndex).trim();
    }

    public static int getNumberOfWorksheetsOfExcelFile(String excelFilePath) throws IOException {
        File excelFile = new File(excelFilePath);
        FileInputStream inputStream = new FileInputStream(excelFile);
        Workbook excelWorkbook = null;

        String fileExtensionName = excelFilePath.substring(excelFilePath.indexOf("."));
        if(fileExtensionName.equals(".xlsx")){
            excelWorkbook = new XSSFWorkbook(inputStream);
        } else if(fileExtensionName.equals(".xls")) {
            excelWorkbook = new HSSFWorkbook(inputStream);
        }
        return excelWorkbook.getNumberOfSheets();
    }

    public static String getCurrentWorksheetNameOfExcelFile(String excelFilePath, int worksheetIdx) throws IOException {
        File excelFile = new File(excelFilePath);
        FileInputStream inputStream = new FileInputStream(excelFile);
        Workbook excelWorkbook = null;

        String fileExtensionName = excelFilePath.substring(excelFilePath.indexOf("."));
        if(fileExtensionName.equals(".xlsx")){
            excelWorkbook = new XSSFWorkbook(inputStream);
        } else if(fileExtensionName.equals(".xls")) {
            excelWorkbook = new HSSFWorkbook(inputStream);
        }
        return excelWorkbook.getSheetName(worksheetIdx);
    }

    public static int getRowSizeOfExcelFile(String excelFilePath, String workSheetName) throws IOException {
        File excelFile = new File(excelFilePath);
        FileInputStream inputStream = new FileInputStream(excelFile);
        Workbook excelWorkbook = null;

        String fileExtensionName = excelFilePath.substring(excelFilePath.indexOf("."));
        if(fileExtensionName.equals(".xlsx")){
            excelWorkbook = new XSSFWorkbook(inputStream);
        } else if(fileExtensionName.equals(".xls")) {
            excelWorkbook = new HSSFWorkbook(inputStream);
        }

        Sheet excelWSheet = excelWorkbook.getSheet(workSheetName);

        return (excelWSheet.getPhysicalNumberOfRows()-1);
    }

    public static int getColumnSizeOfExcelFile(String excelFilePath, String workSheetName) throws IOException {

        File excelFile = new File(excelFilePath);
        FileInputStream inputStream = new FileInputStream(excelFile);
        Workbook excelWorkbook = null;

        String fileExtensionName = excelFilePath.substring(excelFilePath.indexOf("."));
        if(fileExtensionName.equals(".xlsx")){
            excelWorkbook = new XSSFWorkbook(inputStream);
        } else if(fileExtensionName.equals(".xls")) {
            excelWorkbook = new HSSFWorkbook(inputStream);
        }

        Sheet excelWorksheet = excelWorkbook.getSheet(workSheetName);
        Row excelRow = excelWorksheet.getRow(0);

        return excelRow.getLastCellNum();
    }

    public static int getColumnIdx(String excelFilePath, String fieldName, String sheetName, int rowIndex) throws IOException {
        readExcelWorkSheetOfExcelFile(excelFilePath, sheetName, rowIndex);
        int ColIndex = headerListWSheetNameOfExcelFile.get().indexOf(fieldName.trim());
        return ColIndex;
    }

    public static synchronized void setValueWWorkSheetNameOfExcelFile(String excelFilePath,String fieldName, String value, String workSheetName, int rowIndex) throws IOException {

        File excelFile = new File(excelFilePath);
        FileInputStream inputStream = new FileInputStream(excelFile);
        Workbook excelWorkbook = null;

        String fileExtensionName = excelFilePath.substring(excelFilePath.indexOf("."));
        if(fileExtensionName.equals(".xlsx")){
            excelWorkbook = new XSSFWorkbook(inputStream);
        } else if(fileExtensionName.equals(".xls")) {
            excelWorkbook = new HSSFWorkbook(inputStream);
        }

        Sheet excelWorksheet = excelWorkbook.getSheet(workSheetName);
        Row excelRow = excelWorksheet.getRow(rowIndex);
        Cell excelCell = excelRow.getCell(getColumnIdx(excelFilePath,fieldName,workSheetName,0));

        excelCell.setCellValue(value);

        FileOutputStream fileOut = new FileOutputStream(excelFilePath);
        excelWorkbook.write(fileOut);
        fileOut.close();
        excelWorkbook.close();
    }

    public static int getCurrentRow() throws IOException {
        return currentRowNo;
    }

}

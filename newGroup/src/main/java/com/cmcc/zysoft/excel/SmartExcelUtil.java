/**
 * 文件名:ExcelUtil.java
 * 作者 ：李三来<br />
 * 邮箱 ：li.sanlai@ustcinfo.com<br />
 * 描述 ：com.cmcc.zysoft.excel
 * 版本 ：2013-5-16<br />
 * 日期 ： 2013-5-16 下午5:42:16<br />
 * 版权 ：CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.<br />
 */
package com.cmcc.zysoft.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.cmcc.zysoft.rule.Column;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述： Excel工具类
 * <br />版本: 2013-5-16
 * <br />日期： 2013-5-16 下午5:42:16
 */
public class SmartExcelUtil {
	
	public static String EXCEL_VALUES_KEY = "excel";
	
	public static String COLUMNS_KEY = "columns";

	/**
	 * 解析excel文件
	 * @date 2013-5-16 下午6:39:12
	 * @param file
	 * @return
	 * @throws BiffException 
	 * @throws IOException
	 */
	public static Workbook parseWorkbook(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		Workbook workbook = null;
		 if (file.getName().matches("^.+\\.(?i)(xlsx)$"))
	        {
			 workbook =  new XSSFWorkbook(is);
	        }else if (file.getName().matches("^.+\\.(?i)(xls)$")){
	        	workbook =  new HSSFWorkbook(is);
	        }else if (file.getName().matches("^.+\\.(?i)(csv|txt)$")){
	        	String xlsPath = file.getPath().replace("csv", "xls").replace("txt", "xls");
	        	CsvOrTxtToExcel(file.getPath(), xlsPath);
	        	is = new FileInputStream(new File(xlsPath));
	        	workbook =  new HSSFWorkbook(is);
	        }
	        
		return workbook;
	}

	
	/**
	 * 获取sheet的总列数
	 * @param sheet
	 * @return
	 */
	public static int getColumns(Sheet sheet){
		return sheet.getRow(0).getLastCellNum()+1;
	}
	
	/**
	 * 获取sheet的总行数
	 * @param sheet
	 * @return
	 */
	public static int getRows(Sheet sheet){
		return sheet.getLastRowNum()+1;
	}
	public final static void CsvOrTxtToExcel(String csvOrTxt, String excel) throws IOException
 {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("Sheet1");

		BufferedReader r = null;

		try {
			File someFile = new File(csvOrTxt);
			FileInputStream fis = new FileInputStream(someFile);
			InputStreamReader isr = new InputStreamReader(fis, "gb2312");
			r = new BufferedReader(isr);
			int i = 0;

			while (true) {
				String ln = r.readLine();

				if (ln == null)
					break;

				Row row = sheet.createRow((short) i++);
				String[] splitArr = ln.split(",|\\s");
				for(int x=0;x<splitArr.length;x++){
					Cell cell = row.createCell((short) x);
					cell.setCellValue(splitArr[x]);
				}
				
			}
		} finally {
			if (r != null)
				r.close();
		}

		FileOutputStream fileOut = null;

		try {
			fileOut = new FileOutputStream(excel);
			wb.write(fileOut);
		} finally {
			if (fileOut != null)
				fileOut.close();
		}
	}
	/**
	 * 获取excel文件的列集合
	 * @date 2013-5-17 上午11:24:46
	 * @param excelFilePath
	 * @return
	 */
	public static Map<Integer, List<Object>> getExcelValues(String excelFilePath){
		File file = new File(excelFilePath);
		//存放excel文件列内容的map
		Map<Integer, List<Object>> excelValueMap = new HashMap<Integer, List<Object>>(); 
		try {
			Workbook workbook = parseWorkbook(file);
			Sheet sheet = workbook.getSheetAt(0);
			int cols = getColumns(sheet);
//			System.out.println("Excel列数="+cols);
			// 初始化线程数
    		CountDownLatch threadsSignal = new CountDownLatch(cols);
			for(int columnNum=0;columnNum<cols;columnNum++){
				Thread thread = new ExcelValueMapThread(threadsSignal, excelValueMap, sheet, columnNum);
				thread.start();
			}
			threadsSignal.await();
		}  catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return excelValueMap;
	}
	
	/**
	 * 自动识别
	 * @date 2013-5-17 上午10:48:43
	 * @param excelFilePath
	 * @param columns
	 * @return
	 */
	public static Map<String,Object> autoMatches(Map<Integer, List<Object>> excelValueMap,List<Column> columns){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<Integer,Column> columnIndexMap = new HashMap<Integer,Column>();
		// 初始化线程数
        int size = columns.size();
		CountDownLatch threadsSignal = new CountDownLatch(size);
		for (Column column : columns) {
			Thread thread = new AutoMatchThread(threadsSignal, excelValueMap, column, columnIndexMap);
			thread.start();
		}
		try {
			threadsSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//整理返回的数据集合
		List<ExcelColumns> excelColumns = new LinkedList<ExcelColumns>();
		Set<Integer> keyset = excelValueMap.keySet();
		// 初始化线程数
		for (Integer index : keyset) {
			ExcelColumns excelColumn = new ExcelColumns(index, excelValueMap.get(index));
			Column column = columnIndexMap.get(index);
			excelColumn.setColumnName(column==null?"未知":column.getName());
			excelColumns.add(excelColumn);
		}
		map.put("excel", excelColumns);
		List<String> columnNames = new LinkedList<String>();
		for (Column column : columns) {
			columnNames.add(column.getName());
		}
		map.put("columnNames", columnNames);
		map.put("excelRows", excelColumns.get(0).getColumnValues().size());
		return map;
	}
	
	/**
	 * 整理返回结果
	 * @date 2013-5-22 上午10:47:04
	 * @param map
	 * @return
	 */
	public static Map<String,Object> tidyMap(Map<String,Object> map){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		return resultMap;
	}
	
	public static void main(String[] args){
		Logger log = LoggerFactory.getLogger(SmartExcelUtil.class);
		/*try {
			CsvOrTxtToExcel("D:/台州.csv", "D:/台州.xls");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		String filePath = "C:/Users/AMCC/Desktop/groupAddBook/浙江移动/台州 -s.xls";
			try {
				File file = new File(filePath);
				Workbook workbook = SmartExcelUtil.parseWorkbook(file);
				Sheet sheet = workbook.getSheetAt(0);
				int rows = sheet.getLastRowNum();
				int cols = sheet.getRow(0).getLastCellNum();
				log.debug("列：{},行：{}",cols,rows);
			}  catch(IOException e){
				e.printStackTrace();
			}
	}
}

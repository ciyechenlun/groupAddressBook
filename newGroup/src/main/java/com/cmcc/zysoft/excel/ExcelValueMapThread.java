/**
 * 文件名:ExcelValueMapThread.java
 * 作者 ：李三来<br />
 * 邮箱 ：li.sanlai@ustcinfo.com<br />
 * 描述 ：com.cmcc.zysoft.excel
 * 版本 ：2013-5-17<br />
 * 日期 ： 2013-5-17 上午11:14:50<br />
 * 版权 ：CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.<br />
 */
package com.cmcc.zysoft.excel;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述： ExcelValueMapThread
 * <br />版本: 2013-5-17
 * <br />日期： 2013-5-17 上午11:14:50
 */
public class ExcelValueMapThread extends Thread {
	
	/**
	 * 线程个数计数器
	 */
	private CountDownLatch threadsSignal;
	
	/**
	 * excel索引和值得集合
	 */
	private Map<Integer, List<Object>> excelValueMap;
	
	/**
	 * 取样的数据行数
	 */
	private static  int SAMPLE_ROWS = 21;
	
	/**
	 * sheet
	 */
	private Sheet sheet;
	
	/**
	 * 列索引
	 */
	private int columnNum;

	/**
	 * 构造方法
	 */
	public ExcelValueMapThread() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 构造方法
	 * @param threadsSignal
	 * @param excelValueMap
	 * @param sheet
	 * @param columnNum
	 */
	public ExcelValueMapThread(CountDownLatch threadsSignal,
			Map<Integer, List<Object>> excelValueMap, Sheet sheet, int columnNum) {
		super();
		this.threadsSignal = threadsSignal;
		this.excelValueMap = excelValueMap;
		this.sheet = sheet;
		this.columnNum = columnNum;
	}

	/**
	 * 返回 threadsSignal
	 * @return threadsSignal
	 */
	public CountDownLatch getThreadsSignal() {
		return threadsSignal;
	}

	/**
	 * 设置threadsSignal
	 * @param threadsSignal the threadsSignal to set
	 */
	public void setThreadsSignal(CountDownLatch threadsSignal) {
		this.threadsSignal = threadsSignal;
	}

	/**
	 * 返回 excelValueMap
	 * @return excelValueMap
	 */
	public Map<Integer, List<Object>> getExcelValueMap() {
		return excelValueMap;
	}
    /** *//**
     * <ul>
     * <li>Description:[正确地处理整数后自动加零的情况]</li>
     * <li>Created by [Huyvanpull] [Jan 20, 2010]</li>
     * <li>Midified by [modifier] [modified time]</li>
     * <ul>
     * 
     * @param sNum
     * @return
     */
    private String getRightStr(String sNum)
    {
        DecimalFormat decimalFormat = new DecimalFormat("#.000000");
        String resultStr = decimalFormat.format(new Double(sNum));
        if (resultStr.matches("^[-+]?\\d+\\.[0]+$"))
        {
            resultStr = resultStr.substring(0, resultStr.indexOf("."));
        }
        return resultStr;
    }
	/**
	 * 设置excelValueMap
	 * @param excelValueMap the excelValueMap to set
	 */
	public void setExcelValueMap(Map<Integer, List<Object>> excelValueMap) {
		this.excelValueMap = excelValueMap;
	}

	/**
	 * 返回 sheet
	 * @return sheet
	 */
	public Sheet getSheet() {
		return sheet;
	}

	/**
	 * 设置sheet
	 * @param sheet the sheet to set
	 */
	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	/**
	 * 返回 columnNum
	 * @return columnNum
	 */
	public int getColumnNum() {
		return columnNum;
	}

	/**
	 * 设置columnNum
	 * @param columnNum the columnNum to set
	 */
	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}
	private String getCellValue(Cell cell){
		String cellValue = "";
        if (cell == null)
        {
            return cellValue;
        }
        /** *//** 处理数字型的,自动去零 */
        if (Cell.CELL_TYPE_NUMERIC == cell.getCellType())
        {
            /** *//** 在excel里,日期也是数字,在此要进行判断 */
            if (HSSFDateUtil.isCellDateFormatted(cell))
            {
                cellValue = Double.toString(DateUtil.getExcelDate(cell.getDateCellValue()));
            }
            else
            {
                cellValue = getRightStr(cell.getNumericCellValue() + "");
            }
        }
        /** *//** 处理字符串型 */
        else if (Cell.CELL_TYPE_STRING == cell.getCellType())
        {
            cellValue = cell.getStringCellValue();
        }
        /** *//** 处理布尔型 */
        else if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType())
        {
            cellValue = cell.getBooleanCellValue() + "";
        }
        /** *//** 其它的,非以上几种数据类型 */
        else
        {
            cellValue = cell.toString() + "";
        }
        return cellValue;
	}
	/**
	 * 检查正则匹配结果
	 * @date 2013-5-17 下午1:01:22
	 * @param regex
	 * @param str
	 * @return
	 */
	private boolean checkRegex(String regex,String str){
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean flag = m.matches();
		return flag;
	}
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		List<Object> values = new LinkedList<Object>();
		int length = sheet.getLastRowNum()+1<=SAMPLE_ROWS?sheet.getLastRowNum()+1:SAMPLE_ROWS;
		for(int i=0 ; i < length ; i++ ){
			if(i==0){
				boolean flag = false;
				for(int j=0;j<=sheet.getRow(i).getLastCellNum();j++){
					if(checkRegex("^0?(13[0-9]|15[012356789]|18[02356789]|14[57])[0-9]{8}$",getCellValue(sheet.getRow(i).getCell(j)))){
						flag=true;
					}
				}
				if(!flag){
					continue;
				}
			}
			Cell cell = sheet.getRow(i).getCell(columnNum);
			 String cellValue = "";
             if (cell == null)
             {
            	 values.add(cellValue);
                 continue;
             }
             /** *//** 处理数字型的,自动去零 */
             if (Cell.CELL_TYPE_NUMERIC == cell.getCellType())
             {
                 /** *//** 在excel里,日期也是数字,在此要进行判断 */
                 if (HSSFDateUtil.isCellDateFormatted(cell))
                 {
                     cellValue = Double.toString(DateUtil.getExcelDate(cell.getDateCellValue()));
                 }
                 else
                 {
                     cellValue = getRightStr(cell.getNumericCellValue() + "");
                 }
             }
             /** *//** 处理字符串型 */
             else if (Cell.CELL_TYPE_STRING == cell.getCellType())
             {
                 cellValue = cell.getStringCellValue();
             }
             /** *//** 处理布尔型 */
             else if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType())
             {
                 cellValue = cell.getBooleanCellValue() + "";
             }
             /** *//** 其它的,非以上几种数据类型 */
             else
             {
                 cellValue = cell.toString() + "";
             }
             values.add(cellValue);
		}
		excelValueMap.put(columnNum, values);
		// 线程执行完成的时候计数器减1
		threadsSignal.countDown();
	}
	
}

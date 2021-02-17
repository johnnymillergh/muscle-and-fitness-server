package com.jmsoftware.maf.springcloudstarter.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Iterator;

/**
 * <h1>PoiUtil</h1>
 * <p>
 * Change description here.
 *
 * @author 钟俊 (jun.zhong), email: jun.zhong@ucarinc.com
 * @date 4 /30/20 6:37 PM
 */
public class PoiUtil {
    /**
     * Copy cell style.
     *
     * @param fromStyle the from style
     * @param toStyle   the to style
     */
    public static void copyCellStyle(HSSFCellStyle fromStyle,
                                     HSSFCellStyle toStyle) {
        toStyle.setAlignment(fromStyle.getAlignmentEnum());
        //边框和边框颜色
        toStyle.setBorderBottom(fromStyle.getBorderBottomEnum());
        toStyle.setBorderLeft(fromStyle.getBorderLeftEnum());
        toStyle.setBorderRight(fromStyle.getBorderRightEnum());
        toStyle.setBorderTop(fromStyle.getBorderTopEnum());
        toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
        toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
        toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
        toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());

        //背景和前景
        toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
        toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());

        toStyle.setDataFormat(fromStyle.getDataFormat());
        toStyle.setFillPattern(fromStyle.getFillPatternEnum());
//		toStyle.setFont(fromStyle.getFont(null));
        toStyle.setHidden(fromStyle.getHidden());
        toStyle.setIndention(fromStyle.getIndention());//首行缩进
        toStyle.setLocked(fromStyle.getLocked());
        toStyle.setRotation(fromStyle.getRotation());//旋转
        toStyle.setVerticalAlignment(fromStyle.getVerticalAlignmentEnum());
        toStyle.setWrapText(fromStyle.getWrapText());
    }

    /**
     * Sheet复制
     *
     * @param wb        the wb
     * @param fromSheet the from sheet
     * @param toSheet   the to sheet
     * @param copyValue the copy value flag
     */
    public static void copySheet(HSSFWorkbook wb, HSSFSheet fromSheet, HSSFSheet toSheet,
                                 boolean copyValue) {
        //合并区域处理
        mergerRegion(fromSheet, toSheet);
        for (Iterator<Row> rowIt = fromSheet.rowIterator(); rowIt.hasNext(); ) {
            HSSFRow tmpRow = (HSSFRow) rowIt.next();
            HSSFRow newRow = toSheet.createRow(tmpRow.getRowNum());
            //行复制
            copyRow(wb, tmpRow, newRow, copyValue);
        }
    }

    /**
     * 行复制功能
     *
     * @param wb        the wb
     * @param fromRow   the from row
     * @param toRow     the to row
     * @param copyValue the copy value flag
     */
    public static void copyRow(HSSFWorkbook wb, HSSFRow fromRow, HSSFRow toRow, boolean copyValue) {
        for (Iterator<Cell> cellIt = fromRow.cellIterator(); cellIt.hasNext(); ) {
            HSSFCell tmpCell = (HSSFCell) cellIt.next();
            HSSFCell newCell = toRow.createCell(tmpCell.getColumnIndex());
            copyCell(wb, tmpCell, newCell, copyValue);
        }
    }

    /**
     * 复制原有sheet的合并单元格到新创建的sheet
     *
     * @param fromSheet 原有的sheet
     * @param toSheet   新创建sheet
     */
    public static void mergerRegion(HSSFSheet fromSheet, HSSFSheet toSheet) {
        int sheetMergerCount = fromSheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            CellRangeAddress mergedRegionAt = fromSheet.getMergedRegion(i);
            toSheet.addMergedRegion(mergedRegionAt);
        }
    }

    /**
     * 复制单元格
     *
     * @param wb        the wb
     * @param srcCell   the src cell
     * @param distCell  the dist cell
     * @param copyValue true则连同cell的内容一起复制
     */
    public static void copyCell(HSSFWorkbook wb, HSSFCell srcCell, HSSFCell distCell,
                                boolean copyValue) {
        HSSFCellStyle newStyle = wb.createCellStyle();
        copyCellStyle(srcCell.getCellStyle(), newStyle);
        //样式
        distCell.setCellStyle(newStyle);
        //评论
        if (srcCell.getCellComment() != null) {
            distCell.setCellComment(srcCell.getCellComment());
        }
        // 不同数据类型处理
        CellType srcCellType = srcCell.getCellTypeEnum();
        distCell.setCellType(srcCellType);
        if (copyValue) {
            switch (srcCellType) {
                case NUMERIC:
                    if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
                        distCell.setCellValue(srcCell.getDateCellValue());
                    } else {
                        distCell.setCellValue(srcCell.getNumericCellValue());
                    }
                    break;
                case STRING:
                    distCell.setCellValue(srcCell.getRichStringCellValue());
                    break;
                case BLANK:
                    break;
                case BOOLEAN:
                    distCell.setCellValue(srcCell.getBooleanCellValue());
                    break;
                case ERROR:
                    distCell.setCellErrorValue(FormulaError.forInt(srcCell.getErrorCellValue()));
                    break;
                case FORMULA:
                    distCell.setCellFormula(srcCell.getCellFormula());
                    break;
                default:
            }
        }
    }

    /**
     * 行复制功能
     *
     * @param copyValue true则连同cell的内容一起复制
     * @param wb        工作簿
     * @param fromRow   从哪行开始
     * @param toRow     目标行
     */
    public static void copyRow(boolean copyValue, Workbook wb, Row fromRow, Row toRow) {
        toRow.setHeight(fromRow.getHeight());

        for (Iterator<Cell> cellIt = fromRow.cellIterator(); cellIt.hasNext(); ) {
            Cell tmpCell = cellIt.next();
            Cell newCell = toRow.createCell(tmpCell.getColumnIndex());
            copyCell(wb, tmpCell, newCell, copyValue);
        }

        Sheet worksheet = fromRow.getSheet();

        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == fromRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(toRow.getRowNum(),
                                                                            (toRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
                                                                            cellRangeAddress.getFirstColumn(),
                                                                            cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
            }
        }
    }

    /**
     * 复制单元格
     *
     * @param wb        the wb
     * @param srcCell   the src cell
     * @param distCell  the dist cell
     * @param copyValue true则连同cell的内容一起复制
     */
    public static void copyCell(Workbook wb, Cell srcCell, Cell distCell, boolean copyValue) {
//        CellStyle newStyle = wb.createCellStyle();
//        CellStyle srcStyle = srcCell.getCellStyle();
//
//        newStyle.cloneStyleFrom(srcStyle);
//        newStyle.setFont(wb.getFontAt(srcStyle.getFontIndex()));

        // 样式
//        distCell.setCellStyle(srcCell.getCellStyle());

        // 内容
        if (srcCell.getCellComment() != null) {
            distCell.setCellComment(srcCell.getCellComment());
        }

        // 不同数据类型处理
        CellType srcCellType = srcCell.getCellTypeEnum();
        distCell.setCellType(srcCellType);
        if (copyValue) {
            switch (srcCellType) {
                case NUMERIC:
                    if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
                        distCell.setCellValue(srcCell.getDateCellValue());
                    } else {
                        distCell.setCellValue(srcCell.getNumericCellValue());
                    }
                    break;
                case STRING:
                    distCell.setCellValue(srcCell.getRichStringCellValue());
                    break;
                case BLANK:
                    break;
                case BOOLEAN:
                    distCell.setCellValue(srcCell.getBooleanCellValue());
                    break;
                case ERROR:
                    distCell.setCellErrorValue(srcCell.getErrorCellValue());
                    break;
                case FORMULA:
                    distCell.setCellFormula(srcCell.getCellFormula());
                    break;
                default:
            }
        }
    }
}

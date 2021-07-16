package com.jmsoftware.maf.springcloudstarter.util;

import lombok.val;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * <h1>PoiUtil</h1>
 * <p>
 * Change description here.
 *
 * @author @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 2/18/2021 5:37 PM
 */
@SuppressWarnings({"AlibabaRemoveCommentedCode", "unused"})
public class PoiUtil {
    /**
     * Copy cell style.
     *
     * @param sourceCellStyle the source cell style
     * @param targetCellStyle the target cell style
     */
    public static void copyCellStyle(HSSFCellStyle sourceCellStyle, HSSFCellStyle targetCellStyle) {
        targetCellStyle.setAlignment(sourceCellStyle.getAlignment());
        // Boarder style
        targetCellStyle.setBorderBottom(sourceCellStyle.getBorderBottom());
        targetCellStyle.setBorderLeft(sourceCellStyle.getBorderLeft());
        targetCellStyle.setBorderRight(sourceCellStyle.getBorderRight());
        targetCellStyle.setBorderTop(sourceCellStyle.getBorderTop());
        targetCellStyle.setTopBorderColor(sourceCellStyle.getTopBorderColor());
        targetCellStyle.setBottomBorderColor(sourceCellStyle.getBottomBorderColor());
        targetCellStyle.setRightBorderColor(sourceCellStyle.getRightBorderColor());
        targetCellStyle.setLeftBorderColor(sourceCellStyle.getLeftBorderColor());
        // Background and foreground
        targetCellStyle.setFillBackgroundColor(sourceCellStyle.getFillBackgroundColor());
        targetCellStyle.setFillForegroundColor(sourceCellStyle.getFillForegroundColor());
        // Data format
        targetCellStyle.setDataFormat(sourceCellStyle.getDataFormat());
        targetCellStyle.setFillPattern(sourceCellStyle.getFillPattern());
        // toStyle.setFont(fromStyle.getFont(null));
        targetCellStyle.setHidden(sourceCellStyle.getHidden());
        targetCellStyle.setIndention(sourceCellStyle.getIndention());
        targetCellStyle.setLocked(sourceCellStyle.getLocked());
        targetCellStyle.setRotation(sourceCellStyle.getRotation());
        targetCellStyle.setVerticalAlignment(sourceCellStyle.getVerticalAlignment());
        targetCellStyle.setWrapText(sourceCellStyle.getWrapText());
    }

    /**
     * Copy sheet.
     *
     * @param workbook    the workbook
     * @param sourceSheet the source sheet
     * @param targetSheet the target sheet
     * @param copyValue   the copy value
     */
    public static void copySheet(HSSFWorkbook workbook, HSSFSheet sourceSheet, HSSFSheet targetSheet,
                                 boolean copyValue) {
        mergerRegion(sourceSheet, targetSheet);
        sourceSheet.rowIterator().forEachRemaining(oldRow -> {
            val newRow = targetSheet.createRow(oldRow.getRowNum());
            copyRow(workbook, (HSSFRow) oldRow, newRow, copyValue);
        });
    }

    /**
     * Copy row.
     *
     * @param workbook  the workbook
     * @param sourceRow the source row
     * @param targetRow the target row
     * @param copyValue the copy value
     */
    public static void copyRow(HSSFWorkbook workbook, HSSFRow sourceRow, HSSFRow targetRow, boolean copyValue) {
        sourceRow.cellIterator().forEachRemaining(oldCell -> {
            val newCell = targetRow.createCell(oldCell.getColumnIndex());
            copyCell(workbook, oldCell, newCell, copyValue);
        });
    }

    /**
     * Merger region.
     *
     * @param sourceSheet the source sheet
     * @param targetSheet the target sheet
     */
    public static void mergerRegion(HSSFSheet sourceSheet, HSSFSheet targetSheet) {
        val sheetMergerCount = sourceSheet.getNumMergedRegions();
        for (var i = 0; i < sheetMergerCount; i++) {
            val mergedRegionAt = sourceSheet.getMergedRegion(i);
            targetSheet.addMergedRegion(mergedRegionAt);
        }
    }

    /**
     * Copy cell.
     *
     * @param workbook   the workbook
     * @param sourceCell the source cell
     * @param targetCell the target cell
     * @param copyValue  the copy value
     */
    public static void copyCell(HSSFWorkbook workbook, HSSFCell sourceCell, HSSFCell targetCell, boolean copyValue) {
        val newStyle = workbook.createCellStyle();
        copyCellStyle(sourceCell.getCellStyle(), newStyle);
        targetCell.setCellStyle(newStyle);
        if (sourceCell.getCellComment() != null) {
            targetCell.setCellComment(sourceCell.getCellComment());
        }
        val srcCellType = sourceCell.getCellType();
        targetCell.setCellType(srcCellType);
        if (copyValue) {
            switch (srcCellType) {
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(sourceCell)) {
                        targetCell.setCellValue(sourceCell.getDateCellValue());
                    } else {
                        targetCell.setCellValue(sourceCell.getNumericCellValue());
                    }
                    break;
                case STRING:
                    targetCell.setCellValue(sourceCell.getRichStringCellValue());
                    break;
                case BLANK:
                    break;
                case BOOLEAN:
                    targetCell.setCellValue(sourceCell.getBooleanCellValue());
                    break;
                case ERROR:
                    targetCell.setCellErrorValue(FormulaError.forInt(sourceCell.getErrorCellValue()));
                    break;
                case FORMULA:
                    targetCell.setCellFormula(sourceCell.getCellFormula());
                    break;
                default:
            }
        }
    }

    /**
     * Copy row.
     *
     * @param copyValue the copy value
     * @param workbook  the workbook
     * @param sourceRow the source row
     * @param targetRow the target row
     */
    public static void copyRow(boolean copyValue, Workbook workbook, Row sourceRow, Row targetRow) {
        targetRow.setHeight(sourceRow.getHeight());

        sourceRow.cellIterator().forEachRemaining(oldCell -> {
            Cell newCell = targetRow.createCell(oldCell.getColumnIndex());
            copyCell(workbook, oldCell, newCell, copyValue);
        });

        val worksheet = sourceRow.getSheet();

        for (var i = 0; i < worksheet.getNumMergedRegions(); i++) {
            val cellRangeAddress = worksheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                val newCellRangeAddress =
                        new CellRangeAddress(targetRow.getRowNum(),
                                             (targetRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
                                             cellRangeAddress.getFirstColumn(),
                                             cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
            }
        }
    }

    /**
     * Copy cell.
     *
     * @param workbook   the workbook
     * @param sourceCell the source cell
     * @param targetCell the target cell
     * @param copyValue  the copy value
     */
    public static void copyCell(Workbook workbook, Cell sourceCell, Cell targetCell, boolean copyValue) {
        if (sourceCell.getCellComment() != null) {
            targetCell.setCellComment(sourceCell.getCellComment());
        }
        val srcCellType = sourceCell.getCellType();
        if (copyValue) {
            switch (srcCellType) {
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(sourceCell)) {
                        targetCell.setCellValue(sourceCell.getDateCellValue());
                    } else {
                        targetCell.setCellValue(sourceCell.getNumericCellValue());
                    }
                    break;
                case STRING:
                    targetCell.setCellValue(sourceCell.getRichStringCellValue());
                    break;
                case BLANK:
                    break;
                case BOOLEAN:
                    targetCell.setCellValue(sourceCell.getBooleanCellValue());
                    break;
                case ERROR:
                    targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
                    break;
                case FORMULA:
                    targetCell.setCellFormula(sourceCell.getCellFormula());
                    break;
                default:
            }
        }
    }
}

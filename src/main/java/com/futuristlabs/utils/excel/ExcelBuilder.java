package com.futuristlabs.utils.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

public class ExcelBuilder<T> {
    public static final String MIME_TYPE = "application/vnd.ms-excel";

    private static final DateTimeFormatter REPORT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private static final String CHECK = "\u2714";

    private final List<Column<T, Object>> columns;

    private <R extends TemporalAccessor> CellExtractor<T, Object> safeFormater(final DateTimeFormatter fmt, final CellExtractor<T, R> extractor) {
        return t -> {
            final TemporalAccessor tmp = extractor.extractValue(t);
            return tmp != null ? fmt.format(tmp) : null;
        };
    }

    public ExcelBuilder() {
        columns = new ArrayList<>();
    }

    public <R extends TemporalAccessor> ExcelBuilder<T> withTime(final String columnHeader, final CellExtractor<T, R> columnExtractor) {
        this.columns.add(new Column<>(columnHeader, safeFormater(TIME_FMT, columnExtractor)));
        return this;
    }

    public <R extends TemporalAccessor> ExcelBuilder<T> withDate(final String columnHeader, final CellExtractor<T, R> columnExtractor) {
        this.columns.add(new Column<>(columnHeader, safeFormater(DATE_FMT, columnExtractor)));
        return this;
    }

    public <R extends TemporalAccessor> ExcelBuilder<T> withDateTime(final String columnHeader, final CellExtractor<T, R> columnExtractor) {
        this.columns.add(new Column<>(columnHeader, safeFormater(DATETIME_FMT, columnExtractor)));
        return this;
    }

    public ExcelBuilder<T> with(final String columnHeader, final CellExtractor<T, Object> columnExtractor) {
        this.columns.add(new Column<>(columnHeader, columnExtractor));
        return this;
    }

    public ResponseEntity<byte[]> createResponse(final String filename, final List<T> rows) {
        final byte[] bytes = createFile(rows);
        final String attach = String.format("attachment; filename=%s-%s.xlsx", filename, LocalDate.now().format(REPORT_FMT));
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MIME_TYPE));
        headers.add("Content-Disposition", attach);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    private void set(final Cell cell, final Object value, final CellStyle alignCenter) {
        if (value == null) {
            cell.setCellType(CellType.BLANK);
        } else if (value instanceof Boolean) {
            if ((Boolean) value) {
                cell.setCellStyle(alignCenter);
                cell.setCellValue(CHECK);
            } else {
                cell.setCellType(CellType.BLANK);
            }
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else {
            // default to string for now - may handle date-times, formulas, etc ... later
            cell.setCellValue(String.valueOf(value));
        }
    }

    private XSSFWorkbook createWorkbook(final List<T> rows) {
        final XSSFWorkbook wb = new XSSFWorkbook();
        final XSSFSheet sheet = wb.createSheet();
        final CellStyle alignCenter = wb.createCellStyle();
        alignCenter.setAlignment(HorizontalAlignment.CENTER);
        int rowId = 0;

        // generate header
        {
            final Row row = sheet.createRow(rowId++);
            int columnId = 0;

            final Font font = wb.createFont();
            font.setBold(true);
            final CellStyle style = wb.createCellStyle();
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setShrinkToFit(false);

            for (final Column<T, Object> columnDesc : columns) {
                Cell cell = row.createCell(columnId++);
                cell.setCellValue(columnDesc.getHeader());
                cell.setCellStyle(style);
            }

            // freeze header row
            sheet.createFreezePane(0, 1);
        }

        // generate rows
        for (final T rowData : rows) {
            final Row row = sheet.createRow(rowId++);
            int columnId = 0;

            for (final Column<T, Object> columnDesc : columns) {
                final Cell cell = row.createCell(columnId++);
                final Object value = columnDesc.getExtractor().extractValue(rowData);
                set(cell, value, alignCenter);
            }
        }

        // auto-ajust columns' width
        for (int column = 0; column < columns.size(); column++) {
            sheet.autoSizeColumn(column);
        }

        return wb;
    }

    private byte[] createFile(final List<T> rows) {
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            final XSSFWorkbook wb = createWorkbook(rows);
            wb.write(os);
            return os.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

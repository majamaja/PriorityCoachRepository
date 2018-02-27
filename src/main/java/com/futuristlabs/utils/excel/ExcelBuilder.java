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
    private static final DateTimeFormatter REPORT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

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

    public XSSFWorkbook createWorkbook(final List<T> rows) {
        final XSSFWorkbook wb = new XSSFWorkbook();
        final XSSFSheet sheet = wb.createSheet();
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
            style.setShrinkToFit(true);

            for (final Column<T, Object> columnDesc: columns) {
                Cell cell = row.createCell(columnId++);
                cell.setCellStyle(style);
                cell.setCellValue(columnDesc.getHeader());
            }
        }

        // generate rows
        for (final T rowData : rows) {
            final Row row = sheet.createRow(rowId++);
            int columnId = 0;

            for (final Column<T, Object> columnDesc: columns) {
                Cell cell = row.createCell(columnId++);
                final Object cellValue = columnDesc.getExtractor().extractValue(rowData);
                cell.setCellValue(String.valueOf(cellValue != null ? cellValue : ""));
            }
        }

        return wb;
    }

    public ExcelFile createFile(final List<T> rows) {
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            final XSSFWorkbook wb = createWorkbook(rows);
            wb.write(os);
            return new ExcelFile(os.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<byte[]> createResponse(final String filename, final List<T> rows) {
        final ExcelFile xls = createFile(rows);
        final String attach = String.format("attachment; filename=%s-%s.xlsx", filename, LocalDate.now().format(REPORT_FMT));
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(ExcelFile.MIME_TYPE));
        headers.add("Content-Disposition", attach);
        return new ResponseEntity<>(xls.getBytes(), headers, HttpStatus.OK);
    }
}

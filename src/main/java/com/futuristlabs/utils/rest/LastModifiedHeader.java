package com.futuristlabs.utils.rest;

import com.futuristlabs.func.exceptions.NotModifiedException;
import com.futuristlabs.utils.repository.PageData;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class LastModifiedHeader<T> {
    private final LastModifiedExtractor<? super T> extractor;

    public LastModifiedHeader(LastModifiedExtractor<? super T> extractor) {
        this.extractor = extractor;
    }

    private void setLastModified(HttpServletResponse response, LocalDateTime lastModified) {
        response.addHeader("Last-Modified",
                           DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz").format(lastModified.atZone(ZoneId.systemDefault())));
    }

    public <U extends T> U setAndReturn(HttpServletResponse response, U entry) {
        final LocalDateTime lastModified = Optional
                .ofNullable(this.extractor.getLastModified(entry))
                .orElseGet(LocalDateTime::now);
        setLastModified(response, lastModified);
        return entry;
    }

    public <U extends T> U checkSetAndReturn(HttpServletResponse response, U entry, LocalDateTime lastModifiedSince) {
        final LocalDateTime entryLastModified = this.extractor.getLastModified(entry);
        if (lastModifiedSince != null && entryLastModified != null && entryLastModified.isBefore(lastModifiedSince)) {
            throw new NotModifiedException();
        }
        return setAndReturn(response, entry);
    }

    public <U extends T> Stream<U> setAndReturn(HttpServletResponse response, Stream<U> stream) {
        final LocalDateTime lastModified = stream
                .filter(entry -> this.extractor.getLastModified(entry) != null)
                .map(this.extractor::getLastModified)
                .max(LocalDateTime::compareTo)
                .orElseGet(LocalDateTime::now);
        setLastModified(response, lastModified);
        return stream;
    }

    public <U extends T> List<U> setAndReturn(HttpServletResponse response, List<U> data) {
        setAndReturn(response, data.stream());
        return data;
    }

    public <U extends T> PageData<U> setAndReturn(HttpServletResponse response, PageData<U> page) {
        setAndReturn(response, page.getItems().stream());
        return page;
    }
}

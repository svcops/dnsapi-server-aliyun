package io.intellij.devops.server.dnsapi.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * PageResult
 *
 * @author tech@intellij.io
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class PageResult<T> extends Result<T> {
    private Long pageNumber;
    private Long pageSize;
    private Long totalCount;
    private Long totalPage;

    private PageResult(int code, String msg, T data, Long pageNumber, Long pageSize, Long totalCount) {
        super(code, msg, data);
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPage = (totalCount / pageSize) + (totalCount % pageSize == 0 ? 0 : 1);
    }

    private PageResult(int code, String msg) {
        super(code, msg, null);
        this.pageNumber = null;
        this.pageSize = null;
        this.totalCount = null;
        this.totalPage = null;
    }

    public static <T> PageResult<T> ok(T data, Long pageNumber, Long pageSize, Long totalCount) {
        return new PageResult<T>(200, "", data, pageNumber, pageSize, totalCount);
    }

}

package io.intellij.devops.server.dnsapi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * PageRequest
 *
 * @author tech@intellij.io
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@Data
public class PageRequest {
    private Long pageNumber;
    private Long pageSize;
}

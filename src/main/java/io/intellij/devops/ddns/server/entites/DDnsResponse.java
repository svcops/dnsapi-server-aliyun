package io.intellij.devops.ddns.server.entites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * DDnsResponse
 *
 * @author tech@intellij.io
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class DDnsResponse {
    private Date ts;
    private Object apiResponseBody;

    public static DDnsResponse of(Object apiResponseBody) {
        return new DDnsResponse(new Date(), apiResponseBody);
    }

}

package io.intellij.devops.client.ddns.tasks;

import io.intellij.devops.client.ddns.request.DDNSRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * DDNSTask
 * <p>
 * test: @Scheduled(cron = "0/10 * * * * ?")
 *
 * @author tech@intellij.io
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class DDNSTask {

    private final DDNSRequestService ddnsRequestService;

    @Scheduled(cron = "${ddns.cron:0 0/5 * * * ?}")
    public void request() {
        ddnsRequestService.request();
    }

}

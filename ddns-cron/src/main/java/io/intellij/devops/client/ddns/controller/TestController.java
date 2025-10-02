package io.intellij.devops.client.ddns.controller;

import io.intellij.devops.client.ddns.request.DDNSRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author tech@intellij.io
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/")
@RestController
public class TestController {
    private final DDNSRequestService ddnsRequestService;

    @GetMapping("/test")
    public String test() {
        return ddnsRequestService.request();
    }

}

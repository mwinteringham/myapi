package org.wirebridge.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wirebridge.db.QueryResult;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URLDecoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@EnableAutoConfiguration
public class WirebridgeController {

    private static WirebridgeStartup wirebridge;

    @RequestMapping("/**")
    String home(HttpServletRequest request, @RequestParam Map<String,String> allRequestParams) {
        QueryResult queryResult = wirebridge.call(request.getMethod(), request.getRequestURI(), allRequestParams);
        return queryResult.toString();
    }

    public static void main(String[] args) throws Exception {
        wirebridge = new WirebridgeStartup();

        SpringApplication.run(WirebridgeController.class, args);
    }

}

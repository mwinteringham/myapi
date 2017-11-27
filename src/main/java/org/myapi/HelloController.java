package org.myapi;

import com.spun.util.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@EnableAutoConfiguration
public class HelloController {

    @RequestMapping("/**")
    String home(HttpServletRequest request, @RequestParam Map<String,String> allRequestParams) {
        MyAPIStartup myApi = new MyAPIStartup();
        myApi.loadJson(FileUtils.readFromClassPath(getClass(), "../../add_table.json"));
        myApi.loadJson(FileUtils.readFromClassPath(getClass(), "../../query_table.json"));
        myApi.loadJson(FileUtils.readFromClassPath(getClass(), "../../config.json"));
        QueryResult queryResult = myApi.call(request.getMethod(), request.getRequestURI(), allRequestParams);

        return queryResult.toString();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloController.class, args);
    }

}

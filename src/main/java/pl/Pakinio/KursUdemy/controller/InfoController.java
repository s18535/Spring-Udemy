package pl.Pakinio.KursUdemy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.Pakinio.KursUdemy.TaskConfigurationProperties;

@RestController
@RequestMapping("/info")
public class InfoController {
    //@Autowired
    private DataSourceProperties dataSource;
    //@Autowired
    //@Value("${task.allowMultipleTasksFromTemplate}")
    private TaskConfigurationProperties myProp;

    public InfoController(DataSourceProperties dataSource, TaskConfigurationProperties myProp) {
        this.dataSource = dataSource;
        this.myProp = myProp;
    }

    @GetMapping("/url")
    String url(){
        return dataSource.getUrl();
    }

    @GetMapping("/prop")
    boolean myProp(){
        return myProp.getTemplate().isAllowMultipleTasks();
    }
}

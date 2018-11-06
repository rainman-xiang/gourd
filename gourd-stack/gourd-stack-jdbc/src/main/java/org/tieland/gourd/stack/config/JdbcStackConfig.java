package org.tieland.gourd.stack.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.tieland.gourd.stack.api.GourdEventStack;
import org.tieland.gourd.stack.api.GourdStack;
import org.tieland.gourd.stack.support.JdbcGourdEventStack;
import org.tieland.gourd.stack.support.JdbcGourdStack;

/**
 * @author zhouxiang
 * @date 2018/10/29 16:14
 */
@Configuration
@EnableConfigurationProperties(StackProperties.class)
public class JdbcStackConfig {

    @Autowired
    private StackProperties stackProperties;

    @Bean
    public GourdStack gourdStack(JdbcTemplate jdbcTemplate, ConfigurableEnvironment environment){
        JdbcGourdStack stack = new JdbcGourdStack(getServiceId(environment));
        stack.setJdbcTemplate(jdbcTemplate);
        return stack;
    }

    @Bean
    public GourdEventStack gourdEventStack(JdbcTemplate jdbcTemplate, ConfigurableEnvironment environment){
        JdbcGourdEventStack stack = new JdbcGourdEventStack(getServiceId(environment));
        stack.setJdbcTemplate(jdbcTemplate);
        return stack;
    }

    private String getServiceId(ConfigurableEnvironment environment){
        if(stackProperties!=null && stackProperties.getServiceId()!=null){
            return stackProperties.getServiceId();
        }

        return environment.getProperty("spring.application.name");
    }

}

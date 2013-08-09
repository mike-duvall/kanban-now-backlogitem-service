package kanbannow;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class BacklogItemService extends Service<BacklogItemServiceConfiguration> {
    @Override
    public void initialize(Bootstrap<BacklogItemServiceConfiguration> bootstrap) {

    }

    // Test change
    @Override
    public void run(BacklogItemServiceConfiguration configuration, Environment environment) throws Exception {

    }
}

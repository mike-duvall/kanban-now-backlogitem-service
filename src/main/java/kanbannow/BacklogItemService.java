package kanbannow;


import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import kanbannow.health.BacklogItemHealthCheck;
import kanbannow.resources.HelloWorldResource;


public class BacklogItemService extends Service<BacklogItemServiceConfiguration> {



    public static void main(String[] args) throws Exception {
        new BacklogItemService().run(args);
    }


    @Override
    public void initialize(Bootstrap<BacklogItemServiceConfiguration> bootstrap) {

    }


    @Override
    public void run(BacklogItemServiceConfiguration configuration, Environment environment) throws Exception {
        environment.addResource(new HelloWorldResource());
        environment.addHealthCheck(new BacklogItemHealthCheck());
    }



}

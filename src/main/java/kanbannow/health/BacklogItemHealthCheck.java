package kanbannow.health;

import com.yammer.metrics.core.HealthCheck;

public class BacklogItemHealthCheck extends HealthCheck {

    public BacklogItemHealthCheck() {
        super("backlogItemService");
    }

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}

package com.cvnavi.monitor.support;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * @description: Status Change Notifier
 * @author: zjbing
 * @create: 2019-03-28 11:39
 **/
@Slf4j
@Component
public class StatusChangeNotifier extends AbstractStatusChangeNotifier {


    private String[] ignoreChanges = {""};

    public void setIgnoreChanges(String[] ignoreChanges) {
        String[] copy = Arrays.copyOf(ignoreChanges, ignoreChanges.length);
        Arrays.sort(copy);
        this.ignoreChanges = copy;
    }

    public String[] getIgnoreChanges() {
        return ignoreChanges;
    }

    @Override
    protected boolean shouldNotify(InstanceEvent event, Instance instance) {
        if (event instanceof InstanceStatusChangedEvent) {
            InstanceStatusChangedEvent statusChange = (InstanceStatusChangedEvent) event;
            String from = getLastStatus(event.getInstance());
            String to = statusChange.getStatusInfo().getStatus();
            return Arrays.binarySearch(ignoreChanges, from + ":" + to) < 0 &&
                    Arrays.binarySearch(ignoreChanges, "*:" + to) < 0 &&
                    Arrays.binarySearch(ignoreChanges, from + ":*") < 0;
        }
        return false;
    }



    public StatusChangeNotifier(InstanceRepository repository) {
        super(repository);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceStatusChangedEvent) {
                log.info("Instance 【{}】 ({}) is {}", instance.getRegistration().getName(), event.getInstance(),
                        ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus());
            } else {
                log.info("Instance 【{}】 ({}) {}", instance.getRegistration().getName(), event.getInstance(),
                        event.getType());
            }
        });
    }

}

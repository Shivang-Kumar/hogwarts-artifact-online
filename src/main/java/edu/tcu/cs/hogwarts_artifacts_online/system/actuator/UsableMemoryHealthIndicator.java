package edu.tcu.cs.hogwarts_artifacts_online.system.actuator;

import java.io.File;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
public class UsableMemoryHealthIndicator implements HealthIndicator{

	@Override
	public Health health() {
		File path=new File(".");
		long diskUsableSpace=path.getUsableSpace();
		boolean isHealth=diskUsableSpace>=10*1024*1024;
		Status status=isHealth ? Status.UP :Status.DOWN; //Up means there is enough memory
		//Along with status we can add aditions details
		return Health.status(status).withDetail("usable memory", diskUsableSpace).withDetail("threshhold", 10*1024*1024).build();
	}

}

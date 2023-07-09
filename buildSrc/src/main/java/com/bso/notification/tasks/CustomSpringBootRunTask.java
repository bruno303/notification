package com.bso.notification.tasks;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.options.Option;
import org.gradle.work.DisableCachingByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@DisableCachingByDefault(because = "Application should always run")
public abstract class CustomSpringBootRunTask extends JavaExec {
    private final Logger LOGGER = LoggerFactory.getLogger(CustomSpringBootRunTask.class);
    private String customJvmArgs;

    public CustomSpringBootRunTask() {}

    @Input
    public String getCustomJvmArgs() {
        return customJvmArgs;
    }

    @Option(option = "customJvmArgs", description="Command line jvmArgs passed to JVM.")
    public void setCustomJvmArgs(String customJvmArgs) {
        this.customJvmArgs = customJvmArgs;
    }

    @Override
    public void exec() {
        if (!customJvmArgs.equals("")) {
            LOGGER.info("Setting custom jvm arguments on running application");
            setJvmArgs(Arrays.stream(customJvmArgs.split(" ")).toList());
        }
        if (System.console() != null) {
            getEnvironment().put("spring.output.ansi.console-available", true);
        }
        super.exec();
    }

}

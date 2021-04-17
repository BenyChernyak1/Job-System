import job.BaseJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import utils.Constants;

import java.util.List;

public class JobsManager {

    private Scheduler scheduler;

    public JobsManager() {
        StdSchedulerFactory factory = new StdSchedulerFactory();
        try {
            scheduler = factory.getScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void scheduleJob(Class<? extends BaseJob> jobClass, String jobName, String jobGroup,
                            String triggerName, String triggerGroup, int periodicExecutionInHours) {

        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    if(jobKey.getName().equalsIgnoreCase(jobName) && jobKey.getGroup().equalsIgnoreCase(groupName)) {
                        throw new IllegalArgumentException("Job should be unique!");
                    }
                }
            }

            JobDetail job = JobBuilder.newJob(jobClass)
                    .withIdentity(jobName, jobGroup)
                    .build();

            Trigger trigger;
            if (periodicExecutionInHours > 1) {
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerName, triggerGroup)
                        .startNow()
                        .withSchedule(
                                SimpleScheduleBuilder.simpleSchedule()
                                        .withIntervalInHours(periodicExecutionInHours)
                                        .repeatForever())
                        .build();
            } else {
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerName, triggerGroup)
                        .startNow()
                        .withSchedule(
                                SimpleScheduleBuilder.simpleSchedule())
                        .build();
            }
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void scheduleJob(Class<? extends BaseJob> jobClass, String jobName, String jobGroup,
                            int periodicExecutionInHours) {
        this.scheduleJob(jobClass, jobName, jobGroup, Constants.DEFAULT_TRIGGER_NAME, Constants.DEFAULT_TRIGGER_GROUP, periodicExecutionInHours);
    }

    public void scheduleJob(Class<? extends BaseJob> jobClass, String jobName, String jobGroup) {
        this.scheduleJob(jobClass, jobName, jobGroup, Constants.DEFAULT_TRIGGER_NAME, Constants.DEFAULT_TRIGGER_GROUP, 1);
    }

    public void scheduleJob(Class<? extends BaseJob> jobClass, int periodicExecutionInHours) {
        this.scheduleJob(jobClass, Constants.DEFAULT_TRIGGER_NAME, Constants.DEFAULT_JOB_GROUP,
                Constants.DEFAULT_TRIGGER_NAME, Constants.DEFAULT_TRIGGER_GROUP, periodicExecutionInHours);
    }

    public void scheduleJob(Class<? extends BaseJob> jobClass) {
        this.scheduleJob(jobClass, Constants.DEFAULT_TRIGGER_NAME, Constants.DEFAULT_JOB_GROUP,
                Constants.DEFAULT_TRIGGER_NAME, Constants.DEFAULT_TRIGGER_GROUP, 1);
    }

    public void cancelJob(String jobName, String jobGroup) {
        try {
            scheduler.getCurrentlyExecutingJobs()
                    .stream()
                    .filter(jobExecutionContext -> jobExecutionContext.getJobDetail().getKey().getName().equalsIgnoreCase(jobName) &&
                            jobExecutionContext.getJobDetail().getKey().getGroup().equalsIgnoreCase(jobGroup)).forEach(jobExecutionContext -> {
                try {
                    scheduler.interrupt(jobExecutionContext.getJobDetail().getKey());
                } catch (UnableToInterruptJobException e) {
                    e.printStackTrace();
                }
            });

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void cancelJob(String jobName) {
        this.cancelJob(jobName, Constants.DEFAULT_JOB_GROUP);
    }

    public void getIntrospection() throws SchedulerException {
        scheduler.getCurrentlyExecutingJobs().parallelStream().forEach(jobExecutionContext -> {
            System.out.println("Job name: " + jobExecutionContext.getJobDetail().getKey().getName() +
                    ", Job Group: " + jobExecutionContext.getJobDetail().getKey().getGroup());
        });
    }

}

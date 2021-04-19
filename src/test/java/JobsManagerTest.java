import job.BaseJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.matchers.GroupMatcher;
import utils.Constants;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class JobsManagerTest {

    static Runnable task = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
        }
    };

    static class TestJob extends BaseJob {

        @Override
        public void interrupt() throws UnableToInterruptJobException {

        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            task.run();
        }
    }

    @Spy
    private JobsManager jobsManager;

    @BeforeEach
    void before() throws SchedulerException {

    }

    @Test
    void testCreateJob() throws SchedulerException {
        jobsManager.scheduleJob(TestJob.class, Constants.DEFAULT_JOB_NAME, Constants.DEFAULT_JOB_GROUP);

        assertEquals(1, (long) jobsManager.getScheduler().getJobKeys(GroupMatcher.jobGroupEquals(Constants.DEFAULT_JOB_GROUP)).size());
    }
}

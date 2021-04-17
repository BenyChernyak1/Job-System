package job;

import org.quartz.InterruptableJob;

import java.util.UUID;

public abstract class BaseJob implements InterruptableJob {

    private JobStatus status;

    public BaseJob() {
        this.status = JobStatus.CREATED;
    }

    public BaseJob(UUID uuid) {
        this.status = JobStatus.CREATED;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BaseJob{" +
                ", status=" + status +
                '}';
    }
}

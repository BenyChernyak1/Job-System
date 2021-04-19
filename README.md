# Job System
Implementation of basic job system that can schedule and execute jobs of different types.

## User Story

As a customer, I'd like to create different jobs definitions and run asynchronously.

## Prerequisite
* Java 8
* Gradle 6.8.3

## System Explanation

### Jobs Definition
Custom jobs can be created, see the [BaseJob](src/main/java/job/BaseJob.java) class.
Each job has a lifecycle that can be managed by [JobStatus](src/main/java/job/JobStatus.java) enum.

### Scheduling
Jobs can be schedule for periodic execution if `periodicExecutionInHours` parameter of function `JobsManager.scheduleJob(...)` is set > 0.

### Jobs Limit
A limit of amount of jobs that run concurrently at given moment is set by parameter `org.quartz.threadPool.threadCount` in `quartz.properties` file.

### Job Uniqueness
Parameters `jobName` and `jobGroup` defines a uniqueness of job.

### Job Cancellation
Mechanism for canceling jobs is defined in `JobsManager.cancelJob(...)` method.

### Job Introspection
`JobsManager.getIntrospection()` fetch current jobs statistics.
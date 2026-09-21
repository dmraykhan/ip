package twizzy;

import java.time.LocalDate;

/**
 * Represents a task and whether it has been completed.
 */
public class Task {
    protected String description;

    protected boolean isDone;

    private LocalDate snoozedUntil;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the icon used to display the task's completion status.
     *
     * @return {@code X} if completed, or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns this task's description for persistence.
     *
     * @return task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Reports whether this task is completed.
     *
     * @return true when the task is completed
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the date on which this task becomes active again, if it has been snoozed.
     *
     * @return snooze end date, or {@code null} when the task has not been snoozed
     */
    public LocalDate getSnoozedUntil() {
        return snoozedUntil;
    }

    /**
     * Reports whether the task remains deferred on the supplied date.
     *
     * @param date date on which to check the snooze status
     * @return true when the task should remain hidden on that date
     */
    public boolean isSnoozedOn(LocalDate date) {
        return snoozedUntil != null && snoozedUntil.isAfter(date);
    }

    /**
     * Defers this task until the given date.
     *
     * @param until first date on which the task should become active again
     */
    public void snoozeUntil(LocalDate until) {
        snoozedUntil = until;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not completed. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Formats this task with its status icon and description.
     *
     * @return display form of this task
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}

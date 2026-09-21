package twizzy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Manages the collection of tasks used by Twizzy. */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates a task list containing the supplied initial tasks. */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Reports whether a task with the same type, description, and dates already exists.
     *
     * @param task task to check before adding
     * @return true when the task would duplicate an existing task
     */
    public boolean containsDuplicate(Task task) {
        return tasks.stream().anyMatch(existingTask -> !existingTask.isDone()
                && existingTask.hasSameDetailsAs(task));
    }

    /** Returns the task at a zero-based index. */
    public Task get(int index) {
        assertValidIndex(index);
        return tasks.get(index);
    }

    /**
     * Returns an active task using its zero-based position among active tasks.
     *
     * @param index zero-based active-task index
     * @param date date used to determine which tasks are active
     * @return selected active task
     */
    public Task getActive(int index, LocalDate date) {
        List<Task> activeTasks = getActiveTasks(date);
        assert index >= 0 && index < activeTasks.size() : "Task index must refer to an active task.";
        return activeTasks.get(index);
    }

    /** Removes and returns the task at a zero-based index. */
    public Task remove(int index) {
        assertValidIndex(index);
        return tasks.remove(index);
    }

    /**
     * Removes and returns an active task using its zero-based active-task position.
     *
     * @param index zero-based active-task index
     * @param date date used to determine which tasks are active
     * @return removed active task
     */
    public Task removeActive(int index, LocalDate date) {
        Task task = getActive(index, date);
        tasks.remove(task);
        return task;
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }

    /** Returns a read-only task view for persistence. */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }

    /**
     * Returns tasks that are not deferred beyond the supplied date.
     *
     * @param date date used to determine which tasks are active
     * @return active tasks in their original order
     */
    public List<Task> getActiveTasks(LocalDate date) {
        return tasks.stream()
                .filter(task -> !task.isSnoozedOn(date))
                .toList();
    }

    /**
     * Returns tasks that remain deferred beyond the supplied date.
     *
     * @param date date used to determine which tasks are snoozed
     * @return snoozed tasks in their original order
     */
    public List<Task> getSnoozedTasks(LocalDate date) {
        return tasks.stream()
                .filter(task -> task.isSnoozedOn(date))
                .toList();
    }

    /**
     * Returns tasks whose descriptions contain the keyword, ignoring letter case.
     *
     * @param keyword text to search for
     * @return matching tasks in their original order
     */
    public List<Task> findTasks(String keyword) {
        return findMatchingTasks(keyword, tasks);
    }

    /**
     * Returns active tasks whose descriptions contain the keyword, ignoring letter case.
     *
     * @param keyword text to search for
     * @param date date used to determine which tasks are active
     * @return matching active tasks in their original order
     */
    public List<Task> findActiveTasks(String keyword, LocalDate date) {
        return findMatchingTasks(keyword, getActiveTasks(date));
    }

    private List<Task> findMatchingTasks(String keyword, List<Task> searchedTasks) {
        String lowercaseKeyword = keyword.toLowerCase(Locale.ROOT);
        return searchedTasks.stream()
                .filter(task -> task.getDescription().toLowerCase(Locale.ROOT).contains(lowercaseKeyword))
                .toList();
    }

    /**
     * Confirms that an internal caller has already validated a task index.
     *
     * @param index zero-based task index to verify
     */
    private void assertValidIndex(int index) {
        assert index >= 0 && index < tasks.size() : "Task index must refer to an existing task.";
    }
}

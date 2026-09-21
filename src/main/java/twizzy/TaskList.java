package twizzy;

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

    /** Returns the task at a zero-based index. */
    public Task get(int index) {
        assertValidIndex(index);
        return tasks.get(index);
    }

    /** Removes and returns the task at a zero-based index. */
    public Task remove(int index) {
        assertValidIndex(index);
        return tasks.remove(index);
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
     * Returns tasks whose descriptions contain the keyword, ignoring letter case.
     *
     * @param keyword text to search for
     * @return matching tasks in their original order
     */
    public List<Task> findTasks(String keyword) {
        String lowercaseKeyword = keyword.toLowerCase(Locale.ROOT);
        return tasks.stream()
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

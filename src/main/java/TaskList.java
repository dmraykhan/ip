import java.util.ArrayList;
import java.util.List;

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
        return tasks.get(index);
    }

    /** Removes and returns the task at a zero-based index. */
    public Task remove(int index) {
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
}

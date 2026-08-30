/**
 * Represents a task without an attached date or time.
 */
public class Todo extends Task {
    /**
     * Creates an incomplete todo task.
     *
     * @param description description of the task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Formats this task with the todo type icon.
     *
     * @return display form of this todo
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

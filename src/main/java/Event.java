/**
 * Represents a task that takes place between specified start and end times.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an incomplete event task.
     *
     * @param description description of the task
     * @param from start text supplied by the user
     * @param to end text supplied by the user
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the saved event start text.
     *
     * @return start text supplied by the user
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the saved event end text.
     *
     * @return end text supplied by the user
     */
    public String getTo() {
        return to;
    }

    /**
     * Formats this task with its start and end details.
     *
     * @return display form of this event
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}

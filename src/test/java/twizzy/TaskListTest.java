package twizzy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/** Tests task-list operations. */
class TaskListTest {
    @Test
    void findTasks_mixedCaseKeyword_returnsMatchingTasksInOriginalOrder() {
        TaskList tasks = new TaskList(List.of(new Todo("read book"), new Todo("return Book"),
                new Todo("join club")));

        List<String> matchingDescriptions = tasks.findTasks("bOoK")
                .stream()
                .map(Task::getDescription)
                .toList();

        assertEquals(List.of("read book", "return Book"), matchingDescriptions);
    }
}

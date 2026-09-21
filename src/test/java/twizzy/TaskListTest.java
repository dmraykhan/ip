package twizzy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
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

    @Test
    void getActiveTasks_snoozedTask_excludesItUntilItsReturnDate() {
        Todo activeTask = new Todo("active task");
        Todo snoozedTask = new Todo("snoozed task");
        snoozedTask.snoozeUntil(LocalDate.of(2099, 12, 31));
        TaskList tasks = new TaskList(List.of(activeTask, snoozedTask));

        assertEquals(List.of(activeTask), tasks.getActiveTasks(LocalDate.of(2099, 12, 30)));
        assertEquals(List.of(snoozedTask), tasks.getSnoozedTasks(LocalDate.of(2099, 12, 30)));
        assertEquals(List.of(activeTask, snoozedTask), tasks.getActiveTasks(LocalDate.of(2099, 12, 31)));
    }
}

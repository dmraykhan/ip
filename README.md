# Twizzy

> Your task-list twin: quick with the reminders, light on the lectures.

Twizzy is a playful command-line task manager built for the NUS CS2103T individual project. It tracks todos, deadlines, and events without turning productivity into a corporate meeting.

## What it does

- Adds todos, deadlines, and events
- Lists everything in one numbered view
- Marks tasks as done or brings them back
- Deletes tasks that no longer deserve screen time
- Snoozes tasks and returns them to the active list on demand
- Saves automatically to `data/twizzy.txt`
- Reloads tasks the next time it starts
- Validates commands and calendar dates with useful error messages

## Quick start

Twizzy requires **JDK 25**.

1. Clone this repository.
2. Open the project root in IntelliJ IDEA as a **Gradle** project. If prompted, choose **Load Gradle Changes**.
3. Set the project SDK and language level to JDK 25.
4. Run the Gradle `application > run` task to open the GUI. Alternatively, mark `src/main/resources` as a **Resources Root** before running `twizzy.gui.Launcher.main()` directly.
5. Run `Twizzy.main()` only when you want the console interface.

You should see:

```text
____________________________________________________________
 _______        _
|__   __|      (_)
   | |_      ___ __________   _
   | \ \ /\ / / |_  /_  / | | |
   | |\ V  V /| |/ / / /| |_| |
   |_| \_/\_/ |_/___/___| \__, |
                           __/ |
                          |___/
Wsg, I'm Twizzy — your task-list twin.
Type help to see what I can do.
____________________________________________________________
```

## Commands

| Action | Command | Example |
| --- | --- | --- |
| Add a todo | `todo <description>` | `todo finish the README` |
| Add a deadline | `deadline <description> /by <yyyy-MM-dd>` | `deadline submit report /by 2026-09-18` |
| Add an event | `event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>` | `event recess week /from 2026-09-21 /to 2026-09-27` |
| Show tasks | `list` | `list` |
| Show command help | `help` | `help` |
| Complete a task | `mark <number>` | `mark 2` |
| Reopen a task | `unmark <number>` | `unmark 2` |
| Delete a task | `delete <number>` | `delete 1` |
| Snooze a task | `snooze <number> /until <yyyy-MM-dd>` | `snooze 2 /until 2099-12-31` |
| Show snoozed tasks | `list snoozed` | `list snoozed` |
| Return a snoozed task | `unsnooze <number>` | `unsnooze 1` |
| Exit | `bye` | `bye` |

Dates use the unambiguous `yyyy-MM-dd` format. Twizzy displays them in a friendlier form such as `Sep 18 2026`.

## Example

```text
todo survive Monday
deadline submit report /by 2026-09-18
mark 1
list
```

```text
Your active tasks, gang:
1.[T][X] survive Monday
2.[D][ ] submit report (by: Sep 18 2026)
```

For complete usage and error-handling details, see the [Twizzy User Guide](docs/README.md).

## Data and privacy

Twizzy stores tasks locally in `data/twizzy.txt`. The `data/` directory is ignored by Git, so personal tasks are not committed to the repository by default.

## Testing

The exact console regression suite is documented in `test/ui-test-plan.md`. It covers normal commands, invalid input, date boundaries, task mutations, and persistence across restarts.

## Project structure

```text
src/main/java/twizzy/
├── Twizzy.java       Coordinates the application
├── Ui.java           Handles console input and output
├── Parser.java       Validates and interprets commands
├── TaskList.java     Manages the task collection
├── Storage.java      Loads and saves tasks
└── Task classes      Represent todos, deadlines, and events
```

Built with Java 25 for CS2103T.

# Twizzy

> Your task-list twin: quick with the reminders, light on the lectures.

Twizzy is a playful command-line task manager built for the NUS CS2103T individual project. It tracks todos, deadlines, and events without turning productivity into a corporate meeting.

## What it does

- Adds todos, deadlines, and events
- Lists everything in one numbered view
- Marks tasks as done or brings them back
- Deletes tasks that no longer deserve screen time
- Saves automatically to `data/twizzy.txt`
- Reloads tasks the next time it starts
- Validates commands and calendar dates with useful error messages

## Quick start

Twizzy requires **JDK 25**.

1. Clone this repository.
2. Open the project in IntelliJ IDEA.
3. Set the project SDK and language level to JDK 25.
4. Open `src/main/java/Twizzy.java`.
5. Run `Twizzy.main()`.

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
Yo, I'm Twizzy — your task-list twin.
Drop a command. I'll keep the chaos organized.
____________________________________________________________
```

## Commands

| Action | Command | Example |
| --- | --- | --- |
| Add a todo | `todo <description>` | `todo finish the README` |
| Add a deadline | `deadline <description> /by <yyyy-MM-dd>` | `deadline submit report /by 2026-09-18` |
| Add an event | `event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>` | `event recess week /from 2026-09-21 /to 2026-09-27` |
| Show tasks | `list` | `list` |
| Complete a task | `mark <number>` | `mark 2` |
| Reopen a task | `unmark <number>` | `unmark 2` |
| Delete a task | `delete <number>` | `delete 1` |
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
Here's the current chaos:
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
src/main/java/
├── Twizzy.java       Coordinates the application
├── Ui.java           Handles console input and output
├── Parser.java       Validates and interprets commands
├── TaskList.java     Manages the task collection
├── Storage.java      Loads and saves tasks
└── Task classes      Represent todos, deadlines, and events
```

Built with Java 25 for CS2103T.

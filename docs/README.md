# Twizzy User Guide

Twizzy is your task-list twin: it remembers the boring stuff so you do not have to keep it all in your head.

## Command format

- Type one command per line.
- Task numbers come from the latest active `list` output.
- Dates must use `yyyy-MM-dd`, including leading zeroes.
- Extra spaces around command details are ignored.

## Show command help

Use `help` to display the supported commands and their required markers. This is the fastest way to check the syntax for deadlines, events, and snoozing while using the GUI.

## Add a todo

Use `todo <description>` for a task without a date.

```text
todo read chapter 3
```

```text
Locked in, gang. I added:
  [T][ ] read chapter 3
You're juggling 1 task now, twin.
```

## Add a deadline

Use `deadline <description> /by <yyyy-MM-dd>`.

```text
deadline submit report /by 2026-09-18
```

```text
Locked in, gang. I added:
  [D][ ] submit report (by: Sep 18 2026)
You're juggling 2 tasks now, twin.
```

## Add an event

Use `event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>`.

```text
event hackathon /from 2026-09-21 /to 2026-09-22
```

```text
Locked in, gang. I added:
  [E][ ] hackathon (from: Sep 21 2026 to: Sep 22 2026)
You're juggling 3 tasks now, twin.
```

## List tasks

Use `list` to see the current task numbers and statuses.

```text
Your active tasks, gang:
1.[T][ ] read chapter 3
2.[D][ ] submit report (by: Sep 18 2026)
3.[E][ ] hackathon (from: Sep 21 2026 to: Sep 22 2026)
```

`[ ]` means pending and `[X]` means completed. The letters `T`, `D`, and `E` identify todos, deadlines, and events.

## Mark or unmark a task

Use `mark <number>` when a task is done:

```text
mark 2
```

```text
Marked done, twin:
  [D][X] submit report (by: Sep 18 2026)
```

Use `unmark <number>` if the task returns for a sequel:

```text
unmark 2
```

```text
Marked pending, gang:
  [D][ ] submit report (by: Sep 18 2026)
```

## Snooze a task

Use `snooze <number> /until <yyyy-MM-dd>` to postpone an active task until a future date. Snoozed tasks are hidden from normal `list` and `find` results until that date, so remaining active task numbers stay usable with `mark`, `unmark`, and `delete`.

```text
snooze 2 /until 2099-12-31
```

```text
Snoozed, gang:
  [D][ ] submit report (by: Sep 18 2026) (snoozed until: Dec 31 2099)
```

Use `list snoozed` to inspect deferred tasks and their return dates.

## Delete a task

Use `delete <number>` to remove a task. Remaining tasks are renumbered automatically.

```text
delete 1
```

```text
Deleted, broski:
  [T][ ] read chapter 3
You're juggling 2 tasks now, twin.
```

## Exit

Use `bye` to close Twizzy safely.

```text
Catch you later, broski. Don't ghost your tasks.
```

## Saved data

Twizzy saves after every successful add, mark, unmark, delete, or snooze command. On startup, it loads tasks from `data/twizzy.txt`. If the directory or file does not exist, Twizzy starts with an empty list and creates them on the first save. Existing data files remain compatible when snooze information is added. If the GUI cannot read a saved-data file, it shows an error and locks task-changing commands so the file cannot be overwritten accidentally.

## Invalid input

Twizzy rejects incomplete commands, unknown commands, invalid task numbers, impossible dates, snooze dates that are not in the future, and duplicate pending tasks without changing the task list. You can add a completed task again if it becomes relevant in the future.

```text
deadline time travel /by 2025-02-29
```

```text
Nah, you gotta lock in, gang. The deadline date must be a valid date in yyyy-MM-dd format.
```

# Twizzy User Guide

Twizzy is your task-list twin: it remembers the boring stuff so you do not have to keep it all in your head.

## Command format

- Type one command per line.
- Task numbers come from the latest `list` output.
- Dates must use `yyyy-MM-dd`, including leading zeroes.
- Extra spaces around command details are ignored.

## Add a todo

Use `todo <description>` for a task without a date.

```text
todo read chapter 3
```

```text
Locked in. I added:
  [T][ ] read chapter 3
You're juggling 1 task now.
```

## Add a deadline

Use `deadline <description> /by <yyyy-MM-dd>`.

```text
deadline submit report /by 2026-09-18
```

```text
Locked in. I added:
  [D][ ] submit report (by: Sep 18 2026)
You're juggling 2 tasks now.
```

## Add an event

Use `event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>`.

```text
event hackathon /from 2026-09-21 /to 2026-09-22
```

```text
Locked in. I added:
  [E][ ] hackathon (from: Sep 21 2026 to: Sep 22 2026)
You're juggling 3 tasks now.
```

## List tasks

Use `list` to see the current task numbers and statuses.

```text
Here's the current chaos:
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

Use `unmark <number>` if the task returns for a sequel:

```text
unmark 2
```

## Delete a task

Use `delete <number>` to remove a task. Remaining tasks are renumbered automatically.

```text
delete 1
```

## Exit

Use `bye` to close Twizzy safely.

```text
I'm out. Your tasks aren't — don't ghost them.
```

## Saved data

Twizzy saves after every successful add, mark, unmark, or delete command. On startup, it loads tasks from `data/twizzy.txt`. If the directory or file does not exist, Twizzy starts with an empty list and creates them on the first save.

## Invalid input

Twizzy rejects incomplete commands, unknown commands, invalid task numbers, and impossible dates without changing the task list.

```text
deadline time travel /by 2025-02-29
```

```text
Yeah, no. The deadline date must be a valid date in yyyy-MM-dd format.
```

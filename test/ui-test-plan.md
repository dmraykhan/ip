# Twizzy UI Test Plan

Run these cases with the project-specific `$test-ui` skill. Each case starts a fresh Twizzy process, uses Java 25, and compares standard output exactly. `{{STARTUP}}` expands to the banner and greeting, while `{{DIVIDER}}` expands to the 60-character separator.

Within a case, `{{RESTART}}` stops Twizzy and starts it again in the same temporary directory so saved data can be checked.

For GUI-only visual behavior, manually verify that a rejected command begins with “Nah, you gotta lock in, gang.” in a red, bordered response bubble and that widening the window also widens long message bubbles. The console cases below continue to verify the corresponding command responses.

## TC1: Exit the chatbot

**Aim:** Verify that `bye` prints the farewell and terminates normally.

**Inputs:**

```text
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

## TC12: Find matching tasks

**Aim:** Verify that find returns case-insensitive description matches in task-list order and rejects a missing keyword.

**Inputs:**

```text
todo read book
deadline return BOOK /by 2026-09-20
event club meeting /from 2026-09-21 /to 2026-09-22
find book
find
find absent
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] read book
You're juggling 1 task now, twin.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [D][ ] return BOOK (by: Sep 20 2026)
You're juggling 2 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [E][ ] club meeting (from: Sep 21 2026 to: Sep 22 2026)
You're juggling 3 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Here are the matching tasks in your list:
1.[T][ ] read book
2.[D][ ] return BOOK (by: Sep 20 2026)
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. Please provide a keyword. Try: find <keyword>
{{DIVIDER}}
{{DIVIDER}}
Here are the matching tasks in your list:
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

## TC2: Add and list tasks

**Aim:** Verify that task text is stored and listed in insertion order with pending status icons.

**Inputs:**

```text
todo read book
todo return book
list
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] read book
You're juggling 1 task now, twin.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] return book
You're juggling 2 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Your active tasks, gang:
1.[T][ ] read book
2.[T][ ] return book
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

## TC3: Mark and unmark a task

**Aim:** Verify that marking and unmarking update the selected task and persist in subsequent lists.

**Inputs:**

```text
todo read book
todo return book
mark 2
list
unmark 2
list
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] read book
You're juggling 1 task now, twin.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] return book
You're juggling 2 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Marked done, twin:
  [T][X] return book
{{DIVIDER}}
{{DIVIDER}}
Your active tasks, gang:
1.[T][ ] read book
2.[T][X] return book
{{DIVIDER}}
{{DIVIDER}}
Marked pending, gang:
  [T][ ] return book
{{DIVIDER}}
{{DIVIDER}}
Your active tasks, gang:
1.[T][ ] read book
2.[T][ ] return book
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

## TC4: Add deadlines and events

**Aim:** Verify valid deadline and event dates are parsed and displayed in a friendlier format.

**Inputs:**

```text
deadline do homework /by 2026-09-18
event project meeting /from 2026-09-21 /to 2026-09-22
list
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Locked in, gang. I added:
  [D][ ] do homework (by: Sep 18 2026)
You're juggling 1 task now, twin.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [E][ ] project meeting (from: Sep 21 2026 to: Sep 22 2026)
You're juggling 2 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Your active tasks, gang:
1.[D][ ] do homework (by: Sep 18 2026)
2.[E][ ] project meeting (from: Sep 21 2026 to: Sep 22 2026)
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

## TC5: Reject an empty todo and unknown command

**Aim:** Verify that invalid commands show helpful errors and do not change tasks added before or after them.

**Inputs:**

```text
todo first task
todo
blah
todo second task
list
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] first task
You're juggling 1 task now, twin.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. A todo needs a description. Try: todo <description>
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. I don't recognize that command. Try todo, deadline, event, find, list, mark, unmark, delete, snooze, or bye.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] second task
You're juggling 2 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Your active tasks, gang:
1.[T][ ] first task
2.[T][ ] second task
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

## TC6: Reject malformed deadlines and events

**Aim:** Verify specific errors for every missing deadline or event field and confirm later valid timed tasks are stored correctly.

**Inputs:**

```text
deadline
deadline /by Sunday
deadline report /by
deadline report /bypass
event
event /from Mon /to Tue
event meeting /from Mon
event meeting /from /to Tue
event meeting /from Mon /to
event lunch /fromage /toffee
event lunch /from Mon /today
deadline impossible /by 2025-02-29
event meeting /from Monday /to 2026-09-22
event meeting /from 2026-09-21 /to tomorrow
deadline report /by 2024-02-29
event meeting /from 2026-09-21 /to 2026-09-22
list
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Nah, you gotta lock in, gang. A deadline needs /by followed by a date.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. A deadline needs a description. Try: deadline <description> /by <date>
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. The deadline date cannot be empty after /by.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. A deadline needs /by followed by a date.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. An event needs /from followed by a start date.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. An event needs a description. Try: event <description> /from <start-date> /to <end-date>
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. An event needs /to followed by an end date.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. The event start date cannot be empty after /from.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. The event end date cannot be empty after /to.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. An event needs /from followed by a start date.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. An event needs /to followed by an end date.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. The deadline date must be a valid date in yyyy-MM-dd format.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. The event start date must be a valid date in yyyy-MM-dd format.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. The event end date must be a valid date in yyyy-MM-dd format.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [D][ ] report (by: Feb 29 2024)
You're juggling 1 task now, twin.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [E][ ] meeting (from: Sep 21 2026 to: Sep 22 2026)
You're juggling 2 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Your active tasks, gang:
1.[D][ ] report (by: Feb 29 2024)
2.[E][ ] meeting (from: Sep 21 2026 to: Sep 22 2026)
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

## TC7: Reject invalid task selections

**Aim:** Verify empty input and invalid mark or unmark numbers are handled without changing valid task state.

**Inputs:**

```text

mark
unmark 1
todo stable task
mark two
mark 0
mark 2
unmark
unmark 1 extra
unmark 2
mark 1
list
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Nah, you gotta lock in, gang. Please enter a command.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. Please provide a task number. Try: mark <number>
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. There are no tasks to unmark.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] stable task
You're juggling 1 task now, twin.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. The task number must be a whole number.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. Please provide a task number. Try: unmark <number>
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. The task number must be a whole number.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
Marked done, twin:
  [T][X] stable task
{{DIVIDER}}
{{DIVIDER}}
Your active tasks, gang:
1.[T][X] stable task
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

## TC8: Delete and renumber tasks

**Aim:** Verify deletion of middle, first, and last tasks, including the removed-task message, count, status, and list renumbering.

**Inputs:**

```text
todo read book
deadline return book /by 2026-09-20
event project meeting /from 2026-09-21 /to 2026-09-22
todo join club
mark 2
delete 2
list
delete 1
delete 2
list
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] read book
You're juggling 1 task now, twin.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [D][ ] return book (by: Sep 20 2026)
You're juggling 2 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [E][ ] project meeting (from: Sep 21 2026 to: Sep 22 2026)
You're juggling 3 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] join club
You're juggling 4 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Marked done, twin:
  [D][X] return book (by: Sep 20 2026)
{{DIVIDER}}
{{DIVIDER}}
Deleted, broski:
  [D][X] return book (by: Sep 20 2026)
You're juggling 3 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Your active tasks, gang:
1.[T][ ] read book
2.[E][ ] project meeting (from: Sep 21 2026 to: Sep 22 2026)
3.[T][ ] join club
{{DIVIDER}}
{{DIVIDER}}
Deleted, broski:
  [T][ ] read book
You're juggling 2 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Deleted, broski:
  [T][ ] join club
You're juggling 1 task now, twin.
{{DIVIDER}}
{{DIVIDER}}
Your active tasks, gang:
1.[E][ ] project meeting (from: Sep 21 2026 to: Sep 22 2026)
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

## TC9: Reject invalid deletions

**Aim:** Verify missing, malformed, and out-of-range delete numbers do not remove tasks and deletion from an empty list is handled.

**Inputs:**

```text
delete
delete 1
todo safe task
delete zero
delete 0
delete 2
delete 1 extra
list
delete 1
list
delete 1
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Nah, you gotta lock in, gang. Please provide a task number. Try: delete <number>
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. There are no tasks to delete.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] safe task
You're juggling 1 task now, twin.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. The task number must be a whole number.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. The task number must be a whole number.
{{DIVIDER}}
{{DIVIDER}}
Your active tasks, gang:
1.[T][ ] safe task
{{DIVIDER}}
{{DIVIDER}}
Deleted, broski:
  [T][ ] safe task
You're juggling 0 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Your active tasks, gang:
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. There are no tasks to delete.
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

## TC10: Reject command keyword prefixes

**Aim:** Verify command-like prefixes are not mistaken for supported enum command types and do not affect later valid input.

**Inputs:**

```text
todoing read book
listing
marking 1
deleting 1
list extra
bye now
todo valid task
list
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Nah, you gotta lock in, gang. I don't recognize that command. Try todo, deadline, event, find, list, mark, unmark, delete, snooze, or bye.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. I don't recognize that command. Try todo, deadline, event, find, list, mark, unmark, delete, snooze, or bye.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. I don't recognize that command. Try todo, deadline, event, find, list, mark, unmark, delete, snooze, or bye.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. I don't recognize that command. Try todo, deadline, event, find, list, mark, unmark, delete, snooze, or bye.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. I don't recognize that command. Try todo, deadline, event, find, list, mark, unmark, delete, snooze, or bye.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. I don't recognize that command. Try todo, deadline, event, find, list, mark, unmark, delete, snooze, or bye.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] valid task
You're juggling 1 task now, twin.
{{DIVIDER}}
{{DIVIDER}}
Your active tasks, gang:
1.[T][ ] valid task
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

## TC11: Save and reload tasks

**Aim:** Verify todos, deadlines, events, and completion state survive a restart, and later changes remain persistent.

**Inputs:**

```text
todo read A | B
deadline submit work /by 2026-09-18
event consultation /from 2026-09-21 /to 2026-09-22
mark 2
bye
{{RESTART}}
list
delete 1
unmark 1
bye
{{RESTART}}
list
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] read A | B
You're juggling 1 task now, twin.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [D][ ] submit work (by: Sep 18 2026)
You're juggling 2 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [E][ ] consultation (from: Sep 21 2026 to: Sep 22 2026)
You're juggling 3 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Marked done, twin:
  [D][X] submit work (by: Sep 18 2026)
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
{{STARTUP}}
{{DIVIDER}}
Your active tasks, gang:
1.[T][ ] read A | B
2.[D][X] submit work (by: Sep 18 2026)
3.[E][ ] consultation (from: Sep 21 2026 to: Sep 22 2026)
{{DIVIDER}}
{{DIVIDER}}
Deleted, broski:
  [T][ ] read A | B
You're juggling 2 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Marked pending, gang:
  [D][ ] submit work (by: Sep 18 2026)
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
{{STARTUP}}
{{DIVIDER}}
Your active tasks, gang:
1.[D][ ] submit work (by: Sep 18 2026)
2.[E][ ] consultation (from: Sep 21 2026 to: Sep 22 2026)
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

## TC13: Snooze tasks until a future date

**Aim:** Verify snoozed tasks are hidden from active commands, remain viewable separately, reject invalid dates, and persist across restarts.

**Inputs:**

```text
todo active task
todo postponed task
snooze 2 /until 2099-12-31
list
list snoozed
find postponed
mark 2
snooze 1 /until
snooze 1 /until 2020-01-01
{{RESTART}}
list
list snoozed
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] active task
You're juggling 1 task now, twin.
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] postponed task
You're juggling 2 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Snoozed, gang:
  [T][ ] postponed task (snoozed until: Dec 31 2099)
{{DIVIDER}}
{{DIVIDER}}
Your active tasks, gang:
1.[T][ ] active task
{{DIVIDER}}
{{DIVIDER}}
Here are the snoozed tasks:
1.[T][ ] postponed task (snoozed until: Dec 31 2099)
{{DIVIDER}}
{{DIVIDER}}
Here are the matching tasks in your list:
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. The snooze date cannot be empty after /until.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. The snooze date must be after today.
{{DIVIDER}}
{{STARTUP}}
{{DIVIDER}}
Your active tasks, gang:
1.[T][ ] active task
{{DIVIDER}}
{{DIVIDER}}
Here are the snoozed tasks:
1.[T][ ] postponed task (snoozed until: Dec 31 2099)
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

## TC14: Show command help

**Aim:** Verify that a new user can display the supported command syntax from the application.

**Inputs:**

```text
help
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Commands:
  todo <description>
  deadline <description> /by <yyyy-MM-dd>
  event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>
  list | list snoozed | find <keyword>
  mark <number> | unmark <number> | delete <number>
  snooze <number> /until <yyyy-MM-dd>
  bye
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

## TC15: Reject repeated task status changes and invalid event ranges

**Aim:** Verify that repeated mark or unmark commands explain that the task already has that status, and events cannot end on or before their start date.

**Inputs:**

```text
todo prepare slides
mark 1
mark 1
unmark 1
unmark 1
event workshop /from 2026-09-22 /to 2026-09-22
event retrospective /from 2026-09-22 /to 2026-09-21
list
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] prepare slides
You're juggling 1 task now, twin.
{{DIVIDER}}
{{DIVIDER}}
Marked done, twin:
  [T][X] prepare slides
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. That task is already marked as done.
{{DIVIDER}}
{{DIVIDER}}
Marked pending, gang:
  [T][ ] prepare slides
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. That task is already marked as not done.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. An event must end after it starts.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. An event must end after it starts.
{{DIVIDER}}
{{DIVIDER}}
Your active tasks, gang:
1.[T][ ] prepare slides
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

## TC16: Reject pending duplicates but allow completed tasks to be re-added

**Aim:** Verify that a pending duplicate is rejected but a completed task can be added again for future work.

**Inputs:**

```text
todo prepare slides
todo PREPARE SLIDES
mark 1
todo PREPARE SLIDES
list
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] prepare slides
You're juggling 1 task now, twin.
{{DIVIDER}}
{{DIVIDER}}
Nah, you gotta lock in, gang. That task is already on your list, gang. Try editing the existing one.
{{DIVIDER}}
{{DIVIDER}}
Marked done, twin:
  [T][X] prepare slides
{{DIVIDER}}
{{DIVIDER}}
Locked in, gang. I added:
  [T][ ] PREPARE SLIDES
You're juggling 2 tasks now, twin.
{{DIVIDER}}
{{DIVIDER}}
Your active tasks, gang:
1.[T][X] prepare slides
2.[T][ ] PREPARE SLIDES
{{DIVIDER}}
{{DIVIDER}}
Catch you later, broski. Don't ghost your tasks.
{{DIVIDER}}
```

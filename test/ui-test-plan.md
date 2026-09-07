# Twizzy UI Test Plan

Run these cases with the project-specific `$test-ui` skill. Each case starts a fresh Twizzy process, uses Java 25, and compares standard output exactly. `{{STARTUP}}` expands to the banner and greeting, while `{{DIVIDER}}` expands to the 60-character separator.

Within a case, `{{RESTART}}` stops Twizzy and starts it again in the same temporary directory so saved data can be checked.

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
I'm out. Your tasks aren't — don't ghost them.
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
Locked in. I added:
  [T][ ] read book
You're juggling 1 task now.
{{DIVIDER}}
{{DIVIDER}}
Locked in. I added:
  [T][ ] return book
You're juggling 2 tasks now.
{{DIVIDER}}
{{DIVIDER}}
Here's the current chaos:
1.[T][ ] read book
2.[T][ ] return book
{{DIVIDER}}
{{DIVIDER}}
I'm out. Your tasks aren't — don't ghost them.
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
Locked in. I added:
  [T][ ] read book
You're juggling 1 task now.
{{DIVIDER}}
{{DIVIDER}}
Locked in. I added:
  [T][ ] return book
You're juggling 2 tasks now.
{{DIVIDER}}
{{DIVIDER}}
Huge. One less thing haunting you:
  [T][X] return book
{{DIVIDER}}
{{DIVIDER}}
Here's the current chaos:
1.[T][ ] read book
2.[T][X] return book
{{DIVIDER}}
{{DIVIDER}}
Plot twist. This one's back:
  [T][ ] return book
{{DIVIDER}}
{{DIVIDER}}
Here's the current chaos:
1.[T][ ] read book
2.[T][ ] return book
{{DIVIDER}}
{{DIVIDER}}
I'm out. Your tasks aren't — don't ghost them.
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
Locked in. I added:
  [D][ ] do homework (by: Sep 18 2026)
You're juggling 1 task now.
{{DIVIDER}}
{{DIVIDER}}
Locked in. I added:
  [E][ ] project meeting (from: Sep 21 2026 to: Sep 22 2026)
You're juggling 2 tasks now.
{{DIVIDER}}
{{DIVIDER}}
Here's the current chaos:
1.[D][ ] do homework (by: Sep 18 2026)
2.[E][ ] project meeting (from: Sep 21 2026 to: Sep 22 2026)
{{DIVIDER}}
{{DIVIDER}}
I'm out. Your tasks aren't — don't ghost them.
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
Locked in. I added:
  [T][ ] first task
You're juggling 1 task now.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. A todo needs a description. Try: todo <description>
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
{{DIVIDER}}
{{DIVIDER}}
Locked in. I added:
  [T][ ] second task
You're juggling 2 tasks now.
{{DIVIDER}}
{{DIVIDER}}
Here's the current chaos:
1.[T][ ] first task
2.[T][ ] second task
{{DIVIDER}}
{{DIVIDER}}
I'm out. Your tasks aren't — don't ghost them.
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
Yeah, no. A deadline needs /by followed by a date.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. A deadline needs a description. Try: deadline <description> /by <date>
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. The deadline date cannot be empty after /by.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. A deadline needs /by followed by a date.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. An event needs /from followed by a start date.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. An event needs a description. Try: event <description> /from <start-date> /to <end-date>
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. An event needs /to followed by an end date.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. The event start date cannot be empty after /from.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. The event end date cannot be empty after /to.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. An event needs /from followed by a start date.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. An event needs /to followed by an end date.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. The deadline date must be a valid date in yyyy-MM-dd format.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. The event start date must be a valid date in yyyy-MM-dd format.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. The event end date must be a valid date in yyyy-MM-dd format.
{{DIVIDER}}
{{DIVIDER}}
Locked in. I added:
  [D][ ] report (by: Feb 29 2024)
You're juggling 1 task now.
{{DIVIDER}}
{{DIVIDER}}
Locked in. I added:
  [E][ ] meeting (from: Sep 21 2026 to: Sep 22 2026)
You're juggling 2 tasks now.
{{DIVIDER}}
{{DIVIDER}}
Here's the current chaos:
1.[D][ ] report (by: Feb 29 2024)
2.[E][ ] meeting (from: Sep 21 2026 to: Sep 22 2026)
{{DIVIDER}}
{{DIVIDER}}
I'm out. Your tasks aren't — don't ghost them.
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
Yeah, no. Please enter a command.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. Please provide a task number. Try: mark <number>
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. There are no tasks to unmark.
{{DIVIDER}}
{{DIVIDER}}
Locked in. I added:
  [T][ ] stable task
You're juggling 1 task now.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. The task number must be a whole number.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. Please provide a task number. Try: unmark <number>
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. The task number must be a whole number.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
Huge. One less thing haunting you:
  [T][X] stable task
{{DIVIDER}}
{{DIVIDER}}
Here's the current chaos:
1.[T][X] stable task
{{DIVIDER}}
{{DIVIDER}}
I'm out. Your tasks aren't — don't ghost them.
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
Locked in. I added:
  [T][ ] read book
You're juggling 1 task now.
{{DIVIDER}}
{{DIVIDER}}
Locked in. I added:
  [D][ ] return book (by: Sep 20 2026)
You're juggling 2 tasks now.
{{DIVIDER}}
{{DIVIDER}}
Locked in. I added:
  [E][ ] project meeting (from: Sep 21 2026 to: Sep 22 2026)
You're juggling 3 tasks now.
{{DIVIDER}}
{{DIVIDER}}
Locked in. I added:
  [T][ ] join club
You're juggling 4 tasks now.
{{DIVIDER}}
{{DIVIDER}}
Huge. One less thing haunting you:
  [D][X] return book (by: Sep 20 2026)
{{DIVIDER}}
{{DIVIDER}}
Gone. We never knew this task:
  [D][X] return book (by: Sep 20 2026)
You're juggling 3 tasks now.
{{DIVIDER}}
{{DIVIDER}}
Here's the current chaos:
1.[T][ ] read book
2.[E][ ] project meeting (from: Sep 21 2026 to: Sep 22 2026)
3.[T][ ] join club
{{DIVIDER}}
{{DIVIDER}}
Gone. We never knew this task:
  [T][ ] read book
You're juggling 2 tasks now.
{{DIVIDER}}
{{DIVIDER}}
Gone. We never knew this task:
  [T][ ] join club
You're juggling 1 task now.
{{DIVIDER}}
{{DIVIDER}}
Here's the current chaos:
1.[E][ ] project meeting (from: Sep 21 2026 to: Sep 22 2026)
{{DIVIDER}}
{{DIVIDER}}
I'm out. Your tasks aren't — don't ghost them.
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
Yeah, no. Please provide a task number. Try: delete <number>
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. There are no tasks to delete.
{{DIVIDER}}
{{DIVIDER}}
Locked in. I added:
  [T][ ] safe task
You're juggling 1 task now.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. The task number must be a whole number.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. The task number must be a whole number.
{{DIVIDER}}
{{DIVIDER}}
Here's the current chaos:
1.[T][ ] safe task
{{DIVIDER}}
{{DIVIDER}}
Gone. We never knew this task:
  [T][ ] safe task
You're juggling 0 tasks now.
{{DIVIDER}}
{{DIVIDER}}
Here's the current chaos:
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. There are no tasks to delete.
{{DIVIDER}}
{{DIVIDER}}
I'm out. Your tasks aren't — don't ghost them.
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
Yeah, no. I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
{{DIVIDER}}
{{DIVIDER}}
Yeah, no. I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
{{DIVIDER}}
{{DIVIDER}}
Locked in. I added:
  [T][ ] valid task
You're juggling 1 task now.
{{DIVIDER}}
{{DIVIDER}}
Here's the current chaos:
1.[T][ ] valid task
{{DIVIDER}}
{{DIVIDER}}
I'm out. Your tasks aren't — don't ghost them.
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
Locked in. I added:
  [T][ ] read A | B
You're juggling 1 task now.
{{DIVIDER}}
{{DIVIDER}}
Locked in. I added:
  [D][ ] submit work (by: Sep 18 2026)
You're juggling 2 tasks now.
{{DIVIDER}}
{{DIVIDER}}
Locked in. I added:
  [E][ ] consultation (from: Sep 21 2026 to: Sep 22 2026)
You're juggling 3 tasks now.
{{DIVIDER}}
{{DIVIDER}}
Huge. One less thing haunting you:
  [D][X] submit work (by: Sep 18 2026)
{{DIVIDER}}
{{DIVIDER}}
I'm out. Your tasks aren't — don't ghost them.
{{DIVIDER}}
{{STARTUP}}
{{DIVIDER}}
Here's the current chaos:
1.[T][ ] read A | B
2.[D][X] submit work (by: Sep 18 2026)
3.[E][ ] consultation (from: Sep 21 2026 to: Sep 22 2026)
{{DIVIDER}}
{{DIVIDER}}
Gone. We never knew this task:
  [T][ ] read A | B
You're juggling 2 tasks now.
{{DIVIDER}}
{{DIVIDER}}
Plot twist. This one's back:
  [D][ ] submit work (by: Sep 18 2026)
{{DIVIDER}}
{{DIVIDER}}
I'm out. Your tasks aren't — don't ghost them.
{{DIVIDER}}
{{STARTUP}}
{{DIVIDER}}
Here's the current chaos:
1.[D][ ] submit work (by: Sep 18 2026)
2.[E][ ] consultation (from: Sep 21 2026 to: Sep 22 2026)
{{DIVIDER}}
{{DIVIDER}}
I'm out. Your tasks aren't — don't ghost them.
{{DIVIDER}}
```

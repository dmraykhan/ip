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
Bye. Hope to see you again soon!
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
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
{{DIVIDER}}
{{DIVIDER}}
Got it. I've added this task:
  [T][ ] return book
Now you have 2 tasks in the list.
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
1.[T][ ] read book
2.[T][ ] return book
{{DIVIDER}}
{{DIVIDER}}
Bye. Hope to see you again soon!
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
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
{{DIVIDER}}
{{DIVIDER}}
Got it. I've added this task:
  [T][ ] return book
Now you have 2 tasks in the list.
{{DIVIDER}}
{{DIVIDER}}
Nice! I've marked this task as done:
  [T][X] return book
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
1.[T][ ] read book
2.[T][X] return book
{{DIVIDER}}
{{DIVIDER}}
OK, I've marked this task as not done yet:
  [T][ ] return book
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
1.[T][ ] read book
2.[T][ ] return book
{{DIVIDER}}
{{DIVIDER}}
Bye. Hope to see you again soon!
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
Got it. I've added this task:
  [D][ ] do homework (by: Sep 18 2026)
Now you have 1 task in the list.
{{DIVIDER}}
{{DIVIDER}}
Got it. I've added this task:
  [E][ ] project meeting (from: Sep 21 2026 to: Sep 22 2026)
Now you have 2 tasks in the list.
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
1.[D][ ] do homework (by: Sep 18 2026)
2.[E][ ] project meeting (from: Sep 21 2026 to: Sep 22 2026)
{{DIVIDER}}
{{DIVIDER}}
Bye. Hope to see you again soon!
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
Got it. I've added this task:
  [T][ ] first task
Now you have 1 task in the list.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! A todo needs a description. Try: todo <description>
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
{{DIVIDER}}
{{DIVIDER}}
Got it. I've added this task:
  [T][ ] second task
Now you have 2 tasks in the list.
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
1.[T][ ] first task
2.[T][ ] second task
{{DIVIDER}}
{{DIVIDER}}
Bye. Hope to see you again soon!
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
OOPS!!! A deadline needs /by followed by a date.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! A deadline needs a description. Try: deadline <description> /by <date>
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! The deadline date cannot be empty after /by.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! A deadline needs /by followed by a date.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! An event needs /from followed by a start date.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! An event needs a description. Try: event <description> /from <start-date> /to <end-date>
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! An event needs /to followed by an end date.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! The event start date cannot be empty after /from.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! The event end date cannot be empty after /to.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! An event needs /from followed by a start date.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! An event needs /to followed by an end date.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! The deadline date must be a valid date in yyyy-MM-dd format.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! The event start date must be a valid date in yyyy-MM-dd format.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! The event end date must be a valid date in yyyy-MM-dd format.
{{DIVIDER}}
{{DIVIDER}}
Got it. I've added this task:
  [D][ ] report (by: Feb 29 2024)
Now you have 1 task in the list.
{{DIVIDER}}
{{DIVIDER}}
Got it. I've added this task:
  [E][ ] meeting (from: Sep 21 2026 to: Sep 22 2026)
Now you have 2 tasks in the list.
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
1.[D][ ] report (by: Feb 29 2024)
2.[E][ ] meeting (from: Sep 21 2026 to: Sep 22 2026)
{{DIVIDER}}
{{DIVIDER}}
Bye. Hope to see you again soon!
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
OOPS!!! Please enter a command.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! Please provide a task number. Try: mark <number>
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! There are no tasks to unmark.
{{DIVIDER}}
{{DIVIDER}}
Got it. I've added this task:
  [T][ ] stable task
Now you have 1 task in the list.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! The task number must be a whole number.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! Please provide a task number. Try: unmark <number>
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! The task number must be a whole number.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
Nice! I've marked this task as done:
  [T][X] stable task
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
1.[T][X] stable task
{{DIVIDER}}
{{DIVIDER}}
Bye. Hope to see you again soon!
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
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
{{DIVIDER}}
{{DIVIDER}}
Got it. I've added this task:
  [D][ ] return book (by: Sep 20 2026)
Now you have 2 tasks in the list.
{{DIVIDER}}
{{DIVIDER}}
Got it. I've added this task:
  [E][ ] project meeting (from: Sep 21 2026 to: Sep 22 2026)
Now you have 3 tasks in the list.
{{DIVIDER}}
{{DIVIDER}}
Got it. I've added this task:
  [T][ ] join club
Now you have 4 tasks in the list.
{{DIVIDER}}
{{DIVIDER}}
Nice! I've marked this task as done:
  [D][X] return book (by: Sep 20 2026)
{{DIVIDER}}
{{DIVIDER}}
Noted. I've removed this task:
  [D][X] return book (by: Sep 20 2026)
Now you have 3 tasks in the list.
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
1.[T][ ] read book
2.[E][ ] project meeting (from: Sep 21 2026 to: Sep 22 2026)
3.[T][ ] join club
{{DIVIDER}}
{{DIVIDER}}
Noted. I've removed this task:
  [T][ ] read book
Now you have 2 tasks in the list.
{{DIVIDER}}
{{DIVIDER}}
Noted. I've removed this task:
  [T][ ] join club
Now you have 1 task in the list.
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
1.[E][ ] project meeting (from: Sep 21 2026 to: Sep 22 2026)
{{DIVIDER}}
{{DIVIDER}}
Bye. Hope to see you again soon!
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
OOPS!!! Please provide a task number. Try: delete <number>
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! There are no tasks to delete.
{{DIVIDER}}
{{DIVIDER}}
Got it. I've added this task:
  [T][ ] safe task
Now you have 1 task in the list.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! The task number must be a whole number.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! Choose a task number from 1 to 1.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! The task number must be a whole number.
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
1.[T][ ] safe task
{{DIVIDER}}
{{DIVIDER}}
Noted. I've removed this task:
  [T][ ] safe task
Now you have 0 tasks in the list.
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! There are no tasks to delete.
{{DIVIDER}}
{{DIVIDER}}
Bye. Hope to see you again soon!
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
OOPS!!! I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
{{DIVIDER}}
{{DIVIDER}}
OOPS!!! I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
{{DIVIDER}}
{{DIVIDER}}
Got it. I've added this task:
  [T][ ] valid task
Now you have 1 task in the list.
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
1.[T][ ] valid task
{{DIVIDER}}
{{DIVIDER}}
Bye. Hope to see you again soon!
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
Got it. I've added this task:
  [T][ ] read A | B
Now you have 1 task in the list.
{{DIVIDER}}
{{DIVIDER}}
Got it. I've added this task:
  [D][ ] submit work (by: Sep 18 2026)
Now you have 2 tasks in the list.
{{DIVIDER}}
{{DIVIDER}}
Got it. I've added this task:
  [E][ ] consultation (from: Sep 21 2026 to: Sep 22 2026)
Now you have 3 tasks in the list.
{{DIVIDER}}
{{DIVIDER}}
Nice! I've marked this task as done:
  [D][X] submit work (by: Sep 18 2026)
{{DIVIDER}}
{{DIVIDER}}
Bye. Hope to see you again soon!
{{DIVIDER}}
{{STARTUP}}
{{DIVIDER}}
Here are the tasks in your list:
1.[T][ ] read A | B
2.[D][X] submit work (by: Sep 18 2026)
3.[E][ ] consultation (from: Sep 21 2026 to: Sep 22 2026)
{{DIVIDER}}
{{DIVIDER}}
Noted. I've removed this task:
  [T][ ] read A | B
Now you have 2 tasks in the list.
{{DIVIDER}}
{{DIVIDER}}
OK, I've marked this task as not done yet:
  [D][ ] submit work (by: Sep 18 2026)
{{DIVIDER}}
{{DIVIDER}}
Bye. Hope to see you again soon!
{{DIVIDER}}
{{STARTUP}}
{{DIVIDER}}
Here are the tasks in your list:
1.[D][ ] submit work (by: Sep 18 2026)
2.[E][ ] consultation (from: Sep 21 2026 to: Sep 22 2026)
{{DIVIDER}}
{{DIVIDER}}
Bye. Hope to see you again soon!
{{DIVIDER}}
```

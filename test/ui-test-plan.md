# Twizzy UI Test Plan

Run these cases with the project-specific `$test-ui` skill. Each case starts a fresh Twizzy process, uses Java 25, and compares standard output exactly. `{{STARTUP}}` expands to the banner and greeting, while `{{DIVIDER}}` expands to the 60-character separator.

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

**Aim:** Verify that deadline and event details are retained as text and displayed with their task types.

**Inputs:**

```text
deadline do homework /by no idea :-p
event project meeting /from Mon 2pm /to 4pm
list
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
Got it. I've added this task:
  [D][ ] do homework (by: no idea :-p)
Now you have 1 task in the list.
{{DIVIDER}}
{{DIVIDER}}
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 2 tasks in the list.
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
1.[D][ ] do homework (by: no idea :-p)
2.[E][ ] project meeting (from: Mon 2pm to: 4pm)
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
OOPS!!! I don't recognize that command. Try todo, deadline, event, list, mark, unmark, or bye.
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

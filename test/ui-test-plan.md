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
read book
return book
list
bye
```

**Expected output:**

```text
{{STARTUP}}
{{DIVIDER}}
added: read book
{{DIVIDER}}
{{DIVIDER}}
added: return book
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
1.[ ] read book
2.[ ] return book
{{DIVIDER}}
{{DIVIDER}}
Bye. Hope to see you again soon!
{{DIVIDER}}
```

## TC3: Mark and unmark a task

**Aim:** Verify that marking and unmarking update the selected task and persist in subsequent lists.

**Inputs:**

```text
read book
return book
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
added: read book
{{DIVIDER}}
{{DIVIDER}}
added: return book
{{DIVIDER}}
{{DIVIDER}}
Nice! I've marked this task as done:
  [X] return book
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
1.[ ] read book
2.[X] return book
{{DIVIDER}}
{{DIVIDER}}
OK, I've marked this task as not done yet:
  [ ] return book
{{DIVIDER}}
{{DIVIDER}}
Here are the tasks in your list:
1.[ ] read book
2.[ ] return book
{{DIVIDER}}
{{DIVIDER}}
Bye. Hope to see you again soon!
{{DIVIDER}}
```

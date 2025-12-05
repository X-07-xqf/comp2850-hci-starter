# Evaluation Tasks — Week 9

## Task T1: Filter Tasks

**Scenario**:
"You've been asked to find all tasks containing the word 'buy'. Use the filter box to show only matching tasks, then count how many tasks remain."

**Setup**:
- Pre-populate task list with existing tasks (from tasks.csv)
- Example: "buy milks", "buy", "buy milk", plus other tasks
- 3 tasks contain "buy" in title (ID 18, 19, 20)

**Success criteria**:
- Participant uses filter box (types "buy")
- Participant reports correct count (3 tasks)
- Completed within 2 minutes
- No validation errors

**Metrics**:
- Time from page load to stating count (ms)
- Completion (0 = fail, 1 = success)
- Validation errors (count)
- Confidence rating (1–5): "How confident are you that you found all matching tasks?"

**Accessibility checks**:
- Result count announced by screen reader?
- Keyboard-only completion possible?
- Works with JS disabled?

---

## Task T2: Edit Task Title

**Scenario**:
"The task 'buy milk' has a typo. Change it to 'buy milk and eggs' and save the change."

**Setup**:
- Task ID 20: "buy milk" (visible in list)
- Participant must click Edit, change text, save

**Success criteria**:
- Participant activates edit mode
- Participant updates title correctly
- Change persists after save
- Completed within 90 seconds
- No validation errors

**Metrics**:
- Time from click Edit to save confirmation (ms)
- Completion (0/1)
- Validation errors (e.g., blank title submitted by mistake)
- Confidence rating (1–5)

**Accessibility checks**:
- Status message "Updated [title]" announced?
- Focus remains on/near edited task?
- Works with keyboard only?
- Works with JS disabled?

---

## Task T3: Add New Task

**Scenario**:
"You need to remember to 'Call supplier about delivery'. Add this as a new task."

**Setup**:
- Current task list (from tasks.csv)
- Form visible at top of page

**Success criteria**:
- Participant types exact title (or close match)
- Submits form
- New task appears in list
- Completed within 60 seconds

**Metrics**:
- Time from focus in input to confirmation (ms)
- Completion (0/1)
- Validation errors (if they submit blank by accident)
- Confidence rating (1–5)

**Accessibility checks**:
- Status message "Added [title]" announced?
- Form remains usable after error (if triggered)?
- Works with JS disabled (PRG)?

---

## Task T4: Delete Task

**Scenario**:
"The task 'add' is no longer needed. Delete it from the list."

**Setup**:
- Task ID 25: "add" (visible in list)

**Success criteria**:
- Participant clicks Delete button
- Confirms deletion (HTMX path) or submits form (no-JS)
- Task removed from list
- Completed within 45 seconds

**Metrics**:
- Time from click Delete to confirmation (ms)
- Completion (0/1)
- Confirmation dialog acknowledged (HTMX only)
- Confidence rating (1–5)

**Accessibility checks**:
- Delete button has accessible name ("Delete task: add")?
- Status message "Deleted [title]" announced (HTMX)?
- Works with keyboard only?
- Works with JS disabled (no confirmation, but functions)?

---

## Task Order

**Recommended sequence**:
1. **Warm-up** (not timed): "Browse the task list and familiarize yourself with the interface."
2. T3 (Add) — Low cognitive load, builds confidence
3. T1 (Filter) — Medium complexity, tests search
4. T2 (Edit) — Tests inline interaction, validation
5. T4 (Delete) — Destructive action, tests confirmation
6. **Debrief** (qualitative): Open-ended questions

**Counterbalance** if testing multiple participants: alternate T1/T2 order to avoid learning effects.

---

## Notes for Facilitator

### **Important: Data Setup**
Before each test session, ensure the task list contains:
1. At least 3 tasks with "buy" in title (for T1 filtering)
2. Task "buy milk" (ID 20) for editing (T2)
3. Task "add" (ID 25) for deletion (T4)
4. Enough other tasks to make filtering meaningful

### **If missing test data**:
```bash
# Add test tasks if needed
echo "26,Call supplier about delivery" >> data/tasks.csv
# Or use the interface to add them
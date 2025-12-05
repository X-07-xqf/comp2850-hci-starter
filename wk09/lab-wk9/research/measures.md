- Completion (0/1): finished within 180s
- Time-on-task (ms): request start→end
- Errors: validation, server
- A11y confirmations: keyboard-only, status, focus
- UMUX-Lite x2; Confidence 1–5

# Metrics Definitions — Week 9

Reference: [Evaluation Metrics Quick Reference](../references/evaluation-metrics-quickref.md)

---

## Core Metrics (Required)

### 1. Completion (0/1)
**Definition**: Task completed successfully within 180 seconds.

**Scoring**:
- `1` = Fully completed, correct outcome, ≤180s
- `0.5` = Partial completion (e.g., found filter but wrong count)
- `0` = Failed, abandoned, or >180s

**Data source**: Manual observation + server logs (`step=success`)

**Reporting**: Percentage per task  
Example: `T1: 4/5 = 80%`

**Split by conditions**:
- JS-on vs JS-off  
- Keyboard-only vs mouse  
- Screen reader vs visual  

---

### 2. Time-on-Task (ms)
**Definition**: Duration from HTTP request start to response end (server-side processing time).

**Calculation**: `ms` column in `metrics.csv`

**Reporting**:
- **Median** (primary, resistant to outliers)
- **MAD** (Median Absolute Deviation, robust spread measure)
- **Range** (min–max for context)

**Example**:
T1 (Filter): Median 1847ms, MAD 320ms, Range 1200–5200ms

**Note**: This measures server processing only, not client rendering.

**Backup**: Facilitator stopwatch (start when participant finishes reading scenario, stop at "done")

---

### 3. Errors
**Types**:
1. **Validation errors** (client-side or server-side validation failures)
   - Example: `blank_title`, `max_length`
2. **Server errors** (HTTP 5xx, exceptions)
   - Example: `server_timeout`, `database_error`

**Tracking**:
- Validation: `step=validation_error` in `metrics.csv`
- Server: `step=server_error` in `metrics.csv`

**Calculation**:
Error rate = (# error events) / (# total attempts)

**Reporting**:
- Count per task
- Percentage of attempts with errors
- Error type distribution

**Example**:
T3 (Add): 2/5 attempts had errors (40%)
1× blank_title (validation)
1× server_timeout (server)

**HCI insight**: High error rates → poor affordances, unclear constraints, or accessibility issues

---

### 4. Accessibility Confirmations
**Three key checks**:

1. **Keyboard-only completion**
   - Binary: yes/no per task
   - Test: Tab, Enter, Space only (no mouse)
   - Reporting: `T1: Keyboard-accessible ✓` or `T3: Tab order broken ✗`

2. **Status announcements**
   - Screen reader: announced / not announced / partial
   - Visual: visible / not visible
   - WCAG reference: 4.1.3 Status Messages

3. **Focus management**
   - After actions: focus correctly moves?
   - Traps: any keyboard traps?
   - WCAG reference: 2.4.7 Focus Visible

**Reporting**: Simple checklist per task
T2 (Edit):

Keyboard: ✓
Status: X (not announced - Backlog #13)
Focus: ✓ (returned to edited item)

---

### 5. UMUX-Lite (2 questions)
**Questions**:
1. "This system's capabilities meet my requirements."
2. "This system is easy to use."

**Scale**: 1–7 (strongly disagree → strongly agree)

**Collection**: Post-session, takes <30 seconds

**Calculation**: 
UMUX score = (Q1 + Q2) / 2

**Reporting**: 
- Mean score across participants
- Interpretation:
  - 1–3: Poor
  - 4–5: Adequate  
  - 6–7: Good

**Note**: Validated lightweight proxy for SUS (System Usability Scale)

---

### 6. Confidence Rating (1–5)
**Scale**: 
- `1` = Not at all confident
- `2` = Slightly confident
- `3` = Moderately confident
- `4` = Confident
- `5` = Very confident

**Collection**: After each task
> "On a scale of 1 to 5, how confident are you that you completed that task correctly?"

**Reporting**:
- Mean ± SD per task
- Distribution (count of each rating)

**Example**:
T1 (Filter): Mean 4.2 ± 0.8
Distribution: 0×1, 0×2, 1×3, 2×4, 2×5

**HCI insight**: Low confidence despite completion = feedback gap

---

## Qualitative Observations

### 7. Facilitator Notes
**Capture**:
- Pauses / hesitation ("Participant paused 10s before clicking filter")
- Verbalizations ("I'm not sure if it saved")
- Accessibility issues ("Screen reader didn't announce result count")
- Workarounds ("Used Ctrl+F instead of built-in filter")
- Emotional responses (frustration, confusion, satisfaction)

**Format**: Timestamped notes in `pilot-notes.md`

**Analysis**: Thematic coding during Week 10 → cluster issues → map to backlog items

---

## Data Collection Matrix

| Metric | T1 (Filter) | T2 (Edit) | T3 (Add) | T4 (Delete) | Collection Method |
|--------|-------------|-----------|----------|-------------|-------------------|
| Completion (0/1) | ✓ | ✓ | ✓ | ✓ | Observation + logs |
| Time-on-Task (ms) | ✓ | ✓ | ✓ | ✓ | Server logs |
| Errors (count) | ✓ | ✓ | ✓ | ✓ | Server logs |
| Keyboard-only | ✓ | ✓ | ✓ | ✓ | Manual test |
| Status announcements | ✓ | ✓ | ✓ | ✓ | Observation |
| Focus management | (✓) | ✓ | (✓) | (✓) | Observation |
| Confidence (1–5) | ✓ | ✓ | ✓ | ✓ | Post-task Q |
| Facilitator notes | ✓ | ✓ | ✓ | ✓ | Manual notes |

---

## Task-Specific Focus Areas

### T1: Filter Tasks
- **Primary**: Completion rate, Time-on-task
- **A11y focus**: Result count announcement, keyboard navigation
- **Expected issues**: Filter persistence (Backlog #1), contrast (Backlog #9)
- **Success criteria**: Finds 3 "buy" tasks within 120s

### T2: Edit Task Title
- **Primary**: Error rate, Validation errors
- **A11y focus**: Status announcement, focus management
- **Expected issues**: Status messages (Backlog #13), error persistence (Backlog #6)
- **Success criteria**: Edits "buy milk" to "buy milk and eggs" within 90s

### T3: Add New Task
- **Primary**: Completion rate, Error rate
- **A11y focus**: Form error handling, PRG mode
- **Expected issues**: Error persistence (Backlog #6), form validation
- **Success criteria**: Adds "Call supplier about delivery" within 60s

### T4: Delete Task
- **Primary**: Completion rate, Time-on-task
- **A11y focus**: Keyboard access, confirmation feedback
- **Expected issues**: Keyboard access (Backlog #3), button contrast (Backlog #9)
- **Success criteria**: Deletes "add" task within 45s

---

## Data Integrity Checks

**Before analysis (Week 10)**:
- **Completeness**: All tasks have `session_id`, `task_code`, `step`
- **Plausibility**: Times within expected ranges (T1: 1200-10000ms)
- **Consistency**: JS mode matches observed condition
- **Outliers**: Flag times >3× median for review

**Document anomalies** in: `wk09/lab-wk9/research/data-notes.md`

---

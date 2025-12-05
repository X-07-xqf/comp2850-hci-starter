## Pilot P1 Notes 

## Session Info
**Session ID:** P1_7a9f2c  
**Date:** 2025‑11‑28  
**Time:** 14:00–14:15  
**Browser:** Chrome 120  
**JS Mode:** JS-on (T1–T3), JS-off (repeat T1)  
**Assistive tech:** None  
**Input:** Mouse + keyboard  
**Researcher:** [Qinfeng Xu]

---

## Consent
- Consent obtained 14:00  
- Script read  
- No questions  
- Session ID set  

---

# Task T1 — Filter (JS‑on)

**Scenario:** Find all tasks containing “buy”.  
**Timing:** 30 seconds  
**Outcome:** Success  
Confidence: **4/5**

**Observations:**
- Found filter immediately  
- Real-time update observed  
- Counted correctly (3 items)  
- Did not test refresh persistence  

**Accessibility:**  
- Tab worked ✓  
- Visual feedback ✓  
- Focus stable ✓  

---

# Task T2 — Edit Task (JS‑on)

**Scenario:** Change “buy milk” → “buy milk and eggs”.  
**Duration:** 68 seconds  
**Outcome:** Success  
**Errors:** 1 validation error  
**Confidence:** 3/5  
Quote: *“I wasn’t sure if it saved.”*

**Observations:**
- Pressed Enter too early → blank submission  
- Error message seen and corrected  
- Unsure about success  

**Accessibility:**  
- Error message visible but not announced ✗  
- Update message not noticed ✗  

---

# Task T3 — Delete (JS‑on)

**Duration:** 25 seconds  
**Outcome:** Success  
**Confidence:** 5/5

**Observations:**
- Deleted immediately  
- Ignored confirmation dialog text  
- Did not verify after deletion  

**Accessibility:**  
- Delete button not reachable via keyboard ✗  
- Low contrast ✗  

---

# Task T1 Repeat — JS‑off Mode

**Duration:** 92 seconds  
**Outcome:** Success  
**Comment:** “Much slower, but still works.”

Observations:
- Full page reload on every keystroke  
- Functional parity confirmed  

---

# UMUX‑Lite
- Q1: 5/7  
- Q2: 6/7  
**Average: 5.5/7**

---

# Debrief — Verbatim
- “Filter worked really smoothly.”  
- “Not sure the edit saved.”  
- “Delete was straightforward.”  
- “JS‑off was slow but fine.”  

---

# Thematic Analysis
### Positive
- Filter responsiveness  
- Basic CRUD intuitive  

### Issues
- Edit confirmation unclear  
- Keyboard access broken  
- Status message visibility low  

### Unexpected
- User ignored status messages entirely  
- Error recovery was smooth  

---

# Backlog Mapping
- **#3 Delete not keyboard accessible** ✗  
- **#13 Status messages not announced** ✗  
- **#9 Contrast issue** ✗  
- **JS-off parity validated** ✓  

---

# Quantitative Summary

| Metric | T1 JS-on | T2 JS-on | T3 JS-on | T1 JS-off |
|--------|----------|----------|----------|-----------|
| Time (s) | 30 | 68 | 25 | 92 |
| Success | 1 | 1 | 1 | 1 |
| Confidence | 4 | 3 | 5 | — |
| Errors | 0 | 1 | 0 | 0 |
| Keyboard | ✓ | ✓ | ✗ | — |
| Status | ✓ | ✗ | ✓ | — |

Completion rate: **100%**  
Error rate: **25%**  
Mean confidence (JS-on): **4.0**

---

# Technical Notes
- Logger consistent  
- No anomalies  
- All HTMX requests 200 OK  
- No console errors  

---

# Next Pilot Adjustments
- More explicit keyboard testing  
- Highlight status messages  
- Strengthen debrief questions  
- Test PRG error persistence  

---

# Final Checklist
All items completed (consent, logs, UMUX-Lite, notes, reset environment).

---

Document compiled by **[Qinfeng Xu]**  
Date: **2025‑11‑28**


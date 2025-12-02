# Manual WCAG 2.2 AA Checklist — Week 7

**Date**: 2025-11-11
**Tester**: Qinfeng Xu
**Assistive tech**: Edge DevTools, Keyboard only testing

---

## Principle 1: Perceivable

### 1.4.3 Contrast (Minimum) - Level AA
- [ ] **Text**: All text 4.5:1 contrast minimum
- [ ] **Large text**: 18pt+ or 14pt bold - 3:1 minimum
- [ ] **UI components**: Buttons, inputs, borders - 3:1 minimum

**Test method**: Chrome DevTools Color Picker + WebAIM Contrast Checker

**Findings**: 
- Add Task button: 1.11:1 contrast (FAIL - needs 4.5:1)
- 6 × Edit buttons: 1.11:1 contrast (FAIL)
- 6 × Delete buttons: 1.11:1 contrast (FAIL)
- **Total**: 13 elements with contrast failures
- All other text: PASS (white on blue/black on white)

**Evidence**: `color-contrast-details.png`

---

## Principle 2: Operable

### 2.1.1 Keyboard - Level A
- [] **All interactive elements** reachable via Tab
- [] **Focus order** matches visual order
- [] **No keyboard traps** (can Tab out of all elements)

**Test method**: Keyboard only (Tab, Shift+Tab, Enter, Space)

**Findings**:
- All buttons reachable via Tab
- Focus order logical: Skip link → Add form → Task list → Edit/Delete buttons
- No keyboard traps detected
- Enter/Space activates buttons correctly

**Evidence**: `keyboard-focus.png`

### 2.4.1 Bypass Blocks - Level A
- [] **Skip link** present and functional
- [] **Landmarks** (nav, main, aside) for screen reader navigation

**Test method**: Press Tab once → "Skip to main content" appears → Enter → focus jumps

**Findings**:
- Skip link present and functional
- Landmarks: Has `<main>` but missing `<nav>`, `<aside>`

**Evidence**: `skip-link.png`

### 2.4.3 Focus Order - Level A
- [] **Logical sequence**: Tab follows reading order
- [] **No unexpected jumps**: Focus doesn't skip sections

**Test method**: Tab through entire page, note order

**Findings**:
- Logical sequence: Header → Main content → Task list
- No unexpected jumps

### 2.4.7 Focus Visible - Level AA
- [] **All focusable elements** show clear visual indicator
- [] **Indicator visible** against all backgrounds

**Test method**: Tab through page, confirm blue outline visible

**Findings**:
- Focus outline visible but thin (2px blue)
- On blue buttons, blue outline has low contrast

---

## Principle 3: Understandable

### 3.3.1 Error Identification - Level A
- [] **Error messages** identify what's wrong in text
- [] **Errors associated** with fields (`aria-describedby`, `aria-invalid`)

**Test method**: Submit empty form, check error display

**Findings**:
- Error message clear: "Title is required. Please enter at least one character."
- Error associated with field using `aria-describedby`
- `aria-invalid="true"` set on invalid field

**Evidence**: `error-message.png`

### 3.3.2 Labels or Instructions - Level A
- [] **All inputs** have `<label>` or `aria-label`
- [] **Labels descriptive** ("Task title" not "Title")

**Test method**: Check all `<input>`, `<select>`, `<textarea>` have labels

**Findings**:
- Title input has `<label for="title">Task title</label>`
- All form fields properly labeled
- Hint text provided via `aria-describedby`

---

## Principle 4: Robust

### 4.1.3 Status Messages - Level AA
- [] **ARIA live regions** for dynamic updates
- [] **Success/error messages** announced by screen reader

**Test method**: Check HTML structure and test adding tasks

**Findings**:
-  ARIA live region NOT functioning properly
-  Status messages NOT displayed after form submissions
-  HTML has `<div role="status">` but not in correct location/structure
-  No feedback messages shown for add/edit/delete actions

**Evidence**: `missing-live-region.png`

---

## Summary

**Total checks**: 10
**Passed**: 6
**Failed**: 2 (contrast + status messages)
**Partial/Warnings**: 2 (landmarks, focus visibility)

**Critical failures** (Level A):
- None - all Level A criteria pass

**Important failures** (Level AA):
1. **1.4.3 Contrast**: 13 elements fail (1.11:1 needs 4.5:1) - HIGH PRIORITY
2. **4.1.3 Status Messages**: No functioning ARIA live region - HIGH PRIORITY  
3. **2.4.7 Focus Visible**: Focus outline needs improvement - MEDIUM PRIORITY

---

## Fixes Applied During Testing

1. **Image alt text fixed** (WCAG 1.1.1)
   - Before: `<img src="/static/img/icon.png">` (no alt)
   - After: `<img src="/static/img/icon.png" alt="Task icon">`
   - Evidence: `alt-fixed-html.png`

2. **Note**: Color contrast violations increased from 7 to 13 after page reload
   - Indicates more tasks were loaded, revealing additional instances
   - Original axe scan: 7 instances
   - After reload with 6 tasks: 13 instances (1 Add Task + 12 Edit/Delete buttons)
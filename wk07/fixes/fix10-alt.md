# Fix: Image Missing Alternative Text

**Date**: 2025-11-11
**Backlog ID**: 10
**WCAG**: 1.1.1 Non-text Content (Level A)

## Problem
Image element had no `alt` attribute, violating WCAG 1.1.1.

**Before**:
<img src="/static/img/icon.png" width="16" height="16">

**After**:
<img src="/static/img/icon.png" width="16" height="16" alt="Task icon">

**Verification**:
axe scan no longer reports this violation
HTML validates with alt attribute
Screen readers will announce "Task icon"

**Evidence**:
Before: evidence/axe-before-missing-alt.png
After: evidence/axe-after-missing-alt.png
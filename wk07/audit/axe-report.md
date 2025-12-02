# axe DevTools Audit Report — Week 7

**Date**: 2025-11-21
**Page scanned**: http://localhost:8080/tasks
**axe version**: 4.10.3

---

## Summary

- **Total Issues**: 9
- **Critical Issues**: 1
- **Serious Issues**: 8
- **Moderate Issues**: 0
- **Minor Issues**: 0

---

## Violations Found

### 1. Elements must meet minimum color contrast ratio thresholds (7 instances)
**WCAG**: 1.4.3 Contrast (Minimum) - Level AA
**Severity**: Serious
**Issue**: Button text has insufficient contrast ratio of 1.11:1 (needs 4.5:1)
**Affected Elements**:
  - `<button type="submit">Add Task</button>` (添加任务按钮)
  - `<button type="submit">Edit</button>` (所有编辑按钮)
  - `<button type="submit">Delete</button>` (所有删除按钮)
**Color Details**:
  - 前景色：`#6c757d` (灰色)
  - 背景色：`#0172ad` (蓝色)
  - 当前对比度：1.11:1
  - 要求对比度：4.5:1 (AA标准)
**Impact**: 低视力用户、色盲用户难以阅读按钮文字
**Fix**: 改变按钮文字颜色，使其与背景色的对比度达到4.5:1以上

### 2. Images must have alternative text (1 instance)
**WCAG**: 1.1.1 Non-text Content - Level A
**Severity**: Critical
**Issue**: Image element has no alternative text
**Element**: `<img src="/static/img/icon.png" width="16" height="16">`
**Impact**: 屏幕阅读器用户无法知道图片内容
**Fix**: 添加`alt`属性，例如：`alt="Task icon"` 或使用`role="presentation"`

### 3. Links must have discernible text (1 instance)
**WCAG**: 2.4.4 Link Purpose (In Context) - Level A
**Severity**: Serious
**Issue**: Link has no accessible text
**Element**: `<a href="/about"></a>`
**Impact**: 屏幕阅读器用户和键盘用户无法理解链接目的
**Fix**: 添加链接文本或使用`aria-label`属性

---

## Passed Checks (Sample)

- ✅ HTML lang attribute present
- ✅ Form labels associated correctly
- ✅ Focus order logical
- ✅ Error messages properly announced
- ✅ Skip link functional

---

## Priority Fixes (Week 7)

Based on severity + WCAG level + inclusion risk:

### **1. High Priority - Fix this week**
1. **Images must have alternative text** (Critical, WCAG A)
   - 影响：屏幕阅读器用户完全无法访问
   - 修复：添加`alt="Task icon"`

2. **Color contrast on all buttons** (Serious, WCAG AA, 7 instances)
   - 影响：低视力用户、所有用户的可读性
   - 修复：更改按钮文字颜色

### **2. Medium Priority - Fix if time permits**
3. **Links must have discernible text** (Serious, WCAG A)
   - 影响：屏幕阅读器用户
   - 修复：添加链接文本

---

## Evidence

Screenshots saved in `wk07/evidence/`:
- `axe-total-issues.png` - Shows 9 total issues
- `axe-color-contrast-details.png` - Shows contrast ratio details
- `axe-missing-alt.png` - Shows missing alt text issue
- `axe-empty-link.png` - Shows empty link issue
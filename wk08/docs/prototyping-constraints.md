# Prototyping Constraints & Trade-offs

## Rendering splits
- Full page: [describe what `/tasks` returns and when it's used]
The /tasks endpoint returns a complete HTML document including the <html> framework, navigation, footer, "Add Task" form, "Filter" form, and the initial task list area. It is used when: 1) a user first visits the page, 2) the browser is refreshed, or 3) for any navigation when JavaScript is disabled (serving as the "progressive enhancement" baseline).
- Fragment: [describe what `/tasks/fragment` returns and when it's used]
The /tasks/fragment endpoint returns only pure HTML fragments, typically a combination of the _list.peb (task list) and _pager.peb (pagination controls) partials, plus an out-of-band (OOB) status update. It is called asynchronously by HTMX when a user types in the search box or clicks a pagination link to update only the id="task-area" section of the page, enabling a refresh-less experience.

## URL & History
- [Explain how `hx-push-url="true"` maintains browser history]
hx-push-url="true": When HTMX makes a fragment request (e.g., for search or pagination) and successfully updates the page, it automatically pushes the corresponding full URL (like /tasks?q=foo&page=2) into the browser's history stack. This keeps the address bar URL synchronized with the current page state.
- [What breaks if you remove it?]
The URL in the browser's address bar would no longer change. Users could not use the back/forward buttons to return to previous search or pagination states, and could not bookmark or share a link to a specific filtered and paginated view, severely breaking core web conventions.

## Accessibility hooks
- [Describe the live region `#status` and its purpose]
This is a <div> marked with role="status" aria-live="polite". Screen readers automatically monitor this region. When an HTMX operation (like search, add, or delete task) completes, we insert a status message (e.g., "Found 5 tasks") into this area via hx-swap-oob="true". The screen reader then announces this information to the user in a non-interruptive manner, providing crucial operational feedback.
- [Explain `aria-describedby` connection between list and result count]
The task list (<ul>) is associated with a hidden paragraph containing the result count (<p id="result-count">) via the aria-describedby="result-count" attribute. This provides additional context about the list to screen reader users.
- [Why is result count visually hidden?]
The result information (e.g., "Showing 5 of 10 items") is necessary for screen reader users but could be redundant for sighted users, as the pagination controls and the list itself already convey similar information. Hiding it with a .visually-hidden CSS class avoids visual clutter and repetition in the interface while still meeting WCAG's "Adaptable" guideline.

## State management
- [How do query parameters (`q`, `page`) maintain state?]
They act as the single source of truth for the application state. Whether through a full page load or an HTMX fragment update, the current state (search term and page number) is encoded in the URL. This makes any state perfectly restorable via linking, refreshing, and back/forward navigation.
- [Why must pagination links include the filter query?]
 If a pagination link only contained ?page=2, clicking it from the second page of filtered results would cause the user to lose the current search filter (q parameter). The system would show the second page of all tasks, not the second page of filtered results. This violates the principle of "state continuity." Therefore, every pagination link must carry all current state parameters, like ?q=shopping&page=2.

## Performance notes
- Page size: [your choice - justify it]
I chose 10 items per page. This is a balanced choice: it's a manageable number for most screen sizes and cognitive loads. It reduces the amount of data loaded at once, improving response speed, while avoiding the annoyance of too many page turns due to an overly small page size.
- Fragment response size vs full page: [estimate bandwidth savings]
A full-page response (including layout, styles, scripts) is estimated at 8-12 KB. A fragment response (only the list and pagination) might be just 1-3 KB. This translates to bandwidth savings of 70-80% during frequent interactions (like typing a search), significantly reducing browser rendering work.
- Debounce delay: [300ms - why this value?]
This is a typical debounce value. It ensures that when a user types quickly (e.g., typing "shopping"), a request is not sent immediately for each keystroke (s, h, o, …). Instead, the search is triggered only after the user pauses typing for approximately 300 milliseconds. This reduces unnecessary network requests and server load, striking a good balance between responsiveness and performance. A shorter delay (e.g., 100ms) would cause too many requests, while a longer one (e.g., 1000ms) would feel sluggish.

## Future risks
- [What could go wrong with this approach?]
1.State Inconsistency: If the logic for the fragment route (/tasks/fragment) and the full-page route (/tasks) diverges (e.g., different sorting), the user experience breaks.
2.Complexity: The dual rendering paths increase the cognitive burden for testing and maintenance.
- [Scalability concerns (e.g., 10,000 tasks)?]
The current store.search() performs a linear scan in memory, which would become a performance bottleneck with a very large number of tasks (e.g., 10,000). A production environment would require a backend database with indexed search and server-side pagination.
- [Template maintenance burden?]
Using multiple partial templates (_list.peb, _pager.peb, _item.peb) improves reusability but also increases the number of files. It's crucial to ensure variable names and data structures are consistent across all partials; otherwise, a change in one place could cause errors in many others.

## Accessibility verification

### Keyboard testing
- [Results from Tab navigation test]
Using the Tab key, I can navigate all interactive elements in a logical order (skip link → search box → apply filter button → add task title input → add task button → edit/delete buttons within the task list → pagination links). Focus indicators were clearly visible. Result: Full keyboard navigation is functional.

### Screen reader testing
- [If available: what was announced when filtering?]
Tested with NVDA/JAWS (or simulated via Chrome Lighthouse audit). When typing in the search box triggered filtering, the screen reader successfully announced the status region update, e.g., "Found 3 tasks." The list's aria-describedby information was also read correctly. 
Result: Dynamic content updates were effectively communicated.

### No-JS parity
- [Confirmation that all features work without JavaScript]
After disabling JavaScript in the browser settings, all core functions were tested and found to be working:
1.Submitting the "Add Task" form reloads the page and displays the new task.
2.Submitting the "Filter tasks" form reloads the page and displays filtered results.
3.Clicking "Previous/Next" pagination links navigates to the correct page.
Result: Confirmed all features work without JavaScript, fulfilling the promise of progressive enhancement.

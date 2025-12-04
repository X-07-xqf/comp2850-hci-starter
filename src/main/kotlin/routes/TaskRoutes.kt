package routes

// import data.TaskRepository
import io.ktor.http.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.* 
import io.pebbletemplates.pebble.PebbleEngine
import java.io.StringWriter
import storage.TaskStore
import model.Task
import utils.Page

/**
 * NOTE FOR NON-INTELLIJ IDEs (VSCode, Eclipse, etc.):
 * IntelliJ IDEA automatically adds imports as you type. If using a different IDE,
 * you may need to manually add imports. The commented imports below show what you'll need
 * for future weeks. Uncomment them as needed when following the lab instructions.
 *
 * When using IntelliJ: You can ignore the commented imports below - your IDE will handle them.
 */

// Week 7+ imports (inline edit, toggle completion):
// import model.Task               // When Task becomes separate model class
// import model.ValidationResult   // For validation errors
// import renderTemplate            // Extension function from Main.kt
// import isHtmxRequest             // Extension function from Main.kt

// Week 8+ imports (pagination, search, URL encoding):
// import io.ktor.http.encodeURLParameter  // For query parameter encoding
// import utils.Page                       // Pagination helper class

// Week 9+ imports (metrics logging, instrumentation):
// import utils.jsMode              // Detect JS mode (htmx/nojs)
// import utils.logValidationError  // Log validation failures
// import utils.timed               // Measure request timing

// Note: Solution repo uses storage.TaskStore instead of data.TaskRepository
// You may refactor to this in Week 10 for production readiness

/**
 * Week 6 Lab 1: Simple task routes with HTMX progressive enhancement.
 *
 * **Teaching approach**: Start simple, evolve incrementally
 * - Week 6: Basic CRUD with Int IDs
 * - Week 7: Add toggle, inline edit
 * - Week 8: Add pagination, search
 */

fun Route.taskRoutes(store: TaskStore = TaskStore()) {
    val pebble =
        PebbleEngine
            .Builder()
            .loader(
                io.pebbletemplates.pebble.loader.ClasspathLoader().apply {
                    prefix = "templates/"
                },
            ).build()

    /**
     * Helper: Check if request is from HTMX
     */
    fun ApplicationCall.isHtmx(): Boolean = request.headers["HX-Request"]?.equals("true", ignoreCase = true) == true

    /**
     * GET /tasks - List all tasks
     * Returns full page (no HTMX differentiation in Week 6)
     */
    get("/tasks") {
        val query = call.request.queryParameters["q"].orEmpty()
        val pageNum = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
        val error = call.request.queryParameters["error"]  // 新增：获取错误类型
        val msg = call.request.queryParameters["msg"]      // 新增：获取错误消息
        val tasks = store.search(query)
        val data = Page.paginate(tasks.map { it.toPebbleContext() }, pageNum, pageSize = 10)
        // Add session info for footer
        val sessionId = "dev-session-${System.currentTimeMillis() % 1000}"
        val isHtmx = call.request.headers["HX-Request"]?.equals("true", ignoreCase = true) == true

        val model =
            mapOf(
                "title" to "Tasks",
                // "tasks" to TaskRepository.all(),
                "page" to data,
                "query" to query,
                "sessionId" to sessionId,
                "isHtmx" to isHtmx,
                "editingId" to null,         
                "errorMessage" to null,     
                "error" to error,     // 新增：传递错误类型
                "msg" to msg          // 新增：传递错误消息   
            )
        val template = pebble.getTemplate("tasks/index.peb")
        val writer = StringWriter()
        template.evaluate(writer, model)
        call.respondText(writer.toString(), ContentType.Text.Html)
    }

    /**
     * POST /tasks - Add new task
     * Dual-mode: HTMX fragment or PRG redirect
     */
    post("/tasks") {
        val title = call.receiveParameters()["title"].orEmpty().trim()

        // Validation
        if (title.isBlank()) {
            if (call.isHtmx()) {
                val status = """<div id="status" hx-swap-oob="true">Title is required.</div>"""
                return@post call.respondText(status, ContentType.Text.Html, HttpStatusCode.BadRequest)
            } else {
                // No-JS: redirect with error query param
                return@post call.respondRedirect("/tasks?error=title")
            }
        }

        if (title.length > 200) {
            if (call.isHtmx()) {
                val status = """<div id="status" hx-swap-oob="true">Title too long (max 200 chars).</div>"""
                return@post call.respondText(status, ContentType.Text.Html, HttpStatusCode.BadRequest)
            } else {
                return@post call.respondRedirect("/tasks?error=title&msg=too_long")
            }
        }

        // Success path
        val task = Task(title = title)
        store.add(task)
        if (call.isHtmx()) {
        // 修复这里！使用pebble实例而不是PebbleRender
        val template = pebble.getTemplate("tasks/_item.peb")
        val writer = StringWriter()
        template.evaluate(writer, mapOf("t" to task.toPebbleContext()))
        val item = writer.toString()
        
        val status = """<div id="status" hx-swap-oob="true">Added "${task.title}".</div>"""
        return@post call.respondText(item + status, ContentType.Text.Html)
        }
        call.respondRedirect("/tasks")
    }

    /**
     * POST /tasks/{id}/delete - Delete task
     * Dual-mode: HTMX empty response or PRG redirect
     */
    post("/tasks/{id}/delete") {
        // val id = call.parameters["id"]?.toIntOrNull()
        val id = call.parameters["id"]
        // val removed = id?.let { TaskRepository.delete(it) } ?: false
        val removed = id?.let { store.delete(it) } ?: false

        if (call.isHtmx()) {
            val message = if (removed) "Task deleted." else "Could not delete task."
            val status = """<div id="status" hx-swap-oob="true">$message</div>"""
            // Return empty content to trigger outerHTML swap (removes the <li>)
            return@post call.respondText(status, ContentType.Text.Html)

        // No-JS: POST-Redirect-GET pattern (303 See Other)
        } else {
            return@post call.respondRedirect("/tasks") // PRG
        }
    }
     // ========== 在这里添加新的DELETE路由 ==========
    /**
     * DELETE /tasks/{id} - Delete task (HTMX enhanced path)
     * Dual-mode: HTMX uses DELETE method, no-JS uses POST fallback
     */
    delete("/tasks/{id}") {
        val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
        val task = store.getById(id)
        
        store.delete(id)

        val status = """<div id="status" hx-swap-oob="true">Deleted "${task?.title ?: "task"}".</div>"""
        // Return empty string (outerHTML swap removes the <li>)
        call.respondText(status, ContentType.Text.Html)
    }
    // ========== DELETE路由添加结束 ==========

    // TODO: Week 7 Lab 1 Activity 2 Steps 2-5
    // Add inline edit routes here
    // Follow instructions in mdbook to implement:
    // - GET /tasks/{id}/edit - Show edit form (dual-mode)
    // - POST /tasks/{id}/edit - Save edits with validation (dual-mode)
    // - GET /tasks/{id}/view - Cancel edit (HTMX only)

    /**
    * GET /tasks/{id}/edit - Show edit form
    * Dual-mode: HTMX returns _edit.peb fragment, no-JS returns full page
    */
    get("/tasks/{id}/edit") {
        // val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.NotFound)
        val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.NotFound)
        // val task = TaskRepository.get(id) ?: return@get call.respond(HttpStatusCode.NotFound)
        val task = store.getById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
        val errorParam = call.request.queryParameters["error"]

        val errorMessage = when (errorParam) {
            "blank" -> "Title is required. Please enter at least one character."
            else -> null
        }

        if (call.isHtmx()) {
            val template = pebble.getTemplate("tasks/_edit.peb")
            // val model = mapOf("task" to task, "error" to errorMessage)
            val model = mapOf("task" to task.toPebbleContext(), "error" to errorMessage)
            val writer = StringWriter()
            template.evaluate(writer, model)
            call.respondText(writer.toString(), ContentType.Text.Html)
        } else {
            val tasks = store.search("")
            val data = Page.paginate(tasks.map { it.toPebbleContext() }, 1, pageSize = 10)
            val sessionId = "dev-session-${System.currentTimeMillis() % 1000}"
            val isHtmx = false  // 非HTMX请求
            val model = mapOf(
                "title" to "Tasks",
                // "tasks" to TaskRepository.all(),
                "page" to data,
                "query" to "",
                "sessionId" to sessionId,  
                "isHtmx" to isHtmx,          
                "editingId" to id,
                "errorMessage" to errorMessage
            )
            val template = pebble.getTemplate("tasks/index.peb")
            val writer = StringWriter()
            template.evaluate(writer, model)
            call.respondText(writer.toString(), ContentType.Text.Html)
        }
    }


    /**
    * POST /tasks/{id}/edit - Save edits
    * Dual-mode: HTMX returns _item.peb fragment, no-JS redirects to /tasks
    */
    post("/tasks/{id}/edit") {
        // val id = call.parameters["id"]?.toIntOrNull() ?: return@post call.respond(HttpStatusCode.NotFound)
        val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.NotFound)
        // val task = TaskRepository.get(id) ?: return@post call.respond(HttpStatusCode.NotFound)
        val task = store.getById(id) ?: return@post call.respond(HttpStatusCode.NotFound)

        val newTitle = call.receiveParameters()["title"].orEmpty().trim()

        // Validation
        if (newTitle.isBlank()) {
            if (call.isHtmx()) {
                // HTMX path: return edit fragment with error
                val template = pebble.getTemplate("tasks/_edit.peb")
                val model = mapOf(
                    // "task" to task,
                    "task" to task.toPebbleContext(),
                    "error" to "Title is required. Please enter at least one character."
                )
                val writer = StringWriter()
                template.evaluate(writer, model)
                return@post call.respondText(writer.toString(), ContentType.Text.Html, HttpStatusCode.BadRequest)
            } else {
                // No-JS path: redirect with error flag
                return@post call.respondRedirect("/tasks/${id}/edit?error=blank")
            }
        }

        // Update task
        // val updatedTask = TaskRepository.update(id, newTitle)
        val updatedTask = task.copy(title = newTitle)
        // if (updatedTask == null) {
            // return@post call.respond(HttpStatusCode.NotFound, "Task not found")
        // }
        val success = store.update(updatedTask)  
            if (!success) {
                return@post call.respond(HttpStatusCode.NotFound, "Task not found")
            }
        if (call.isHtmx()) {
            // HTMX path: return view fragment + OOB status
            val viewTemplate = pebble.getTemplate("tasks/_item.peb")
            val viewWriter = StringWriter()
            // viewTemplate.evaluate(viewWriter, mapOf("task" to updatedTask))
            viewTemplate.evaluate(viewWriter, mapOf("task" to updatedTask.toPebbleContext()))

            val status = """<div id="status" hx-swap-oob="true">Task "${updatedTask.title}" updated successfully.</div>"""

            return@post call.respondText(viewWriter.toString() + status, ContentType.Text.Html)
            
        }else{
            // No-JS path: PRG redirect
            call.respondRedirect("/tasks")
        }
    }

    /**
    * GET /tasks/{id}/view - Cancel edit (HTMX only)
    * Returns task in view mode without saving changes
    */
    get("/tasks/{id}/view") {
        // val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.NotFound)
        val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.NotFound)
        // val task = TaskRepository.get(id) ?: return@get call.respond(HttpStatusCode.NotFound)
        val task = store.getById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
        // HTMX path only (cancel is just a link to /tasks in no-JS)
        val template = pebble.getTemplate("tasks/_item.peb")
        // val model = mapOf("task" to task)
        val model = mapOf("task" to task.toPebbleContext())
        val writer = StringWriter()
        template.evaluate(writer, model)
        call.respondText(writer.toString(), ContentType.Text.Html)
    }

    get("/tasks/fragment") {
        val query = call.request.queryParameters["q"].orEmpty()
        val pageNum = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
        val tasks = store.search(query)
        val data = Page.paginate(tasks.map { it.toPebbleContext() }, pageNum, pageSize = 10)
        
        val listTemplate = pebble.getTemplate("tasks/_list.peb")
        val listWriter = StringWriter()
        listTemplate.evaluate(listWriter, mapOf("page" to data, "query" to query))
        
        val pagerTemplate = pebble.getTemplate("tasks/_pager.peb")
        val pagerWriter = StringWriter()
        pagerTemplate.evaluate(pagerWriter, mapOf("page" to data, "query" to query))
        
        val status = """<div id="status" hx-swap-oob="true">Found ${data.totalItems} tasks.</div>"""
        
        call.respondText(listWriter.toString() + pagerWriter.toString() + status, ContentType.Text.Html)
    }
    /**
    * Helper to paginate and map tasks for template rendering.
    * Reduces code duplication in validation and filter routes.
    */
    fun paginateTasks(
        store: TaskStore,
        query: String,
        page: Int
    ): Page<Map<String, Any>> {
        val tasks = store.search(query).map { it.toPebbleContext() }
        return Page.paginate(tasks, currentPage = page, pageSize = 10)
    }
}

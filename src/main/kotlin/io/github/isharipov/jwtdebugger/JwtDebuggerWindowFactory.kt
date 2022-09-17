package io.github.isharipov.jwtdebugger

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class JwtDebuggerWindowFactory : ToolWindowFactory, DumbAware {
    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val jwtDebuggerWindow = JwtDebuggerWindow(toolWindow)
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(jwtDebuggerWindow.getContent(), "", false)
        toolWindow.contentManager.addContent(content)
    }
}
package io.github.isharipov.jwtdebugger

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.intellij.openapi.wm.ToolWindow
import org.apache.commons.lang3.StringUtils
import java.util.*
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class JwtDebuggerWindow(toolWindow: ToolWindow) {
    var jwtDebuggerWindowContent: JPanel? = null
    var algorithmsDropdown: JComboBox<String>? = null
    var encodedToken: JTextArea? = null
    private var headerArea: JTextArea? = null
    var payloadArea: JTextArea? = null
    var signatureArea: JTextArea? = null
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    init {
        encodedToken!!.document
            .addDocumentListener(EncodedTokenChangeListener(encodedToken!!, headerArea!!, payloadArea!!, gson))
    }

    fun getContent(): JPanel = jwtDebuggerWindowContent!!

    class EncodedTokenChangeListener(
        private val encodedToken: JTextArea,
        private val headerArea: JTextArea,
        private val payloadArea: JTextArea,
        private val gson: Gson
    ) : DocumentListener {
        override fun insertUpdate(e: DocumentEvent?) {
            try {
                val token = parseEncodedToken(encodedToken.text)
                headerArea.text = gson.toJson(gson.fromJson(token.header, Any::class.java))
                payloadArea.text = gson.toJson(gson.fromJson(token.payload, Any::class.java))
            } catch (e: Exception) {
                println(e)
            }
        }

        override fun removeUpdate(e: DocumentEvent?) {
            try {
                if (StringUtils.isEmpty(encodedToken.text)) {
                    headerArea.text = "{}"
                    payloadArea.text = "{}"
                } else {
                    val token = parseEncodedToken(encodedToken.text)
                    headerArea.text = gson.toJson(gson.fromJson(token.header, Any::class.java))
                    payloadArea.text = gson.toJson(gson.fromJson(token.payload, Any::class.java))
                }
            } catch (e: Exception) {
                println(e)
            }
        }

        override fun changedUpdate(e: DocumentEvent?) {
            TODO("not implemented")
        }

        private fun parseEncodedToken(token: String): Token {
            val parts = token.split(".")
            val header = decode(parts[0])
            val payload = decode(parts[1])
            val signature = parts[2]
            return Token(header, payload, signature)
        }

        private fun decode(token: String): String = String(Base64.getUrlDecoder().decode(token))
    }

    data class Token(val header: String, val payload: String, val signature: String)
}
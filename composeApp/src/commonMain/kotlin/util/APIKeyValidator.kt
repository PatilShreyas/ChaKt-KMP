package util

/**
 * Validates the [key] to check whether it's a valid Google Gemini API key or not
 */
fun isValidApiKey(key: String): Boolean {
    return "AIza[0-9A-Za-z-_]{35}".toRegex().matches(key)
}
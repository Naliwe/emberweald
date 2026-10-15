package net.hexacell.emberweald

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

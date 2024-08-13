package dev.nordix.wsserver.helpers

import kotlinx.coroutines.delay
import java.awt.Robot
import java.awt.event.KeyEvent

object KbHelper {
    private val robot = Robot()

    suspend fun startTimer() {
        robot.apply {
            keyPress(KeyEvent.VK_CONTROL)
            keyPress(KeyEvent.VK_SHIFT)
            keyPress(KeyEvent.VK_M)
        }
        delay(100)
        robot.apply {
            keyRelease(KeyEvent.VK_CONTROL)
            keyRelease(KeyEvent.VK_SHIFT)
            keyRelease(KeyEvent.VK_M)
        }
    }

    suspend fun pauseTimer() {
        robot.apply {
            keyPress(KeyEvent.VK_CONTROL)
            keyPress(KeyEvent.VK_SHIFT)
            keyPress(KeyEvent.VK_L)
        }
        delay(100)
        robot.apply {
            keyRelease(KeyEvent.VK_CONTROL)
            keyRelease(KeyEvent.VK_SHIFT)
            keyRelease(KeyEvent.VK_L)
        }
    }
}
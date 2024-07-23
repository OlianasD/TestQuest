package locator

import gamification.GamificationManager
import gamification.UserProfile
import ui.GUIManager
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.io.path.isRegularFile
/*

//TODO: OLD IMPLEMENTATION, TO REMOVE
class LocatorsMonitor(
    private val gamificationManager: GamificationManager,
    private val userProfile: UserProfile,
    private val guiManager: GUIManager,
    private val filePaths: List<Path>
)

{
    private val executor = Executors.newSingleThreadScheduledExecutor()
    private val extractor = LocatorsExtractor()

    init {
        // Update monitor and GUI panel
        guiManager.updateGUI(userProfile, false)
    }

    fun startMonitoring() {
        val watcher = FileSystems.getDefault().newWatchService()
        filePaths.forEach { it.parent.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY) }
        var previousLocators = filePaths.flatMap { extractor.parseLocators(it) }
        // Every 1 second, if any change happens on the locators, the GUI is updated
        executor.scheduleWithFixedDelay({
            val key = watcher.poll(1, TimeUnit.MILLISECONDS) ?: return@scheduleWithFixedDelay
            for (event in key.pollEvents()) {
                val kind = event.kind()
                if (kind == StandardWatchEventKinds.OVERFLOW) continue
                val changed = event.context() as Path
                val fullPath = key.watchable() as Path
                val changedFilePath = fullPath.resolve(changed)
                //TODO here a more precise check over newly added + changed + removed locators must be implemented
                if (filePaths.contains(changedFilePath) && changedFilePath.isRegularFile()) {
                    val currentLocators = filePaths.flatMap { extractor.parseLocators(it) }
                    val modifiedLocators = currentLocators.filter { it !in previousLocators }
                    if (modifiedLocators.isNotEmpty()) {
                        val notifyChanges = gamificationManager.updateProgresses(previousLocators, currentLocators, userProfile)
                        guiManager.updateGUI(userProfile, true)//notifyChanges=true if any change occurred and popup must be shown
                        previousLocators = currentLocators
                    }
                }
            }
            guiManager.updateGUI(userProfile, false)
            key.reset()
        }, 0, 1, TimeUnit.MILLISECONDS)
    }


}
*/
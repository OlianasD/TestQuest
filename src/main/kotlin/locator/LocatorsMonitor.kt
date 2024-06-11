package locator

import gamification.GamificationManager
import gamification.LocatorsAnalyzer
import gamification.UserProfile
import ui.GUIManager
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.io.path.isRegularFile

class LocatorsMonitor(
    private val gamificationManager: GamificationManager,
    private val userProfile: UserProfile,
    private val guiManager: GUIManager,
    filePaths: List<Path>
) {
    private val executor = Executors.newSingleThreadScheduledExecutor()

    init {
        // Update monitor and GUI panel
        val extractor = LocatorsExtractor()
        val analyzer = LocatorsAnalyzer()
        val initialLocators = filePaths.flatMap { extractor.parseLocators(it) }
        val initialMetricsResults = analyzer.analyzeLocators(initialLocators)
        guiManager.updateGUI(initialMetricsResults, userProfile, false)
    }

    fun startMonitoring(filePaths: List<Path>) {
        val watcher = FileSystems.getDefault().newWatchService()
        filePaths.forEach { it.parent.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY) }
        val extractor = LocatorsExtractor()
        val analyzer = LocatorsAnalyzer()
        var previousLocators = filePaths.flatMap { extractor.parseLocators(it) }
        var currentMetricsResults = analyzer.analyzeLocators(previousLocators)
        var previousMetricsResults: Map<String, Int>

        // Every 10 seconds, if any change happens on the locators, the GUI is updated
        executor.scheduleWithFixedDelay({
            val key = watcher.poll(10, TimeUnit.SECONDS) ?: return@scheduleWithFixedDelay
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
                        previousLocators = currentLocators
                        previousMetricsResults = currentMetricsResults
                        currentMetricsResults = analyzer.analyzeLocators(currentLocators)
                        val results = analyzer.compareMetrics(previousMetricsResults, currentMetricsResults)
                        gamificationManager.updateProgresses(userProfile, results)
                        guiManager.updateGUI(currentMetricsResults, userProfile, true)
                    }
                }
            }
            guiManager.updateGUI(currentMetricsResults, userProfile, false)
            key.reset()
        }, 0, 10, TimeUnit.SECONDS)
    }


}

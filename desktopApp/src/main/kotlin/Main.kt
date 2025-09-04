import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.moneyapp.shared.App
import com.moneyapp.shared.cache.Database
import com.moneyapp.shared.cache.DatabaseDriverFactory

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "MoneyApp") {
        val database = Database(DatabaseDriverFactory())
        App()
    }
}

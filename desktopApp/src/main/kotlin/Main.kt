import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.moneyapp.shared.App
import com.moneyapp.shared.PlaidRedirectServer
import com.moneyapp.shared.cache.Database
import com.moneyapp.shared.cache.DatabaseDriverFactory
import kotlin.concurrent.thread

fun main() = application {
    thread {
        PlaidRedirectServer().start()
    }

    Window(onCloseRequest = ::exitApplication, title = "MoneyApp") {
        val database = Database(DatabaseDriverFactory())
        App(database)
    }
}

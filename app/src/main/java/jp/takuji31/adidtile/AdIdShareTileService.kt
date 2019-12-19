package jp.takuji31.adidtile

import android.content.Intent
import android.service.quicksettings.TileService
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.*

class AdIdShareTileService : TileService() {
    private val mainScope = MainScope()
    override fun onClick() {
        mainScope.launch {
            val id = async {
                withContext(Dispatchers.IO) {
                    AdvertisingIdClient.getAdvertisingIdInfo(this@AdIdShareTileService).id
                }
            }
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, id.await())
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivityAndCollapse(shareIntent)

        }
    }

    override fun onDestroy() {
        mainScope.cancel()
        super.onDestroy()
    }
}

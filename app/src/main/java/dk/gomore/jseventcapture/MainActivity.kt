package dk.gomore.jseventcapture

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.print.PrintManager
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.print.PrintAttributes
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val data = """
    <script language="javascript" type="text/javascript">
        function handleButtonClick() {
            alert('[JS] Button was clicked');
            windowx.print();
        }
    </script>
        
    <button type='button' id='someButton' onclick='windowx.print();'>Click me</button>
    """

    @SuppressLint("JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        //val webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.settings.loadsImagesAutomatically = true
        webView.settings.allowContentAccess = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.settings.setMixedContentMode(0)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.addJavascriptInterface(CaptureClickJavascriptInterface(), "windowx")
        //.loadData(data, "text/html", "UTF-8")
        webView.loadUrl("http://192.168.0.179:8069/test-print");
    }

    private fun createWebPrintJob(webView: WebView) {
        webView.post {
            println("javscript done..") }

        // Get a PrintManager instance
        (this?.getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->

            val jobName = "${getString(R.string.app_name)} Document"

            // Get a print adapter instance
            val printAdapter = webView.createPrintDocumentAdapter(jobName)

            // Create a print job with name and adapter instance
            printManager.print(
                jobName,
                printAdapter,
                PrintAttributes.Builder().build()
            ).also { printJob ->

                // Save the job object for later status checking
                //printJobs += printJob
            }
        }
    }
    private fun showClickMessage() {
        Toast.makeText(
            this@MainActivity,
            "[Android] JavaScript button was clicked",
            Toast.LENGTH_LONG
        ).show()
    }

    private inner class CaptureClickJavascriptInterface {
        @JavascriptInterface
        fun print() {
            showClickMessage()
            print("message121212")
            createWebPrintJob(webView)
        }
    }
}

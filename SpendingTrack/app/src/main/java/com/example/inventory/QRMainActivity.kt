package com.example.inventory

import android.content.Intent
import android.os.*
import android.os.StrictMode.ThreadPolicy
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.inventory.data.Invoice
import com.example.inventory.data.ItemRoomDatabase
import com.example.inventory.databinding.ActivityQrmainBinding
import com.journeyapps.barcodescanner.CaptureManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*
import kotlin.collections.ArrayList

class QRMainActivity : AppCompatActivity() {



    private lateinit var binding: ActivityQrmainBinding
    private lateinit var captureManager: CaptureManager
    private var torchState: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityQrmainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        var calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"))
        val thisyear=calendar.get(Calendar.YEAR).toString()
        val thismonth=(calendar.get(Calendar.MONTH)+1).toString()

        binding.viewResult.text = ""

        binding.invoiceHistory.setOnClickListener {
            if (Build.VERSION.SDK_INT > 9) {
                val policy = ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
            }
            try {
                //topic = findViewById<TextView>(R.id.head)
                //從一個URL載入一個Document物件。
                var str :String=""
                //從一個URL載入一個Document物件。
                val doc1 = Jsoup.connect("https://invoice.etax.nat.gov.tw/index.html").get()
                val elements1 = doc1.getElementsByClass("etw-web")
                val top = elements1.select("a.etw-on")
                var startmonth=top.text().substring(4,9).split("-")[0].toInt()
                var endmonth=top.text().substring(4,9).split("-")[1].toInt()

                var startmonth1=top.text().substring(4,9).split("-")[0].toInt()-2
                var endmonth1=top.text().substring(4,9).split("-")[0].toInt()-1

                var monlist= arrayListOf(startmonth.toString(),endmonth.toString(),startmonth1.toString(),endmonth1.toString())
                println(startmonth.toString()+" "+endmonth.toString())
                val doc: Document =
                    Jsoup.connect("https://www.cinvoice.tw/prize").get()
                val elements: Elements = doc.getElementsByClass("container")
                for (element in elements) {
                    val s: String = element.getElementsByClass("bold").text()
                    //println("""$s""".trimIndent())
                    str += s
                }
                //println(str)

                val tokens: ArrayList<String> = str.split(" ".toRegex()) as ArrayList<String>
                val targetnumlist:ArrayList<String>
                targetnumlist= ArrayList()
                for (i in 1..11){
                    targetnumlist.add(tokens[i])
                    println(tokens[i])
                }
                val intent = Intent(applicationContext,InvoiceActivity::class.java).apply {
                    putStringArrayListExtra("month", monlist)
                    putStringArrayListExtra("target", targetnumlist)
                   //putStringArrayListExtra()
                }
                startActivity(intent)

            } catch (e: Exception) {
                println(e.toString())
            }


        }


        captureManager = CaptureManager(this,binding.viewBarcode)
        captureManager.initializeFromIntent(intent, savedInstanceState)



        binding.viewScan.setOnClickListener {
            binding.viewBarcode.decodeSingle { result ->
                val sb = StringBuilder()
                println(result.text)
                binding.viewResult.text = result.text.substring(2,10)
                var in_date=result.text.substring(10,18)
                sb.append(in_date.substring(0,3)).append("/").append(in_date.substring(3,5)).append("/").append(in_date.substring(5,7))

                println(sb.toString())
                vibrate()
                binding.invoiceStore.setOnClickListener {
                    if (!binding.viewResult.text.isNullOrBlank()){
                        val invoice = Invoice(null,sb.toString(),binding.viewResult.text.toString())
                        val a =thisyear.toInt()-1911
                        val b =sb.toString().split('/')[0].toInt()
                        val c =sb.toString().split('/')[1].toInt()
                        if (a-b==0){
                            GlobalScope.launch(Dispatchers.IO){
                                ItemRoomDatabase.getDatabase(applicationContext).InvoiceDao().insert(invoice)
                            }
                        }
                        else if(a-b>0){
                            if ((a-b==1) and  ((thismonth.toInt()==1) || (thismonth.toInt()==2))){
                                if(c==11||c==12){
                                    GlobalScope.launch(Dispatchers.IO){
                                        ItemRoomDatabase.getDatabase(applicationContext).InvoiceDao().insert(invoice)
                                    }
                                }
                            }
                        }
                        else{
                            Toast.makeText(this.applicationContext,"此發票過期", Toast.LENGTH_SHORT).show()
                        }
                        sb.clear()
                        binding.viewResult.text =""
                    }
                }
            }

        }



        binding.viewTorch.setOnClickListener {
            torchState = if (torchState) {
                binding.viewBarcode.setTorchOff()
                false
            } else {
                binding.viewBarcode.setTorchOn()
                true
            }
        }
    }

    private fun vibrate() {
        val vibrator: Vibrator? = getSystemService(Vibrator::class.java)

        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        100,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(100)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        captureManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        captureManager.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        captureManager.onDestroy()
    }
}


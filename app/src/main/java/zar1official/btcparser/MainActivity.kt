package zar1official.btcparser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.android.material.snackbar.Snackbar
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import zar1official.btcparser.databinding.ActivityMainBinding
import java.io.IOException
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    companion object {
        const val LINK = "https://www.finanz.ru/valyuty/btc-usd"
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btcImage.setImageResource(R.drawable.btc)
        updateData()

        binding.updateButton.setOnClickListener {
            val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.button_anim);
            it.startAnimation(animation)
            updateData()
        }

    }

    private fun updateData() {
        thread(true) {
            getData()
        }
    }


    private fun getData() = with(binding) {
        try {
            val responseContent = Jsoup.connect(LINK).get()
            val table: Elements = responseContent
                .getElementsByClass("main_left")[0]
                .selectFirst("table")
                .getElementsByTag("th")

            runOnUiThread {
                currentCourse.text = table[0].text()
                courseChanged.text = table[1].text()
                percentChanged.text = table[2].text()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Snackbar.make(this.root, "Проверьте подключение к интернету", Snackbar.LENGTH_SHORT).show()
        }
    }

}
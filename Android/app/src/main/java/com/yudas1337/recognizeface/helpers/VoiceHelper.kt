import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

class VoiceHelper constructor(context: Context) {

    private lateinit var tts: TextToSpeech

    companion object {
        private var instance: VoiceHelper? = null

        fun getInstance(context: Context): VoiceHelper {
            if (instance == null) {
                instance = VoiceHelper(context)
            }
            return instance!!
        }
    }

    fun runVoice(text: String) {
        instance?.tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    fun stopAndShutdown() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
    }


     fun initializeTextToSpeech(context: Context) {
        if (!::tts.isInitialized || !tts.isSpeaking) {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val result = tts.setLanguage(Locale("id", "ID"))
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language specified is not supported!")
                    }
                } else {
                    Log.e("TTS", "Initialization Failed!")
                }
            }
        }

    }
}

package edu.nmhu.bssd5250.sb_bssd5250_hwk51

import org.json.JSONObject
import java.util.*

class Note(var name:String, var desc:String, var date: String?, var flag:Boolean) {

    init{
        if (date == null) {
            date = Date().toString()
        }
    }

    fun toJSON(): JSONObject {
        //make a new json object
        val jsonObject = JSONObject().apply {
            //put each piece of data inot the object
            put("name", name)
            put("date", date)
            put("desc", desc)
            put("flag", flag)
        }
        return jsonObject
    }

    override fun toString(): String {
        return "$name, $date, $desc, $flag"
    }
}
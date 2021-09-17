package edu.nmhu.bssd5250.sb_bssd5250_hwk51


import android.annotation.SuppressLint
import android.util.Log
import org.json.JSONArray

@SuppressLint("StaticFieldLeak")
object NotesData {

    private val notesData:ArrayList<Note> = ArrayList()

    /* begin listener interface setup */
    interface NotesDataUpdateListener {
        fun  updateNotesDependents()
    }

    private var mListener:NotesDataUpdateListener? = null

    fun registerListener(listener:NotesDataUpdateListener){
        mListener = listener
    }

    fun deleteNote(index:Int){
        notesData.removeAt(index)
        mListener?.updateNotesDependents()
    }

    fun addNote(note: Note) {
        notesData.add(note)
    }

    fun getNoteList():ArrayList<Note> {
        return notesData
    }

    fun toJSON(): JSONArray {
        val jsonArray = JSONArray()
        for (note in notesData) {
            jsonArray.put(note.toJSON())
        }
        return jsonArray
    }

    fun loadNotes(data: JSONArray) {
        Log.d("NotesData", data.length().toString())
        for(i in 0 until data.length()) {
            val obj = data.getJSONObject(i)
            addNote(
                (Note(
                    obj.getString("name"),
                    obj.getString("date"),
                    obj.getString("desc"),
                    obj.getBoolean("importance")
                ))
            )
        }
    }

    override fun toString(): String {
        var allNotes = ""
        for (note in notesData) {
            allNotes += note.toString()
        }
        return allNotes
    }

}
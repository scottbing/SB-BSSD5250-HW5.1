package edu.nmhu.bssd5250.sb_bssd5250_hwk51

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.commit
import org.json.JSONArray
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.*

class MainActivity : NotesData.NotesDataUpdateListener, AppCompatActivity() {

    private var fid = 0
    private var which: Boolean = false
    private var importance: Boolean = false       // indicates importance
    private var fragmentLinearLayout: LinearLayoutCompat? = null

    override fun onPause() {
        super.onPause()
        writeDataToFile()
    }

    override fun onResume() {
        super.onResume()

        val jsonArray = readDataAsJSON()
        if(jsonArray != null) {
            loadJSONNotes(jsonArray)
            createNoteFragments()
        }
    }

    private fun removeExistingNotes() {
        for(noteF in supportFragmentManager.fragments){
            supportFragmentManager.commit{
                remove(noteF)
            }
        }
    }

    private fun createNoteFragments(){
        removeExistingNotes()
        for(i in 0 until NotesData.getNoteList().size){
            supportFragmentManager.commit {
                if(NotesData.getNoteList()[i].importance) {
                    which = true
                    add(fid, NoteFragment.newInstance(i, which, true), null)
                }
                else {
                    which = false
                    add(fid, NoteFragment.newInstance(i, which, false), null)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotesData.registerListener(this)
        val addButton = Button(this).apply {
            text = "+"
            id = View.generateViewId()
            setOnClickListener {
                supportFragmentManager.commit {
                    //setReorderingAllowed(true)

                    // do not add more than 10 notes
                    if (supportFragmentManager.fragments.size >= 10) {
                        //TODO add Alert Dialog here
                        val alert: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                        alert.setTitle("Notes limit Exceeded")
                        alert.setMessage("Notes limit of 10 reached")
                        alert.setPositiveButton("OK", null)
                        alert.show()
                    }
                    else {
                        which = false
                        NotesData.addNote(Note("", "", null, false))
                        val uniqueID = NotesData.getNoteList().size-1
                        add(fid, NoteFragment.newInstance(uniqueID, which, importance), null)
                    }
                }
            }
        }

        val addButtonRed = Button(this).apply {
            text = "+"
            id = View.generateViewId()
            setOnClickListener {
                supportFragmentManager.commit {
                    //setReorderingAllowed(true)

                    // do not add more than 10 notes
                    if (supportFragmentManager.fragments.size >= 10) {
                        //TODO add Alert Dialog here
                        val alert: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                        alert.setTitle("Notes limit Exceeded")
                        alert.setMessage("Notes limit of 10 reached")
                        alert.setPositiveButton("OK", null)
                        alert.show()
                    }
                    else {
                        which = true
                        NotesData.addNote(Note("", "", null, true))
                        val uniqueID = NotesData.getNoteList().size-1
                        add(fid, NoteFragment.newInstance(uniqueID, which, importance), null)
                    }
                }
            }
        }

        val buttonsLinearLayout = LinearLayoutCompat(this).apply {
            id = View.generateViewId()
            fid = id
            orientation = LinearLayoutCompat.HORIZONTAL
            setBackgroundColor(Color.GRAY)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            fragmentLinearLayout?.let {
                (layoutParams as RelativeLayout.LayoutParams).addRule(
                    RelativeLayout.ABOVE, it.id)
            }
            addView(addButton)
            addView(addButtonRed)
        }

        fragmentLinearLayout = LinearLayoutCompat(this).apply {
            id = View.generateViewId()
            fid = id
            orientation = LinearLayoutCompat.VERTICAL
            setBackgroundColor(Color.BLUE)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)
            (layoutParams as RelativeLayout.LayoutParams).addRule(
                RelativeLayout.BELOW, buttonsLinearLayout.id)
        }



        val relativeLayout = RelativeLayout(this).apply {
            setBackgroundColor(Color.GRAY)
            addView(buttonsLinearLayout)
            addView(fragmentLinearLayout)
        }

        setContentView(relativeLayout)
    }

    private fun readDataAsJSON(): JSONArray? {
        try {
            Log.i("MainActivity file", "in readData()")
            val fileInputStream: FileInputStream? = openFileInput("notes.json")
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String?
            while (run {
                    text = bufferedReader.readLine()
                    text
                } != null) {
                stringBuilder.append(text)
            }
            fileInputStream?.close()
            return JSONArray(stringBuilder.toString())
        } catch(e: FileNotFoundException) {
            return null
        }

    }

    private fun loadJSONNotes(data: JSONArray) {
        NotesData.loadNotes(data)
    }

    private fun writeDataToFile() {
        Log.i("MainActivity file", "in writeDataToFile()")
        val file = "notes.json"
        val data:String = NotesData.toJSON().toString()
        //val fileOutputStream: FileOutputStream
        try {
            val fileOutputStream = openFileOutput(file, Context.MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
            fileOutputStream.flush()
            fileOutputStream.close()
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    override fun updateNotesDependents() {
        Log.i("Recreating Notes", "recreate")
        removeExistingNotes()
        createNoteFragments()
    }
}
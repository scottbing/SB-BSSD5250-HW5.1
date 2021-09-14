package edu.nmhu.bssd5250.sb_bssd5250_hwk51

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.DialogFragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class NoteEditorDialog : DialogFragment() {

    private var targetResultKey:String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // format today's date
        val date: Date = Calendar.getInstance().time
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
        var strDate: String = dateFormat.format(date)

        val editName = EditText(context).apply {
            setHint(R.string.name_place_holder)
            setText(NotesData.getNoteList()[indexVal].name)
        }
        val editDate = EditText(context).apply {
            setHint(R.string.date_place_holder)
            setText(NotesData.getNoteList()[indexVal].date)
        }
        val editDesc = EditText(context).apply {
            setHint(R.string.desc_place_holder)
            setText(NotesData.getNoteList()[indexVal].desc)
        }
        val linearLayout = LinearLayoutCompat(requireContext()).apply{
            orientation = LinearLayoutCompat.VERTICAL
            addView(editName)
            addView(editDate)
            addView(editDesc)
        }

        val ad = AlertDialog.Builder(requireContext()).apply {
            setTitle("Note Editor")
            setMessage("Edit Content")
            setView(linearLayout)
            setPositiveButton("Save") { _,_ ->
                NotesData.getNoteList()[indexVal].name = editName.text.toString()
                NotesData.getNoteList()[indexVal].date = editDate.text.toString()
                NotesData.getNoteList()[indexVal].desc = editDesc.text.toString()
                //setFragmentResult(targetResultKey, Bundle.EMPTY)
            }
            setNegativeButton("Cancel") { _,_ -> }
        }
        return ad.create()
    }

    companion object {
        const val TAG = "NoteEditorDialog"
        private var existingData:Bundle = Bundle.EMPTY
        var indexVal:Int = -1
        private var importance: Boolean = false

        @JvmStatic
        fun newInstance(target:String, existing:Bundle, unique:Int, flag: Boolean) =
            NoteEditorDialog().apply {
                indexVal = unique
                targetResultKey = target
                existingData = existing
                importance = flag
            }
    }
}


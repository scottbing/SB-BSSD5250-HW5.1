package edu.nmhu.bssd5250.sb_bssd5250_hwk51

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.bundleOf
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [NoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var resultKey: String = ""
    private var setRedOn:  Boolean = false
    private var indexVal:Int = -1

    private lateinit var nameView: TextView
    private lateinit var dateView: TextView
    private lateinit var descView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    val buttonWidth:Int = 200
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        nameView = TextView(context).apply {
            setHint(R.string.name_place_holder)
            text = NotesData.getNoteList()[indexVal].name
        }
        dateView = TextView(context).apply {
            // Format today's date
            val current = LocalDateTime.now()
            //val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy. HH:mm:ss")
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            var strDate: String =  current.format(formatter)
            setText(strDate)
            text = NotesData.getNoteList()[indexVal].date
        }
        descView = TextView(context).apply {
            setHint(R.string.desc_place_holder)
            text = NotesData.getNoteList()[indexVal].desc
        }

        val textHolderLL = LinearLayoutCompat(requireContext()).apply {
            orientation = LinearLayoutCompat.VERTICAL
            addView(nameView)
            addView(dateView)
            addView(descView)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            (layoutParams as RelativeLayout.LayoutParams).addRule(
                RelativeLayout.ALIGN_PARENT_LEFT)
        }
        //End Text for the left side

        //Deletebutton on the right side
        val deleteButton = Button(requireContext()).apply {
            id = View.generateViewId()
            text = "Delete"
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            (layoutParams as RelativeLayout.LayoutParams).addRule(
                RelativeLayout.ALIGN_PARENT_RIGHT)

            setOnClickListener {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete Note?")
                    setPositiveButton("Yes", DialogInterface.OnClickListener{
                            dialogInterface, i ->
                            /*activity?.supportFragmentManager?.commit {
                                remove(this@NoteFragment)
                            }*/
                            NotesData.deleteNote(indexVal)
                    })
                    setNegativeButton("No", null) //do nothing if the say no
                    create()
                    show()
                }

            }
        }
        //End of editButton

        //Edit button on hte right side
        val editButton = Button(requireContext()).apply {
            text = "edit"
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            (layoutParams as RelativeLayout.LayoutParams).addRule(

                RelativeLayout.LEFT_OF, deleteButton.id)

            setOnClickListener {
                val currentData = bundleOf(
                    "name" to nameView.text,
                    "date" to dateView.text,
                    "desc" to descView.text
                )
                NoteEditorDialog.newInstance(resultKey, currentData, indexVal).show(
                    parentFragmentManager, NoteEditorDialog.TAG)
            }

        }
        //End of editButton


        val relativeLayout = RelativeLayout(requireContext()).apply {
            setBackgroundColor(Color.WHITE)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            addView(textHolderLL)
            addView(editButton)
            addView(deleteButton)
        }
        // Inflate the layout for this fragment
        return relativeLayout
    }

    companion object {

        private var importance: Boolean = false

        @JvmStatic
        fun newInstance(unique: Int, which: Boolean, flag: Boolean) =
            NoteFragment().apply {
                indexVal = unique
                resultKey = "NoteDataChange$unique"
                setRedOn = which
                importance = flag
            }
    }

    override fun onStart() {
        Log.d("NoteFragment", "onStart()")
        super.onStart()
        if (setRedOn)
            getView()?.setBackgroundColor(Color.RED)
    }
}
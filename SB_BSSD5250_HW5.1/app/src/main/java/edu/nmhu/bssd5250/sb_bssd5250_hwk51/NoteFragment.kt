package edu.nmhu.bssd5250.sb_bssd5250_hwk51


import android.annotation.SuppressLint
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
import androidx.fragment.app.setFragmentResultListener
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

        setFragmentResultListener(resultKey){ _, _ ->
            nameView.text = NotesData.getNoteList()[indexVal].name
            dateView.text = NotesData.getNoteList()[indexVal].date
            descView.text = NotesData.getNoteList()[indexVal].desc
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nameView = TextView(context).apply {
            setHint(R.string.name_place_holder)
            text = NotesData.getNoteList()[indexVal].name
        }
        dateView = TextView(context).apply {
            // Format today's date
            val current = LocalDateTime.now()
            //val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy. HH:mm:ss")
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            val strDate: String =  current.format(formatter)
            text = strDate
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
            text = context.getString(R.string.delete_button_text)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            (layoutParams as RelativeLayout.LayoutParams).addRule(
                RelativeLayout.ALIGN_PARENT_RIGHT)

            setOnClickListener {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete Note?")
                    setPositiveButton("Yes") { _, _ ->
                        /*activity?.supportFragmentManager?.commit {
                            remove(this@NoteFragment)
                        }*/
                        NotesData.deleteNote(indexVal)
                    }
                    setNegativeButton("No", null) //do nothing if the say no
                    create()
                    show()
                }

            }
        }
        //End of editButton

        //Edit button on hte right side
        val editButton = Button(requireContext()).apply {
            "edit".also { text = it }
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
                Log.i("Save- Name: ", nameView.text.toString())
                Log.i("Save - Date: ", dateView.text.toString())
                Log.i("Save - Desc: ", descView.text.toString())
                Log.i("Listen - setRedON", descView.text.toString())
                NoteEditorDialog.newInstance(resultKey, currentData, indexVal, setRedOn).show(
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

        @JvmStatic
        fun newInstance(unique: Int, which: Boolean, importance: Boolean) =
            NoteFragment().apply {
                indexVal = unique
                resultKey = "NoteDataChange$unique"
                setRedOn = which
                Log.i("which: ", which.toString())
            }
    }

    override fun onStart() {
        Log.d("NoteFragment", "onStart()")
        super.onStart()
        if (setRedOn)
            view?.setBackgroundColor(Color.RED)
    }
}


package com.example.secretsanta

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var sharedPreference: SharedPreferences
    private lateinit var optionsAdapter: OptionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreference = getSharedPreferences("com.example.app", MODE_PRIVATE)
        var options = grabOptions(sharedPreference)

        // Made recyclerview work
        optionsAdapter = OptionsAdapter(options, this)
        rvOptions.adapter = optionsAdapter
        rvOptions.layoutManager = LinearLayoutManager(this)

        // Make the dropdown list
        val spinner = findViewById<Spinner>(R.id.spinner)
        val items = arrayOf("Close Family", "Extended Family", "Friend/Other")
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            items
        )
        spinner.setAdapter(adapter)

        // Add Item
        btnAddOption.setOnClickListener {
            val optionTitle = etAddName.text.toString()
            val optionRelation = spinner.selectedItem.toString()

            if (optionTitle.isNotEmpty()) {
                val option = Option(optionTitle, optionRelation)
                optionsAdapter.addOption(option)
                etAddName.text.clear()
            }
        }

        // Select target
        btnSelect.setOnClickListener {
            pick(optionsAdapter.options)
        }
    }

    fun saveOptions(sharedPreference: SharedPreferences, options: MutableList<Option>) {
        var editor = sharedPreference.edit()
        if (options.size > 0) {
            editor.putString("option", options.toString())
        } else {
            editor.putString("option", mutableListOf<Option>().toString())
        }
        editor.commit()
    }

    private fun grabOptions(sharedPreference: SharedPreferences): MutableList<Option> {
        var options = mutableListOf<Option>()
        var items = sharedPreference.getString("option", "default")
        if (items!!.length > 25) {
            listOf(items
                .toString()
                .replace("[", "")
                .replace("Option(title=", "")
                .replace("relation=", "")
                .replace(")", "")
                .replace("]", "")
                .split(", ")).forEach{(name, relation) ->
                    options.add(Option(name, relation))
                }
        }
        return options
    }

    private fun pick(options: MutableList<Option>) {
        // Pick the person
        var rand: Int = (options.indices).random()
        var selection: Option = options[rand]

        val textView: TextView = findViewById(R.id.tvSecretSanta) as TextView

        // Display the selection
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Secret Santa Selection")
        builder.setMessage("You will be Secret Santa for ${selection.title}")

        builder.setPositiveButton(android.R.string.yes) { _, _ -> }
        builder.show()
    }

}
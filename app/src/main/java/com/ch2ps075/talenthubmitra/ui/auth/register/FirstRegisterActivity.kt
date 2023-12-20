package com.ch2ps075.talenthubmitra.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.ch2ps075.talenthubmitra.R
import com.ch2ps075.talenthubmitra.databinding.ActivityFirstRegisterBinding

class FirstRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstRegisterBinding
    private var selectedCategory: String? = null
    private var selectedGroupType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAutoComplete()

        with(binding) {
            nextRegister1Button.setOnClickListener {
                when {
                    edRegisterTalentname.text.toString().isEmpty() -> {
                        edRegisterTalentname.error = getString(R.string.error_empty_field)
                    }

                    edRegisterAddress.text.toString().isEmpty() -> {
                        edRegisterAddress.error = getString(R.string.error_empty_field)
                    }

                    edRegisterContact.text.toString().isEmpty() -> {
                        edRegisterContact.error = getString(R.string.error_empty_field)
                    }

                    edRegisterPrice.text.toString().isEmpty() -> {
                        edRegisterPrice.error = getString(R.string.error_empty_field)
                    }

                    else -> setupUI()
                }
            }
        }
    }

    private fun setupUI() {
        with(binding) {
            val talentName = edRegisterTalentname.text.toString()
            val address = edRegisterAddress.text.toString()
            val contact = edRegisterContact.text.toString()
            val price = edRegisterPrice.text.toString()

            val selectedCategory = selectedCategory
            val selectedGroupType = selectedGroupType

            if (!selectedCategory.isNullOrEmpty() && !selectedGroupType.isNullOrEmpty()) {
                val intent = Intent(this@FirstRegisterActivity, SecondRegisterActivity::class.java)
                val bundle = Bundle()

                bundle.putString(TALENT_NAME, talentName)
                bundle.putString(ADDRESS, address)
                bundle.putString(CONTACT, contact)
                bundle.putString(PRICE, price)
                bundle.putString(CATEGORY, selectedCategory)
                bundle.putString(GROUP_TYPE, selectedGroupType)

                intent.putExtras(bundle)
                startActivity(intent)
            } else {
                Toast.makeText(this@FirstRegisterActivity, getString(R.string.required_category_grouptype), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupAutoComplete() {
        setupAutoCompleteOptions(binding.autoCompleteCategory, R.array.category_titles)
        setupAutoCompleteOptions(binding.autoCompleteGrouptype, R.array.group_type_titles)
    }

    private fun setupAutoCompleteOptions(
        autoCompleteTextView: AutoCompleteTextView,
        arrayResId: Int,
    ) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(arrayResId)
        )
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedValue = adapter.getItem(position).toString()
            when (autoCompleteTextView) {
                binding.autoCompleteCategory -> {
                    selectedCategory = selectedValue
                }

                binding.autoCompleteGrouptype -> {
                    selectedGroupType = selectedValue
                }
            }
        }
    }

    companion object {
        const val TALENT_NAME = "TALENT_NAME"
        const val ADDRESS = "ADDRESS"
        const val CONTACT = "CONTACT"
        const val PRICE = "PRICE"
        const val CATEGORY = "CATEGORY"
        const val GROUP_TYPE = "GROUP_TYPE"
        const val EMAIL = "EMAIL"
        const val PASSWORD = "PASSWORD"
    }
}
package com.solution.citylogia

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.solution.citylogia.models.PlaceType
import com.solution.citylogia.network.RetrofitSingleton.retrofit
import com.solution.citylogia.network.api.IPlaceApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FiltersFragment : Fragment() {
    val typeArray = ArrayList<PlaceType>();
    val selectedTypes = ArrayList<PlaceType>()

    companion object {
        fun getNewInstance(args: Bundle?): FiltersFragment {
            val filtersFragment = FiltersFragment();
            println(args)
            filtersFragment.arguments = args;
            return filtersFragment;
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.filter_layout, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setUpButtons(view);
        this.loadTypes(view);
    }

    @SuppressLint("CheckResult")
    private fun loadTypes(view: View) {
        val placeApi = retrofit.create(IPlaceApi::class.java)

        placeApi.getPlaceTypes().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
            val types = response.data.elements;
            val typesLayout = view.findViewById<LinearLayout>(R.id.types_layout)

            types?.forEach { placeType ->
                val cricketerView = layoutInflater.inflate(R.layout.type_select_layout, null, false)
                val typeCheckBox = cricketerView.findViewById<CheckBox>(R.id.type_select_checkBox);
                typeCheckBox.text = placeType.name;
                typeCheckBox.setOnClickListener {
                    val checkBox = it as CheckBox;
                    if (checkBox.isChecked) {
                        this.selectedTypes.add(placeType)
                    }
                    else{
                        this.selectedTypes.remove(placeType)
                    }
                }
                typesLayout.addView(cricketerView);
            }
        }, {})

    }

    private fun setUpButtons(view: View) {
        val backBtn = view.findViewById<TextView>(R.id.filters_cancel)

        backBtn.setOnClickListener{
            this.goBack();
        }

        val doneBtn = view.findViewById<TextView>(R.id.filters_complete)

        doneBtn.setOnClickListener{
            this.goBack();
        }
    }

    private fun goBack() {
        println(this.selectedTypes);
        var activity = requireActivity() as MapActivity
        activity.intent.putExtra("12", "1212");
        requireActivity().supportFragmentManager
                .beginTransaction()
                .remove(this)
                .commit()

        setFragmentResult("filters_fragment_key", bundleOf("selected_types" to this.selectedTypes))
    }
}
package com.solution.citylogia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.solution.citylogia.network.RetrofitSingleton.retrofit
import com.solution.citylogia.network.api.IPlaceApi
import com.solution.citylogia.utils.Generator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FiltersFragment : Fragment() {

    companion object {
        fun newInstance(): FiltersFragment {
            return FiltersFragment();
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.filter_layout, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setUpButtons(view);
        this.loadTypes(view);
    }

    private fun loadTypes(view: View) {
        val placeApi = retrofit.create(IPlaceApi::class.java)

        placeApi.getPlaceTypes().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            val types = it.data.elements;
            val typesLayout = view.findViewById<LinearLayout>(R.id.types_layout)

            types?.forEach{
                val cricketerView = layoutInflater.inflate(R.layout.type_select_layout, null, false)
                val typeCheckBox = cricketerView.findViewById<CheckBox>(R.id.type_select_checkBox);
                typeCheckBox.text = it.name;
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
        requireActivity().supportFragmentManager
                .beginTransaction()
                .remove(this)
                .commit()
    }
}
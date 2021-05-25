package com.solution.citylogia

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.solution.citylogia.models.PlaceType
import com.solution.citylogia.network.RetrofitSingleton.retrofit
import com.solution.citylogia.network.api.IPlaceApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FiltersFragment : Fragment(), SeekBar.OnSeekBarChangeListener {
    private var selectedTypes: ArrayList<PlaceType>? = ArrayList()
    private var radius: Int = 10

    companion object {
        fun getNewInstance(args: Bundle?): FiltersFragment {
            val filtersFragment = FiltersFragment();
            println(args)
            filtersFragment.arguments = args;
            return filtersFragment;
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.filter_layout, container, false)

        visiblePanels(false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.selectedTypes = arguments?.getSerializable("selected_types") as ArrayList<PlaceType>
        this.radius = arguments?.getSerializable("selected_radius") as Int
        super.onViewCreated(view, savedInstanceState)
        this.setUpButtons(view)
        this.loadTypes(view)
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

                if (this.selectedTypes?.contains(placeType) == true){
                    typeCheckBox.isChecked = true;
                }

                typeCheckBox.setOnClickListener {
                    val checkBox = it as CheckBox;
                    if (checkBox.isChecked) {
                        this.selectedTypes?.add(placeType)
                    }
                    else{
                        this.selectedTypes?.remove(placeType)
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
            setFragmentResult("filters_fragment_apply", bundleOf("selected_types" to this.selectedTypes, "radius" to this.radius))
        }

        val rangeSeekBar = view.findViewById<SeekBar>(R.id.filter_seekBar)
        view?.findViewById<TextView>(R.id.filter_distance)!!.text = this.radius.toString();
        rangeSeekBar.progress = this.radius
        rangeSeekBar.setOnSeekBarChangeListener(this)
    }

    private fun goBack() {
        println(this.selectedTypes);
        var activity = requireActivity() as MapActivity
        activity.intent.putExtra("12", "1212");
        requireActivity().supportFragmentManager
                .beginTransaction()
                .remove(this)
                .commit()
        visiblePanels(true)
    }

    private fun visiblePanels(isVisible: Boolean) {
        val filterPanel: LinearLayout = requireActivity().findViewById(R.id.maps_tools)
        filterPanel.isVisible = isVisible
        val menuPanel: LinearLayout = requireActivity().findViewById(R.id.menu)
        menuPanel.isVisible = isVisible
        val btnPanel: LinearLayout = requireActivity().findViewById(R.id.btn_panel)
        btnPanel.isVisible = isVisible
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        this.radius = progress
        this.view?.findViewById<TextView>(R.id.filter_distance)!!.text = progress.toString();
    }


    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        println("start tracking touch")
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        println("stop tracking touch")
    }
}
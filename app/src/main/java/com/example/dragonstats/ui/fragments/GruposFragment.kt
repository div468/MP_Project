package com.example.dragonstats.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dragonstats.ui.fragments.BracketStageFragment
import com.example.dragonstats.ui.fragments.FaseGruposFragment
import com.example.dragonstats.R


class GruposFragment : Fragment() {

    private lateinit var tabFaseGrupos: TextView
    private lateinit var tabBracketStage: TextView
    private var currentTab = true // true = FaseGrupos, false = BracketStage

    companion object {
        private const val KEY_CURRENT_TAB = "current_tab"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grupos_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restaurar el estado si existe
        savedInstanceState?.let {
            currentTab = it.getBoolean(KEY_CURRENT_TAB, true)
        }

        setupTabs(view)

        // Mostrar el fragment correcto basado en el estado
        if (savedInstanceState == null) {
            showCurrentTab()
        }

        // Asegurar que el tab visual esté sincronizado
        selectTab(currentTab)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_CURRENT_TAB, currentTab)
    }

    private fun setupTabs(view: View) {
        tabFaseGrupos = view.findViewById(R.id.tabFaseGrupos)
        tabBracketStage = view.findViewById(R.id.tabBracketStage)

        tabFaseGrupos.setOnClickListener {
            currentTab = true
            selectTab(true)
            showFaseGrupos()
        }

        tabBracketStage.setOnClickListener {
            currentTab = false
            selectTab(false)
            showBracketStage()
        }
    }

    private fun selectTab(isFaseGrupos: Boolean) {
        if (isFaseGrupos) {
            tabFaseGrupos.setBackgroundResource(R.drawable.tab_selected_background)
            tabBracketStage.setBackgroundResource(R.drawable.tab_unselected_background)
        } else {
            tabFaseGrupos.setBackgroundResource(R.drawable.tab_unselected_background)
            tabBracketStage.setBackgroundResource(R.drawable.tab_selected_background)
        }
    }

    private fun showCurrentTab() {
        if (currentTab) {
            showFaseGrupos()
        } else {
            showBracketStage()
        }
    }

    private fun showFaseGrupos() {
        val fragment = FaseGruposFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.grupos_content_container, fragment)
            .commit()
    }

    private fun showBracketStage() {
        val fragment = BracketStageFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.grupos_content_container, fragment)
            .commit()
    }
}
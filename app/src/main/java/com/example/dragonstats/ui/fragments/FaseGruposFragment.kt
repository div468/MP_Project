package com.example.dragonstats.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonstats.data.TorneoData
import com.example.dragonstats.R
import com.example.dragonstats.ui.adapters.GruposAdapter

class FaseGruposFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GruposAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fase_grupos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)
        loadData()
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewGrupos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val grupos = TorneoData.obtenerGrupos()
        adapter = GruposAdapter(grupos)
        recyclerView.adapter = adapter
    }

    private fun loadData() {
        // Los datos ya se cargan en setupRecyclerView
        // Aquí podriasmos conectar una API
    }
}
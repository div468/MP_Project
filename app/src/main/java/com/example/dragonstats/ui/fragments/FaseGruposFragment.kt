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
import com.example.dragonstats.data.Grupo
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

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        // Mejorar el rendimiento del RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = null // Desactivar animaciones para mejor rendimiento

        val grupos = TorneoData.obtenerGrupos()
        adapter = GruposAdapter(grupos)
        recyclerView.adapter = adapter
    }

    private fun loadData() {
        // Simular carga de datos
        // En una implementación real, aquí cargarías datos desde una API
        // y actualizarías el adapter
    }

    // Método para refrescar datos
    fun refreshData() {
        val nuevosGrupos = TorneoData.obtenerGrupos()
        adapter.updateData(nuevosGrupos)
    }
}

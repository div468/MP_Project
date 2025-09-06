package com.example.dragonstats.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonstats.R
import com.example.dragonstats.data.Grupo

class GruposAdapter(private var grupos: List<Grupo>) : RecyclerView.Adapter<GruposAdapter.GrupoViewHolder>() {

    class GrupoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreGrupo: TextView = view.findViewById(R.id.tvNombreGrupo)
        val recyclerViewEquipos: RecyclerView = view.findViewById(R.id.recyclerViewEquipos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrupoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grupo, parent, false)
        return GrupoViewHolder(view)
    }

    override fun onBindViewHolder(holder: GrupoViewHolder, position: Int) {
        val grupo = grupos[position]

        holder.nombreGrupo.text = grupo.nombre

        // Configurar RecyclerView para equipos
        val equiposAdapter = EquiposAdapter(grupo.equipos)
        holder.recyclerViewEquipos.apply {
            layoutManager = LinearLayoutManager(holder.itemView.context)
            adapter = equiposAdapter
            // Desactivar scroll anidado para mejor rendimiento
            isNestedScrollingEnabled = false
        }
    }

    override fun getItemCount() = grupos.size

    // Método para actualizar datos
    fun updateData(nuevosGrupos: List<Grupo>) {
        grupos = nuevosGrupos
        notifyDataSetChanged()
    }
}
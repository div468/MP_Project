package com.example.dragonstats.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonstats.R
import com.example.dragonstats.data.Equipo

class EquiposAdapter(private val equipos: List<Equipo>) : RecyclerView.Adapter<EquiposAdapter.EquipoViewHolder>() {

    class EquipoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val posicion: TextView = view.findViewById(R.id.tvPosicion)
        val iconoEquipo: ImageView = view.findViewById(R.id.ivIconoEquipo)
        val nombreEquipo: TextView = view.findViewById(R.id.tvNombreEquipo)
        val partidos: TextView = view.findViewById(R.id.tvPartidos)
        val golDiferencia: TextView = view.findViewById(R.id.tvGolDiferencia)
        val puntos: TextView = view.findViewById(R.id.tvPuntos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_equipo, parent, false)
        return EquipoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        val equipo = equipos[position]

        holder.posicion.text = (position + 1).toString()
        holder.nombreEquipo.text = equipo.nombre
        holder.partidos.text = equipo.partidos.toString()
        holder.golDiferencia.text = equipo.golDiferencia.toString()
        holder.puntos.text = equipo.puntos.toString()

        // Usar icono genérico o asignar iconos específicos según el equipo
        holder.iconoEquipo.setImageResource(R.drawable.ic_equipo_default)
    }

    override fun getItemCount() = equipos.size
}
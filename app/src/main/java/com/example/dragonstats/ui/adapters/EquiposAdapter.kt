package com.example.dragonstats.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonstats.R
import com.example.dragonstats.data.Equipo

class EquiposAdapter(private var equipos: List<Equipo>) : RecyclerView.Adapter<EquiposAdapter.EquipoViewHolder>() {

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
        val posicionActual = position + 1

        holder.posicion.text = posicionActual.toString()
        holder.nombreEquipo.text = equipo.nombre
        holder.partidos.text = equipo.partidos.toString()
        holder.golDiferencia.text = equipo.golDiferencia.toString()
        holder.puntos.text = equipo.puntos.toString()

        // Cambiar color del icono según la posición (clasificación)
        val iconColor = when (posicionActual) {
            1, 2 -> ContextCompat.getColor(holder.itemView.context, R.color.green_primary) // Clasifican
            3, 4 -> ContextCompat.getColor(holder.itemView.context, android.R.color.holo_orange_light) // En peligro
            else -> ContextCompat.getColor(holder.itemView.context, android.R.color.holo_red_light) // Eliminados
        }

        holder.iconoEquipo.setColorFilter(iconColor)
        holder.iconoEquipo.setImageResource(R.drawable.ic_equipo_default)

        // Resaltar al líder del grupo
        if (posicionActual == 1) {
            holder.nombreEquipo.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green_primary))
        } else {
            holder.nombreEquipo.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
        }
    }

    override fun getItemCount() = equipos.size

    // Método para actualizar datos
    fun updateData(nuevosEquipos: List<Equipo>) {
        equipos = nuevosEquipos
        notifyDataSetChanged()
    }
}
package com.example.dragonstats.utils

import com.example.dragonstats.R

object EquipoLogoHelper {

    /**
     * Mapea el nombre del equipo a su recurso drawable correspondiente
     */
    fun getLogoResource(nombreEquipo: String): Int {
        return when (nombreEquipo.trim()) {
            "Pikos fc" -> R.drawable.pikos_fc
            "pikos fc" -> R.drawable.pikos_fc
            "Inter Galaxi" -> R.drawable.inter_galaxi
            "Siervas" -> R.drawable.siervas_fc
            "Ixbalanque" -> R.drawable.ixbalanque
            "Chilacayotes" -> R.drawable.chilacayotes
            "Cuxecitos" -> R.drawable.cuxecitos
            "El asilo" -> R.drawable.el_asilo
            "Hombres de Negro" -> R.drawable.hombres_de_negro
            "El Barrio" -> R.drawable.el_barrio
            "Xell select" -> R.drawable.xell_select
            "Panitos" -> R.drawable.panitos_fc
            "Inter de mi casa" -> R.drawable.inter_de_mi_casa
            "Atletico Pusca" -> R.drawable.atletico_pusca
            "Belixe" -> R.drawable.belixe_fc
            "Los Acabados" -> R.drawable.los_acabados
            "Ptrago" -> R.drawable.ptrago
            "Fresitas" -> R.drawable.fresitas
            "Pinchazo" -> R.drawable.pinchazo
            "Fenix" -> R.drawable.fenix
            "Reserva de cabros" -> R.drawable.reserva_de_cabros
            "Rabanos" -> R.drawable.r_banos
            "Chicacao" -> R.drawable.chicacao_fc
            "Caríanos" -> R.drawable.carianos
            "All in" -> R.drawable.all_in_fc
            else -> R.drawable.ic_equipo_default // Logo por defecto
        }
    }

    /**
     * Obtiene las iniciales del equipo para usar como fallback
     */
    fun getInitials(nombreEquipo: String): String {
        val words = nombreEquipo.split(' ').filter { it.isNotEmpty() }
        return if (words.size == 2) {
            (words[0].take(1) + words[1].take(2)).uppercase()
        } else {
            nombreEquipo.take(3).uppercase()
        }
    }
}
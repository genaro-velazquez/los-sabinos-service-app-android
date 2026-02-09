package com.lossabinos.serviceapp.models.ui.enums

enum class IssueCategoryTypeUIModel(val label: String) {
    MECHANICAL("Mecánica"),
    ELECTRICAL("Eléctrica"),
    //BODYWORK("Carrocería"),
    STRUCTURAL("Estructural"),
    //SAFETY("Seguridad"),
    AESTHETIC("Estético"),
    SAFETY("Seguridad"),
    OTHER("Otros")
}

// para el servicio report-issue
//(mechanical|electrical|structural|aesthetic|safety|other)
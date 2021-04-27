package com.solution.citylogia.utils

import com.solution.citylogia.models.ShortPlace

fun List<ShortPlace>.getNearest(): ShortPlace? {
    return this.filter { p->p.distanceTo != null }.minByOrNull { p -> p.distanceTo!! }
}
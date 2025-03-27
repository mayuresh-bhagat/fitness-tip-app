package com.example.fitnesstips.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class FitnessTip(
    @StringRes val title: Int,
    @StringRes val description: Int,
    @DrawableRes val image: Int
)

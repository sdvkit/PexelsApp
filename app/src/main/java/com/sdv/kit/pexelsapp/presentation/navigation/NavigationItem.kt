package com.sdv.kit.pexelsapp.presentation.navigation

import androidx.annotation.DrawableRes

data class NavigationItem(
    val name: String,
    @DrawableRes val icon: Int,
    @DrawableRes val activeIcon: Int,
    val onClick: () -> Unit
)
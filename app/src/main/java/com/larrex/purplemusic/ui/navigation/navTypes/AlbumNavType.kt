package com.larrex.purplemusic.ui.navigation.navTypes

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.larrex.purplemusic.domain.model.AlbumItem


class AlbumNavType : NavType<AlbumItem>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): AlbumItem? {
       return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): AlbumItem {
       return Gson().fromJson(value,AlbumItem::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: AlbumItem) {
       bundle.putParcelable(key,value)
    }
}
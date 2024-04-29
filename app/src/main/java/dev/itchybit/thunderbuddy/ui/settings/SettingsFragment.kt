package dev.itchybit.thunderbuddy.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import dev.itchybit.thunderbuddy.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) =
        setPreferencesFromResource(R.xml.preferences, rootKey)
}
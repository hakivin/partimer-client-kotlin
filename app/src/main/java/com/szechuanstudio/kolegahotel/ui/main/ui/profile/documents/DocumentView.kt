package com.szechuanstudio.kolegahotel.ui.main.ui.profile.documents

import com.szechuanstudio.kolegahotel.data.model.Model

interface DocumentView {
    fun showDocuments(profile: Model.Profile?)
    fun imageUploaded()
}
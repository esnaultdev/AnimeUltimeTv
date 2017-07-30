package blue.aodev.animeultimetv.presentation.common

import android.support.v17.leanback.widget.Presenter
import android.view.LayoutInflater
import android.view.ViewGroup
import blue.aodev.animeultimetv.R

class LoadingPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_loading, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {}

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {}

}
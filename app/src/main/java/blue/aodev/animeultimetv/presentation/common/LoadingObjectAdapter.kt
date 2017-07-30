package blue.aodev.animeultimetv.presentation.common

import android.support.v17.leanback.widget.ObjectAdapter


class LoadingObjectAdapter : ObjectAdapter(LoadingPresenter()) {

    override fun size(): Int {
        return 1
    }

    override fun get(position: Int): Any {
        return Unit
    }
}
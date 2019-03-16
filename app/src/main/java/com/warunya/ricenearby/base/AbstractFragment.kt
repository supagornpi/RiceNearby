package com.warunya.ricenearby.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.warunya.ricenearby.customs.FragmentNavigation
import kotlinx.android.synthetic.main.layout_action_bar.*

abstract class AbstractFragment : Fragment() {

    var fragmentNavigation: FragmentNavigation? = null

    protected abstract fun setLayoutView(): Int
    protected abstract fun setupView(view: View)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(setLayoutView(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (parentFragment != null && parentFragment is FragmentNavigation) {
            fragmentNavigation = parentFragment as FragmentNavigation
        }
    }

    fun setTitle(stringId: Int) {
        setTitle(context?.resources!!.getString(stringId))
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }
}
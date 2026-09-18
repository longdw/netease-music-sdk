package com.smart.neteasemusic.business.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

/**
 * @author david.long 2026/3/5 09:53
 */
abstract class BaseFragment<V: ViewBinding, M: BaseViewModel> : Fragment() {

    protected val vm: M by lazy {
        val viewModel = ViewModelProvider(this)[getModelClass() ?: throw IllegalStateException("The return value of getModelClass() cannot be null")]
        viewModel
    }
    private var _binding: V? = null
    protected val viewBinding: V
        get() = _binding ?: throw IllegalStateException("ViewBinding is only valid between onCreateView and onDestroyView")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): V
    protected abstract fun initView()
    protected open fun getModelClass(): Class<M>? = null

    open fun enterAnimation() = 0

    open fun exitAnimation() = 0

    open fun popEnterAnimation() = 0

    open fun popExitAnimation() = 0
}
package com.smart.neteasemusic.business.util

import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.smart.neteasemusic.business.base.BaseFragment
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil

/**
 * AFragment-->BFragment 调用startFragment
 * BFragment-->AFragment 调用finishFragment
 * @author david.long 2024/12/10 13:56
*/
object FragmentNavigator {

    private val switchingContainers = mutableSetOf<Int>()

    /**
     * 切换 Fragment
     * @param manager Fragment 管理器
     * @param fragmentClass Fragment 的 Class
     * @param containerId 容器 ID
     * @param args 传递的参数，可选
     */
    fun startFragment(
        manager: FragmentManager,
        fragmentClass: Class<out Fragment>,
        containerId: Int,
        args: Bundle? = null,
        isAddToBackStack: Boolean = true,
        customTag: String? = null
    ) {
        check(Looper.myLooper() == Looper.getMainLooper())
        SDKLogUtil.i("[FragmentNavigator][startFragment] containerId=$containerId")
        if (switchingContainers.contains(containerId)) {
            SDKLogUtil.i("[FragmentNavigator][startFragment] Fragment switching in progress, ignoring.")
            return
        }

        switchingContainers.add(containerId)

        val tag = customTag ?: fragmentClass.name
        var fragment = manager.findFragmentByTag(tag)
        val transaction = manager.beginTransaction()

        try {
            if (fragment == null) {
                // 创建新的 Fragment 实例
                fragment = manager.fragmentFactory.instantiate(fragmentClass.classLoader!!, fragmentClass.name)
//                fragment = fragmentClass.getDeclaredConstructor().newInstance()
                if (args != null) {
                    fragment.arguments = args
                }
                if (fragment is BaseFragment<*, *>) {
                    // 设置 Fragment 动画
                    transaction.setCustomAnimations(
                        fragment.enterAnimation(),
                        fragment.exitAnimation(),
                        fragment.popEnterAnimation(),
                        fragment.popExitAnimation()
                    )
                }
                if (isAddToBackStack) {
                    // 是否需要加入回退栈
                    transaction.addToBackStack(tag)
                }
                transaction.add(containerId, fragment, tag)
            } else {
                when {
                    //Fragment 已存在的处理逻辑
                    !fragment.isAdded -> transaction.add(containerId, fragment, tag)
                    fragment.isAdded && fragment.isHidden -> transaction.show(fragment)
                }
            }

            // 隐藏其他 Fragment
            hideBeforeFragment(manager, transaction, fragment)
            transaction.commitAllowingStateLoss()
            manager.executePendingTransactions()
        } catch (e: Exception) {
            SDKLogUtil.e("[FragmentNavigator][startFragment] Error switching fragment: $tag", e)
        } finally {
            SDKLogUtil.i("[FragmentNavigator][startFragment] remove containerId-->$containerId")
            switchingContainers.remove(containerId)
        }
    }

    /**
     * 结束当前 Fragment
     * @param manager Fragment 管理器
     */
    fun finishFragment(manager: FragmentManager) {
        check(Looper.myLooper() == Looper.getMainLooper())
        if (manager.backStackEntryCount > 0) {
            manager.popBackStack()
        }
    }

    /**
     * 栈顶Fragment及其下面所有的Fragment都会被弹出。
     * @param manager Fragment 管理器
     * @return 是否成功完成所有 Fragment
     */
    fun finishAllFragments(manager: FragmentManager, filterClsList: List<Class<out Fragment>>? = null): Boolean {
        check(Looper.myLooper() == Looper.getMainLooper())
        val result = manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        SDKLogUtil.i("[FragmentNavigator][finishAllFragments] result=$result")

        if (!result) {
            val transaction = manager.beginTransaction()
            manager.fragments.forEach {
                if (filterClsList?.contains(it.javaClass) == true) return@forEach
                if (it.isAdded) transaction.remove(it)
            }
            transaction.commitAllowingStateLoss()
        }

        return result
    }

    /**
     * 隐藏除当前 Fragment 外的其他 Fragment
     * @param manager Fragment 管理器
     * @param transaction Fragment 事务
     * @param currentFragment 当前显示的 Fragment
     */
    private fun hideBeforeFragment(
        manager: FragmentManager,
        transaction: FragmentTransaction,
        currentFragment: Fragment?
    ) {
        check(Looper.myLooper() == Looper.getMainLooper())
        manager.fragments.forEach { fragment ->
            if (fragment != currentFragment && fragment.isAdded && !fragment.isHidden) {
                transaction.hide(fragment)
            }
        }
    }
}
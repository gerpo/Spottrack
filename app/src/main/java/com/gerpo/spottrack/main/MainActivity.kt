package com.gerpo.spottrack.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gerpo.spottrack.R
import com.gerpo.spottrack.main.MainContract.MainView
import com.gerpo.spottrack.overview.OverviewFragment
import com.gerpo.spottrack.search.SearchFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), MainView {

    @Inject
    lateinit var presenter: MainContract.MainPresenter

    @Inject
    lateinit var searchFragment: SearchFragment

    @Inject
    lateinit var overviewFragment: OverviewFragment

    private var navFragments: ArrayList<Fragment> = ArrayList(3)

    private var active: Int = 0

    var backStack: Deque<Int> = ArrayDeque(3)
    var isBackPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buildFragmentsList()

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_overview -> {
                    if (!isBackPressed) {
                        pushFragmentIntoStack(R.id.action_overview);
                    }
                    switchFragment(active, 0)
                    active = 0
                    it.isChecked = true
                }
                R.id.action_search -> {
                    if (!isBackPressed) {
                        pushFragmentIntoStack(R.id.action_search);
                    }
                    switchFragment(active, 1)
                    active = 1
                    it.isChecked = true
                }
                R.id.action_settings -> {
                    if (!isBackPressed) {
                        pushFragmentIntoStack(R.id.action_settings);
                    }
                    switchFragment(active, 2)
                    active = 2
                    it.isChecked = true
                }
            }
            true

        }


        pushFragmentIntoStack(R.id.action_overview)
    }

    private fun buildFragmentsList() {

        val settingsFragment = OverviewFragment()

        initFragment(overviewFragment, TAG_FRAGMENT_OVERVIEW, true)
        initFragment(searchFragment, TAG_FRAGMENT_SEARCH)
        initFragment(settingsFragment, TAG_FRAGMENT_SETTINGS)

        this.navFragments.add(overviewFragment)
        this.navFragments.add(searchFragment)
        this.navFragments.add(settingsFragment)
    }

    private fun initFragment(fragment: Fragment, tag: String, show: Boolean = false) {
        val newFragmentTransaction = this.supportFragmentManager
                .beginTransaction()
                .add(R.id.content_frame, fragment, tag)

        if (show) {
            newFragmentTransaction.commit()
            return
        }

        newFragmentTransaction.hide(fragment).commit()
    }

    override fun onBackPressed() {
        if (backStack.size > 1) {
            isBackPressed = true;
            backStack.pop();
            bottom_navigation.setSelectedItemId(backStack.peek());
        } else {
            this.finish()
        }
    }


    private fun switchFragment(old: Int, new: Int) {
        this.supportFragmentManager
                .beginTransaction()
                .hide(this.navFragments[old])
                .show(this.navFragments[new])
                .commit()
        this.navFragments[new].onResume()
    }

    private fun pushFragmentIntoStack(id: Int) {
        if (backStack.size < 3) {
            backStack.push(id)
        } else {
            backStack.removeLast()
            backStack.push(id)
        }
    }

    companion object {
        private const val TAG_FRAGMENT_OVERVIEW: String = "tag_fragment_overview"
        private const val TAG_FRAGMENT_SEARCH: String = "tag_fragment_search"
        private const val TAG_FRAGMENT_SETTINGS: String = "tag_fragment_settings"
    }
}
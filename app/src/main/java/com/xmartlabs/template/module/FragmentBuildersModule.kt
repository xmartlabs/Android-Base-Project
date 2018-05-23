package com.xmartlabs.template.module

import com.xmartlabs.bigbang.ui.BaseFragment
import com.xmartlabs.bigbang.ui.BaseFragment_MembersInjector
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeBaseFragment(): BaseFragment
}

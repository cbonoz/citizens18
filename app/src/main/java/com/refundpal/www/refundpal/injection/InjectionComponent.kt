package com.refundpal.www.refundpal.injection

import javax.inject.Singleton

import dagger.Component
import com.refundpal.www.refundpal.activities.MainActivity
import com.refundpal.www.refundpal.activities.SplashActivity

@Singleton
@Component(modules = arrayOf(RefundModule::class))
interface InjectionComponent {

    // Activities
//    fun inject(activity: LoginActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: SplashActivity)

    // Fragments
//    fun inject(favoritesFragment: FavoritesFragment)
//    fun inject(genomeFragment: RefundFragment)
//    fun inject(recipeFragment: RecipeFragment)
}

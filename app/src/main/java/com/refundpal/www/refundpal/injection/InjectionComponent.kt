package com.refundpal.www.refundpal.injection

import com.refundpal.www.refundpal.activities.LoginActivity
import javax.inject.Singleton

import dagger.Component
import com.refundpal.www.refundpal.activities.MainActivity
import com.refundpal.www.refundpal.activities.SplashActivity
import com.refundpal.www.refundpal.fragments.HomeFragment
import com.refundpal.www.refundpal.fragments.ProfileFragment
import com.refundpal.www.refundpal.fragments.QuestionFragment

@Singleton
@Component(modules = arrayOf(RefundModule::class))
interface InjectionComponent {

    // Activities
//    fun inject(activity: LoginActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: SplashActivity)
    fun inject(questionFragment: QuestionFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(loginActivity: LoginActivity)

    // Fragments
//    fun inject(favoritesFragment: FavoritesFragment)
//    fun inject(genomeFragment: RefundFragment)
//    fun inject(recipeFragment: RecipeFragment)
}

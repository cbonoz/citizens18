package com.refundpal.www.refundpal.activities

import android.content.Intent

import com.daimajia.androidanimations.library.Techniques
import com.viksaa.sssplash.lib.activity.AwesomeSplash
import com.viksaa.sssplash.lib.cnst.Flags
import com.viksaa.sssplash.lib.model.ConfigSplash
import timber.log.Timber
import com.refundpal.www.refundpal.BuildConfig
import com.refundpal.www.refundpal.RefundApplication
import com.refundpal.www.refundpal.R
import com.refundpal.www.refundpal.util.UserSessionManager

import javax.inject.Inject
import com.afollestad.materialdialogs.MaterialDialog
import android.text.InputType
import android.util.Log
import android.widget.Toast
import com.refundpal.www.refundpal.models.User


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : AwesomeSplash() {

    @Inject
    lateinit var userSessionManager: UserSessionManager

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!
    override fun initSplash(configSplash: ConfigSplash) {
        /* you don't have to override every property */
        RefundApplication.injectionComponent.inject(this)

        val duration: Int
        if (BuildConfig.DEBUG) {
            duration = 500
        } else {
            // Production duration for into splash screen animations.
            duration = 1000
        }
        Timber.d("Splash duration: %s", duration)

        //Customize Circular Reveal
        configSplash.backgroundColor = R.color.md_brown_100
        configSplash.animCircularRevealDuration = duration //int ms
        configSplash.revealFlagX = Flags.REVEAL_LEFT  //or Flags.REVEAL_RIGHT
        configSplash.revealFlagY = Flags.REVEAL_TOP //or Flags.REVEAL_BOTTOM

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.logoSplash = R.drawable.props_170
        configSplash.animLogoSplashDuration = duration //int ms
        configSplash.animLogoSplashTechnique = Techniques.FadeIn //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Title
        configSplash.titleSplash = getString(R.string.slogan)
        configSplash.titleTextColor = R.color.md_brown_500
        configSplash.titleTextSize = 18f //float value
        configSplash.animTitleDuration = duration
        configSplash.animTitleTechnique = Techniques.FadeInDown
        // configSplash.setTitleFont("fonts/myfont.ttf"); //provide string to your font located in assets/fonts/
    }

    override fun animationsFinished() {
        // Transit to another activity here or perform other actions.
        val intent: Intent
        if (userSessionManager.hasLoggedInUser()) {
             intent = Intent(this, MainActivity::class.java)
        } else {
            intent =  Intent(this, LoginActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

    }

}

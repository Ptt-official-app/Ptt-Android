package tw.y_studio.ptt.UI.common.extension

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import tw.y_studio.ptt.R

fun NavController.defaultNavigateForward(resId: Int) = navigateForward(
    resId,
    useDefaultAnim = false
)

fun NavController.defaultNavigateForward(
    resId: Int,
    args: Bundle? = null,
    isSingleTop: Boolean = true
) = navigateForward(resId, args, isSingleTop, useDefaultAnim = true)

fun NavController.navigateForward(
    resId: Int,
    args: Bundle? = null,
    isSingleTop: Boolean = true,
    useDefaultAnim: Boolean = false
) =
    navigate(
        resId,
        args,
        NavOptions.Builder()
            .setLaunchSingleTop(isSingleTop)
            .setDefaultAnim(useDefaultAnim)
            .build()
    )

fun NavOptions.Builder.setDefaultAnim(useDefaultAnim: Boolean) = apply {
    if (useDefaultAnim) {
        setEnterAnim(R.anim.slide_in_right_250)
        setExitAnim(R.anim.fade_out)
        setPopEnterAnim(R.anim.fade_in)
        setPopExitAnim(R.anim.slide_out_right_250)
    }
}

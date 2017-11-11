package danielweidensdoerfer.com.reaction;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class ViewAnimUtils {

    public static void translateYAlphaAnimIn(float translateY, long delay, View... views) {
        if(views == null || views.length == 0 || delay < 0) return;
        for(View view : views) {
            if(view == null) return;
            view.setTranslationY(translateY);
            view.setAlpha(0);
            view.animate()
                    .setStartDelay(delay)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(300)
                    .alpha(1)
                    .translationY(0)
                    .start();
        }
    }

    public static void translateYAlphaAnimOut(float translateY, long delay, long duration, View... views) {
        if(views == null || views.length == 0 || delay < 0) return;
        for (View view : views) {
            view.animate()
                    .setStartDelay(delay)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(duration)
                    .alpha(0)
                    .translationY(translateY)
                    .start();
        }
    }
}

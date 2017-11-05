package danielweidensdoerfer.com.reaction;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class ViewAnimUtils {

    public static void translateYAlphaAnim(View view, long delay) {
        if(view == null || delay < 0) return;
        float translateY = view.getHeight();
        view.setTranslationY(-translateY/4);
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

package pl.aprilapps.switcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;

/**
 * Created by Jacek Kwiecień on 15.04.15.
 */
public class BlurUtils {

    public static Bitmap takeViewScreenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.draw(c);
        return bitmap;
    }

    public static Bitmap takeBlurredScreenshot(View view) {
        return blurBitmap(takeViewScreenShot(view), view.getContext());
    }

    public static Bitmap blurBitmap(Bitmap bitmapToBlur, Context context) {
        RenderScript rs = RenderScript.create(context);

        //use this constructor for best performance, because it uses USAGE_SHARED mode which reuses memory
        final Allocation input = Allocation.createFromBitmap(rs, bitmapToBlur);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(16f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmapToBlur);
        return bitmapToBlur;
    }
}

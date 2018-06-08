package utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by supanonymous on 06/06/18.
 */

public class DecoderUtil {
    public static Bitmap decodeBase64(String encodedDataString){
        encodedDataString = encodedDataString.replace("data:image/png;base64,","");
        byte[] imageAsBytes = Base64.decode(encodedDataString.getBytes(), 0);
        return BitmapFactory.decodeByteArray(
                imageAsBytes, 0, imageAsBytes.length);
    }
}

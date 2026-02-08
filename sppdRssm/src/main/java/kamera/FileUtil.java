package kamera;

import android.content.Context;
import android.graphics.*;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;

import java.io.*;
import java.util.Objects;

public class FileUtil {

    public static File compressAndRotate(
            Context c,
            Uri uri,
            int maxWidth,
            int maxFileSizeKB
    ) throws IOException {

        InputStream is = c.getContentResolver().openInputStream(uri);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, opts);
        Objects.requireNonNull(is).close();

        // Hitung sample size
        opts.inSampleSize = calculateInSampleSize(opts, maxWidth);
        opts.inJustDecodeBounds = false;

        is = c.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
        Objects.requireNonNull(is).close();

        // Rotate EXIF
        bitmap = rotateIfRequired(c, bitmap, uri);

        File outFile = new File(c.getCacheDir(), "upload.jpg");

        int quality = 90;
        do {
            FileOutputStream fos = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.close();
            quality -= 5;
        } while (outFile.length() / 1024 > maxFileSizeKB && quality > 70);

        return outFile;
    }

    private static int calculateInSampleSize(BitmapFactory.Options opts, int reqWidth) {
        int width = opts.outWidth;
        int inSampleSize = 1;
        while (width / inSampleSize > reqWidth) {
            inSampleSize *= 2;
        }
        return inSampleSize;
    }

    private static Bitmap rotateIfRequired(Context c, Bitmap bitmap, Uri uri) throws IOException {
        InputStream is = c.getContentResolver().openInputStream(uri);
        ExifInterface exif = new ExifInterface(Objects.requireNonNull(is));
        is.close();

        int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
        );

        Matrix m = new Matrix();
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) m.postRotate(90);
        else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) m.postRotate(180);
        else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) m.postRotate(270);

        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), m, true);
    }
}

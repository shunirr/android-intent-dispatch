package jp.s5r.android.intentdispatch.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class ImageUtil {
  private ImageUtil() {}

  public static Bitmap createBitmapFromJpeg(File jpegFile) {
    return BitmapFactory.decodeFile(jpegFile.getAbsolutePath());
  }

  public static void saveBinary(File path, byte[] binaryData) throws IOException {
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(path);
      out.write(binaryData);
    } finally {
      if (out != null) {
        out.close();
      }
    }
  }

  public static void saveImage(File path, Bitmap bmp) throws IOException {
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(path);
      bmp.compress(Bitmap.CompressFormat.JPEG, 95, out);
      out.flush();
    } finally {
      if (out != null) {
        out.close();
      }
    }
  }

  public static Uri addGarally(Context context, File path, String mimeType, String title) throws IOException {
    ContentValues values = new ContentValues();
    ContentResolver contentResolver = context.getContentResolver();
    values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
    values.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000);
    values.put(MediaStore.Images.Media.SIZE, path.length());
    values.put(MediaStore.Images.Media.TITLE, title);
    values.put(MediaStore.Images.Media.DESCRIPTION, title);
    values.put(MediaStore.Images.Media.DATA, path.getPath());
    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
  }
}

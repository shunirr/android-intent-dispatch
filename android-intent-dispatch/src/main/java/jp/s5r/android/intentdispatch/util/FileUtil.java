package jp.s5r.android.intentdispatch.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public final class FileUtil {
  private FileUtil() {}

  public static boolean copy(File src, File dst) {
    FileChannel srcChannel = null;
    FileChannel dstChannel = null;
    try {
      srcChannel = new FileInputStream(src).getChannel();
      dstChannel = new FileOutputStream(dst).getChannel();
      srcChannel.transferTo(0, srcChannel.size(), dstChannel);
    } catch (FileNotFoundException e) {
      if (dst.exists()) {
        dst.delete();
      }
      return false;
    } catch (IOException e) {
      if (dst.exists()) {
        dst.delete();
      }
      return false;
    } finally {
      if (srcChannel != null) {
        try { srcChannel.close(); } catch (IOException ignored) {}
      }
      if (dstChannel != null) {
        try { dstChannel.close(); } catch (IOException ignored) {}
      }
    }

    return true;
  }

  public static File getRealFile(Context context, Uri uri) {
    if (uri != null) {
      String scheme = uri.getScheme();
      if ("file".equals(scheme)) {
        return new File(uri.getPath());
      } else if ("content".equals(scheme)) {
        return getRealFileFromContentUri(context, uri);
      }
    }

    return null;
  }

  private static File getRealFileFromContentUri(Context context, Uri uri) {
    File result = null;

    ContentResolver cr = context.getContentResolver();
    Cursor cursor = null;
    try {
      cursor = cr.query(uri, new String[] {MediaStore.Images.ImageColumns.DATA}, null, null, null);
      if (cursor != null && cursor.moveToFirst()) {
        String data = cursor.getString(0);
        if (!TextUtils.isEmpty(data)) {
          result = new File(data);
        }
      }
    } finally {
      if (cursor != null && !cursor.isClosed()) {
        cursor.close();
      }
    }

    return result;
  }
}

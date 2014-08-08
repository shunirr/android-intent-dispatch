package jp.s5r.android.intentdispatch;

import jp.s5r.android.intentdispatch.util.FileUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class GalleryActivity extends Activity {
  private File mSaveFile;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent args = getIntent();
    if (args != null) {
      Uri uri = args.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
      if (uri != null) {
        mSaveFile = FileUtil.getRealFile(this, uri);
      }
    }

    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
    i.setType("image/*");
    startActivityForResult(i, 0);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      File file = FileUtil.getRealFile(this, data.getData());

      if (file != null && mSaveFile != null) {
        FileUtil.copy(file, mSaveFile);
        setResult(RESULT_OK);
        finish();
        return;
      }

      Bitmap bitmap = null;
      InputStream is = null;
      try {
        is = getContentResolver().openInputStream(data.getData());
        bitmap = BitmapFactory.decodeStream(is);
      } catch (IOException ignored) {
        setResult(RESULT_CANCELED);
        finish();
        return;
      } finally {
        if (is != null) {
          try { is.close(); } catch (IOException ignored) {}
        }
      }

      if (bitmap == null) {
        setResult(RESULT_CANCELED);
        finish();
        return;
      }

      if (mSaveFile != null) {
        FileOutputStream fos = null;
        try {
          fos = new FileOutputStream(mSaveFile);
          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
          setResult(RESULT_OK);
        } catch (IOException ignored) {
          setResult(RESULT_CANCELED);
        } finally {
          if (fos != null) {
            try { fos.close(); } catch (IOException ignored) {}
          }
        }
      } else {
        Intent result = new Intent();
        result.putExtra("data", bitmap);
        setResult(RESULT_OK);
      }
      finish();
    }
  }
}
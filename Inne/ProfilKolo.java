package com.example.jacek.gympartner.Inne;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class ProfilKolo extends BitmapTransformation {
    public ProfilKolo(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        //jeśli nie ma źródła nic nie zwracaj
        if (source == null) return null;

        // tworzenie wymiarów
        int rozmiar = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - rozmiar) / 2;
        int y = (source.getHeight() - rozmiar) / 2;
        // tworzenie bitmapy o konkretynym rozmiarze
        Bitmap squared = Bitmap.createBitmap(source, x, y, rozmiar, rozmiar);
        // odebranie bitmapy 1 pixel przechowuje 4 bity
        Bitmap result = pool.get(rozmiar, rozmiar, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(rozmiar, rozmiar, Bitmap.Config.ARGB_8888);
        }
        // Za pomocą klasy canvas tworze obiekt koła w którym będzie profil autora
        Canvas canvas = new Canvas(result);
        // Klasa paint ustawia kolory style dla obiektu
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float roz = rozmiar / 2f;
        canvas.drawCircle(roz, roz, roz, paint);
        return result;
    }

    //    @Override
//    public String getId() {
//        return getClass().getName();
//    }
    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
//do nothing
    }
}
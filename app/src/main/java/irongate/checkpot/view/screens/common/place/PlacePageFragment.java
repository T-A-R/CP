package irongate.checkpot.view.screens.common.place;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import irongate.checkpot.R;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.utils.PicUrl;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.SmartFragment;

public class PlacePageFragment extends SmartFragment {
    private String url;
    private Bitmap bmp;
    private ImageView img;
    private View cont;

    public PlacePageFragment() {
        super(R.layout.fragment_place_page);
    }

    public PlacePageFragment setUrl(String url) {
        this.url = url;
        return this;
    }

    public PlacePageFragment setBmp(Bitmap bmp) {
        this.bmp = bmp;
        return this;
    }

    @Override
    protected void onReady() {

        MainFragment.disableSideMenu();
        img = (ImageView) findViewById(R.id.img);
        cont = findViewById(R.id.images_cont);

        if (bmp != null) {
            img.setImageBitmap(bmp);
            img.setVisibility(View.VISIBLE);
            return;
        }

        setSmallImage();
    }

    public void setSmallImage() {
        if (url != null) {
            Context context = getContext();
            if (context == null)
                return;
            String urlMin = PicUrl.getMinPicUrl(url);
            cont.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.get()
                            .load(urlMin)
                            .transform(new Transformation() {
                                @Override
                                public Bitmap transform(Bitmap source) {
                                    if (source == null) {
                                        return null;
                                    }

                                    Bitmap bitmap = source;
                                    if (cont.getWidth() > 0) {  // Иногда не успевает определить размер (и не понятно, как знать его наверняка)
                                        bitmap = ImageUtils.getCropedBitmap(source, cont.getWidth(), cont.getHeight());
                                    }

                                    bitmap = ImageUtils.getRoundedCornerBitmap(bitmap, 15);

                                    if (bitmap != source) {
                                        source.recycle();
                                    }
                                    return bitmap;
                                }

                                @Override
                                public String key() {
                                    return "EventFragment";
                                }
                            }).into(img, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            setBigImage();
                        }

                        @Override
                        public void onError(Exception e) {
                            setBigImage();
                        }
                    });
                }
            });

            img.setVisibility(View.VISIBLE);
            img.startAnimation(Anim.getAppear(context));
        }
    }

    public void setBigImage() {

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                img.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };

        cont.post(new Runnable() {
            @Override
            public void run() {
                Picasso.get()
                        .load(url)
                        .transform(new Transformation() {
                            @Override
                            public Bitmap transform(Bitmap source) {
                                if (source == null) {
                                    return null;
                                }

                                Bitmap bitmap = source;
                                if (cont.getWidth() > 0) {  // Иногда не успевает определить размер (и не понятно, как знать его наверняка)
                                    bitmap = ImageUtils.getCropedBitmap(source, cont.getWidth(), cont.getHeight());
                                }

                                bitmap = ImageUtils.getRoundedCornerBitmap(bitmap, 15);

                                if (bitmap != source) {
                                    source.recycle();
                                }
                                return bitmap;
                            }

                            @Override
                            public String key() {
                                return "EventFragment";
                            }
                        }).into(target);
            }
        });
    }
}

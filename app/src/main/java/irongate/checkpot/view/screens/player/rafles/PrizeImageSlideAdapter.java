package irongate.checkpot.view.screens.player.rafles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.util.List;

import irongate.checkpot.R;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.utils.PicUrl;

public class PrizeImageSlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<String> images;
    RelativeLayout cont;

    public PrizeImageSlideAdapter(Context context) {
        this.context = context;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_raffle_card_slider, container, false);
        cont = (RelativeLayout) view.findViewById(R.id.cont_raffle_card);
        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);

        if (images != null)
            if (images.get(position) != null) {
                String url = images.get(position);
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
                                        if (cont.getWidth() > 0) {
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
                                        return "RaffleCardFragment" + String.valueOf(cont.getWidth());
                                    }
                                })
                                .into(slideImageView, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        loadBigImage(slideImageView, url);
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        loadBigImage(slideImageView, url);
                                    }
                                });
                    }
                });

            }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public void loadBigImage(ImageView image, String url) {

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                image.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
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
                                if (cont.getWidth() > 0) {
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
                                return "RaffleCardFragment" + String.valueOf(cont.getWidth());
                            }
                        }).into(target);
            }
        });
    }
}


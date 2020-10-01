package irongate.checkpot.utils;

public class PicUrl {

    private static String picUrl;

    public static String getMinPicUrl(String url) {

        if(url != null && url.length() > 0)
        for(int i = 0; i < url.length(); i++) {
            if(url.charAt(i) == 's' && url.charAt(i+1) == 't' && url.charAt(i+2) == 'a') {
                PicUrl.picUrl = url.substring(0, i + 7) + "min_" + url.substring(i + 7) ;
            }
        }
        return picUrl;
    }
}

package library.common.framework.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.request.FutureTarget;

import java.io.File;

/**
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public interface Displayable {
    /**
     * 显示图片
     *
     * @param context
     * @param imageView
     * @param url
     * @param placeHolderId 占位图
     * @param errorHolderId 错误图
     */
    void display(Context context, ImageView imageView, String url, @DrawableRes int placeHolderId, @DrawableRes int errorHolderId);
    
    /**
     * 显示图片
     *
     * @param context
     * @param imageView
     * @param url
     */
    void display(Context context, ImageView imageView, String url);
    
    /**
     * 显示圆形图片
     *
     * @param context
     * @param imageView
     * @param url
     * @param placeHolderId 占位图
     * @param errorHolderId 错误图
     */
    void displayCircle(Context context, ImageView imageView, String url, @DrawableRes int placeHolderId, @DrawableRes int errorHolderId);
    
    /**
     * 显示圆形图片
     *
     * @param context
     * @param imageView
     * @param url
     */
    void displayCircle(Context context, ImageView imageView, String url);
    
    /**
     * 显示圆角图片
     *
     * @param context
     * @param imageView
     * @param url
     * @param radius        圆角半径
     * @param placeHolderId 占位图
     * @param errorHolderId 错误图
     */
    void displayRoundedCorners(Context context, ImageView imageView, String url, int radius, @DrawableRes int placeHolderId, @DrawableRes int errorHolderId);


    /**
     * 显示圆角图片
     *
     * @param context
     * @param imageView
     * @param drawable
     * @param radius        圆角半径
     * @param placeHolderId 占位图
     * @param errorHolderId 错误图
     */
    void displayRoundedCorners(Context context, ImageView imageView, Drawable drawable, int radius, @DrawableRes int placeHolderId, @DrawableRes int errorHolderId);

    /**
     * 显示圆角图片
     *
     * @param context
     * @param imageView
     * @param url
     * @param radius    圆角半径
     */
    void displayRoundedCorners(Context context, ImageView imageView, String url, int radius);
    
    /**
     * 下载图片
     * FutureTarget.get() 获取下载结果，注意：此方法会阻塞
     *
     * @param context
     * @param url
     * @return
     */
    File download(Context context, String url);
    
    /**
     * URL 处理
     *
     * @param url 原始 URL
     * @return 返回处理后的 URL
     */
    String decorateUrl(String url);
    
    /**
     * 加载 File
     *
     * @param context
     * @param imageView
     * @param file
     */
    void displayLocal(Context context, ImageView imageView, @Nullable File file);
    
    /**
     * 加载 Drawable
     *
     * @param context
     * @param imageView
     * @param drawable
     */
    void displayLocal(Context context, ImageView imageView, @Nullable Drawable drawable);
    
    /**
     * 加载 Resource
     *
     * @param context
     * @param imageView
     * @param resourceId
     */
    void displayLocal(Context context, ImageView imageView, @Nullable Integer resourceId);

    /**
     * 显示Gif
     *
     * @param context
     * @param imageView
     * @param resourceId
     */
    void displayGif(Context context, ImageView imageView, @Nullable Integer resourceId);
}

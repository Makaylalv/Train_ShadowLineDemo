package com.example.train_shadowlinedemo.Personal;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoUtils {
    public static final int REQUEST_CODE_FROM_CAMERA = 5001;
    public static final int REQUEST_CODE_FROM_ALBUM = 5002;
    public static final int REQUEST_CODE_CROP = 5003;



    /**
     * 存放拍照图片的uri地址
     */
    private static Uri imageUriFromALBUM;
    private static Uri imageUriFromCamera;
    /**
     * 记录是处于什么状态：拍照or相册
     */
    private static int state = 0;
    /**
     * 打开本地相册选取图片
     */
    public static void pickImageFromAlbum(final Activity activity){

        //隐式调用，可能出现多种选择
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent,REQUEST_CODE_FROM_ALBUM);

        /**
         Intent intent = new Intent();
         intent.setAction(Intent.ACTION_PICK);
         intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         activity.startActivityForResult(intent,REQUEST_CODE_FROM_ALBUM);
         */
    }
    /**
     * 打开相机拍照获取图片
     */
    public static void pickImageFromCamera(final Activity activity){

        imageUriFromCamera = getImageUri();

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUriFromCamera);    //指定拍照的图片存在imageUriFromCamera下,如果直接在返回时使用getData()获取的是压缩过的Bitmap数据
        activity.startActivityForResult(intent,REQUEST_CODE_FROM_CAMERA);
    }

    /**
     * 复制一条Uri，避免因为操作而对原图片产生影响
     */
    public static void copyImageUri(Activity activity, Uri uri){

        String imageName = new SimpleDateFormat("yyMMddHHmmss").format(new Date()) + ".jpg";
        String copyPath = MyConstant.PhotoDir + imageName;
        FileUtils.copyfile(getRealFilePath(activity,uri),copyPath,true);
        imageUriFromALBUM = getUriFromFilePath(copyPath);
    }

    /**
     * 删除一条图片Uri
     */
    public static void deleteImageUri(Context context, Uri uri){
        context.getContentResolver().delete(uri, null, null);
    }


    /**
     * 裁剪图片返回
     */
    public static void cropImageUri(Activity activity, Uri uri, int outputX, int outputY){

        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        activity.startActivityForResult(intent, REQUEST_CODE_CROP);

    }


    /**
     * 根据状态返回图片Uri
     */
    public static Uri getCurrentUri(){

        if (state == 1){
            return imageUriFromCamera;
        }
        else if (state == 2){
            return imageUriFromALBUM;
        }
        else return null;
    }

    /**
     * 根据指定目录产生一条图片Uri
     */
    private static Uri getImageUri(){
        String imageName = new SimpleDateFormat("yyMMddHHmmss").format(new Date()) + ".jpg";
        String path = MyConstant.PhotoDir + imageName;
        return getUriFromFilePath(path);
    }
    /**
     * 将Uri转换成Bitmap
     */
    public static Bitmap decodeUriAsBitmap(Context context, Uri uri){

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return bitmap;
    }
    /**
     * 将Uri转为路径地址
     */
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    /**
     * 将路径地址转换为Uri
     */
    public static Uri getUriFromFilePath(final String path){

        return Uri.fromFile(new File(path));
    }
}

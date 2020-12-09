package com.example.train_shadowlinedemo.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.Personal.BlurTransformation;
import com.example.train_shadowlinedemo.Personal.CityCollectionActivity;
import com.example.train_shadowlinedemo.Personal.DeleteCommentPopupWindow;
import com.example.train_shadowlinedemo.Personal.MovieCollectionActivity;
import com.example.train_shadowlinedemo.Personal.PhotoPopupWindow;
import com.example.train_shadowlinedemo.Personal.PlaceCollectionActivity;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.LoginActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class PersonalFragment extends Fragment implements View.OnClickListener{
    private ImageView img_head;
    private ImageView mImg;
    private Bitmap head;
    private PhotoPopupWindow popupWindow;
    private TextView username;
    private TextView name;
    private TextView type;
    private TextView collection;
    private TextView movie;
    private TextView city;
    private TextView cut;
    private TextView route;
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_SMALL_IMAGE_CUTTING = 2;
    private static final int REQUEST_BIG_IMAGE_CUTTING = 3;
    private static final String IMAGE_FILE_NAME = "icon.jpg";
    private OkHttpClient okHttpClient=new OkHttpClient();
    private Drawable drawable;//用户头像
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    drawable= (Drawable) msg.obj;
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    head = bitmapDrawable.getBitmap();
                    Bitmap bitmap =  toRoundBitmap(head);
                    img_head.setImageBitmap(bitmap);
                    Glide.with(PersonalFragment.this)
                            .load(bitmap)
                            .apply(bitmapTransform(new BlurTransformation(PersonalFragment.this.getContext(),20,2)))
                            .into(mImg);
                   // mImg.setImageBitmap(bitmap);
                    break;
            }
        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout__personal, container, false);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        //控件的获取
        RelativeLayout rv_movie = view.findViewById(R.id.rv_movie);
        rv_movie.setOnClickListener(PersonalFragment.this);
        RelativeLayout rv_city = view.findViewById(R.id.rv_city);
        rv_city.setOnClickListener(PersonalFragment.this);
        RelativeLayout rv_place = view.findViewById(R.id.rv_cut);
        rv_place.setOnClickListener(PersonalFragment.this);
        username = view.findViewById(R.id.username);
        name = view.findViewById(R.id.tv_name);
        type = view.findViewById(R.id.tv_signature);
        collection = view.findViewById(R.id.tv_mycollection);
        movie = view.findViewById(R.id.tv_movie);
        cut = view.findViewById(R.id.tv_cut);
        city = view.findViewById(R.id.tv_city);
        route = view.findViewById(R.id.tv_route);
        //头像
        mImg = view.findViewById(R.id.mImage);
        img_head = view.findViewById(R.id.img_head);
       //Drawable drawable = getResources().getDrawable(R.drawable.head1);
        //获取到数据库中的图片
        loadImageFromNetwork(ConfigUtil.SERVER_ADDR+"imgs/user/userimgs/"+ LoginActivity.user.getUser_id()+".jpg");
       // BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//        Glide.with(PersonalFragment.this.getContext())
//                .load(ConfigUtil.SERVER_ADDR+"imgs/user/userimgs/"+ LoginActivity.user.getUser_id()+".jpg")
//                .apply(bitmapTransform(new BlurTransformation(PersonalFragment.this.getContext(),20,2)))
//                .into(mImg);

       // head = bitmapDrawable.getBitmap();
      //  Bitmap bitmap =  toRoundBitmap(head);
       // img_head.setImageBitmap(bitmap);
        changePhoto(img_head);
        //设置字体
        //从asset 读取字体
        AssetManager mgr = getResources().getAssets();
        //根据路径得到Typeface
        Typeface tf = Typeface.createFromAsset(mgr, "Regular.ttf");//仿宋
        username.setTypeface(tf);
        name.setTypeface(tf);
        type.setTypeface(tf);
        collection.setTypeface(tf);
        movie.setTypeface(tf);
        cut.setTypeface(tf);
        city.setTypeface(tf);
        route.setTypeface(tf);
        return view;
    }
    //处理点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rv_movie:
                Intent movieIntent = new Intent();
                movieIntent.setClass(PersonalFragment.this.getContext(), MovieCollectionActivity.class);
                startActivity(movieIntent);
                break;
            case R.id.rv_city:
                Intent cityIntent = new Intent();
                cityIntent.setClass(PersonalFragment.this.getContext(), CityCollectionActivity.class);
                startActivity(cityIntent);
                break;
            case R.id.rv_cut:
                Intent placeIntent = new Intent();
                placeIntent.setClass(PersonalFragment.this.getContext(), PlaceCollectionActivity.class);
                startActivity(placeIntent);
                break;
        }
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = PersonalFragment.this.getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.action, menu);
        changeActionbar();
        Log.e("onCreateOptionsMenu","111");
        super.onCreateOptionsMenu(menu, menuInflater);

    }
    public void changeActionbar(){
        ActionBar actionBar = PersonalFragment.this.getActivity().getActionBar();
        actionBar.setLogo(R.drawable.hiking);

    }



    //将图片设置为圆形
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        //圆形图片宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //正方形的边长
        int r = 0;
        //取最短边做边长
        if (width > height) {
            r = height;
        } else {
            r = width;
        }
        //构建一个bitmap
        Bitmap backgroundBmp = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        //new一个Canvas，在backgroundBmp上画图
        Canvas canvas = new Canvas(backgroundBmp);
        Paint paint = new Paint();
        //设置边缘光滑，去掉锯齿
        paint.setAntiAlias(true);
        //宽高相等，即正方形
        RectF rect = new RectF(0, 0, r, r);
        //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        //且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, r / 2, r / 2, paint);
        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, paint);
        //返回已经绘画好的backgroundBmp
        return backgroundBmp;
    }
    //更换头像
    public void changePhoto(ImageView view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow = new PhotoPopupWindow(PersonalFragment.this.getActivity(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //进入相册选择
                        popupWindow.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        // 判断系统中是否有处理该 Intent 的 Activity
                        if (intent.resolveActivity(PersonalFragment.this.getActivity().getPackageManager()) != null) {
                            startActivityForResult(intent, REQUEST_IMAGE_GET);
                        } else {
                            Toast.makeText(PersonalFragment.this.getActivity(), "未找到图片查看器", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //拍照
                        popupWindow.dismiss();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                });
                View rootView = LayoutInflater.from(PersonalFragment.this.getActivity()).inflate(R.layout.fragment_layout__personal,null);
              popupWindow.showAtLocation(rootView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,10,10);

            }
        });

    }


    /**
     * 处理回调结果
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 回调成功
        switch (requestCode) {
            // 小图切割
            case REQUEST_SMALL_IMAGE_CUTTING:
                if (data != null) {
                    setPicToView(data);
                }
                break;

            // 相册选取
            case REQUEST_IMAGE_GET:
                try {
                    startSmallPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            // 拍照
            case REQUEST_IMAGE_CAPTURE:
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startSmallPhotoZoom(Uri.fromFile(temp));
                break;
            //......
        }

    }
    /**
     * 小图模式切割图片
     * 此方式直接返回截图后的 bitmap，由于内存的限制，返回的图片会比较小
     */
    public void startSmallPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300); // 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_SMALL_IMAGE_CUTTING);
    }
    /**
     * 小图模式中，保存图片后，设置到视图中
     */
    private void setPicToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data"); // 直接获得内存中保存的 bitmap
            // 创建 smallIcon 文件夹
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String storage = Environment.getExternalStorageDirectory().getPath();
                File dirFile = new File(storage + "/smallIcon");
                if (!dirFile.exists()) {
                    if (!dirFile.mkdirs()) {
                        Log.e("TAG", "文件夹创建失败");
                    } else {
                        Log.e("TAG", "文件夹创建成功");
                    }
                }
                File file = new File(dirFile, System.currentTimeMillis() + ".jpg");
                // 保存图片
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 在视图中显示图片
            Glide.with(PersonalFragment.this.getContext())
                    .load(photo)
                    .apply(bitmapTransform(new BlurTransformation(PersonalFragment.this.getContext(),20,2)))
                    .into(mImg);
            Bitmap bitmap =  toRoundBitmap(photo);
            img_head.setImageBitmap(bitmap);
            uploadUserImg(photo);

        }
    }
    /**
     * 大图模式切割图片
     * 直接创建一个文件将切割后的图片写入
     */
    public void startBigPhotoZoom(Uri uri) {
        // 创建大图文件夹
        Uri imageUri = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String storage = Environment.getExternalStorageDirectory().getPath();
            File dirFile = new File(storage + "/bigIcon");
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    Log.e("TAG", "文件夹创建失败");
                } else {
                    Log.e("TAG", "文件夹创建成功");
                }
            }
            File file = new File(dirFile, System.currentTimeMillis() + ".jpg");
            imageUri = Uri.fromFile(file);
        }
        // 开始切割
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 600); // 输出图片大小
        intent.putExtra("outputY", 600);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false); // 不直接返回数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 返回一个文件
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, REQUEST_BIG_IMAGE_CUTTING);
    }
    /**
     * 将用户头像上传到数据库
     */
    public void uploadUserImg(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        RequestBody requestBody=RequestBody.create(MediaType.parse("application/octet-stream"),baos.toByteArray());
        Request request=new Request.Builder()
                .post(requestBody)
                .url(ConfigUtil.SERVER_ADDR+"UploadUserImgServlet?filename="+ LoginActivity.user.getUser_id())
                .build();
        //创建Call对象,发送请求，并接受响应
        Call call=okHttpClient.newCall(request);
        //异步网络请求(不需要创建子线程)
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调
                e.printStackTrace();
                Log.e("登录失败","发布失败");

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("上传用户头像的结果",response.body().toString());

            }
        });
    }
    private void loadImageFromNetwork(String imageUrl) {

       new Thread(){
           @Override
           public void run() {
               super.run();
               try {
                   URL url=new URL(imageUrl);
                   Log.e("aaaaa",imageUrl);
                   HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                   httpURLConnection.setRequestMethod("POST");
                   InputStream in=url.openStream();
                   Drawable drawable=new BitmapDrawable(BitmapFactory.decodeStream(in));
                   Message message=handler.obtainMessage();
                   message.what=1;
                   message.obj=drawable;
                   handler.sendMessage(message);

               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }.start();

    }


}
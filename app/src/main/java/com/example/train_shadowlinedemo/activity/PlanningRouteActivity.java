package com.example.train_shadowlinedemo.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.CustomMarker;
import com.example.train_shadowlinedemo.entity.Distance;
import com.example.train_shadowlinedemo.entity.Place;
import com.example.train_shadowlinedemo.view.LiveRoom.NearPlaceRecyclerViewAdapter;
import com.example.train_shadowlinedemo.view.MovieDetail.PlaningPlaceRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlanningRouteActivity extends AppCompatActivity {
    private EditText editText;
    private LocationClient locationClient;
    private MapView mMapView;
    private BaiduMap baiduMap;
    private double latitude;
    private double longitude;
    private RoutePlanSearch search;
    private Button button;
    private Button button1;
    private int i=0;
    private List<Distance> distanceList;
    private List<PlanNode> planNodeList1;
    private List<PlanNode> planNodeList2;
    private List<Double> doubleList;
    private List<CustomMarker> markerList;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private int flag=0;
    private int tag=0;
    private int n=0;
    private PlaningPlaceRecyclerViewAdapter planingPlaceRecyclerViewAdapter;
    private List<Place> places;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    baiduMap.clear();
                    LatLng point=new LatLng(latitude,longitude);
                    //在当前位置添加标注覆盖物
                    BitmapDescriptor descriptor= BitmapDescriptorFactory.fromResource(R.drawable.marker);
                    MarkerOptions options=new MarkerOptions().position(point).icon(descriptor);
                    baiduMap.addOverlay(options);
                    searchLine();
                    break;
                case 2:
                    if(n<planNodeList2.size()-1){
                        search.drivingSearch(new DrivingRoutePlanOption().from(planNodeList2.get(n)).to(planNodeList2.get(n + 1)));
                        n++;
                    }
                    break;
                case 3:
                    Log.e("size",places.size()+"");
                    planingPlaceRecyclerViewAdapter=new PlaningPlaceRecyclerViewAdapter(places,PlanningRouteActivity.this);
                    recyclerView.setAdapter(planingPlaceRecyclerViewAdapter);
                    planingPlaceRecyclerViewAdapter.setOnItemClickListener(onItemClickListener);
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planing_route);
        //初始化bar
        initBar();

        location();

        getView();

        initMap();

        initMarkerList();

        initSearch();


    }

    private void initSearch() {
        search= RoutePlanSearch.newInstance();

        search.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                // 获取步行线路规划结果
                if (walkingRouteResult == null || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(PlanningRouteActivity.this, "抱歉，未找到结果"+walkingRouteResult.error,
                            Toast.LENGTH_SHORT).show();
                }
                if (walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    // result.getSuggestAddrInfo()
                    return;
                }
                if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    walkGo(walkingRouteResult);

                }

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

                if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(PlanningRouteActivity.this, "抱歉，未找到结果"+drivingRouteResult.error,
                            Toast.LENGTH_SHORT).show();
                }
                if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    // result.getSuggestAddrInfo()
                    return;
                }
                if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    drivingGo(drivingRouteResult);

                }

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });
    }

    private void walkGo(WalkingRouteResult walkingRouteResult) {
        LatLng oldLng = null;
        if(planNodeList2.size()!=planNodeList1.size()) {
            List<WalkingRouteLine> routeLines = walkingRouteResult.getRouteLines();
            double distance = walkingRouteResult.getRouteLines().get(0).getDistance();
            PlanNode p1 = PlanNode.withLocation(walkingRouteResult.getRouteLines().get(0).getStarting().getLocation());
            PlanNode p2 = PlanNode.withLocation(walkingRouteResult.getRouteLines().get(0).getTerminal().getLocation());
            Distance dt = new Distance();
            dt.setI(p1);
            dt.setJ(p2);
            dt.setDistance(distance);
            distanceList.add(dt);
        }else {
            int endMarker = 0;
            //route = result.getRouteLines().get(0);
            for(int i=0;i<markerList.size();i++){
                if(markerList.get(i).getTag()==0){
                    endMarker=markerList.get(i).getMarker();
                    markerList.get(i).setTag(1);
                    break;
                }
            }
            //route = result.getRouteLines().get(0);
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(baiduMap,endMarker);
            baiduMap.setOnMarkerClickListener(overlay);
            //routeOverlay = overlay;
            overlay.setData(walkingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            //overlay.zoomToSpan();
        }
        if(distanceList.size()==planNodeList1.size()*(planNodeList1.size()-1)/2&&planNodeList2.size()!=planNodeList1.size()){
            List<PlanNode> okPNL=new ArrayList<>();
            int l=0;
            planNodeList2.add(planNodeList1.get(0));
            Distance distance1=new Distance();
            for(int i=0;i<planNodeList1.size();i++){
                if(planNodeList2.size()==planNodeList1.size()){
                    break;
                }
                List<Distance> list=new ArrayList<>();
                for(int j=0;j<distanceList.size();j++) {
                    int a=new Double(distanceList.get(j).getI().getLocation().latitude*10000).intValue();
                    int b=new Double(distanceList.get(j).getI().getLocation().longitude*10000).intValue();
                    int c=new Double(distanceList.get(j).getJ().getLocation().latitude*10000).intValue();
                    int d=new Double(distanceList.get(j).getJ().getLocation().longitude*10000).intValue();
                    int e=new Double(planNodeList1.get(l).getLocation().latitude*10000).intValue();
                    int f=new Double(planNodeList1.get(l).getLocation().longitude*10000).intValue();
                    if ((Math.abs(a-e)<=1&&Math.abs(b-f)<=1)||(Math.abs(c-e)<=1&&Math.abs(d-f)<=1)){
                        list.add(distanceList.get(j));
                        Log.e("zxczxczx", distanceList.get(j).getDistance()+"");
                    }
                }
                LatLng lng = null;
                int u;
                int o;
                if(oldLng!=null) {
                    u = new Double(oldLng.latitude * 10000).intValue();
                    o = new Double(oldLng.longitude * 10000).intValue();
                    distance1 = list.get(0);
                    for (int m = 0; m < list.size(); m++) {
                        int p = new Double(distance1.getDistance() * 100).intValue();
                        int q = new Double(list.get(m).getDistance() * 100).intValue();
                        if (p > q) {
                            distance1 = list.get(m);
                        }
                    }
                    int a=new Double(distance1.getI().getLocation().latitude * 10000).intValue();
                    int b=new Double(distance1.getI().getLocation().longitude * 10000).intValue();
                    if(u==a&&o==b){
                        lng=distance1.getJ().getLocation();
                    }else {
                        lng=distance1.getI().getLocation();
                    }

                }else{
                    distance1 = list.get(0);
                    lng = list.get(0).getJ().getLocation();
                    for (int m = 0; m < list.size(); m++) {
                        int p = new Double(distance1.getDistance() * 100).intValue();
                        int q = new Double(list.get(m).getDistance() * 100).intValue();
                        if (p > q) {
                            distance1 = list.get(m);
                            lng = list.get(m).getJ().getLocation();

                        }
                    }
                }
                PlanNode pN;
                pN= PlanNode.withLocation(lng);
                planNodeList2.add(pN);
                for(int j=0;j<list.size();j++) {
                    distanceList.remove(list.get(j));
                }
                for(int m=0;m<planNodeList1.size();m++){
                    int a=new Double(lng.latitude*10000).intValue();
                    int b=new Double(lng.longitude*10000).intValue();
                    int c=new Double(planNodeList1.get(m).getLocation().latitude*10000).intValue();
                    int d=new Double(planNodeList1.get(m).getLocation().longitude*10000).intValue();
                    Log.e("llllllllllll",a+"    "+c+"       "+b+"      "+d);
                    if(Math.abs(a-c)<=1&&Math.abs(b-d)<=1){
                        l=m;
                        break;
                    }
                }
                Log.e("llllllllllll",""+l);
                oldLng=lng;
            }
            if(planNodeList2.size()==planNodeList1.size()){
                new Thread() {
                    @Override
                    public void run() {
                        for(int n = 0;n<planNodeList2.size()-1;n++)
                        {
                            search.walkingSearch(new WalkingRoutePlanOption().from(planNodeList2.get(n)).to(planNodeList2.get(n + 1)));
                            try {
                                sleep(40);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }.run();
            }
        }

    }

    private void location() {
        locationClient=new LocationClient(getApplicationContext());
        //2.动态申请权限
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                100);
        //6.启动定位
        locationClient.start();
    }

    private void initMap() {

        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
        baiduMap.setMapStatus(msu);
        //修改指南针位置
        UiSettings uiSettings = baiduMap.getUiSettings();
        uiSettings.setCompassEnabled(false);//先设置UiSettings为false
        baiduMap.setCompassEnable(true);//baidumap为true
        baiduMap.setCompassPosition(new Point(100,100));
        //修改图标
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.compass);
        baiduMap.setCompassIcon(bitmap);

    }

    private void getView() {
        LinearLayoutManager layout = new LinearLayoutManager(PlanningRouteActivity.this);
        layout.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(layout);
        mMapView=findViewById(R.id.map_view);
        baiduMap=mMapView.getMap();
        button=findViewById(R.id.button);
        baiduMap.setMaxAndMinZoomLevel(20,5);

      /*  button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reStartActivity();
            }
        });*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View v) {
                synchronized (this) {
                    if (tag != 1) {
                        tag = 1;
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                    LatLng point=new LatLng(latitude,longitude);
                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
                    //移动地图界面
                    baiduMap.animateMapStatus(update);
                }


            }
        });

    }

    private void searchLine() {
        int j=planNodeList1.size()-1;

        new Thread(){
            @Override
            public void run() {
                for(int i=0;i<planNodeList1.size();i++){
                    for(int l=i;l<planNodeList1.size();l++){
                        if(l==planNodeList1.size()-1){
                            continue;
                        }else {

                                if(planNodeList2.size()!=planNodeList1.size()) {
                                    //睡眠s
                                    search.drivingSearch(new DrivingRoutePlanOption().from(planNodeList1.get(i)).to(planNodeList1.get(l + 1)));
                                    //search.walkingSearch(new WalkingRoutePlanOption().from(planNodeList1.get(i)).to(planNodeList1.get(l + 1)));
                                }

                        }
                    }
                }
            }
        }.run();

    }

    private void drivingGo(DrivingRouteResult drivingRouteResult) {
        LatLng oldLng = null;
        if(planNodeList2.size()!=planNodeList1.size()) {
            List<DrivingRouteLine> routeLines = drivingRouteResult.getRouteLines();
            double distance = drivingRouteResult.getRouteLines().get(0).getDistance();
            PlanNode p1 = PlanNode.withLocation(drivingRouteResult.getRouteLines().get(0).getStarting().getLocation());
            PlanNode p2 = PlanNode.withLocation(drivingRouteResult.getRouteLines().get(0).getTerminal().getLocation());
            Distance dt = new Distance();
            dt.setI(p1);
            dt.setJ(p2);
            dt.setDistance(distance);
            distanceList.add(dt);
            Log.e("distance",dt.getDistance()+"");
        }else {
            int endMarker = 0;
            //route = result.getRouteLines().get(0);
            for(int i=0;i<markerList.size();i++){
                if(markerList.get(i).getTag()==0){
                    endMarker=markerList.get(i).getMarker();
                    markerList.get(i).setTag(1);
                    break;
                }
            }

            //route = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(baiduMap,endMarker);
            baiduMap.setOnMarkerClickListener(overlay);
            //routeOverlay = overlay;
            overlay.setData(drivingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            Message message=new Message();
            message.what=2;
            handler.sendMessage(message);
            //overlay.zoomToSpan();
        }
        if(distanceList.size()==planNodeList1.size()*(planNodeList1.size()-1)/2&&planNodeList2.size()!=planNodeList1.size()){
            List<PlanNode> okPNL=new ArrayList<>();
            int l=0;
            planNodeList2.add(planNodeList1.get(0));
            Distance distance1=new Distance();
            for(int i=0;i<planNodeList1.size();i++){
                if(planNodeList2.size()==planNodeList1.size()){
                    break;
                }
                List<Distance> list=new ArrayList<>();
                for(int j=0;j<distanceList.size();j++) {
                    int a=new Double(distanceList.get(j).getI().getLocation().latitude*10000).intValue();
                    int b=new Double(distanceList.get(j).getI().getLocation().longitude*10000).intValue();
                    int c=new Double(distanceList.get(j).getJ().getLocation().latitude*10000).intValue();
                    int d=new Double(distanceList.get(j).getJ().getLocation().longitude*10000).intValue();
                    int e=new Double(planNodeList1.get(l).getLocation().latitude*10000).intValue();
                    int f=new Double(planNodeList1.get(l).getLocation().longitude*10000).intValue();
                    if ((Math.abs(a-e)<=1&&Math.abs(b-f)<=1)||(Math.abs(c-e)<=1&&Math.abs(d-f)<=1)){
                        list.add(distanceList.get(j));
                        Log.e("zxczxczx", distanceList.get(j).getDistance()+"");
                    }
                }
                LatLng lng = null;
                int u;
                int o;
                if(oldLng!=null) {
                    u = new Double(oldLng.latitude * 10000).intValue();
                    o = new Double(oldLng.longitude * 10000).intValue();
                    distance1 = list.get(0);
                    for (int m = 0; m < list.size(); m++) {
                        int p = new Double(distance1.getDistance() * 100).intValue();
                        int q = new Double(list.get(m).getDistance() * 100).intValue();
                        if (p > q) {
                            distance1 = list.get(m);
                        }
                    }
                    int a=new Double(distance1.getI().getLocation().latitude * 10000).intValue();
                    int b=new Double(distance1.getI().getLocation().longitude * 10000).intValue();
                    if(u==a&&o==b){
                        lng=distance1.getJ().getLocation();
                    }else {
                        lng=distance1.getI().getLocation();
                    }

                }else{
                    distance1 = list.get(0);
                    lng = list.get(0).getJ().getLocation();
                    for (int m = 0; m < list.size(); m++) {
                        int p = new Double(distance1.getDistance() * 100).intValue();
                        int q = new Double(list.get(m).getDistance() * 100).intValue();
                        if (p > q) {
                            distance1 = list.get(m);
                            lng = list.get(m).getJ().getLocation();
                        }
                    }
                }
                PlanNode pN;
                pN= PlanNode.withLocation(lng);
                planNodeList2.add(pN);
                for(int j=0;j<list.size();j++) {
                    distanceList.remove(list.get(j));
                }
                for(int m=0;m<planNodeList1.size();m++){
                    int a=new Double(lng.latitude*10000).intValue();
                    int b=new Double(lng.longitude*10000).intValue();
                    int c=new Double(planNodeList1.get(m).getLocation().latitude*10000).intValue();
                    int d=new Double(planNodeList1.get(m).getLocation().longitude*10000).intValue();
                    Log.e("排序",a+"    "+c+"       "+b+"      "+d);
                    if(Math.abs(a-c)<=1&&Math.abs(b-d)<=1){
                        l=m;
                        break;
                    }
                    oldLng=lng;
                }

            }
            if(planNodeList2.size()==planNodeList1.size()&&flag==0){
                flag=1;
                Message message=new Message();
                message.what=2;
                handler.sendMessage(message);
//                new Thread() {
//                    @Override
//                    public void run() {
//                        for(int n = 0;n<planNodeList2.size()-1;n++)
//                        {
//
//                            Log.e("lng",planNodeList2.get(n).getLocation().latitude+"HHHHHH"+planNodeList2.get(n).getLocation().longitude);
//                            try {
//                                sleep(100);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                }.run();

            }
        }
    }

    private void initMarkerList() {
        markerList=new ArrayList<>();
        planNodeList1=new ArrayList<>();
        planNodeList2=new ArrayList<>();
        distanceList=new ArrayList<>();
        doubleList=new ArrayList<>();
        CustomMarker marker1=new CustomMarker();
        CustomMarker marker2=new CustomMarker();
        CustomMarker marker3=new CustomMarker();
        CustomMarker marker4=new CustomMarker();
        CustomMarker marker5=new CustomMarker();
        CustomMarker marker6=new CustomMarker();
        marker1.setMarker(R.drawable.location_1);
        marker1.setTag(0);
        marker2.setMarker(R.drawable.location_2);
        marker2.setTag(0);
        marker3.setMarker(R.drawable.location_3);
        marker3.setTag(0);
        marker4.setMarker(R.drawable.location_4);
        marker4.setTag(0);
        marker5.setMarker(R.drawable.location_5);
        marker5.setTag(0);
        marker6.setMarker(R.drawable.location_6);
        marker6.setTag(0);
        markerList.add(marker1);
        markerList.add(marker2);
        markerList.add(marker3);
        markerList.add(marker4);
        markerList.add(marker5);
        markerList.add(marker6);
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //grantResults数组当中存储的是用户选择的结果，0表示允许，-1表示拒绝
        if(requestCode==100&&grantResults[0]==0){//表示用户允许了这个权限
            //3.创建LocationClientOption对象
            LocationClientOption option=new LocationClientOption();
            //配置一些定位的参数
            //设置打开GPS
            option.setOpenGps(true);
            //4.设置坐标系类型
            option.setCoorType("bd09ll");
            //设置定位模式(推荐使用低功耗的定位模式)
            option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
            //将定位的参数应用到定位客户端
            locationClient.setLocOption(option);
            //5.设置定位成功的监听器(实现异步定位操作，定位成功后会自动调用接口中的方法)
            locationClient.registerLocationListener(new BDAbstractLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    //定位成功后自动执行，定位成功后位置信息保存在BDLocation对象中
                    latitude=bdLocation.getLatitude();//纬度
                    longitude=bdLocation.getLongitude();//精度
                    int code=bdLocation.getLocType();//获取定位错误码
                    Log.e("定位成功","维度"+latitude+"精度"+longitude+"定位结果"+code);
                    places=new ArrayList<>();
                    Type type=new TypeToken<List<Place>>(){}.getType();
                    Intent intent=getIntent();
                    String string=intent.getStringExtra("places");
                    Gson gson=new Gson();
                    places=gson.fromJson(string,type);
                    Log.e("size",string+"");
                    PlanNode node = PlanNode.withLocation(new LatLng(latitude, longitude));
                    planNodeList1.add(node);
                    for(int i=0;i<places.size();i++){
                        PlanNode node1 = PlanNode.withLocation(new LatLng(places.get(i).getPlaceLatitude(), places.get(i).getPlaceLongitude()));
                        planNodeList1.add(node1);
                    }
                    //移动地图界面显示到当前位置
                    //把哪一个坐标点显示到地图中间
                    LatLng point=new LatLng(latitude,longitude);
                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
                    //移动地图界面
                    baiduMap.animateMapStatus(update);
                    Message message=new Message();
                    message.what=3;
                    handler.sendMessage(message);

                }
            });


        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {
        private int endMarker;
        public MyWalkingRouteOverlay(BaiduMap baiduMap, int endMarker) {
            super(baiduMap);
            this.endMarker=endMarker;
        }
        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(endMarker);
        }


    }


    private  class MyDrivingRouteOverlay extends DrivingRouteOverlay {
        private int endMarker;
        /**
         * 构造函数
         *
         * @param baiduMap 该DrivingRouteOvelray引用的 BaiduMap
         */
        public MyDrivingRouteOverlay(BaiduMap baiduMap, int endMarker) {
            super(baiduMap);
            this.endMarker=endMarker;
        }
        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(endMarker);
        }
    }


    /**
     * 初始化bar
     */
    private void initBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.custom_bar);  //绑定自定义的布局：actionbar_layout.xml
            imageView=actionBar.getCustomView().findViewById(R.id.back);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else {
            Log.e("actionbar","is null");
        }
    }



    private void reStartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    PlaningPlaceRecyclerViewAdapter.OnItemClickListener onItemClickListener=new PlaningPlaceRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, PlaningPlaceRecyclerViewAdapter.ViewName viewName, int position) {
            switch (v.getId()){
                default:

                    Place place=places.get(position);
                    LatLng point=new LatLng(place.getPlaceLatitude(),place.getPlaceLongitude());
                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);

                    baiduMap.setMaxAndMinZoomLevel(18,16);
                    //移动地图界面
                    baiduMap.animateMapStatus(update);
                    if(tag==0) {
                        baiduMap.clear();
                        //在当前位置添加标注覆盖物
                        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker);
                        MarkerOptions options = new MarkerOptions().position(point).icon(descriptor);
                        baiduMap.addOverlay(options);
                    }
                    baiduMap.setMaxAndMinZoomLevel(20,5);
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v) {

        }
    };

}
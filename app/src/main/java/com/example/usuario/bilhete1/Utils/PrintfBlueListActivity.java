package com.example.usuario.bilhete1.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.usuario.bilhete1.MyApplication;
import com.example.usuario.bilhete1.R;
import com.example.usuario.bilhete1.Utils.BlueListViewAdapter;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.usuario.bilhete1.Utils.Util.rotate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class PrintfBlueListActivity extends Activity {
    

    private BluetoothAdapter mBluetoothAdapter = null;

    private PrintfManager printfManager;

    private View root;

    private static int REQUEST_ENABLE_BT = 2;
    private List<BluetoothDevice> bluetoothDeviceArrayList;

    private List<BluetoothDevice> alreadyBlueList;

    private Context context;

    private ListView lv_blue_list,lv_already_blue_list;
    private TextView tv_blue_list_back, tv_blue_list_operation;
    private String TAG = "PrintfBlueListActivity";
    private BlueListViewAdapter adapter = null;
    private boolean isRegister;

    private TextView tv_blue_list_modify,tv_blue_list_name,tv_blue_list_address;
    private ImageView iv_blue_list_already_paired,iv_blue_list_unpaired;

    private PrintfManager.BluetoothChangLister bluetoothChangLister;

    private LinearLayout ll_blue_list_already_paired,ll_blue_list_unpaired;
    private Button btbimp;
    private List<Mode> listData;

    /**
     * 是否打开了配对
     */
    private boolean ALREADY_PAIRED_IS_OPEN,UNPAIRED_IS_OPEN;

    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printf_blue_list);
        context = this;
        initView();
        initData();
        setLister();

        // Verificar permissões e iniciar busca somente após inicialização

        if (checkBluetoothPermissions() && !printfManager.isConnect()) {
            starSearchBlue();
        }

        btbimp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                imprimeBT();
            }
        });
    }

    private boolean checkBluetoothPermissions() {
        String[] permissions = {
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
        };
        List<String> permissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
                Log.d(TAG, "Permissão ausente: " + permission);
            }
        }
        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[0]), PermissionUtil.MY_PERMISSIONS_REQUEST_CODE);
            return false;
        }
        Log.d(TAG, "Todas as permissões concedidas");
        return true;
    }

    @SuppressLint("MissingPermission")
    private void stopSearchBlue() {
        tv_blue_list_operation.setText(getString(R.string.printf_blue_list_search));
        if (mReceiver != null && isRegister) {
            try {
                unregisterReceiver(mReceiver);
                isRegister = false;
                Util.ToastText(context, getString(R.string.stop_search));
            } catch (Exception e) {
                Log.e(TAG, "Erro ao desregistrar receiver: " + e.getMessage());
            }
        }
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.MY_PERMISSIONS_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted && !printfManager.isConnect()) {
                Log.d(TAG, "Permissões concedidas, iniciando busca");
                starSearchBlue();
            } else {
                Log.w(TAG, "Permissões negadas");
                new AlertDialog.Builder(context)
                        .setMessage(getString(R.string.permissions_are_rejected_bluetooth))
                        .setPositiveButton(getString(R.string.to_set_up), (dialog, which) -> {
                            Intent intent = Util.getAppDetailSettingIntent(context);
                            startActivity(intent);
                            dialog.dismiss();
                        })
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                        .setTitle(getString(R.string.prompt))
                        .show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopSearchBlue();
        if (bluetoothChangLister != null) {
            printfManager.removeBluetoothChangLister(bluetoothChangLister);
        }
        super.onDestroy();
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        isRegister = true;
        return super.registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        super.unregisterReceiver(receiver);
        isRegister = false;
    }

    @SuppressLint("MissingPermission")
    private void starSearchBlue() {
        if (!checkBluetoothPermissions()) {
            Util.ToastText(context, "Permissões de Bluetooth necessárias");
            return;
        }
        if (mBluetoothAdapter == null) {
            Util.ToastText(context, "Bluetooth não suportado");
            finish();
            return;
        }
        tv_blue_list_operation.setText(getString(R.string.printf_blue_list_stop));
        Util.ToastText(context, getString(R.string.start_search));
        bluetoothDeviceArrayList.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Log.d(TAG, "Iniciando descoberta Bluetooth");
            mBluetoothAdapter.startDiscovery();
        }
    }

    private void setLister() {

        printfManager.setConnectSuccess(new PrintfManager.ConnectSuccess() {
            @Override
            public void success() {
                finish();
            }
        });

        tv_blue_list_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!printfManager.isConnect()){
                    Util.ToastText(context,getString(R.string.please_connect_bluetooth));
                    return;
                }

                final PopupWindowManager popupWindowManager = PopupWindowManager.getInstance(context);
                popupWindowManager.showPopupWindow(getString(R.string.bluetooth_name),
                        getString(R.string.please_input_bluetooth_name),getString(R.string.bluetooth_name),root);
                popupWindowManager.changOrdinaryInputType();
                popupWindowManager.setPopCallback(new PopupWindowManager.PopCallback() {
                    @Override
                    public void callBack(String data) {
                        printfManager.changBlueName(data);
                    }
                });
            }
        });


        ll_blue_list_already_paired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ALREADY_PAIRED_IS_OPEN) {
                    ALREADY_PAIRED_IS_OPEN = false;
                    closeRotate(iv_blue_list_already_paired);
                    lv_already_blue_list.setVisibility(View.GONE);
                } else {
                    openAlreadyPaired();
                }
            }
        });

        ll_blue_list_unpaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UNPAIRED_IS_OPEN) {
                    UNPAIRED_IS_OPEN = false;
                    closeRotate(iv_blue_list_unpaired);
                    lv_blue_list.setVisibility(View.GONE);
                } else {
                    openUnpaired();
                }
            }
        });


        iv_blue_list_unpaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        lv_already_blue_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                MyApplication.getInstance().getCachedThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        printfManager.openPrinter(alreadyBlueList.get(position));
                    }
                });
            }
        });

        lv_blue_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Util.ToastText(context,getString(R.string.connect_now));
                //先停止搜索
                stopSearchBlue();
                //进行配对
                MyApplication.getInstance().getCachedThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BluetoothDevice mDevice = mBluetoothAdapter.getRemoteDevice(bluetoothDeviceArrayList.get(position).getAddress());
                            printfManager.openPrinter(mDevice);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


        tv_blue_list_operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tv_blue_list_operation.getText().toString();
                String stopText = getString(R.string.printf_blue_list_stop);
                String searchText = getString(R.string.printf_blue_list_search);
                if (text.equals(searchText)) {//点了搜索
                    starSearchBlue();
                } else if (text.equals(stopText)) {//点击了停止
                    stopSearchBlue();
                }
            }
        });

        tv_blue_list_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        PrintfManager.getInstance(context).addBluetoothChangLister(bluetoothChangLister);
    }

    public void imprimeBT(){
        if (printfManager.isConnect()){
            listData = new ArrayList<>();
            listData.add(new Mode("Test-P26",200,320));
            listData.add(new Mode("Test-P16",20,188));
            printfManager.printf_50(
                    this.getString(R.string.TEST),
                    this.getString(R.string.godown_keeper)
                    ,this.getString(R.string.send_printer),listData
            );
        }else{
            PrintfBlueListActivity.startActivity(this);
        }
    }

    private void openUnpaired() {
        UNPAIRED_IS_OPEN = true;
        openRotate(iv_blue_list_unpaired);
        lv_blue_list.setVisibility(View.VISIBLE);
    }

    private void openAlreadyPaired() {
        ALREADY_PAIRED_IS_OPEN = true;
        openRotate(iv_blue_list_already_paired);
        lv_already_blue_list.setVisibility(View.VISIBLE);
    }

    private void closeRotate(View v) {
        rotate(v, 90f, 0f);
    }

    private void openRotate(View v) {
        rotate(v, 0f, 90f);
    }

    @SuppressLint("MissingPermission")
    private void initData() {
        printfManager = PrintfManager.getInstance(context);
        bluetoothDeviceArrayList = new ArrayList<>();
        alreadyBlueList = new ArrayList<>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Util.ToastText(context, "Dispositivo não suporta Bluetooth");
            finish();
            return;
        }
        if (!checkBluetoothPermissions()) {
            Util.ToastText(context, "Permissões de Bluetooth necessárias");
            return;
        }
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        Log.d(TAG, "Dispositivos pareados encontrados: " + bondedDevices.size());
        for (BluetoothDevice device : bondedDevices) {
            if (!judge(device, alreadyBlueList)) {
                alreadyBlueList.add(device);
                Log.d(TAG, "Dispositivo pareado adicionado: " + (device.getName() != null ? device.getName() : "Desconhecido") + " " + device.getAddress());
            }
        }
        // Inicializar adapters
        adapter = new BlueListViewAdapter(context, bluetoothDeviceArrayList);
        lv_blue_list.setAdapter(adapter);
        Log.d(TAG, "Adapter inicializado para lv_blue_list com " + bluetoothDeviceArrayList.size() + " dispositivos");
        BlueListViewAdapter alreadyAdapter = new BlueListViewAdapter(context, alreadyBlueList);
        lv_already_blue_list.setAdapter(alreadyAdapter);
        Log.d(TAG, "Adapter inicializado para lv_already_blue_list com " + alreadyBlueList.size() + " dispositivos");

        // Configurar nome e endereço
        String blueName = getString(R.string.no);
        String blueAddress = getString(R.string.no);
        if (printfManager.isConnect()) {
            blueName = SharedPreferencesManager.getBluetoothName(context);
            blueAddress = SharedPreferencesManager.getBluetoothAddress(context);
        }
        tv_blue_list_name.setText(getString(R.string.name_colon) + blueName);
        tv_blue_list_address.setText(getString(R.string.address_colon) + blueAddress);

        bluetoothChangLister = (name, address) -> {
            tv_blue_list_name.setText(getString(R.string.name_colon) + name);
            tv_blue_list_address.setText(getString(R.string.address_colon) + address);
        };

        openUnpaired();
    }

    private void initView() {
        root = findViewById(R.id.root);

        ll_blue_list_already_paired = (LinearLayout)findViewById(R.id.ll_blue_list_already_paired);
        ll_blue_list_unpaired = (LinearLayout)findViewById(R.id.ll_blue_list_unpaired);

        lv_blue_list = (ListView) findViewById(R.id.lv_blue_list);
        lv_already_blue_list = (ListView)findViewById(R.id.lv_already_blue_list);
        tv_blue_list_back = (TextView) findViewById(R.id.tv_blue_list_back);
        tv_blue_list_operation = (TextView) findViewById(R.id.tv_blue_list_operation);

        tv_blue_list_modify = (TextView)findViewById(R.id.tv_blue_list_modify);
        tv_blue_list_name = (TextView)findViewById(R.id.tv_blue_list_name);
        tv_blue_list_address = (TextView)findViewById(R.id.tv_blue_list_address);

        iv_blue_list_already_paired = (ImageView)findViewById(R.id.iv_blue_list_already_paired);
        iv_blue_list_unpaired = (ImageView)findViewById(R.id.iv_blue_list_unpaired);
        btbimp = (Button) findViewById(R.id.btnImprime);
    }

    @SuppressLint("MissingPermission")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "Bluetooth ativado, iniciando descoberta");
                if (checkBluetoothPermissions()) {
                    mBluetoothAdapter.startDiscovery();
                }
            } else {
                Log.d(TAG, "Bluetooth não ativado pelo usuário");
                Util.ToastText(context, "Bluetooth necessário para listar dispositivos");
            }
        }
    }

    @SuppressLint("MissingPermission")
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (isFinishing() || isDestroyed()) {
                Log.w(TAG, "Atividade destruída, ignorando evento");
                return;
            }
            String action = intent.getAction();
            Log.d(TAG, "Ação recebida: " + action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    Log.d(TAG, "Dispositivo encontrado: " + (device.getName() != null ? device.getName() : "Desconhecido") + " " + device.getAddress());
                    if (!judge(device, bluetoothDeviceArrayList)) {
                        bluetoothDeviceArrayList.add(device);
                        Log.d(TAG, "Dispositivo adicionado: " + (device.getName() != null ? device.getName() : "Desconhecido"));
                        if (adapter != null) {
                            Log.d(TAG, "Atualizando adapter com " + bluetoothDeviceArrayList.size() + " dispositivos");
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "Adapter é null, reinicializando");
                            adapter = new BlueListViewAdapter(context, bluetoothDeviceArrayList);
                            lv_blue_list.setAdapter(adapter);
                        }
                    }
                } else {
                    Log.w(TAG, "Dispositivo nulo recebido");
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d(TAG, "Descoberta finalizada");
                tv_blue_list_operation.setText(getString(R.string.printf_blue_list_search));
                stopSearchBlue();
            }
        }
    };

    private boolean judge(BluetoothDevice device, List<BluetoothDevice> devices) {
        if (device == null || devices.contains(device)) {
            return true;
        }
        return false;
    }

    public static void startActivity(Activity activity){

        if(PrintfManager.getInstance(activity).isCONNECTING()){
            Util.ToastText(activity,activity.getString(R.string.bluetooth_is_being_connected));
            return;
        }
        Intent intent = new Intent(activity,PrintfBlueListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = MyContextWrapper.wrap(newBase);
        super.attachBaseContext(context);
    }

}

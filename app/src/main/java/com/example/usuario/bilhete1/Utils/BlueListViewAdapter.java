package com.example.usuario.bilhete1.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import androidx.core.app.ActivityCompat;

import com.example.usuario.bilhete1.R;

import java.util.List;

public class BlueListViewAdapter extends BaseAdapter {
    private final Context context;
    private final List<BluetoothDevice> devices;

    public BlueListViewAdapter(Context context, List<BluetoothDevice> devices) {
        this.context = context;
        this.devices = devices;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("MissingPermission")
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bluetooth_device, parent, false);
        }
        BluetoothDevice device = devices.get(position);
        TextView name = convertView.findViewById(R.id.tv_device_name);
        TextView address = convertView.findViewById(R.id.tv_device_address);
        name.setText(device.getName() != null ? device.getName() : "Desconhecido");
        address.setText(device.getAddress());
        return convertView;
    }
}
package com.hiroyky.bluetoothcomn;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Bluetoothでシリアル通信を行うクラスです。
 */
public class BluetoothComn {
    static protected BluetoothComn instance = new BluetoothComn();
    static final UUID TECHBOOSTER_BTSAMPLE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket socket = null;

    /**
     * @return インスタンス
     * */
    public static BluetoothComn getInstance() {
        return instance;
    }

    /**
     * 過去に接続したことのあるアダプタリストを取得します。
     * @return java.util.Set<BluetoothDevice>
     * */
    public List<BluetoothDevice> getBoundedDevices() {
        return new ArrayList<BluetoothDevice>(adapter.getBondedDevices());
    }

    /**
     * デバイスのBluetoothをONにします。
     * @return true: 成功, false: 失敗
     * */
    public boolean enableBluetooth() {
        return adapter.enable();
    }

    /**
     * Bluetoothが有効であるかどうかを取得します。
     * */
    public boolean isEnabledBluetooth() {
        return adapter.isEnabled();
    }

    /**
     * デバイスがBluetoothに対応しているかどうかを取得します。
     * @return true: 対応している, false: 対応していない
     * */
    public boolean isAvailableBluetooth() {
        return adapter != null;
    }

    /**
     * デバイスに接続します。
     * @param deviceAddress 接続先デバイスのアドレス
     * @exception IOException 接続に失敗した場合
     * */
    public void connect(String deviceAddress) throws IOException {
        for(BluetoothDevice device: adapter.getBondedDevices()) {
            if(!device.getAddress().equals(deviceAddress)) {
                continue;
            }
            connect(device);
        }
        throw new IOException("Bluetooth device is not found. " + deviceAddress);
    }

    /**
     * デバイスに接続します。
     * @param device 接続先デバイス
     * @exception IOException 接続に失敗した場合
     * */
    public void connect(BluetoothDevice device) throws IOException {
        socket = device.createRfcommSocketToServiceRecord(TECHBOOSTER_BTSAMPLE_UUID);
        socket.connect();
    }

    public void close() throws IOException {
        socket.close();
    }

    public void write(String data) throws IOException {
        OutputStream ostream = socket.getOutputStream();
        ostream.write(data.getBytes());
    }

    public int readAvailable() throws IOException {
        return socket.getInputStream().available();
    }

    public String read() throws IOException {
        byte[] buf = new byte[1024];
        InputStream istream = socket.getInputStream();
        int bytes = istream.read(buf);
        return new String(buf, 0, bytes);
    }

    public String hello() {
        return "Hello World";
    }

    public static String staticHello() {
        return "static Hello World";
    }

    public void exception() throws Exception {
        throw new Exception("Hello Exception");
    }
}

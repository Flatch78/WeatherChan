package com.guillaumek.weatherchannel.Tools;

import android.util.Log;

import com.guillaumek.weatherchannel.BuildConfig;

/**
 * Created by flatch on 13/11/15.
 */
public class MessageTool {

    /**
     * This Attributes config is set for Log messages
     * Example : if (DBG) Log.d(Tag, "Message Debug !");
     * DBG -> DEBUG Log.d
     * ERR -> ERROR Log.e
     * VERB -> VERBOSE Log.v
     * WARN -> WARNING Log.w
     */
    public String TAG;
    private static final boolean DBG = BuildConfig.DEBUG;
    private static final boolean ERR = true;
    private static final boolean VERB = true;
    private static final boolean WARN = true;
    private static final boolean INFO = true;
    private static final boolean BLE = BuildConfig.DEBUG;

    public MessageTool(String ClassName) {
        TAG = ClassName; }
    public void MsgDebug(String msg) {
        if (DBG) Log.d(TAG, msg); }
    public void MsgError(String msg) {
        if (ERR) Log.e(TAG, msg); }
    public void MsgBLE(String msg) {
        if (BLE) Log.e(TAG, msg); }
    public void MsgVerbose(String msg) {
        if (VERB) Log.v(TAG, msg); }
    public void MsgWarning(String msg) {
        if (WARN) Log.w(TAG, msg); }
    public void MsgInformation(String msg) {
        if (INFO) Log.i(TAG, msg); }
}

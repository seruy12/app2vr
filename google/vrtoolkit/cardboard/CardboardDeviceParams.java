package com.google.vrtoolkit.cardboard;

import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.util.Base64;
import android.util.Log;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.vrtoolkit.cardboard.proto.CardboardDevice;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public class CardboardDeviceParams {
    private static final float DEFAULT_INTER_LENS_DISTANCE = 0.06f;
    private static final String DEFAULT_MODEL = "Cardboard v1";
    private static final float DEFAULT_SCREEN_TO_LENS_DISTANCE = 0.042f;
    private static final String DEFAULT_VENDOR = "Google, Inc.";
    private static final float DEFAULT_VERTICAL_DISTANCE_TO_LENS_CENTER = 0.035f;
    private static final int STREAM_SENTINEL = 894990891;
    private static final String TAG = "CardboardDeviceParams";
    private static final String URI_HOST_GOOGLE = "google.com";
    private static final String URI_KEY_PARAMS = "p";
    private static final String URI_PATH_CARDBOARD_CONFIG = "cardboard/cfg";
    private static final String URI_PATH_CARDBOARD_HOME = "cardboard";
    private static final String URI_SCHEME_LEGACY_CARDBOARD = "cardboard";
    private Distortion distortion;
    private boolean hasMagnet;
    private float interLensDistance;
    private FieldOfView leftEyeMaxFov;
    private String model;
    private float screenToLensDistance;
    private String vendor;
    private VerticalAlignmentType verticalAlignment;
    private float verticalDistanceToLensCenter;
    private static final String URI_HOST_LEGACY_CARDBOARD = "v1.0.0";
    private static final Uri URI_ORIGINAL_CARDBOARD_NFC = new Uri.Builder().scheme("cardboard").authority(URI_HOST_LEGACY_CARDBOARD).build();
    private static final String HTTP_SCHEME = "http";
    private static final String URI_HOST_GOOGLE_SHORT = "g.co";
    private static final Uri URI_ORIGINAL_CARDBOARD_QR_CODE = new Uri.Builder().scheme(HTTP_SCHEME).authority(URI_HOST_GOOGLE_SHORT).appendEncodedPath("cardboard").build();
    private static final VerticalAlignmentType DEFAULT_VERTICAL_ALIGNMENT = VerticalAlignmentType.BOTTOM;
    private static final CardboardDeviceParams DEFAULT_PARAMS = new CardboardDeviceParams();

    public enum VerticalAlignmentType {
        BOTTOM(0),
        CENTER(1),
        TOP(2);

        private final int protoValue;

        VerticalAlignmentType(int i) {
            this.protoValue = i;
        }

        static VerticalAlignmentType fromProtoValue(int i) {
            for (VerticalAlignmentType verticalAlignmentType : values()) {
                if (verticalAlignmentType.protoValue == i) {
                    return verticalAlignmentType;
                }
            }
            Log.e(CardboardDeviceParams.TAG, String.format("Unknown alignment type from proto: %d", Integer.valueOf(i)));
            return BOTTOM;
        }

        final int toProtoValue() {
            return this.protoValue;
        }
    }

    public CardboardDeviceParams() {
        setDefaultValues();
    }

    public CardboardDeviceParams(CardboardDeviceParams cardboardDeviceParams) {
        copyFrom(cardboardDeviceParams);
    }

    public CardboardDeviceParams(CardboardDevice.DeviceParams deviceParams) {
        setDefaultValues();
        if (deviceParams == null) {
            return;
        }
        this.vendor = deviceParams.getVendor();
        this.model = deviceParams.getModel();
        this.interLensDistance = deviceParams.getInterLensDistance();
        this.verticalAlignment = VerticalAlignmentType.fromProtoValue(deviceParams.getVerticalAlignment());
        this.verticalDistanceToLensCenter = deviceParams.getTrayToLensDistance();
        this.screenToLensDistance = deviceParams.getScreenToLensDistance();
        this.leftEyeMaxFov = FieldOfView.parseFromProtobuf(deviceParams.leftEyeFieldOfViewAngles);
        if (this.leftEyeMaxFov == null) {
            this.leftEyeMaxFov = new FieldOfView();
        }
        this.distortion = Distortion.parseFromProtobuf(deviceParams.distortionCoefficients);
        if (this.distortion == null) {
            this.distortion = new Distortion();
        }
        this.hasMagnet = deviceParams.getHasMagnet();
    }

    private void copyFrom(CardboardDeviceParams cardboardDeviceParams) {
        this.vendor = cardboardDeviceParams.vendor;
        this.model = cardboardDeviceParams.model;
        this.interLensDistance = cardboardDeviceParams.interLensDistance;
        this.verticalAlignment = cardboardDeviceParams.verticalAlignment;
        this.verticalDistanceToLensCenter = cardboardDeviceParams.verticalDistanceToLensCenter;
        this.screenToLensDistance = cardboardDeviceParams.screenToLensDistance;
        this.leftEyeMaxFov = new FieldOfView(cardboardDeviceParams.leftEyeMaxFov);
        this.hasMagnet = cardboardDeviceParams.hasMagnet;
        this.distortion = new Distortion(cardboardDeviceParams.distortion);
    }

    public static CardboardDeviceParams createFromInputStream(InputStream inputStream) {
        CardboardDeviceParams cardboardDeviceParams;
        if (inputStream == null) {
            return null;
        }
        try {
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(8);
            if (inputStream.read(byteBufferAllocate.array(), 0, byteBufferAllocate.array().length) == -1) {
                Log.e(TAG, "Error parsing param record: end of stream.");
                cardboardDeviceParams = null;
            } else {
                int i = byteBufferAllocate.getInt();
                int i2 = byteBufferAllocate.getInt();
                if (i != STREAM_SENTINEL) {
                    Log.e(TAG, "Error parsing param record: incorrect sentinel.");
                    cardboardDeviceParams = null;
                } else {
                    byte[] bArr = new byte[i2];
                    if (inputStream.read(bArr, 0, bArr.length) == -1) {
                        Log.e(TAG, "Error parsing param record: end of stream.");
                        cardboardDeviceParams = null;
                    } else {
                        cardboardDeviceParams = new CardboardDeviceParams((CardboardDevice.DeviceParams) MessageNano.mergeFrom(new CardboardDevice.DeviceParams(), bArr));
                    }
                }
            }
            return cardboardDeviceParams;
        } catch (InvalidProtocolBufferNanoException e) {
            Log.w(TAG, "Error parsing protocol buffer: " + e.toString());
            return null;
        } catch (IOException e2) {
            Log.w(TAG, "Error reading Cardboard parameters: " + e2.toString());
            return null;
        }
    }

    public static CardboardDeviceParams createFromNfcContents(NdefMessage ndefMessage) {
        if (ndefMessage == null) {
            Log.w(TAG, "Could not get contents from NFC tag.");
            return null;
        }
        for (NdefRecord ndefRecord : ndefMessage.getRecords()) {
            CardboardDeviceParams cardboardDeviceParamsCreateFromUri = createFromUri(ndefRecord.toUri());
            if (cardboardDeviceParamsCreateFromUri != null) {
                return cardboardDeviceParamsCreateFromUri;
            }
        }
        return null;
    }

    public static CardboardDeviceParams createFromUri(Uri uri) {
        CardboardDevice.DeviceParams deviceParams;
        Exception e;
        if (uri == null) {
            return null;
        }
        if (isOriginalCardboardDeviceUri(uri)) {
            Log.d(TAG, "URI recognized as original cardboard device.");
            CardboardDeviceParams cardboardDeviceParams = new CardboardDeviceParams();
            cardboardDeviceParams.setDefaultValues();
            return cardboardDeviceParams;
        }
        if (!isCardboardDeviceUri(uri)) {
            Log.w(TAG, String.format("URI \"%s\" not recognized as cardboard device.", uri));
            return null;
        }
        String queryParameter = uri.getQueryParameter(URI_KEY_PARAMS);
        if (queryParameter != null) {
            try {
                deviceParams = (CardboardDevice.DeviceParams) MessageNano.mergeFrom(new CardboardDevice.DeviceParams(), Base64.decode(queryParameter, 11));
            } catch (Exception e2) {
                deviceParams = null;
                e = e2;
            }
            try {
                Log.d(TAG, "Read cardboard params from URI.");
            } catch (Exception e3) {
                e = e3;
                Log.w(TAG, "Parsing cardboard parameters from URI failed: " + e.toString());
            }
        } else {
            Log.w(TAG, "No cardboard parameters in URI.");
            deviceParams = null;
        }
        return new CardboardDeviceParams(deviceParams);
    }

    private static boolean isCardboardDeviceUri(Uri uri) {
        return HTTP_SCHEME.equals(uri.getScheme()) && URI_HOST_GOOGLE.equals(uri.getAuthority()) && "/cardboard/cfg".equals(uri.getPath());
    }

    public static boolean isCardboardUri(Uri uri) {
        return isOriginalCardboardDeviceUri(uri) || isCardboardDeviceUri(uri);
    }

    public static boolean isOriginalCardboardDeviceUri(Uri uri) {
        return URI_ORIGINAL_CARDBOARD_QR_CODE.equals(uri) || (URI_ORIGINAL_CARDBOARD_NFC.getScheme().equals(uri.getScheme()) && URI_ORIGINAL_CARDBOARD_NFC.getAuthority().equals(uri.getAuthority()));
    }

    private void setDefaultValues() {
        this.vendor = DEFAULT_VENDOR;
        this.model = DEFAULT_MODEL;
        this.interLensDistance = DEFAULT_INTER_LENS_DISTANCE;
        this.verticalAlignment = DEFAULT_VERTICAL_ALIGNMENT;
        this.verticalDistanceToLensCenter = DEFAULT_VERTICAL_DISTANCE_TO_LENS_CENTER;
        this.screenToLensDistance = DEFAULT_SCREEN_TO_LENS_DISTANCE;
        this.leftEyeMaxFov = new FieldOfView();
        this.hasMagnet = true;
        this.distortion = new Distortion();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CardboardDeviceParams)) {
            return false;
        }
        CardboardDeviceParams cardboardDeviceParams = (CardboardDeviceParams) obj;
        if (this.vendor.equals(cardboardDeviceParams.vendor) && this.model.equals(cardboardDeviceParams.model) && this.interLensDistance == cardboardDeviceParams.interLensDistance && this.verticalAlignment == cardboardDeviceParams.verticalAlignment) {
            return (this.verticalAlignment == VerticalAlignmentType.CENTER || this.verticalDistanceToLensCenter == cardboardDeviceParams.verticalDistanceToLensCenter) && this.screenToLensDistance == cardboardDeviceParams.screenToLensDistance && this.leftEyeMaxFov.equals(cardboardDeviceParams.leftEyeMaxFov) && this.distortion.equals(cardboardDeviceParams.distortion) && this.hasMagnet == cardboardDeviceParams.hasMagnet;
        }
        return false;
    }

    public Distortion getDistortion() {
        return this.distortion;
    }

    public boolean getHasMagnet() {
        return this.hasMagnet;
    }

    public float getInterLensDistance() {
        return this.interLensDistance;
    }

    public FieldOfView getLeftEyeMaxFov() {
        return this.leftEyeMaxFov;
    }

    public String getModel() {
        return this.model;
    }

    public float getScreenToLensDistance() {
        return this.screenToLensDistance;
    }

    public String getVendor() {
        return this.vendor;
    }

    public VerticalAlignmentType getVerticalAlignment() {
        return this.verticalAlignment;
    }

    public float getVerticalDistanceToLensCenter() {
        return this.verticalDistanceToLensCenter;
    }

    float getYEyeOffsetMeters(ScreenParams screenParams) {
        switch (getVerticalAlignment()) {
            case BOTTOM:
                return getVerticalDistanceToLensCenter() - screenParams.getBorderSizeMeters();
            case TOP:
                return screenParams.getHeightMeters() - (getVerticalDistanceToLensCenter() - screenParams.getBorderSizeMeters());
            default:
                return screenParams.getHeightMeters() / 2.0f;
        }
    }

    public boolean isDefault() {
        return DEFAULT_PARAMS.equals(this);
    }

    public void setHasMagnet(boolean z) {
        this.hasMagnet = z;
    }

    public void setInterLensDistance(float f) {
        this.interLensDistance = f;
    }

    public void setModel(String str) {
        if (str == null) {
            str = "";
        }
        this.model = str;
    }

    public void setScreenToLensDistance(float f) {
        this.screenToLensDistance = f;
    }

    public void setVendor(String str) {
        if (str == null) {
            str = "";
        }
        this.vendor = str;
    }

    public void setVerticalAlignment(VerticalAlignmentType verticalAlignmentType) {
        this.verticalAlignment = verticalAlignmentType;
    }

    public void setVerticalDistanceToLensCenter(float f) {
        this.verticalDistanceToLensCenter = f;
    }

    byte[] toByteArray() {
        CardboardDevice.DeviceParams deviceParams = new CardboardDevice.DeviceParams();
        deviceParams.setVendor(this.vendor);
        deviceParams.setModel(this.model);
        deviceParams.setInterLensDistance(this.interLensDistance);
        deviceParams.setVerticalAlignment(this.verticalAlignment.toProtoValue());
        if (this.verticalAlignment == VerticalAlignmentType.CENTER) {
            deviceParams.setTrayToLensDistance(DEFAULT_VERTICAL_DISTANCE_TO_LENS_CENTER);
        } else {
            deviceParams.setTrayToLensDistance(this.verticalDistanceToLensCenter);
        }
        deviceParams.setScreenToLensDistance(this.screenToLensDistance);
        deviceParams.leftEyeFieldOfViewAngles = this.leftEyeMaxFov.toProtobuf();
        deviceParams.distortionCoefficients = this.distortion.toProtobuf();
        if (this.hasMagnet) {
            deviceParams.setHasMagnet(this.hasMagnet);
        }
        return MessageNano.toByteArray(deviceParams);
    }

    public String toString() {
        return "{\n" + ("  vendor: " + this.vendor + ",\n") + ("  model: " + this.model + ",\n") + ("  inter_lens_distance: " + this.interLensDistance + ",\n") + ("  vertical_alignment: " + this.verticalAlignment + ",\n") + ("  vertical_distance_to_lens_center: " + this.verticalDistanceToLensCenter + ",\n") + ("  screen_to_lens_distance: " + this.screenToLensDistance + ",\n") + ("  left_eye_max_fov: " + this.leftEyeMaxFov.toString().replace("\n", "\n  ") + ",\n") + ("  distortion: " + this.distortion.toString().replace("\n", "\n  ") + ",\n") + ("  magnet: " + this.hasMagnet + ",\n") + "}\n";
    }

    public Uri toUri() {
        byte[] byteArray = toByteArray();
        return new Uri.Builder().scheme(HTTP_SCHEME).authority(URI_HOST_GOOGLE).appendEncodedPath(URI_PATH_CARDBOARD_CONFIG).appendQueryParameter(URI_KEY_PARAMS, Base64.encodeToString(byteArray, 0, byteArray.length, 11)).build();
    }

    public boolean writeToOutputStream(OutputStream outputStream) {
        try {
            byte[] byteArray = toByteArray();
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(8);
            byteBufferAllocate.putInt(STREAM_SENTINEL);
            byteBufferAllocate.putInt(byteArray.length);
            outputStream.write(byteBufferAllocate.array());
            outputStream.write(byteArray);
            return true;
        } catch (IOException e) {
            Log.w(TAG, "Error writing Cardboard parameters: " + e.toString());
            return false;
        }
    }
}

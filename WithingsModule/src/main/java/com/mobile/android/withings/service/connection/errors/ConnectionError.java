package com.mobile.android.withings.service.connection.errors;

import android.os.Parcel;
import android.os.Parcelable;

public class ConnectionError implements Parcelable {
    private int mStatusCode = -1;

    private String mStatusText = "Unknown Error";

    public ConnectionError(int code, String statusText) {
        this.mStatusCode = code;
        this.mStatusText = statusText;
    }

    public int getCode() {
        return mStatusCode;
    }

    public String getStatusText() {
        return mStatusText;
    }

    public void setStatusText(String statusText) {
        this.mStatusText = statusText;
    }

    @Override
    public String toString() {
        return mStatusCode + " " + mStatusText;
    }

    // Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    private ConnectionError(Parcel in) {
        mStatusCode = in.readInt();
        mStatusText = in.readString();
    }

    public static final Parcelable.Creator<ConnectionError> CREATOR = new Parcelable.Creator<ConnectionError>() {
        public ConnectionError createFromParcel(Parcel in) {
            return new ConnectionError(in);
        }

        public ConnectionError[] newArray(int size) {
            return new ConnectionError[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mStatusCode);
        dest.writeString(mStatusText);
    }
}

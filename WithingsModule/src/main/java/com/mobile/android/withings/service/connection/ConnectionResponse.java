package com.mobile.android.withings.service.connection;

import com.mobile.android.withings.service.connection.errors.ConnectionError;

import android.os.Parcel;
import android.os.Parcelable;

public class ConnectionResponse implements Parcelable {

    private String mResponseString;

    private ConnectionError mConnectionError;

    public ConnectionResponse() {
    }

    public String getResponse() {
        return mResponseString;
    }

    public void setResponse(String response) {
        this.mResponseString = response;
    }

    public ConnectionError getError() {
        return mConnectionError;
    }

    public void setError(ConnectionError error) {
        this.mConnectionError = error;
    }

    public boolean hasError() {
        return mConnectionError != null;
    }

    @Override
    public String toString() {
        return (mResponseString != null ? mResponseString : mConnectionError.getCode() + " "
                + mConnectionError.getStatusText());
    }

    // Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    private ConnectionResponse(Parcel in) {
        mResponseString = in.readString();
        mConnectionError = in.readParcelable(getClass().getClassLoader());
    }

    public static final Parcelable.Creator<ConnectionResponse> CREATOR = new Parcelable.Creator<ConnectionResponse>() {
        public ConnectionResponse createFromParcel(Parcel in) {
            return new ConnectionResponse(in);
        }

        public ConnectionResponse[] newArray(int size) {
            return new ConnectionResponse[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mResponseString);
        dest.writeParcelable(mConnectionError, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
    }
}

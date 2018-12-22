package com.gerpo.spottrack.database.entities

import android.os.Parcel
import android.os.Parcelable
import kaaes.spotify.webapi.android.models.Track

class PlayedTrack() : Parcelable {
    var played_at: String? = null
    lateinit var track: Track

    constructor(pa: Parcel) : this() {
        this.played_at = pa.readString()
        this.track = pa.readParcelable<Parcelable>(Track::class.java.classLoader) as Track
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.played_at)
        dest.writeParcelable(this.track, 0)
    }

    companion object CREATOR : Parcelable.Creator<PlayedTrack> {
        override fun createFromParcel(source: Parcel): PlayedTrack {
            return PlayedTrack(source)
        }

        override fun newArray(size: Int): Array<PlayedTrack?> {
            return arrayOfNulls(size)
        }
    }
}
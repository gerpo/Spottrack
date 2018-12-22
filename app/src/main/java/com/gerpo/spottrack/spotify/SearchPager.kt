package com.gerpo.spottrack.spotify

import android.os.Parcel
import android.os.Parcelable
import kaaes.spotify.webapi.android.models.Album
import kaaes.spotify.webapi.android.models.AlbumSimple
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.PlaylistSimple

class SearchPager() : Parcelable {
    lateinit var playlists: Pager<PlaylistSimple>
    lateinit var albums: Pager<Album>


    constructor(parcel: Parcel) : this() {
        this.playlists = parcel.readParcelable<Parcelable>(Pager::class.java.classLoader) as Pager<PlaylistSimple>
        this.albums = parcel.readParcelable<Parcelable>(Pager::class.java.classLoader) as Pager<Album>
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(this.playlists, 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchPager> {
        override fun createFromParcel(parcel: Parcel): SearchPager {
            return SearchPager(parcel)
        }

        override fun newArray(size: Int): Array<SearchPager?> {
            return arrayOfNulls(size)
        }
    }
}
package com.gerpo.spottrack.database

import com.gerpo.spottrack.database.dao.TrackDao
import com.gerpo.spottrack.database.entities.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class TrackDataSource(val dao: TrackDao) {

    suspend fun findByPlaylistId(playlistId: Long) : List<Track>?
    {
        return GlobalScope.async(Dispatchers.IO) { dao.findByPlaylistId(playlistId) }.await()
    }

    suspend fun getAll(): List<Track>?
    {
        return GlobalScope.async(Dispatchers.IO) { dao.getAll() }.await()
    }


    suspend fun getAllIncomplete(): List<Track>?
    {
        return GlobalScope.async(Dispatchers.IO) { dao.getAllIncomplete() }.await()
    }

    suspend fun markAsComplete(ids: List<Int>) {
        return GlobalScope.async(Dispatchers.IO) { dao.markAsComplete(ids) }.await()
    }
}
package com.example.tanut.simpletodo.backend;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by tanut on 8/4/2017.
 */

@Dao
public interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addItem(Item task);

    @Query("select * from item ORDER BY status,priority DESC")
    public List<Item> getAllItem();

    @Query("select * from item where id = :id")
    public Item getItem(int id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateItem(Item item);

    @Query("delete from item where id = :id")
    void removeItem(int id);

    @Query("delete from item")
    void removeAllItem();
}

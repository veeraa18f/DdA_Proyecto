package com.tuempresa.investtrack.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AssetDao {

    @Query("SELECT * FROM assets ORDER BY display_order ASC")
    List<AssetEntity> getAllAssets();

    @Query("SELECT * FROM assets WHERE id = :assetId LIMIT 1")
    AssetEntity getAssetById(String assetId);

    @Query("SELECT * FROM assets WHERE ticker = :ticker LIMIT 1")
    AssetEntity getAssetByTicker(String ticker);

    @Query("SELECT COUNT(*) FROM assets")
    int countAssets();

    @Query("SELECT COALESCE(MAX(display_order), -1) FROM assets")
    int getMaxDisplayOrder();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssets(List<AssetEntity> assets);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertAssetIfAbsent(AssetEntity asset);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertAsset(AssetEntity asset);

    @Query("UPDATE assets SET current_price = :currentPrice, quantity = :quantity WHERE id = :assetId")
    int updatePosition(String assetId, double currentPrice, double quantity);
}

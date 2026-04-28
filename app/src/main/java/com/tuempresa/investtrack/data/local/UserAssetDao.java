package com.tuempresa.investtrack.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserAssetDao {

    @Query(
            "SELECT assets.id AS id, assets.name AS name, assets.ticker AS ticker, " +
                    "assets.type AS type, user_assets.current_price AS current_price, " +
                    "user_assets.quantity AS quantity, user_assets.average_price AS average_price, " +
                    "assets.logo_drawable_name AS logo_drawable_name, " +
                    "user_assets.display_order AS display_order " +
                    "FROM assets INNER JOIN user_assets ON assets.id = user_assets.asset_id " +
                    "WHERE user_assets.user_id = :userId " +
                    "ORDER BY user_assets.display_order ASC"
    )
    List<AssetEntity> getPortfolioAssets(String userId);

    @Query(
            "SELECT assets.id AS id, assets.name AS name, assets.ticker AS ticker, " +
                    "assets.type AS type, user_assets.current_price AS current_price, " +
                    "user_assets.quantity AS quantity, user_assets.average_price AS average_price, " +
                    "assets.logo_drawable_name AS logo_drawable_name, " +
                    "user_assets.display_order AS display_order " +
                    "FROM assets INNER JOIN user_assets ON assets.id = user_assets.asset_id " +
                    "WHERE user_assets.user_id = :userId AND assets.id = :assetId " +
                    "LIMIT 1"
    )
    AssetEntity getPortfolioAssetById(String userId, String assetId);

    @Query("SELECT COUNT(*) FROM user_assets WHERE user_id = :userId")
    int countPortfolioAssets(String userId);

    @Query(
            "SELECT COUNT(*) FROM user_assets " +
                    "INNER JOIN assets ON user_assets.asset_id = assets.id " +
                    "WHERE user_assets.user_id = :userId " +
                    "AND user_assets.current_price = assets.current_price " +
                    "AND user_assets.quantity = assets.quantity " +
                    "AND user_assets.average_price = assets.average_price"
    )
    int countPortfolioAssetsMatchingCatalog(String userId);

    @Query("SELECT COUNT(*) FROM user_assets WHERE user_id = :userId AND asset_id = :assetId")
    int containsAsset(String userId, String assetId);

    @Query("SELECT COALESCE(MAX(display_order), -1) FROM user_assets WHERE user_id = :userId")
    int getMaxDisplayOrder(String userId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUserAsset(UserAssetEntity userAsset);

    @Query(
            "UPDATE user_assets SET current_price = :currentPrice, quantity = :quantity " +
                    "WHERE user_id = :userId AND asset_id = :assetId"
    )
    int updatePosition(String userId, String assetId, double currentPrice, double quantity);

    @Query("DELETE FROM user_assets WHERE user_id = :userId AND asset_id = :assetId")
    int deleteUserAsset(String userId, String assetId);

    @Query("DELETE FROM user_assets WHERE user_id = :userId")
    int deletePortfolioForUser(String userId);
}

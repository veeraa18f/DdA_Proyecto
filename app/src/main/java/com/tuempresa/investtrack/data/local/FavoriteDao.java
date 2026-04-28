package com.tuempresa.investtrack.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT COUNT(*) FROM favorites WHERE user_id = :userId AND asset_id = :assetId")
    int isFavorite(String userId, String assetId);

    @Query(
            "SELECT assets.id AS id, assets.name AS name, assets.ticker AS ticker, " +
                    "assets.type AS type, user_assets.current_price AS current_price, " +
                    "user_assets.quantity AS quantity, user_assets.average_price AS average_price, " +
                    "assets.logo_drawable_name AS logo_drawable_name, " +
                    "user_assets.display_order AS display_order " +
                    "FROM assets " +
                    "INNER JOIN favorites ON assets.id = favorites.asset_id " +
                    "INNER JOIN user_assets ON assets.id = user_assets.asset_id " +
                    "WHERE favorites.user_id = :userId " +
                    "AND user_assets.user_id = :userId " +
                    "ORDER BY user_assets.display_order ASC"
    )
    List<AssetEntity> getFavoriteAssets(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(FavoriteEntity favorite);

    @Query("DELETE FROM favorites WHERE user_id = :userId AND asset_id = :assetId")
    int deleteFavorite(String userId, String assetId);

    @Query("DELETE FROM favorites WHERE user_id = :userId")
    int deleteFavoritesForUser(String userId);
}

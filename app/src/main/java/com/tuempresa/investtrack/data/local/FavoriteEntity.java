package com.tuempresa.investtrack.data.local;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "favorites",
        primaryKeys = {"user_id", "asset_id"},
        foreignKeys = {
                @ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = CASCADE
                ),
                @ForeignKey(
                        entity = AssetEntity.class,
                        parentColumns = "id",
                        childColumns = "asset_id",
                        onDelete = CASCADE
                )
        },
        indices = {
                @Index(value = {"user_id"}),
                @Index(value = {"asset_id"})
        }
)
public class FavoriteEntity {

    @NonNull
    @ColumnInfo(name = "user_id")
    public final String userId;

    @NonNull
    @ColumnInfo(name = "asset_id")
    public final String assetId;

    public FavoriteEntity(@NonNull String userId, @NonNull String assetId) {
        this.userId = userId;
        this.assetId = assetId;
    }
}

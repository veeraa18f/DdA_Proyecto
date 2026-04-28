package com.tuempresa.investtrack.data.local;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "user_assets",
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
public class UserAssetEntity {

    @NonNull
    @ColumnInfo(name = "user_id")
    public final String userId;

    @NonNull
    @ColumnInfo(name = "asset_id")
    public final String assetId;

    @ColumnInfo(name = "current_price")
    public final double currentPrice;

    @ColumnInfo(name = "quantity")
    public final double quantity;

    @ColumnInfo(name = "average_price")
    public final double averagePrice;

    @ColumnInfo(name = "display_order")
    public final int displayOrder;

    public UserAssetEntity(
            @NonNull String userId,
            @NonNull String assetId,
            double currentPrice,
            double quantity,
            double averagePrice,
            int displayOrder
    ) {
        this.userId = userId;
        this.assetId = assetId;
        this.currentPrice = currentPrice;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
        this.displayOrder = displayOrder;
    }
}

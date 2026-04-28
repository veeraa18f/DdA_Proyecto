package com.tuempresa.investtrack.data.local;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "users",
        indices = {
                @Index(value = {"email"}, unique = true)
        }
)
public class UserEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public final String id;

    @NonNull
    @ColumnInfo(name = "email")
    public final String email;

    @NonNull
    @ColumnInfo(name = "password")
    public final String password;

    @NonNull
    @ColumnInfo(name = "name")
    public final String name;

    @NonNull
    @ColumnInfo(name = "phone")
    public final String phone;

    @ColumnInfo(name = "profile_photo_index")
    public final int profilePhotoIndex;

    public UserEntity(
            @NonNull String id,
            @NonNull String email,
            @NonNull String password,
            @NonNull String name,
            @NonNull String phone,
            int profilePhotoIndex
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.profilePhotoIndex = profilePhotoIndex;
    }
}

package com.tuempresa.investtrack.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUsers(UserEntity... users);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertUser(UserEntity user);

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    UserEntity getUserById(String userId);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    UserEntity getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    UserEntity authenticate(String email, String password);

    @Query("SELECT * FROM users WHERE id NOT IN (:excludedUserIds)")
    List<UserEntity> getUsersExcluding(String[] excludedUserIds);

    @Query("UPDATE users SET name = :name, phone = :phone, email = :email WHERE id = :userId")
    int updateProfile(String userId, String name, String phone, String email);

    @Query("UPDATE users SET profile_photo_index = :profilePhotoIndex WHERE id = :userId")
    int updateProfilePhoto(String userId, int profilePhotoIndex);

    @Query("UPDATE users SET password = :password WHERE email = :email")
    int updatePassword(String email, String password);
}

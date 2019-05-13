package com.example.fatcalapplication;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserStepsDao {
    @Query("SELECT * FROM usersteps")
    List<UserSteps> getAll();

    @Query("SELECT * FROM usersteps WHERE userid = :userId LIMIT 1")
    UserSteps findByID(int userId);
    @Insert
    void insertAll(UserSteps... userSteps);
    @Insert
    long insert(UserSteps customer);
    @Delete
    void delete(UserSteps customer);
    @Update(onConflict = REPLACE)
    public void updateUsers(UserSteps... userSteps);
    @Query("DELETE FROM usersteps")
    void deleteAll();

}

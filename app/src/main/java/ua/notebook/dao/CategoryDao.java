package ua.notebook.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import ua.notebook.entity.Category;

@Dao
public interface CategoryDao {
    @Query("select * from category where name = :name")
    Category findByName(String name);

    @Query("select * from category")
    List<Category> findAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long add(Category category);
}

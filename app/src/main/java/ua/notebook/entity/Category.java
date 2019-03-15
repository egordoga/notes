package ua.notebook.entity;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long cid;

    public String name;

    public Category(String name) {
        this.name = name;
    }
}

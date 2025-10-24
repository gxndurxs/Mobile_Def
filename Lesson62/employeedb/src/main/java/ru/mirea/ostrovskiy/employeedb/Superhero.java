package ru.mirea.ostrovskiy.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Superhero {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    public int power;
}
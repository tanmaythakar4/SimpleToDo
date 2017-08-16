package com.example.tanut.simpletodo.backend;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import static android.R.attr.description;

/**
 * Created by tanut on 8/4/2017.
 */
@Entity
public class Item implements Parcelable{

   @PrimaryKey(autoGenerate = true)
    public int id;
    public String item;
    public String notes;
    public int status;     //   0 = Open / ToDO  1 = Done/Finish
    public int priority;   //   0 = low 1 = mid 2 = high
    public long created;
    public long due;

    public Item(int id, String item,String notes,int status,int priority,long created,long due) {
        this.id = id;
        this.item = item;
        this.notes = notes;
        this.status = status;
        this.priority = priority;
        this.created = created;
        this.due = due;
    }

    private Item(Parcel in){
        id = in.readInt();
        item = in.readString();
        notes=in.readString();
        status = in.readInt();
        priority = in.readInt();
        created = in.readLong();
        due = in.readLong();

    }
    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public static ItemBuilder builder(){
        return new ItemBuilder();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(item);
        dest.writeString(notes);
        dest.writeInt(status);
        dest.writeInt(priority);
        dest.writeLong(created);
        dest.writeLong(due);
    }


    public static class ItemBuilder{

    private int id;
    private String item;
    private String notes = "";
    private int status = 0;
    private int priority = 0;
    private long created = new Date().getTime();
    private long due = 0;

        public ItemBuilder setId(int id){
            this.id = id;
            return this;
        }
        public ItemBuilder setItem(String item){
            this.item = item;
            return this;
        }
        public ItemBuilder setNote(String notes){
            this.notes = notes;
            return this;
        }
        public ItemBuilder setStatus(int status){
            this.status = status;
            return this;
        }
        public ItemBuilder setPriority(int priority){
            this.priority = priority;
            return this;
        }
        public ItemBuilder setDue(long due){
            this.due = due;
            return this;
        }
        public Item build(){
            return new Item(id,item,notes,status,priority,created,due);
        }

    }

    @Override
    public String toString() {

        return "Item{"+
                "id="+id+
                ", item="+item + '\'' +
                ", notes='" + notes + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", created='" + created + '\'' +
                ", due='" + due +
                '}';

    }
}

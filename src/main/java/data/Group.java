package data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class Group {
    private ArrayList<Entry> entries = new ArrayList<>();
    private StringProperty name ;

    public Group(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }


    public void addEntry(Entry e){
        this.entries.add(e);
    }



    @Override
    public String toString() {
        return  name.get();
    }

}

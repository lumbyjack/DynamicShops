package com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class StorableHashMap<K, V> extends HashMap<K, V> implements Serializable
{
	
    private static final long serialVersionUID = -3535879180214628774L;

    private transient final String extension = ".dat";

    private transient File parentFolder;
    private transient File saveFile;
    

    public StorableHashMap(File parentFolder, String fileName) throws IOException
    {
        super();
        initialise(parentFolder, fileName);
    }

    public StorableHashMap(int initialCapacity, File parentFolder, String fileName) throws IOException
    {
        super(initialCapacity);
        initialise(parentFolder, fileName);
    }

    public StorableHashMap(int initialCapacity, float loadFactor, File parentFolder, String fileName) throws IOException
    {
        super(initialCapacity, loadFactor);
        initialise(parentFolder, fileName);
    }

    public StorableHashMap(HashMap<K, V> m, File parentFolder, String fileName) throws IOException
    {
        super(m);
        initialise(parentFolder, fileName);
    }

    private void initialise(File parentFolder, String fileName) throws IOException
    {
        this.parentFolder = parentFolder;
        this.saveFile = new File(this.parentFolder, fileName + extension);
        createFile();
    }

    public boolean createFile() throws IOException
    {
        if (!parentFolder.exists() && !parentFolder.mkdirs())
            return false;
        if (!saveFile.exists() && !saveFile.createNewFile())
            return false;

        return true;
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile() throws FileNotFoundException, IOException, ClassNotFoundException
    {
        if (saveFile.length() > 0)
        {
            try (FileInputStream fileIn = new FileInputStream(saveFile);
                    BukkitObjectInputStream objectIn = new BukkitObjectInputStream(fileIn))
            {
                StorableHashMap<K, V> map = (StorableHashMap<K, V>) objectIn.readObject();
                super.clear();
                super.putAll(map);
            }
        }
    }

    public void saveToFile() throws FileNotFoundException, IOException
    {
        try (FileOutputStream fileOut = new FileOutputStream(saveFile);
                BukkitObjectOutputStream objectOut = new BukkitObjectOutputStream(fileOut))
        {
            objectOut.writeObject(this);
        }
    }
}

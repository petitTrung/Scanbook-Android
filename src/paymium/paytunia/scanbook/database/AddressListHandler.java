package paymium.paytunia.scanbook.database;

import java.math.BigDecimal;
import java.util.LinkedList;

import paymium.paytunia.scanbook.Wallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AddressListHandler
{

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "addressesListManager";

	// Table name
	private static final String TABLE_ADDRESSES = "addresses";
	
	// Table Columns names
	private static final String KEY_ADDRESS = "address";
	private static final String KEY_BALANCE = "balance";
	private static final String KEY_TOTAL_RECEIVED = "total_received";
	private static final String KEY_TOTAL_SENT = "total_sent";
	
	private static final String TAG = "DBAdapter";
	
	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_ADDRESSES + "(" 
																		+ KEY_ADDRESS + " TEXT PRIMARY KEY,"								 
																		+ KEY_BALANCE + " TEXT NOT NULL,"
																		+ KEY_TOTAL_RECEIVED + " TEXT NOT NULL,"
																		+ KEY_TOTAL_SENT + " TEXT NOT NULL"
																  + ")"; 
	
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public AddressListHandler(Context context) 
	{
		this.DBHelper = new DatabaseHelper(context);
	}
	
	public void truncate()
	{
		this.DBHelper.truncate(db);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
		
		// Creating Tables
		@Override
        public void onCreate(SQLiteDatabase db)
        {
            try 
            {
                db.execSQL(DATABASE_CREATE);
            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }
		
		// Upgrading database
		@Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
            			+ newVersion + ", which will destroy all old data");
            
        	truncate(db);
        }
		
		// Truncate table's content
		public void truncate(SQLiteDatabase db)
		{ 
			// Drop older table if existed
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESSES);

			// Create tables again
			onCreate(db);

		}
	}
	
	//---opens the database---
    public AddressListHandler open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a wallet into the database---
    public void addWallet(Wallet wallet) 
    {
    	if (!this.verifyBeforeAdding(wallet))
    	{
    		//System.out.println("PREPARING FOR ADDING A WALLET!!");
    		this.open();
    		
    		ContentValues value = new ContentValues();		

    		value.put(KEY_ADDRESS, wallet.getAddress());
    		value.put(KEY_BALANCE, wallet.getBalance().toString());
    		value.put(KEY_TOTAL_RECEIVED, wallet.getTotal_received().toString());
    		value.put(KEY_TOTAL_SENT, wallet.getTotal_sent().toString());
    		
    		db.insert(TABLE_ADDRESSES, null, value);
    		
    		this.close();
    		
    		//System.out.println("ADDING A WALLET IS DONE !!");
    	}	
    }
    
    
    // Updating a wallet
	public void updateWallet(Wallet wallet) 
	{ 
		this.open();
			
	    ContentValues value = new ContentValues();
	    
		value.put(KEY_ADDRESS, wallet.getAddress());
		value.put(KEY_BALANCE, wallet.getBalance().toString());
		value.put(KEY_TOTAL_RECEIVED, wallet.getTotal_received().toString());
		value.put(KEY_TOTAL_SENT, wallet.getTotal_sent().toString());
	 
	    // updating row
	    db.update(TABLE_ADDRESSES, value, KEY_ADDRESS + " = ?", new String[] { wallet.getAddress() });
	    
	    this.close();
	}
    
	
	// Getting a single wallet
	public Wallet getWallet(String address) 
	{
		this.open();
	 
	    Cursor cursor = db.query(TABLE_ADDRESSES, new String[] {KEY_ADDRESS, KEY_BALANCE, KEY_TOTAL_RECEIVED, KEY_TOTAL_SENT } , KEY_ADDRESS + "=?",
	            								new String[] { address }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    Wallet wallet = new Wallet();
		
		wallet.setAddress(address);
		wallet.setBalance(new BigDecimal(cursor.getString(1)));
		wallet.setTotal_received(new BigDecimal(cursor.getString(2)));
		wallet.setTotal_sent(new BigDecimal(cursor.getString(3)));

	    cursor.close();
	    
	    
	    this.close();
	    
	    // return wallet
	    return wallet;
	}
	
	
	// Getting all wallets
	public LinkedList<Wallet> getAllWallets() 
	{
		LinkedList<Wallet> walletsList = new LinkedList<Wallet>();
		
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_ADDRESSES;

		this.open();
		
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) 
		{
			do 
			{
				Wallet wallet = new Wallet();
				
				wallet.setAddress(cursor.getString(0));
				wallet.setBalance(new BigDecimal(cursor.getString(1)));
				wallet.setTotal_received(new BigDecimal(cursor.getString(2)));
				wallet.setTotal_sent(new BigDecimal(cursor.getString(3)));
				
				walletsList.add(wallet);
				
			}
			
			while (cursor.moveToNext());
		}
		
		cursor.close();
		
		this.close();
		
		// return wallets list
		
		return walletsList;
	}
	
	public LinkedList<String> getAllWalletsAddresses()
	{
		LinkedList<String> walletsAddressesList = new LinkedList<String>();
		
		
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_ADDRESSES;

		this.open();
		
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) 
		{
			do 
			{				
				walletsAddressesList.add(cursor.getString(0));	
			}
			
			while (cursor.moveToNext());
		}
		
		cursor.close();
		
		this.close();
		
		// return wallets list
		
		return walletsAddressesList;

	}
	
	public boolean verifyBeforeAdding(Wallet wallet)
	{
		LinkedList<String> walletsAddressesList = this.getAllWalletsAddresses();
		
		for (int i = 0 ; i < walletsAddressesList.size() ; i++)
		{
			if (walletsAddressesList.get(i).equals(wallet.getAddress()))
			{
				return true;
			}
		}
		return false;
	}

}

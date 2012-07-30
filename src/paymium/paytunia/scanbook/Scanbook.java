package paymium.paytunia.scanbook;

import java.io.IOException;

import paymium.paytunia.scanbook.connection.Connection;
import paymium.paytunia.scanbook.connection.ConnectionNotInitializedException;
import paymium.paytunia.scanbook.connection.DescriptionAdapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


public class Scanbook extends SherlockActivity 
{

	private Connection connection;
	
	private TextView short_address;
	private TextView full_address;
	
	private ListView description;
	private DescriptionAdapter descriptionAdapter;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	setTheme(R.style.Theme_Sherlock); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanbook);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        this.short_address = (TextView) findViewById(R.id.short_address);
        this.full_address = (TextView) findViewById(R.id.full_address);
        
        this.connection = Connection.getInstance().initialize();
              
        this.description = (ListView) findViewById(R.id.description);
        this.descriptionAdapter = new DescriptionAdapter(this);
        this.description.setAdapter(descriptionAdapter);

        new getWallet().execute();
    }

    private void setAddress(String address)
    {
    	int length = address.length();
    	this.short_address.setText(address.substring(0,3)+"..."+address.substring(length-5, length-1));
    	this.full_address.setText(address);
    }
    
    private ProgressDialog progDialog;
	private static final int DIALOG_GETTING_WALLET_PROGRESS = 1;
    
	@Override
    protected Dialog onCreateDialog(int id) 
	{
        switch (id) 
        {
            case DIALOG_GETTING_WALLET_PROGRESS: 
            	
            	this.progDialog = new ProgressDialog(this);
            	this.progDialog.setTitle("please wait");
            	this.progDialog.setIcon(R.drawable.sablier);
            	this.progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            	this.progDialog.setMessage("getting wallet");
            	this.progDialog.setCancelable(false);
            	//this.progDialog.show();
                
                return this.progDialog;
                
            default:
            	
                return null;
        }
    }
    
    public class getWallet extends AsyncTask<String, Integer, String>
    {
    	private Wallet wallet;
    	
    	public getWallet()
    	{
    		this.wallet = new Wallet();
    	}
    	
    	@Override
    	protected void onPreExecute() 
    	{
    		super.onPreExecute();
    		showDialog(DIALOG_GETTING_WALLET_PROGRESS);
    	}
    	
		@Override
		protected String doInBackground(String... params) 
		{
			try {
				wallet = connection.getWallet("1E6aHHeH6XD6ZBNWBpsyzsCytJrWoD2UJ3");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ConnectionNotInitializedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(wallet);
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);
			dismissDialog(DIALOG_GETTING_WALLET_PROGRESS);
			descriptionAdapter.setWallet(wallet);
		}
    	
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	menu.add("Refresh")
        .setIcon(R.drawable.ic_refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    	
    	return true;
    }
}

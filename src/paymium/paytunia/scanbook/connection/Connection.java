package paymium.paytunia.scanbook.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import paymium.paytunia.scanbook.Wallet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Connection 
{
	private static Connection instance;
	
	private Gson gson;
	
	private boolean isInitialized = false;
	
	/**
	 * Instantiates a new connection.
	 * Private constructor prevents instantiation from other classes
	 */
	private Connection() 
	{
		super();
	}
	
	
	/**
	 * Gets the single instance of Connection (Singleton)
	 *
	 * @return Singleton Connection
	 */
	public final static Connection getInstance() 
	{
		if (Connection.instance == null) 
		{
			synchronized (Connection.class) 
			{
				if (Connection.instance == null) 
				{
					Connection.instance = new Connection();
				}
			}
		}

		return Connection.instance;
	}
	
	public Connection initialize()
	{
		GsonBuilder gsonBuilder = new GsonBuilder();

		this.gson = gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

		this.isInitialized = true;

		return (Connection.getInstance());
	}
	
	private String getMethod(String url) throws IOException, ConnectionNotInitializedException 
	{
		if (!this.isInitialized) 
		{
			throw new ConnectionNotInitializedException("Connection has not been initialized");
		}
		else
		{
			System.setProperty("http.keepAlive", "false");
			
			HttpClient http_client = new DefaultHttpClient();
        	
	        HttpGet http_get = new HttpGet(url);
	        http_get.setHeader("Accept", "application/json");
	        
	        HttpResponse response = http_client.execute(http_get);
			InputStream content = response.getEntity().getContent();
			
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(content));
			
			StringBuilder responseBuilder = new StringBuilder();
			String line = null;
			
			while ((line = responseReader.readLine()) != null) 
			{
				responseBuilder.append(line);
			}
			
			return (responseBuilder.toString());
						
		}
	}
	
	
	public Wallet getWallet(String address) throws IOException, ConnectionNotInitializedException 
	{
		Pattern pattern;
		Matcher matcher;
		boolean successful;
		
		String response = this.getMethod(Constant.addressUrl(address));
		
		pattern = Pattern.compile("hash160");
		matcher = pattern.matcher(response);
		successful = matcher.find();
		
		//System.out.println(response);
		
		if (successful)
		{
			Wallet a = gson.fromJson(response, Wallet.class);
			
			return a;
		}
		else
		{
			return null;
		}
	}
	
}

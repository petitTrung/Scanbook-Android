package paymium.paytunia.scanbook.connection;

public class Constant 
{
	/** The Constant GET. */
	public final static String GET = "GET";
	
	public final static String backendUrl = "http://blockchain.info/address";
	
	public static String addressUrl (String address)
	{
		return backendUrl + "/" + address + "?format=json";
	}
}

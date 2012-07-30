package paymium.paytunia.scanbook;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

public class Wallet 
{
	@SerializedName("address")
	private String address;
	
	@SerializedName("final_balance")
	private BigDecimal balance;
	
	@SerializedName("total_sent")
	private BigDecimal total_sent;
	
	@SerializedName("total_received")
	private BigDecimal total_received;

	public Wallet() 
	{
		super();
	}

	public Wallet(String address) 
	{
		super();
		this.address = address;
	}

	public String getAddress() 
	{
		return address;
	}
	
	public BigDecimal getBalance() 
	{
		return balance;
	}

	public void setBalance(BigDecimal balance) 
	{
		this.balance = balance;
	}

	public void setAddress(String address) 
	{
		this.address = address;
	}
	
	public BigDecimal getTotal_sent() 
	{
		return total_sent;
	}

	public void setTotal_sent(BigDecimal total_sent) 
	{
		this.total_sent = total_sent;
	}

	public BigDecimal getTotal_received() 
	{
		return total_received;
	}

	public void setTotal_received(BigDecimal total_received) 
	{
		this.total_received = total_received;
	}

	public String toString()
	{
		StringBuilder asString = new StringBuilder();
		
	    asString.append("Wallet\n=============================\n");
	    asString.append("Address               : ");
	    asString.append(this.address);
	    asString.append("\n");
	    
	    asString.append("Balance               : ");
	    asString.append(this.balance.toString());
	    asString.append("\n");
	    
	    asString.append("Total Sent            : ");
	    asString.append(this.total_sent.toString());
	    asString.append("\n");
	    
	    asString.append("Total Received        : ");
	    asString.append(this.total_received.toString());
	    asString.append("\n\n");
	    
	    return (asString.toString());
	}
	
}

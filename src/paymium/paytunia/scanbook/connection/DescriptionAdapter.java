package paymium.paytunia.scanbook.connection;

import java.math.BigDecimal;

import paymium.paytunia.scanbook.R;
import paymium.paytunia.scanbook.Wallet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DescriptionAdapter extends BaseAdapter
{

	private LayoutInflater layoutInflater;
	private Context context;
	private Wallet wallet;
	
	private String[] items = {"Balance","Total Received","Total Sent"};
	
	public DescriptionAdapter(Context context) 
	{
		this.context = context;
		this.layoutInflater = LayoutInflater.from(this.context);
		this.wallet = new Wallet();
		this.wallet.setBalance(new BigDecimal(0));
		this.wallet.setTotal_received(new BigDecimal(0));
		this.wallet.setTotal_sent(new BigDecimal(0));
	}
	
	public Wallet getWallet() 
	{
		return wallet;
	}

	public void setWallet(Wallet wallet) 
	{
		this.wallet = wallet;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() 
	{
		return 3;
	}

	@Override
	public Object getItem(int arg0) 
	{
		return items[arg0];
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView == null)
		{
			convertView = this.layoutInflater.inflate(R.layout.list_item, null);
		}
		
		TextView label = (TextView) convertView.findViewById(R.id.label);
		TextView value = (TextView) convertView.findViewById(R.id.value);
		
		if (wallet != null)
		{
			if (position == 0)
			{
				label.setText(items[0]);
				value.setText(wallet.getBalance().toString());
			}
			else if (position == 1)
			{
				label.setText(items[1]);
				value.setText(wallet.getTotal_received().toString());
			}
			else if (position == 2)
			{
				label.setText(items[2]);
				value.setText(wallet.getTotal_sent().toString());
			}	
		}
		
		return convertView;
	}

}

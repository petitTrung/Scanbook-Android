package paymium.paytunia.scanbook;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddressListAdapter extends BaseAdapter 
{
	private LayoutInflater layoutInflater;
	private LinkedList<Wallet> walletsList;
	
	
	private Context context;

	public AddressListAdapter(Context context) 
	{
		super();
		this.context = context;
		
		this.layoutInflater = LayoutInflater.from(this.context);
		this.walletsList = new LinkedList<Wallet>();
	}

	public void addItem(LinkedList<Wallet> a)
	{
		for (int i = 0; i < a.size(); i++)
		{
			this.walletsList.add(a.get(i));
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() 
	{
		return this.walletsList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return this.walletsList.get(position);
	}

	@Override
	public long getItemId(int arg0) 
	{
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null)
		{
			convertView = layoutInflater.inflate(R.layout.scanbook_address_list_item, null);
		}
		
		TextView address = (TextView) convertView.findViewById(R.id.textView1);
		TextView balance = (TextView) convertView.findViewById(R.id.textView5);
		TextView total_received = (TextView) convertView.findViewById(R.id.textView6);
		TextView total_sent = (TextView) convertView.findViewById(R.id.textView7);
		
		Wallet wallet = this.walletsList.get(position);
		
		address.setText(wallet.getAddress());
		balance.setText(wallet.getBalance().toString());
		total_received.setText(wallet.getTotal_received().toString());
		total_sent.setText(wallet.getTotal_sent().toString());
		
		return convertView;
	}

}

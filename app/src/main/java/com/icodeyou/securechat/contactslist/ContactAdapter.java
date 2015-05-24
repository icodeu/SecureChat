package com.icodeyou.securechat.contactslist;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.icodeyou.securechat.R;
import com.icodeyou.securechat.model.Contact;
import com.icodeyou.securechat.util.DebugUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
	
	private static final int VIEW_TYPE_CONTACT = 0;
	private static final int VIEW_TYPE_LETTER = 1;

	private Context context;
	private List<Contact> contacts;
	
	private List<Object> datas;
	private Map<String, Integer> letterPosition;
	
	private void initList() {
		datas = new ArrayList<Object>();
		letterPosition = new HashMap<String, Integer>();
		Collections.sort(contacts, new Comparator<Contact>() {

			@Override
			public int compare(Contact lhs, Contact rhs) {
                try{
                    String lhsName = PinYinUtils.trans2PinYin(lhs.getName()).toUpperCase();
                    String rhsName = PinYinUtils.trans2PinYin(rhs.getName()).toUpperCase();
//                    DebugUtil.print("ContactAdapter sort lhs " + lhsName);
//                    DebugUtil.print("ContactAdapter sort rhs " + rhsName);
                    return lhsName.compareTo(rhsName);
                }catch (Exception e){

                }
                return 1;
			}
		});

        try {
            for(int i=0; i<contacts.size(); i++) {
                Contact contact = contacts.get(i);

                String firstLetter = getFirstLetter(contact.getName());
                if(!letterPosition.containsKey(firstLetter)) {
                    letterPosition.put(firstLetter, datas.size());
                    datas.add(firstLetter);
                }

                datas.add(contact);
            }
        } catch (Exception e){

        }
	}
	
	private String getFirstLetter(String name) {
		String firstLetter = "";
		char c = PinYinUtils.trans2PinYin(name).toUpperCase().charAt(0);
		if(c >= 'A' && c <= 'Z') {
			firstLetter = String.valueOf(c);
		}
		return firstLetter;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		return datas.get(position) instanceof Contact ?
				VIEW_TYPE_CONTACT : VIEW_TYPE_LETTER;
	}
	
	public int getLetterPosition(String letter) {
		Integer positoin = letterPosition.get(letter);
		return positoin == null ? -1 : positoin;
	}
	
	public void updateList() {
		initList();
		notifyDataSetChanged();
	}
	
	public ContactAdapter(Context context, List<Contact> contacts) {
		this.context = context;
		this.contacts = contacts;
		initList();
	}
	
	@Override 
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int itemViewType = getItemViewType(position);
		return itemViewType == VIEW_TYPE_CONTACT ? 
				getContactView(position, convertView) : getLetterView(position, convertView);
	}

	private View getContactView(int position, View convertView) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_contact, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.tv_contactPic = (TextView) convertView.findViewById(R.id.tvContactPic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

        Contact contact = (Contact) getItem(position);
		
		holder.tv_name.setText(contact.getName());
		holder.tv_phone.setText(contact.getNumber());
        String firstWord = ((Contact) getItem(position)).getName().substring(0, 1);
        holder.tv_contactPic.setText(firstWord);
        holder.tv_contactPic.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		
		return convertView;
	}
	
	private View getLetterView(int position, View convertView) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_letter, null);
			holder.tv_letter = (TextView) convertView.findViewById(R.id.tv_letter);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String letter = (String) getItem(position);
		
		holder.tv_letter.setText(letter);
		
		return convertView;
	}
	
	
	static class ViewHolder {
		TextView tv_letter;
		TextView tv_name;
		TextView tv_phone;
        TextView tv_contactPic;
	}


	@Override
	public boolean isItemViewTypePinned(int viewType) {
		return viewType == VIEW_TYPE_LETTER;
	}

}

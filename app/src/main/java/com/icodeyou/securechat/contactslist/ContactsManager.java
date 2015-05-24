package com.icodeyou.securechat.contactslist;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;

import com.icodeyou.securechat.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsManager {

	public static List<Contact> getContacts(Context context) {
		List<Contact> contacts = new ArrayList<Contact>();

		ContentResolver resolver = context.getContentResolver();
		Cursor cRawContact = resolver.query(RawContacts.CONTENT_URI,
				new String[] { RawContacts._ID }, null, null, null);

        Contact contact;
		while (cRawContact.moveToNext()) {
			contact = new Contact();

			long rawContactId = cRawContact.getLong(cRawContact
					.getColumnIndex(RawContacts._ID));
			contact.setRawContactId(rawContactId);

			Cursor dataCursor = resolver.query(Data.CONTENT_URI, null,
					Data.RAW_CONTACT_ID + "=?",
					new String[] { String.valueOf(rawContactId) }, null);

			while (dataCursor.moveToNext()) {
				String data1 = dataCursor.getString(dataCursor
						.getColumnIndex(Data.DATA1));
				String mimetype = dataCursor.getString(dataCursor
						.getColumnIndex(Data.MIMETYPE));

				if (mimetype.equals(StructuredName.CONTENT_ITEM_TYPE)) {
					contact.setName(data1);
				} else if (mimetype.equals(Phone.CONTENT_ITEM_TYPE)) {
					contact.setNumber(data1);
				}

			}

			contacts.add(contact);
			dataCursor.close();
		}

		cRawContact.close();
		return contacts;
	}

	public static void addContact(Context context, Contact contact) {
		ContentResolver resolver = context.getContentResolver();

		ContentValues values = new ContentValues();
		Uri rawContactUri = resolver.insert(RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);

		ContentValues valuesData1 = new ContentValues();
		valuesData1.put(Data.RAW_CONTACT_ID, rawContactId);
		valuesData1.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		valuesData1.put(Phone.NUMBER, contact.getNumber());
		resolver.insert(Data.CONTENT_URI, valuesData1);

		ContentValues valuesData2 = new ContentValues();
		valuesData2.put(Data.RAW_CONTACT_ID, rawContactId);
		valuesData2.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		valuesData2.put(StructuredName.DISPLAY_NAME, contact.getName());
		resolver.insert(Data.CONTENT_URI, valuesData2);

	}

	public static void updateContact(Context context, Contact contact) {
		ContentResolver resolver = context.getContentResolver();

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		ops.add(ContentProviderOperation
				.newUpdate(Data.CONTENT_URI)
				.withSelection(
						Data.RAW_CONTACT_ID + "=? AND " + Data.MIMETYPE + "=?",
						new String[] {
								String.valueOf(contact.getRawContactId()),
								StructuredName.CONTENT_ITEM_TYPE })
				.withValue(StructuredName.DISPLAY_NAME, contact.getName())
				.build());

		ops.add(ContentProviderOperation
				.newUpdate(Data.CONTENT_URI)
				.withSelection(
						Data.RAW_CONTACT_ID + "=? AND " + Data.MIMETYPE + "=?",
						new String[] {
								String.valueOf(contact.getRawContactId()),
								Phone.CONTENT_ITEM_TYPE })
				.withValue(Phone.NUMBER, contact.getNumber()).build());
		try {
			resolver.applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			e.printStackTrace();
		}

	}

	public static void deleteContact(Context context, Contact contact) {
		ContentResolver resolver = context.getContentResolver();
		resolver.delete(RawContacts.CONTENT_URI, RawContacts._ID + "=?",
				new String[] { String.valueOf(contact.getRawContactId()) });
	}

}

package works.tonny.mobile.autobackup;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.io.IOException;

import works.tonny.mobile.FileUtils;
import works.tonny.mobile.utils.Log;

/**
 * Created by tonny on 2016/2/25.
 */
public class ContactReader {
    static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, ContactsContract.Data.MIMETYPE, Phone.TYPE,
            Phone.NUMBER, ContactsContract.CommonDataKinds.Note.NOTE};


    public static void read(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
//        Uri uri = Uri.parse("content://com.android.contacts/data/filter/赵岩");//Phone.CONTENT_URI
        Cursor cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, null, null, ContactsContract.Data.RAW_CONTACT_ID);
        if (cursor == null) {
            return;
        }
        int oldId = -1;
        int contactId = -1;
        FileWriter fileWriter = null;
        Person person = null;
        try {
            fileWriter = new FileWriter(FileUtils.getCacheDirFile("/contact"));
            StringBuilder builder = new StringBuilder();
            while (cursor.moveToNext()) {

                contactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
                if (oldId != contactId) {
                    if (person != null) {
                        write(fileWriter, person);
                    }
                    person = new Person();
                    oldId = contactId;
                }
                String mimeType = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
                switch (mimeType) {
                    case ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE:
                        person.name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
                        person.lastName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                        person.middleName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
                        person.firstName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                        break;
                    case Phone.CONTENT_ITEM_TYPE:
                        int type = cursor.getInt(cursor.getColumnIndex(Phone.TYPE));
                        String number = cursor.getString(cursor.getColumnIndex(Phone.NUMBER)).replace("-", "");
                        switch (type) {
                            case Phone.TYPE_MOBILE:
                                person.mobile = number;
                                break;
                            case Phone.TYPE_HOME:
                                person.home = number;
                                break;
                            case Phone.TYPE_WORK:
                                person.work = number;
                                break;

                        }
                        break;
                    case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                        person.email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        break;
                    case ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE:
                        person.group = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.DISPLAY_NAME));

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.error(e);
        } finally {
            try {
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }


    }

    private static void write(FileWriter fileWriter, Person person) throws IOException {
        fileWriter.write("BEGIN:VCARD\n");
        fileWriter.write("VERSION:3.0\n");
        fileWriter.write("FN:" + person.name + "\n");
        if (!StringUtils.isEmpty(person.firstName) || !StringUtils.isEmpty(person.middleName) || !StringUtils.isEmpty(person.lastName)) {
            fileWriter.write("N:" + StringUtils.defaultString(person.firstName) + ";" + StringUtils.defaultString(person.middleName) + ";" + StringUtils.defaultString(person.lastName) + ";;\n");
        }
        if (!StringUtils.isEmpty(person.group))
            fileWriter.write("CATEGORIES:" + person.group + "\n");
        if (!StringUtils.isEmpty(person.home))
            fileWriter.write("TEL;TYPE=HOME:" + person.home + "\n");
        if (StringUtils.isNotEmpty(person.work))
            fileWriter.write("TEL;TYPE=WOEK:" + person.work + "\n");
        if (!StringUtils.isEmpty(person.mobile))
            fileWriter.write("TEL;TYPE=CELL:" + person.mobile + "\n");
        fileWriter.write("END:VCARD\n");
    }

    static class Person {
        String name;
        String firstName;
        String middleName;
        String lastName;
        String mobile;
        String home;
        String work;
        String email;
        String group;

    }
}

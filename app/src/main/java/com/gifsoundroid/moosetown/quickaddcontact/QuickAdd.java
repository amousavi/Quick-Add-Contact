package com.gifsoundroid.moosetown.quickaddcontact;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Locale;


public class QuickAdd extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add);

        EditText inputField = (EditText) findViewById(R.id.numberEdit);
        inputField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        try{
            contacts = RecentContactsStorage.getContacts(getApplicationContext());
        } catch (Exception e){
            contacts = new ArrayList<RecentContact>();
        }
        displayContactsList();

    }

    private RecentsAdapter recentsAdapter;
    private ArrayList<RecentContact> contacts;

    private void displayContactsList(){
            final String methodTag = "displayContactsList";
            recentsAdapter = new RecentsAdapter(QuickAdd.this, 0, contacts);
            getListView().setAdapter(recentsAdapter);
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    Log.v(methodTag, "item in menu clicked: "+position);

                    PopupMenu pm = new PopupMenu(getApplicationContext(),view);
                    pm.inflate(R.menu.contact_menu);
                    pm.show();

                    PopupMenu.OnMenuItemClickListener popupListener = new PopupMenu.OnMenuItemClickListener(){
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            String resultMsg = "";
                            switch(id){
                                case R.id.save:
                                    if(contacts.get(position).getHasBeenSaved()){
                                        resultMsg = "Contact has already been saved.";
                                    } else {
                                        boolean result = saveContactFromIndex(position);
                                        if(result){
                                            contacts.get(position).setHasBeenSaved(true);
                                            resultMsg = "Contact Saved: " +
                                                    "\n" + contacts.get(position).getName() +
                                                    "\n" + contacts.get(position).getNumber();
                                            notifyAdapter();
                                        } else {
                                            resultMsg = "Contact failed to save.";
                                        }
                                    }
                                    break;
                                case R.id.text:
                                    startSmsIntent(contacts.get(position).getNumber());
                                    break;
                                case R.id.delete:
                                    resultMsg = "Deleted";
                                    contacts.remove(position);
                                    resaveContacts();
                                    notifyAdapter();
                                    break;
                            }
                            if(!resultMsg.isEmpty()){
                                Toast.makeText(getApplicationContext(),resultMsg,Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }
                    };


                    pm.setOnMenuItemClickListener(popupListener);


                }
            });
    }

    private void notifyAdapter(){
        recentsAdapter.notifyDataSetChanged();
    }

    private void startSmsIntent(String number){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("smsto:" + number));
        startActivity(sendIntent);
    }

    private void resetContacts(){
        contacts = new ArrayList<RecentContact>();
        recentsAdapter = new RecentsAdapter(QuickAdd.this, 0, contacts);
        getListView().setAdapter(recentsAdapter);
        resaveContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quick_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.clear) {
            resetContacts();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public boolean saveContactFromIndex(int index){
        String name = contacts.get(index).getName();
        String number = contacts.get(index).getNumber();

        return saveToContacts(name,number);
    }

    public boolean saveToContacts(String name,String number){
        try {
            ContactStorageManager.storeNameAndNumber(
                    name,
                    number,
                    getApplicationContext());
            return true;
        } catch (ContactStorageException e){
            e.printStackTrace();
            return false;
        }
    }

    public void saveButtonClick(View v){
        String name = getNameFromField();
        String number = getNumberFromField();

        number = formatNumber(number);
        RecentContact rc = new RecentContact(name,number);
        contacts.add(rc);
        notifyAdapter();
        clearNameField();
        clearNumberFiled();
        resaveContacts();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private String formatNumber(String number){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            number = PhoneNumberUtils.formatNumber(number);
        } else {
            Log.v("Locale","iso2 country: " +  Locale.getDefault().getDisplayCountry());
            number = PhoneNumberUtils.formatNumber(number, Locale.getDefault().getCountry());
        }
        return number;
    }

    private void resaveContacts(){
        try{
            RecentContactsStorage.saveContacts(getApplicationContext(),contacts);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    private String getNameFromField(){
        EditText nameField = (EditText)findViewById(R.id.nameEdit);
        return nameField.getText().toString();
    }

    private void clearNameField(){
        EditText nameField = (EditText)findViewById(R.id.nameEdit);
        nameField.setText("", TextView.BufferType.EDITABLE);
    }

    private String getNumberFromField(){
        EditText numField = (EditText)findViewById(R.id.numberEdit);
        return numField.getText().toString();
    }

    private void clearNumberFiled(){
        EditText numField = (EditText)findViewById(R.id.numberEdit);
        numField.setText("", TextView.BufferType.EDITABLE);
    }

    private ListView getListView(){
        return (ListView) findViewById(R.id.listView);
    }

}

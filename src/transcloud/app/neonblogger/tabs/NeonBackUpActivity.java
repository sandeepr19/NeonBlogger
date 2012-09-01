package transcloud.app.neonblogger.tabs;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import transcloud.app.neonblogger.NeonApp;
import transcloud.app.neonblogger.NeonBloggerActivity;
import transcloud.app.neonblogger.NeonXmlParser;
import transcloud.app.neonblogger.R;
import transcloud.app.neonblogger.objects.NeonBlogStatus;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class NeonBackUpActivity extends ListActivity {
	NeonApp mNeonApp = NeonBloggerActivity.mNeonApp;
	 static final String URL = "http://api.androidhive.info/pizza/?format=xml";
	 static final String KEY_ITEM = "post"; // parent node
	    static final String KEY_ID = "id";
	    static final String KEY_TITLE = "title";
	    static final String KEY_CONTENT = "content";
	    static final String KEY_TAGS = "tags";
	    static final String KEY_STATUS = "status";
	    static final String KEY_CREATED_TIME = "create_time";
	    static final String KEY_UPDATED_TIME = "update_time";
	    static final String KEY_AUTHOR_ID = "author_id";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
/*		setContentView(R.layout.backup);*/
        ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
        
        NeonXmlParser parser = new NeonXmlParser();
        String userId = mNeonApp.getSession().getUserId();
        String token = mNeonApp.getSession().getToken();
        
        String xml = mNeonApp.getXML(userId, token); // getting XML
        parser.createAndStoreXML(xml);
        Document doc = parser.getDomElement(xml); // getting DOM element
        doc.getDocumentElement().normalize();
        Log.d("TAG", "response = " + doc.getDocumentElement().getNodeName());
        NodeList nodeList = doc.getElementsByTagName(KEY_ITEM);
        // looping through all item nodes <item>
        for (int i = 0; i < nodeList.getLength(); i++) {
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element element = (Element) nodeList.item(i);
            // adding each child node to HashMap key => value
           
            
            map.put(KEY_ID, "Blog Id: "+parser.getValue(element, KEY_ID));
            map.put(KEY_TITLE,"Title: "+ parser.getValue(element, KEY_TITLE));
            map.put(KEY_CONTENT, "Content: " + parser.getValue(element, KEY_CONTENT));
            map.put(KEY_TAGS,"Tags: " +parser.getValue(element, KEY_TAGS));
            map.put(KEY_STATUS,"Status: "+ NeonBlogStatus.values()[Integer.parseInt(parser.getValue(element, KEY_STATUS))]);
            map.put(KEY_CREATED_TIME,"Created Time: "+ parser.getValue(element, KEY_CREATED_TIME));
            map.put(KEY_UPDATED_TIME, "Updated Time: "+parser.getValue(element, KEY_UPDATED_TIME));
            map.put(KEY_AUTHOR_ID,"Author Id: "+ parser.getValue(element, KEY_AUTHOR_ID));
 
            // adding HashList to ArrayList
            menuItems.add(map);
        }
 
        // Adding menuItems to ListView
        ListAdapter adapter = new SimpleAdapter(this, menuItems,
                R.layout.list_item,
                new String[] { KEY_TITLE, KEY_CONTENT, KEY_TAGS,KEY_STATUS,KEY_CREATED_TIME,KEY_UPDATED_TIME,KEY_AUTHOR_ID }, new int[] {
                        R.id.titleBackup, R.id.contentBackup, R.id.tagsBackup,R.id.statusBackup,R.id.createdTimeBackup,R.id.updatedTimeBackup,R.id.authorIdBackup});
 
        setListAdapter(adapter);
 
        // selecting single ListView item
        ListView lv = getListView();
                // listening to single listitem click
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
               /* String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                String cost = ((TextView) view.findViewById(R.id.cost)).getText().toString();
                String description = ((TextView) view.findViewById(R.id.desciption)).getText().toString();
 
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
                in.putExtra(KEY_NAME, name);
                in.putExtra(KEY_COST, cost);
                in.putExtra(KEY_DESC, description);
                startActivity(in);*/
 
            }
        });
	}

	

}

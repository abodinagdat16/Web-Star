package com.star4droid.WebStar;

import android.app.Activity;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import org.json.*;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.CheckBox;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.HorizontalScrollView;
import android.widget.Button;
import java.util.Timer;
import java.util.TimerTask;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.animation.ObjectAnimator;
import android.view.animation.LinearInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.DialogFragment;
import android.Manifest;
import android.content.pm.PackageManager;

public class LayoutEditorActivity extends Activity {
	
	private Timer _timer = new Timer();
	
	private String path = "";
	private HashMap<String, Object> map = new HashMap<>();
	private boolean ev = false;
	private boolean refresh = false;
	
	private ArrayList<String> names = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> widgets_listmap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> add_widgets = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> events_and_properties = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout main;
	private LinearLayout bottom;
	private ImageView imageview1;
	private ImageView imageview2;
	private ImageView imageview3;
	private ImageView imageview5;
	private ImageView imageview6;
	private ImageView imageview7;
	private Spinner spinner1;
	private LinearLayout linear5;
	private LinearLayout layout;
	private ListView events;
	private TextView nameT;
	private CheckBox checkbox1;
	private ListView widgets_list;
	private WebView webview1;
	private LinearLayout linear3;
	private HorizontalScrollView hscroll1;
	private Button button1;
	private Button button2;
	private LinearLayout linear6;
	private GridView gridview1;
	
	private TimerTask timer;
	private AlertDialog.Builder dl;
	private ObjectAnimator obj = new ObjectAnimator();
	private Intent intent = new Intent();
	private AlertDialog dialog;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.layout_editor);
		initialize(_savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
			||checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
			} else {
				initializeLogic();
			}
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		main = findViewById(R.id.main);
		bottom = findViewById(R.id.bottom);
		imageview1 = findViewById(R.id.imageview1);
		imageview2 = findViewById(R.id.imageview2);
		imageview3 = findViewById(R.id.imageview3);
		imageview5 = findViewById(R.id.imageview5);
		imageview6 = findViewById(R.id.imageview6);
		imageview7 = findViewById(R.id.imageview7);
		spinner1 = findViewById(R.id.spinner1);
		linear5 = findViewById(R.id.linear5);
		layout = findViewById(R.id.layout);
		events = findViewById(R.id.events);
		nameT = findViewById(R.id.nameT);
		checkbox1 = findViewById(R.id.checkbox1);
		widgets_list = findViewById(R.id.widgets_list);
		webview1 = findViewById(R.id.webview1);
		webview1.getSettings().setJavaScriptEnabled(true);
		webview1.getSettings().setSupportZoom(true);
		linear3 = findViewById(R.id.linear3);
		hscroll1 = findViewById(R.id.hscroll1);
		button1 = findViewById(R.id.button1);
		button2 = findViewById(R.id.button2);
		linear6 = findViewById(R.id.linear6);
		gridview1 = findViewById(R.id.gridview1);
		dl = new AlertDialog.Builder(this);
		
		imageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				finish();
			}
		});
		
		imageview5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), FilesManagerActivity.class);
				intent.putExtra("path", path);
				startActivity(intent);
			}
		});
		
		imageview7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (layout.getVisibility() == View.VISIBLE) {
					layout.setVisibility(View.GONE);
					events.setVisibility(View.VISIBLE);
					imageview7.setImageResource(R.drawable.layout);
				}
				else {
					layout.setVisibility(View.VISIBLE);
					events.setVisibility(View.GONE);
					imageview7.setImageResource(R.drawable.icons8_click_64);
				}
			}
		});
		
		widgets_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				try {
					String str = "";
					try {
						str = add_widgets.get((int)_getPosByName(nameT.getText().toString(), names)).get("type").toString();
					} catch (Exception e) {
						SketchwareUtil.showMessage(getApplicationContext(), "getting type eror!\n".concat((e.toString())));
					}
					if (str.equals("layout")) {
						double btn = 0;
						btn++;
						while(names.contains(widgets_listmap.get((int)_position).get("name").toString().concat(String.valueOf((long)(btn))))) {
							btn++;
						}
						try {
							map = new HashMap<>();
							map.put("name", widgets_listmap.get((int)_position).get("name").toString().concat(String.valueOf((long)(btn))));
							map.put("type", widgets_listmap.get((int)_position).get("type").toString());
							map.put("img", widgets_listmap.get((int)_position).get("img").toString());
						} catch (Exception e) {
							SketchwareUtil.showMessage(getApplicationContext(), "setting map eror!\n".concat((e.toString())));
						}
						try {
							names.add(widgets_listmap.get((int)_position).get("name").toString().concat(String.valueOf((long)(btn))));
							webview1.loadUrl("javascript:".concat(widgets_listmap.get((int)_position).get("before").toString().concat(names.get((int)(_getPosByName(nameT.getText().toString(), names))).concat(widgets_listmap.get((int)_position).get("after").toString())).concat("")).replace("btn", widgets_listmap.get((int)_position).get("name").toString().concat(String.valueOf((long)(btn)))));
							add_widgets.add(map);
						} catch (Exception e) {
							SketchwareUtil.showMessage(getApplicationContext(), "setting script eror!\n".concat((e.toString())));
						}
					}
					else {
						SketchwareUtil.showMessage(getApplicationContext(), "The seleted item is not layout!!");
					}
				} catch(Exception ex) {
					SketchwareUtil.showMessage(getApplicationContext(), "Unknown eror!\n".concat((ex.toString())));
				}
			}
		});
		
		//webviewOnProgressChanged
		webview1.setWebChromeClient(new WebChromeClient() {
				@Override public void onProgressChanged(WebView view, int _newProgress) {
					
				}
		});
		
		//OnDownloadStarted
		webview1.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				DownloadManager.Request webview1a = new DownloadManager.Request(Uri.parse(url));
				String webview1b = CookieManager.getInstance().getCookie(url);
				webview1a.addRequestHeader("cookie", webview1b);
				webview1a.addRequestHeader("User-Agent", userAgent);
				webview1a.setDescription("Downloading file...");
				webview1a.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
				webview1a.allowScanningByMediaScanner(); webview1a.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); webview1a.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
				
				DownloadManager webview1c = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
				webview1c.enqueue(webview1a);
				showMessage("Downloading File....");
				BroadcastReceiver onComplete = new BroadcastReceiver() {
					public void onReceive(Context ctxt, Intent intent) {
						showMessage("Download Complete!");
						unregisterReceiver(this);
						
					}};
				registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
			}
		});
		
		webview1.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
				final String _url = _param2;
				
				super.onPageStarted(_param1, _param2, _param3);
			}
			
			@Override
			public void onPageFinished(WebView _param1, String _param2) {
				final String _url = _param2;
				
				super.onPageFinished(_param1, _param2);
			}
		});
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				button1.getBackground().setColorFilter(0xFF29354B, PorterDuff.Mode.MULTIPLY);
				button2.getBackground().setColorFilter(0xFF0067EE, PorterDuff.Mode.MULTIPLY);
				events_and_properties.clear();
				String properties = "name code choice1 choice2..........etc\ncss css\nheight style.height auto value initial inherit 50% 100%\nwidth style.width auto value initial inherit 50% 100%\nmax-height style[\"max-height\"]\nmax-width style[\"max-width\"]\nmin-width style[\"min-width\"]\nmin-height style[\"min-height\"]\ntext innerText\nsrc src\nbackground-color style[\"background-color\"] transparent\ncolor style[\"color\"]\nid id\ntext-align style[\"text-align\"] center left right\nfont-weight lighter bolder bold normal\nvertical-align style[\"vertical-align\"] middle left right\npadding style[\"padding\"]\noverflow-x style[\"overflow-x\"] scroll hidden\noverflow-y style[\"overflow-y\"] scroll hidden\nopacity style[\"opacity\"]\nmargin style[\"margin\"]\nimage style.backgroundImage url('path')\ndisplay style.display flex inline-block block none\ntype type week url time text tel submit search reset range radio password number month image hidden file email datetime-local date color checkbox button\npattern pattern\nvalue value\nmin min 2000-01-01\nborder-radius style[\"border-radius\"]\nborder style[\"border\"]\nborderColor style.borderColor\nmax max 2000-01-01\nchecked checked true false\nmaxlength maxlength\nfont-family style[\"font-family\"]\njustify-content style[\"justify-content\"] center\nalignItems style.alignItems stretch center flex-start flex-end baseline initial inherit".replace("name code choice1 choice2..........etc\n", "");
				for (String s:properties.split("\n")){
					{
						HashMap<String, Object> _item = new HashMap<>();
						_item.put("properties", s);
						events_and_properties.add(_item);
					}
					
				}
				gridview1.setNumColumns((int)events_and_properties.size());
				gridview1.setVerticalSpacing((int)3);
				gridview1.setHorizontalSpacing((int)3);
				ev = false;
				gridview1.setLayoutParams(new LinearLayout.LayoutParams((int) (events_and_properties.size() * 220),(int) (ViewGroup.LayoutParams.MATCH_PARENT)));
				gridview1.setAdapter(new Gridview1Adapter(events_and_properties));
				linear6.setVisibility(View.GONE);
				linear6.setVisibility(View.VISIBLE);
			}
		});
		
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				events_and_properties.clear();
				map = new HashMap<>();
				_putKeysBySplit("name\n1OnClick\n1data\n1%element.addEventListener( 'click', ( event ) => {%script\n});\n1parameters\n1target event.target", map);
				events_and_properties.add(map);
				map = new HashMap<>();
				_putKeysBySplit("name\n1touchmove\n1data\n1%element.addEventListener( 'touchmove', ( event ) => {%script\n});\n1parameters\n1target event.target", map);
				events_and_properties.add(map);
				map = new HashMap<>();
				_putKeysBySplit("name\n1touchStart\n1data\n1%element.addEventListener( 'touchstart', ( event ) => {%script\n});\n1parameters\n1target event.target", map);
				events_and_properties.add(map);
				map = new HashMap<>();
				_putKeysBySplit("name\n1touchmove\n1data\n1%element.addEventListener( 'touchmove', ( event ) => {%script\n});\n1parameters\n1target event.target", map);
				events_and_properties.add(map);
				map = new HashMap<>();
				_putKeysBySplit("name\n1onload\n1data\n1%element.onload = function(){%script\n};", map);
				events_and_properties.add(map);
				ev = true;
				gridview1.setColumnWidth((int)320);
				gridview1.setNumColumns((int)events_and_properties.size());
				gridview1.setVerticalSpacing((int)2);
				gridview1.setHorizontalSpacing((int)2);
				gridview1.setLayoutParams(new LinearLayout.LayoutParams((int) (events_and_properties.size() * 210),(int) (ViewGroup.LayoutParams.MATCH_PARENT)));
				gridview1.setAdapter(new Gridview1Adapter(events_and_properties));
				button1.getBackground().setColorFilter(0xFF0067EE, PorterDuff.Mode.MULTIPLY);
				button2.getBackground().setColorFilter(0xFF212D3D, PorterDuff.Mode.MULTIPLY);
			}
		});
	}
	
	private void initializeLogic() {
		path = getIntent().getStringExtra("path");
		webview1.setWebViewClient(new WebViewClient()); 
		webview1.getSettings().setLoadWithOverviewMode(true);
		//webview1.getSettings().setUseWideViewPort(true);
		webview1.getSettings().setJavaScriptEnabled(true);
		webview1.getSettings().setPluginState(WebSettings.PluginState.ON);
		webview1.getSettings().setAllowFileAccess(true);
		webview1.getSettings().setAllowContentAccess(true);
		webview1.getSettings().setAllowFileAccessFromFileURLs(true);
		webview1.getSettings().setAllowUniversalAccessFromFileURLs(true);
		webview1.getSettings().setJavaScriptEnabled(true);
		webview1.getSettings().setDomStorageEnabled(true);
		webview1.addJavascriptInterface(new Winterface(LayoutEditorActivity.this), "Winterface");
		webview1.loadUrl("file://".concat(path.concat("/index.html.Editor.html")));
		main.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
		events.setVisibility(View.GONE);
		String scr = "document.body.addEventListener( 'click', ( event ) => {\nWinterface.Winterface(\"document.body\"); \n});";
		if ("".equals(FileUtil.readFile(path.concat("/index.html/lists/widgets.json")))) {
			map = new HashMap<>();
			map.put("type", "layout");
			map.put("name", "document.body");
			add_widgets.add(map);
			names.add("document.body");
			FileUtil.writeFile(path.concat("/index.html/lists/widgets.json"), new Gson().toJson(add_widgets));
		}
		else {
			add_widgets = new Gson().fromJson(FileUtil.readFile(path.concat("/index.html/lists/widgets.json")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			double n = 0;
			for(int _repeat44 = 0; _repeat44 < (int)(add_widgets.size()); _repeat44++) {
				if (n > 0) {
					scr = scr.concat("\n".concat(add_widgets.get((int)n).get("name").toString().concat(".addEventListener( 'click', ( event ) => {\nWinterface.Winterface(event.target.id); \n});")));
					names.add(add_widgets.get((int)n).get("name").toString());
				}
				n++;
			}
		}
		final String str=scr; 
		timer = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						webview1.evaluateJavascript("try {".concat(str.concat("} catch(Exception e){\nWinterface.refresh();\n} ")), null);
					}
				});
			}
		};
		_timer.schedule(timer, (int)(100));
		
		_setupTheListMap();
		android.graphics.drawable.GradientDrawable clr = new android.graphics.drawable.GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TL_BR , new int[] {0xFF454473,0xFF454473});
		clr.setCornerRadii(new float[] { (float)(15), (float)(15), (float)(15), (float)(15), (float)(0),(float)(0),(float)(0),(float)(0) }); //LeftTop, //RightTop, //RightBottom, //LeftBottom,
		bottom.setBackground(clr);
		button1.getBackground().setColorFilter(0xFF0067EE, PorterDuff.Mode.MULTIPLY);
		button2.getBackground().setColorFilter(0xFF0067EE, PorterDuff.Mode.MULTIPLY);
		gridview1.setLayoutParams(new LinearLayout.LayoutParams((int) (1000),(int) (ViewGroup.LayoutParams.MATCH_PARENT)));
	}
	
	@Override
	public void onBackPressed() {
		if (bottom.getTranslationY() == 0) {
			finish();
		}
		else {
			obj.setTarget(bottom);
			obj.setPropertyName("translationY");
			obj.setFloatValues((float)(bottom.getTranslationY()), (float)(0));
			obj.setDuration((int)(300));
			obj.start();
		}
	}
	public void _My_Interface() {
	}
	class Winterface {
		Activity mContext;
		public Winterface(Activity c) { mContext = c; }
		
		@JavascriptInterface
		public void Winterface(String s) {
			double n=0;
			for(String in:names){
				if (in.equals(s.replace("\"", ""))) {
					break;
				}
				n++;
			}
			nameT.setText(s.replace("\"", ""));
			timer = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							_show_properties();
							linear6.setVisibility(View.GONE);
							linear6.setVisibility(View.VISIBLE);
						}
					});
				}
			};
			_timer.schedule(timer, (int)(100));
		}
		@JavascriptInterface
		public void refresh() {
			dl.setTitle("Eror!");
			dl.setMessage("an error occurred!! \ntry load the content again? ");
			dl.setPositiveButton("yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					String scr = "document.body.addEventListener( 'click', ( event ) => {\nWinterface.click(\"document.body\");\n}); ";
					double n = 0;
					for(int _repeat21 = 0; _repeat21 < (int)(add_widgets.size()); _repeat21++) {
						if (n > 0) {
							scr = scr.concat("\n".concat(add_widgets.get((int)n).get("name").toString().concat(".addEventListener( 'click', ( event ) => {\nWinterface.click(event.target.id); \n});")));
						}
						n++;
					}
					final String str=scr;
					timer = new TimerTask() {
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									webview1.evaluateJavascript("try {".concat(str.concat("} catch(Exception e){\ninterface.refresh();\n} ")), null);
								}
							});
						}
					};
					_timer.schedule(timer, (int)(100));
				}
			});
			dl.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					finish();
				}
			});
			dl.create().show();
		}
		@JavascriptInterface
		public void refresh(String id) {
			final double se = _getPosByName(nameT.getText().toString(), names);
			map = new HashMap<>();
			map = new Gson().fromJson(new Gson().toJson(add_widgets.get((int)(se))), new TypeToken<HashMap<String, Object>>(){}.getType());
			map.put("name", id.replace("\"", ""));
			add_widgets.remove((int)(se));
			names.remove((int)(se));
			add_widgets.add((int)se, map);
			/**/
			/*
timer = new TimerTask() {
@Override
public void run() {
runOnUiThread(new Runnable() {
@Override
public void run() {


}
});
}
};
_timer.schedule(timer, (int)(100));
*/
		}
	}
	
	
	public void _putKeysBySplit(final String _string, final HashMap<String, Object> _map) {
		boolean b = false;
		String set="";
		for(String s:_string.split("\n1")){
				if (b) {
						map.put(set, s);
				}
				else {
						set=s; 
				}
				b = !b;
		}
	}
	
	
	public void _setupTheListMap() {
		map = new HashMap<>();
		_putKeysBySplit("name\n1button\n1before\n1var btn = document.createElement(\"button\");\n\n1after\n1.appendChild(btn);\nbtn.id=\"btn\";\nbtn.innerText=\"btn\";\nbtn.addEventListener( 'click', ( event ) => {\nWinterface.Winterface(event.target.id);\n});\n1html\n1<button id=\"btn\">button</button>\n1type\n1button\n1img\n1widget_button", map);
		widgets_listmap.add(map);
		map = new HashMap<>();
		_putKeysBySplit("name\n1layout\n1before\n1var btn = document.createElement(\"div\");\n\n1after\n1.appendChild(btn);\nbtn.id=\"btn\";\nbtn.style[\"min-height\"]=\"50px\";\nbtn.style[\"min-width\"]=\"50px\";\nbtn.style[\"border\"]=\"1px solid black\";\nbtn.addEventListener( 'click', ( event ) => {\nWinterface.Winterface(event.target.id);\n});\n1html\n1<div id=\"btn\"></div>\n1type\n1layout\n1img\n1widget_linear_horizontal", map);
		widgets_listmap.add(map);
		map = new HashMap<>();
		_putKeysBySplit("name\n1textarea\n1before\n1var btn = document.createElement(\"textarea\");\n\n1after\n1.appendChild(btn);\nbtn.id=\"btn\";\nbtn.innerText=\"btn\";\nbtn.addEventListener( 'click', ( event ) => {\nWinterface.Winterface(event.target.id);\n});\n1html\n1<textarea id=\"btn\">button</textarea>\n1type\n1button\n1img\n1widget_edit_text", map);
		widgets_listmap.add(map);
		map = new HashMap<>();
		_putKeysBySplit("name\n1text\n1before\n1var btn = document.createElement(\"div\");\n\n1after\n1.appendChild(btn);\nbtn.id=\"btn\";\nbtn.innerText=\"btn\";\nbtn.addEventListener( 'click', ( event ) => {\nWinterface.Winterface(event.target.id);\n});\n1html\n1<div id=\"btn\">button</div>\n1type\n1text\n1img\n1widget_text_view", map);
		widgets_listmap.add(map);
		map = new HashMap<>();
		_putKeysBySplit("name\n1progressbar\n1before\n1var btn = document.createElement(\"progress\");\n\n1after\n1.appendChild(btn);\nbtn.id=\"btn\";\nbtn.addEventListener( 'click', ( event ) => {\nWinterface.Winterface(event.target.id);\n});\n1html\n1<div id=\"btn\"></div>\n1type\n1progress\n1img\n1widget_progress", map);
		widgets_listmap.add(map);
		map = new HashMap<>();
		_putKeysBySplit("name\n1form\n1before\n1var btn = document.createElement(\"form\");\n\n1after\n1.appendChild(btn);\nbtn.id=\"btn\";\nbtn.style[\"min-height\"]=\"50px\";\nbtn.style[\"min-width\"]=\"50px\";\nbtn.style[\"border\"]=\"1px solid black\";\nbtn.addEventListener( 'click', ( event ) => {\nWinterface.Winterface(event.target.id);\n});\n1html\n1<div id=\"btn\"></div>\n1type\n1layout\n1img\n1widget_linear_horizontal", map);
		widgets_listmap.add(map);
		map = new HashMap<>();
		_putKeysBySplit("name\n1input\n1before\n1var btn = document.createElement(\"input\");\nbtn.type=\"button\";\n1after\n1.appendChild(btn);\nbtn.id=\"btn\";\ntry {\nbtn.innerText=\"btn\"; \n} catch(e){}\nbtn.addEventListener( 'click', ( event ) => {\nWinterface.Winterface(event.target.id);\n});\n1html\n1<button id=\"btn\">button</button>\n1type\n1input\n1img\n1widget_button", map);
		widgets_listmap.add(map);
		widgets_list.setAdapter(new Widgets_listAdapter(widgets_listmap));
	}
	
	
	public void _show_properties() {
		obj.setTarget(bottom);
		obj.setPropertyName("translationY");
		obj.setFloatValues((float)(bottom.getTranslationY()), (float)(-350));
		obj.setDuration((int)(300));
		obj.start();
		if (ev) {
			
		}
		else {
			button1.performClick(); 
		}
	}
	
	
	public String _removeCharAt(final double _n, final String _string) {
		
		    String front = _string.substring(0,(int) _n);
		    String back = _string.substring((int) _n +1,_string.length());
		    return front + back;
	}
	
	
	public double _getPosByName(final String _name, final ArrayList<String> _list) {
		double n=0;
		for(int _repeat10 = 0; _repeat10 < (int)(_list.size()); _repeat10++) {
			if (_name.equals(_list.get((int)(n)))) {
				break;
			}
			n++;
		}
		return (n);
	}
	
	public class Widgets_listAdapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Widgets_listAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.widget_csv, null);
			}
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			
			textview1.setText(_data.get((int)_position).get("name").toString());
			
			return _view;
		}
	}
	
	public class Gridview1Adapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Gridview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.property, null);
			}
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			
			linear1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFFFFFFFF));
			if (ev) {
				textview1.setText(_data.get((int)_position).get("name").toString());
			}
			else {
				final String pr = _data.get((int)_position).get("properties").toString();
				textview1.setText(pr.split(" ")[0]);
				linear1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						final AlertDialog dialog = new AlertDialog.Builder(LayoutEditorActivity.this).create();
						LayoutInflater dialogLI = getLayoutInflater();
						View dialogCV = (View) dialogLI.inflate(R.layout.edit_attribute, null);
						dialog.setView(dialogCV);
						final LinearLayout con = (LinearLayout)
						dialogCV.findViewById(R.id.con);
						final TextView cancel = (TextView)
						dialogCV.findViewById(R.id.cancel);
						final TextView save = (TextView)
						dialogCV.findViewById(R.id.save);
						final EditText value = (EditText)
						dialogCV.findViewById(R.id.value);
						final WebView web = (WebView)
						dialogCV.findViewById(R.id.web);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
						dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT));
						dialog.show();
						con.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFF29354B));
						save.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFF0067EE));
						cancel.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFF0067EE));
						double n=0;
						for(String iu:pr.split(" ")){
							if (n>1){
							}
							n++;
						}
						cancel.setOnClickListener(new View.OnClickListener(){
							@Override
							public void onClick(View _view){
								dialog.dismiss();
							}
						});
						save.setOnClickListener(new View.OnClickListener(){
							@Override
							public void onClick(View _view){
								if (pr.split(" ")[0].equals("id")) {
									webview1.evaluateJavascript("document.getElementById(\"%obj\").%property=\"%value\";\nWinterface.refresh(obj.id);".replace("%obj", add_widgets.get((int)_getPosByName(nameT.getText().toString(), names)).get("name").toString()).replace("%value", value.getText().toString()).replace("%property", pr.split(" ")[1]), null);
								}
								else {
									webview1.evaluateJavascript("document.getElementById(\"%obj\").%property=\"%value\";".replace("%obj", add_widgets.get((int)_getPosByName(nameT.getText().toString(), names)).get("name").toString()).replace("%value", value.getText().toString()).replace("%property", pr.split(" ")[1]), null);
								}
								dialog.dismiss();
							}
						});
						webview1.evaluateJavascript(add_widgets.get((int)_getPosByName(nameT.getText().toString(), names)).get("name").toString().concat(".".concat(pr.split(" ")[1])), new ValueCallback<String>() {
							    @Override
							    public void onReceiveValue(String s888) {
								            if ("\"".equals(String.valueOf(s888.charAt((int)(s888.length() - 1)))) && "\"".equals(String.valueOf(s888.charAt((int)(0))))) {
									value.setText(_removeCharAt(0, _removeCharAt(s888.length() - 1, s888)));
								}
								else {
									value.setText(s888);
								}
								        }
							    });
						web.setWebViewClient(new WebViewClient()); 
						webview1.getSettings().setLoadWithOverviewMode(true);
						//web.getSettings().setUseWideViewPort(true);
						web.getSettings().setJavaScriptEnabled(true);
						web.getSettings().setPluginState(WebSettings.PluginState.ON);
						web.getSettings().setAllowFileAccess(true);
						web.getSettings().setAllowContentAccess(true);
						web.getSettings().setAllowFileAccessFromFileURLs(true);
						web.getSettings().setAllowUniversalAccessFromFileURLs(true);
						web.getSettings().setJavaScriptEnabled(true);
						web.getSettings().setDomStorageEnabled(true);
						web.loadUrl("data:text/html ,<html>".concat("<html>\n<body><input type=\"color\" id=\"p\"/><label id=\"txt\" height=\"50%\" width=\"50%\">black</label>\n<script>\ndocument.getElementById(\"p\").addEventListener(\"change\",function(event){\ndocument.getElementById(\"txt\").innerText=event.target.value;\nWinterface.pick(event.target.value);\nalert(event.target.value);\n});\ndocument.body.addEventListener(\"click\",function(event){\n Winterface.pick(\"dismiss\");\ndocument.getElementById(\"p\").click();\n});\n</script>\n</body></html>".concat("<html>")));
					}
				});
			}
			
			return _view;
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}
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
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.DialogFragment;
import android.Manifest;
import android.content.pm.PackageManager;

public class MainActivity extends Activity {
	
	private ArrayList<HashMap<String, Object>> listmap = new ArrayList<>();
	private ArrayList<String> lst = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private LinearLayout NoProjects;
	private ListView listview1;
	private LinearLayout linear3;
	private Button button2;
	private Button button3;
	private LinearLayout lin;
	private ImageView imageview2;
	private TextView textview1;
	private LinearLayout info_lin;
	private LinearLayout linear4;
	private LinearLayout add_lin;
	private ImageView imageview1;
	private Button button1;
	
	private AlertDialog dialog;
	private Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
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
		linear2 = findViewById(R.id.linear2);
		NoProjects = findViewById(R.id.NoProjects);
		listview1 = findViewById(R.id.listview1);
		linear3 = findViewById(R.id.linear3);
		button2 = findViewById(R.id.button2);
		button3 = findViewById(R.id.button3);
		lin = findViewById(R.id.lin);
		imageview2 = findViewById(R.id.imageview2);
		textview1 = findViewById(R.id.textview1);
		info_lin = findViewById(R.id.info_lin);
		linear4 = findViewById(R.id.linear4);
		add_lin = findViewById(R.id.add_lin);
		imageview1 = findViewById(R.id.imageview1);
		button1 = findViewById(R.id.button1);
		
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Intent i = new Intent();
				i.setClassName("com.star4droid.BlocksLibV2", "com.star4droid.BlocksLibV2.ManagerActivity");
				i.putExtra("save_path", FileUtil.getExternalStorageDir().concat("/.WebStar/css_blocks.json"));
				startActivity(i);;
			}
		});
		
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Intent i = new Intent();
				i.setClassName("com.star4droid.BlocksLibV2", "com.star4droid.BlocksLibV2.ManagerActivity");
				i.putExtra("save_path", FileUtil.getExternalStorageDir().concat("/.WebStar/blocks.json"));
				startActivity(i);;
			}
		});
		
		lin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				button1.performClick(); 
			}
		});
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
				LayoutInflater dialogLI = getLayoutInflater();
				View dialogCV = (View) dialogLI.inflate(R.layout.project_dialog, null);
				dialog.setView(dialogCV);
				final EditText name = (EditText)
				dialogCV.findViewById(R.id.name);
				final Button create = (Button)
				dialogCV.findViewById(R.id.create);
				final LinearLayout con = (LinearLayout)
				dialogCV.findViewById(R.id.con);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
				dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT));
				dialog.show();
				create.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFF0067EE));
				con.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFF29354B));
				create.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View _view){
						if ("".equals(name.getText().toString())) {
							SketchwareUtil.showMessage(getApplicationContext(), "hi fool 😑\nwrite something!!");
						}
						else {
							if (FileUtil.isDirectory(FileUtil.getExternalStorageDir().concat("/.WebStar/projects/".concat(name.getText().toString())))) {
								SketchwareUtil.showMessage(getApplicationContext(), "there is folder with the same name!! ");
							}
							else {
								FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.WebStar/projects/".concat(name.getText().toString().concat("/drag.js"))), "");
								try{
									int count;
									java.io.InputStream input= MainActivity.this.getAssets().open("drag.js");
									java.io.OutputStream output = new  java.io.FileOutputStream(FileUtil.getExternalStorageDir().concat("/.WebStar/projects/".concat(name.getText().toString().concat("/")))+"drag.js");
									byte data[] = new byte[1024];
									while ((count = input.read(data))>0) {
										output.write(data, 0, count);
									}
									output.flush();
									output.close();
									input.close();
									 
									}catch(Exception xe){
											SketchwareUtil.CustomToast(getApplicationContext(), "extarcing resources eror: \n".concat(xe.toString()), 0xFFFFFFFF, 12, 0xFF454473, 5, SketchwareUtil.CENTER);
									}
								FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.WebStar/projects/".concat(name.getText().toString().concat("/index.html.Editor.html"))), "<html>\n<head><title>title</title></head>\n<body>\n<script src=\"drag.js\"></script>\n<script id=\"here1937\">\n//script here19373783\n</script>\n</body>\n<html>");
								FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.WebStar/projects/".concat(name.getText().toString().concat("/index.html"))), "");
								FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.WebStar/projects/".concat(name.getText().toString().concat("/Lists/index.html"))), "");
								_refresh();
								dialog.dismiss();
							}
						}
					}
				});
			}
		});
	}
	
	private void initializeLogic() {
		button2.getBackground().setColorFilter(0xFF0067EE, PorterDuff.Mode.MULTIPLY);
		button3.getBackground().setColorFilter(0xFF0067EE, PorterDuff.Mode.MULTIPLY);
		add_lin.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)90, 0xFF0067EE));
		info_lin.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)90, 0xFF0067EE));
	}
	
	@Override
	public void onStart() {
		super.onStart();
		_refresh();
	}
	public void _refresh() {
		listmap.clear();
		lst.clear();
		FileUtil.listDir(FileUtil.getExternalStorageDir().concat("/.WebStar/projects/"), lst);
		if (lst.size() > 0) {
			NoProjects.setVisibility(View.GONE);
			for(String i:lst){
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("file", i);
					listmap.add(_item);
				}
				
			}
			listview1.setAdapter(new Listview1Adapter(listmap));
		}
		else {
			NoProjects.setVisibility(View.VISIBLE);
		}
	}
	
	public class Listview1Adapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
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
				_view = _inflater.inflate(R.layout.project, null);
			}
			
			final LinearLayout item = _view.findViewById(R.id.item);
			final LinearLayout linear2 = _view.findViewById(R.id.linear2);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			final ImageView drop = _view.findViewById(R.id.drop);
			final LinearLayout delete = _view.findViewById(R.id.delete);
			final LinearLayout backup = _view.findViewById(R.id.backup);
			final ImageView imageview3 = _view.findViewById(R.id.imageview3);
			final TextView textview2 = _view.findViewById(R.id.textview2);
			final ImageView imageview4 = _view.findViewById(R.id.imageview4);
			final TextView textview3 = _view.findViewById(R.id.textview3);
			
			textview1.setText(Uri.parse(_data.get((int)_position).get("file").toString()).getLastPathSegment());
			linear2.setVisibility(View.GONE);
			drop.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View _view){
					if (linear2.getVisibility() == View.VISIBLE) {
						linear2.setVisibility(View.GONE);
					}
					else {
						linear2.setVisibility(View.VISIBLE);
					}
					drop.setScaleY((float)(drop.getScaleY() * -1));
				}
			});
			delete.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View _view){
					FileUtil.deleteFile(_data.get((int)_position).get("file").toString());
					_refresh();
				}
			});
			item.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View _view){
					intent.putExtra("path", _data.get((int)_position).get("file").toString());
					intent.setClass(getApplicationContext(), LayoutEditorActivity.class);
					startActivity(intent);
				}
			});
			
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
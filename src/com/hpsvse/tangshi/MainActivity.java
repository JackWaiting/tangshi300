package com.hpsvse.tangshi;

import java.io.File;
import java.util.List;

import com.hpsvse.tangshi.db.PoemDAO;
import com.hpsvse.tangshi.entity.Poem;
import com.hpsvse.tangshi.entity.Type;
import com.hpsvse.tangshi.util.MyAdapter;
import com.hpsvse.tangshi.util.PopupAdapter;

import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	private static final int TYPE = 0;
	private PoemDAO poemDAO;
	private ListView lv_catelog;
	private Button btn_all, btn_search, btn_type, btn_author,btn_more;
	PopupWindow popupWindow;
	PopupWindow popupWindowType;
	PopupWindow popupWindowAuth;
	LinearLayout parent;
	SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		poemDAO = new PoemDAO(this);
		sp = this.getSharedPreferences("recoder", Context.MODE_PRIVATE);
		loadFind();//setupViews()
		loadResource();
		loadLister();
		loadPopupWindow();
	}

	/**
	 * ����Popup����
	 */
	private void loadPopupWindow() {
		parent = (LinearLayout) findViewById(R.id.parent);
		popupWindowType = loadPopupWindowType(poemDAO.getType(), 1);
		popupWindowAuth = loadPopupWindowType(poemDAO.getAuth(), 2);
	}

	/**
	 * ����Popup����
	 * 
	 * @param lists
	 * @param type
	 * @return
	 */
	private PopupWindow loadPopupWindowType(List<Type> lists, final int type) {
		View contentView = getLayoutInflater().inflate(R.layout.list_popup,null);	// ��䴰��
		ListView lv_show = (ListView) contentView.findViewById(R.id.lv_show);
		lv_show.setAdapter(new PopupAdapter(this, lists));	// �����ڵ�ListView���������
		lv_show.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					searchByType(type, parent, position);	// ������������
			}
		});
		// ����һ��popupWindow���ڣ����Ϊ400px���߶�Ϊ��������
		popupWindow = new PopupWindow(contentView, 400,LayoutParams.WRAP_CONTENT);	
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.dialog_anim);	// ���ö���
		return popupWindow;
	}

	public void searchByType(final int type, AdapterView<?> parent,
			int position) {
		Type typ = (Type) parent.getItemAtPosition(position);
		String curId = typ.getId() + "";
		List<Poem> list = null;
		if (type == 1) {
			list = poemDAO.getGeneral("type", curId);
			popupWindowType.dismiss();	// �رմ���
		} else if (type == 2) {
			list = poemDAO.getGeneral("author", curId);
			popupWindowAuth.dismiss();	// �رմ���
		}
		System.out.println("---------- dismiss() ----------");
		MyAdapter adapter = new MyAdapter(MainActivity.this, list);
		lv_catelog.setAdapter(adapter);
	}

	/**
	 * ��ʾɾ��¼���Ի���
	 * @param position 
	 * @param parent2 
	 */
	protected void showDelRecordDialog(AdapterView<?> parent2, int position) {
		Poem poem = (Poem) parent2.getItemAtPosition(position);
		final String title = poem.getTitle();
		final String recordName = sp.getString(title, "");
		if (!recordName.equals("")) {
			AlertDialog.Builder builder = new Builder(MainActivity.this);
			builder.setMessage("�Ƿ�ɾ��¼���ļ���");
			builder.setNegativeButton("ȡ��", null);
			builder.setPositiveButton("ɾ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					File recordPath = new File(Environment.getExternalStorageDirectory() + "/tangshi/recorder/"+recordName+".amr");
					if (recordPath.exists()) {
						recordPath.delete();
						sp.edit().putString(title, "").commit();
						Toast.makeText(MainActivity.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(MainActivity.this, "ɾ��ʧ��", Toast.LENGTH_SHORT).show();
					}
				}
			});
			builder.create().show();
		}
	}

	/**
	 * ���ؼ����¼�
	 */
	private void loadLister() {
		btn_all.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		btn_type.setOnClickListener(this);
		btn_author.setOnClickListener(this);
		btn_more.setOnClickListener(this);
	}

	/**
	 * ������Դ
	 */
	private void loadResource() {
		showAll();
		lv_catelog.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Poem poem = (Poem) parent.getItemAtPosition(position);
				// ��ת����һ��ҳ��
				Intent intent = new Intent(MainActivity.this,ContentActivity.class);
				intent.putExtra("id", poem.getId() + "");
				intent.putExtra("auth", poem.getAuth());
				startActivityForResult(intent, 0);
				// ������ת����
				MainActivity.this.overridePendingTransition(
						R.anim.view_move_left_show, R.anim.view_move_left_hide);
			}
		});
		lv_catelog.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				showDelRecordDialog(parent,position);
				return true;
			}
		});
	}

	/**
	 * ���ҿؼ�
	 */
	private void loadFind() {
		lv_catelog = (ListView) findViewById(R.id.lv_catelog);
		btn_all = (Button) findViewById(R.id.btn_all);
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_type = (Button) findViewById(R.id.btn_type);
		btn_author = (Button) findViewById(R.id.btn_author);
		btn_more = (Button) findViewById(R.id.btn_more);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			showExitDialog();
		}
		return true;
	}

	/**
	 * �����˳��Ի���
	 */
	private void showExitDialog() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setTitle("�˳�");
		builder.setMessage("ȷ��Ҫ�˳���");
		builder.setNegativeButton("ȡ��", null);
		builder.setNeutralButton("֧��", null);
		builder.setPositiveButton("�˳�", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ֹͣ����
				stopService(new Intent(MainActivity.this, MainService.class));
				// ������ǰ����
				MainActivity.this.finish();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_anim);
	}

	@Override
	protected void onDestroy() {
		poemDAO.close();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_all:
			showAll();
			break;
		case R.id.btn_search:
			showDialogBySearch();
			break;
		case R.id.btn_type:
			showPopupByType();
			break;
		case R.id.btn_author:
			showPopupByAuthor();
			break;
		case R.id.btn_more:
			startActivityForResult(new Intent(MainActivity.this, MoreActivtiy.class), 0);
			overridePendingTransition(R.anim.view_move_left_show, R.anim.view_move_left_hide);
			break;
		}
	}

	/**
	 * ��ʾ ��ʫ�� ��Popup����
	 */
	private void showPopupByAuthor() {
		popupWindowAuth.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
	}

	/**
	 * ��ʾ ������ ��Popup����
	 */
	private void showPopupByType() {
		popupWindowType.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	/**
	 * ��ʾ�����Ի���
	 */
	private void showDialogBySearch() {
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_anim);	// ���Ի������ö���
		window.setContentView(R.layout.search_dialog);		// �Զ���Ի���View
		final RadioGroup rg = (RadioGroup) window.findViewById(R.id.radioGroup1);
		final EditText et_word = (EditText) window.findViewById(R.id.et_word);
		Button btn_search = (Button) window.findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				search(rg, et_word);	// ����
				dialog.dismiss();	// �رնԻ���
			}
		});
	}

	public void search(final RadioGroup rg, final EditText et_word) {
		List<Poem> list = null;
		String word = et_word.getText().toString().trim();
		int rbId = rg.getCheckedRadioButtonId();
		switch (rbId) {
		case R.id.radio0: // ����
			list = poemDAO.getGeneral("title", word);
			break;
		case R.id.radio1: // ʫ��
			list = poemDAO.getGeneral("content", word);
			break;
		case R.id.radio2: // ע��
			list = poemDAO.getGeneral("desc", word);
			break;
		}
		MyAdapter adapter = new MyAdapter(MainActivity.this, list);
		lv_catelog.setAdapter(adapter);
	}

	/**
	 * ��ʾ����
	 */
	private void showAll() {
		List<Poem> list = poemDAO.getGeneral();
		MyAdapter adapter = new MyAdapter(this, list);
		adapter.notifyDataSetChanged();
		lv_catelog.setAdapter(adapter);
	}

}

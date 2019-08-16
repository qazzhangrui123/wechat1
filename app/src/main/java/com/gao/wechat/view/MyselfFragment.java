package com.gao.wechat.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gao.wechat.R;
import com.gao.wechat.action.UserAction;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.Result;
import com.gao.wechat.data.UserInfo;
import com.gao.wechat.database.DatabaseUtil;
import com.gao.wechat.myself.MyBirthday;
import com.gao.wechat.myself.MyHeadpic;
import com.gao.wechat.myself.MyName;
import com.gao.wechat.myself.MySignature;
import com.gao.wechat.set.UserSetpanel;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyselfFragment extends Fragment {

    private TextView tvNickName;
    private TextView tvUserID;
    private TextView tvSignature;
    private TextView tvGender;
    private TextView tvBirthday;
    private TextView tvEmail;
    private LinearLayout menuName;
    private LinearLayout menuSignature;
    private LinearLayout menuBirthday;
    private LinearLayout menuEmail;
    private LinearLayout menuSettings;
    private LinearLayout menuGender;
    private ImageView imageView;

    private View view;
    private UserInfo myself;

    public MyselfFragment() {
        // Required empty public constructor
    }

    public static MyselfFragment newInstance() {
        MyselfFragment fragment = new MyselfFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myself, container, false);
        myself = AppData.getInstance().getMyInfo();
        initView();
        setClickListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initAppData();
        bindViewData();
    }

    private void initAppData() {
        myself = AppData.getInstance().getMyInfo();
    }

    private void bindViewData() {
        tvNickName.setText(myself.getNickName());
        tvUserID.setText("ID：" + myself.getUserID());
        tvSignature.setText("个性签名  " + myself.getSignature());
        tvGender.setText("性别  " + (myself.getGender()>0?"男":"女"));
        tvBirthday.setText("生日 " + myself.getBirthday());
        tvEmail.setText("电子邮箱 " + myself.getEmail());
    }

    private void initView() {
        tvNickName = view.findViewById(R.id.myself_name);
        tvUserID = view.findViewById(R.id.myself_id);
        tvSignature = view.findViewById(R.id.myself_signature_text);
        tvGender = view.findViewById(R.id.myself_gender_text);
        tvBirthday = view.findViewById(R.id.myself_birthday_text);
        tvEmail = view.findViewById(R.id.myself_email_text);
        menuName = view.findViewById(R.id.myself_name_area);
        menuSignature = view.findViewById(R.id.myself_signature);
        menuGender = view.findViewById(R.id.myself_gender);
        menuBirthday = view.findViewById(R.id.myself_birthday);
        menuEmail = view.findViewById(R.id.myself_email);
        menuSettings = view.findViewById(R.id.myself_settings);
        imageView = view.findViewById(R.id.myself_image);
    }

    public void setClickListener(){
        menuName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MyName.class);
                startActivity(intent);
            }
        });
        menuSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MySignature.class);
                startActivity(intent);
            }
        });
        menuBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MyBirthday.class);
                startActivity(intent);
            }
        });
        menuSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), UserSetpanel.class);
                startActivity(intent);
            }
        });
        menuGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogGender();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MyHeadpic.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 弹出对话框，更新性别信息
     */
    private void dialogGender() {
        final String [] genderSet = {"女","男"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),0);
        builder.setTitle("修改性别");
        builder.setSingleChoiceItems(genderSet, myself.getGender(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myself.setGender(i);
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                submitGenderResult(dialogInterface);
            }
        });
        builder.create().show();
    }

    private void submitGenderResult(final DialogInterface dialogInterface) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                toast("正在提交...");
            }
            @Override
            protected Integer doInBackground(Void... voids) {
                try {
                    UserAction.updateMyInfo(myself);
                    AppData.getInstance().initUpdate();
                    AppData.getInstance().waitForServer();
                    return 1;
                } catch (NullPointerException e) {
                    return -1;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return -2;
            }
            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                switch (result) {
                    case -1:
                        Log.d("WeChat:UpdateUserInfo:NickName", "无法连接到服务器！");
                        toast("无法连接到服务器");
                        break;
                    case -2:
                        Log.d("WeChat:UpdateUserInfo:NickName", "I/O 错误！");
                        toast("系统出现错误，请稍候重试");
                        break;
                    case 1:
                        Result r = AppData.getInstance().getUpdateResult();
                        if (r.typeEquals(Result.UpdateSuccess)) {
                            AppData.getInstance().setMyInfo(myself);
                            DatabaseUtil.get(getActivity()).saveMyInfo(myself);
                            toast("修改成功");
                            onResume();
                            dialogInterface.dismiss();
                        } else {
                            toast("修改失败");
                        }
                        break;
                    default:
                        toast("参数错误！");
                        break;
                }
            }
        }.execute();

    }

    protected void toast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected void toastLong(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

}

package com.competition.android.competition_five.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.competition.android.competition_five.Entity.OcrList;
import com.competition.android.competition_five.HomepageActivity;
import com.competition.android.competition_five.Ocr.FileUtil;
import com.competition.android.competition_five.Ocr.IDCardActivity;
import com.competition.android.competition_five.OcrRecycViewAdapter;
import com.competition.android.competition_five.R;
import com.competition.android.competition_five.Ocr.RecognizeService;
import com.competition.android.competition_five.Ocr.Test;
import com.competition.android.competition_five.Uilt.L;
import com.competition.android.competition_five.Uilt.OpenUile;
import com.competition.android.competition_five.Uilt.StaticUilt;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2017/8/18.
 */

public class NodeFragment extends Fragment {

    private static final int REQUEST_CODE_GENERAL = 105;
    private static final int REQUEST_CODE_GENERAL_BASIC = 106;
    private static final int REQUEST_CODE_GENERAL_ENHANCED = 107;
    private static final int REQUEST_CODE_GENERAL_WEBIMAGE = 108;
    private static final int REQUEST_CODE_BANKCARD = 110;
    private static final String AK = StaticUilt.AK;
    private static final String SK = StaticUilt.SK;

    private boolean hasGotToken = false;

    private AlertDialog.Builder alertDialog;
    private EditText orc_content;
    private EditText ocr_title;

    private  View dialogview;
    private AlertDialog.Builder dialog;

    //得到的结果
    private StringBuilder content = new StringBuilder();

    private LayoutInflater mInflater;

    //spinner
    private ArrayAdapter <String> spinner_adapter;
    private List <String> spinner_list = new ArrayList<>();
    private List <OcrList> mOcrLists = new ArrayList<>();
    private Spinner spinner_ocr_list;
    private EditText spinner_add_item;
    private Button finish_button;
    private Button spinner_add_button;
    MaterialEditText materialEditText;
    private String spinnertext = null;

    private RecyclerView ocr_list_recyclerview;
    private OcrRecycViewAdapter ocrRecycViewAdapter;
    private static NodeFragment nodeFragment = new NodeFragment();
    public static Fragment newInstance(){

        return nodeFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_node_layout,container,false);
        mInflater = LayoutInflater.from(getActivity());
        alertDialog = new AlertDialog.Builder(getActivity());
        orc_content =  (EditText)view.findViewById(R.id.ocr_content);
        dialog = new AlertDialog.Builder(getActivity());
        initDlalogView();

       ocr_list_recyclerview = (RecyclerView) view.findViewById(R.id.orc_list_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        ocr_list_recyclerview.setLayoutManager(linearLayoutManager);
        ocrRecycViewAdapter = new OcrRecycViewAdapter(getActivity(),mOcrLists);
        ocrRecycViewAdapter.setOnItemClickLitsener(new OcrRecycViewAdapter.OnItemClickListenr() {
            @Override
            public void onItemClick(View view, int position) {
                HomepageActivity.RepleceFragment(2);

            }
        });
        ocr_list_recyclerview.setAdapter(ocrRecycViewAdapter);


        //spinner

        OpenUile.setPrimaryDark(getActivity(),R.color.mediumslateblue);
        view.findViewById(R.id.general_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                L.d("触发点击");
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getActivity().getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_GENERAL);
            }
        });

        view.findViewById(R.id.general_basic_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getActivity().getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);
            }
        });

        view.findViewById(R.id.general_enhance_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getActivity().getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_GENERAL_ENHANCED);
            }
        });

        view.findViewById(R.id.general_webimage_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getActivity().getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_GENERAL_WEBIMAGE);
            }
        });

        view.findViewById(R.id.idcard_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(getActivity(), IDCardActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.bankcard_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getActivity().getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_BANK_CARD);
                startActivityForResult(intent, REQUEST_CODE_BANKCARD);
            }
        });

        // 请选择您的初始化方式
        // initAccessToken();
        initAccessTokenWithAkSk();


        return view;
    }
    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getActivity().getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    private void initAccessToken() {

        OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("licence方式获取token失败", error.getMessage());
            }
        }, getActivity().getApplicationContext());
    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getActivity().getApplicationContext(), AK, SK);
    }

    private void alertText(final String title, final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

    private void infoPopText(final String result) {
      /*  alertText("", result);
        Log.d("TAG", "infoPopText: "+result);*/
        StringToJSON(result);
        SaveTheContent();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessToken();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }
    }

    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GENERAL && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneral(FileUtil.getSaveFile(getActivity().getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }
        if (requestCode == REQUEST_CODE_GENERAL_BASIC && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneralBasic(FileUtil.getSaveFile(getActivity().getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }
        if (requestCode == REQUEST_CODE_GENERAL_ENHANCED && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneralEnhanced(FileUtil.getSaveFile(getActivity().getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }
        if (requestCode == REQUEST_CODE_GENERAL_WEBIMAGE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recWebimage(FileUtil.getSaveFile(getActivity().getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }
        if (requestCode == REQUEST_CODE_BANKCARD && resultCode == Activity.RESULT_OK) {
            RecognizeService.recBankCard(FileUtil.getSaveFile(getActivity().getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }
    }
    public void StringToJSON( String result) {
        Gson gson = new Gson();
        Test test = gson.fromJson(result,Test.class);
        for(int i= 0;i<test.getWords_result().size();i++)
        {
            content.append(test.getWords_result().get(i).getWords());
        }

    /*    Test.WordsResultBean test2 = gson.fromJson(result,Test.WordsResultBean.class);
        orc_content.setText("22"+test2.getWords()+"88");*/


    }
    public void  initDlalogView()
    {
/*        dialogview = mInflater.inflate(R.layout.dialog_ocr_layout,null);
        spinner_ocr_list = (Spinner) dialogview.findViewById(R.id.ocr_list_spinner);
        spinner_add_button = (Button) dialogview.findViewById(R.id.add_spinner_button);
        spinner_add_item = (EditText) dialogview.findViewById(R.id.spinner_add_listitem);
        finish_button = (Button) dialogview.findViewById(R.id.finish_spinner_button);
        initSpinner();*/
    }

    public  void  SaveTheContent()
    {
        dialogview = mInflater.inflate(R.layout.dialog_ocr_layout,null);
        materialEditText = (MaterialEditText)dialogview.findViewById(R.id.ocr_contents);
        spinner_ocr_list = (Spinner) dialogview.findViewById(R.id.ocr_list_spinner);
        spinner_add_button = (Button) dialogview.findViewById(R.id.add_spinner_button);
        spinner_add_item = (EditText) dialogview.findViewById(R.id.spinner_add_listitem);
        finish_button = (Button) dialogview.findViewById(R.id.finish_spinner_button);
        initSpinner();

        spinner_ocr_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnertext = spinner_ocr_list.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog.setMessage("                              保存")
                .setView(dialogview);
               dialog .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String context = spinner_add_item.getText().toString();
                        for(int i = 0;i < spinner_adapter.getCount();i++)
                        {
                            if(context.equals(spinner_adapter.getItem(i)))
                            {
                              return;
                            }

                        }
                        for (int i = 0;i < mOcrLists.size();i++)
                        {
                            if(spinnertext.equals(mOcrLists.get(i).getList_name()))
                            {

                                mOcrLists.get(i).AddArraylist(content.toString());
                                content= new StringBuilder();
                                L.i(mOcrLists.get(i).getArrayList().toString());
                            }
                        }


                        //判断添加想是否为空
                        if(!context.equals(""))
                        {
                            spinner_adapter.add(context);
                            OcrList ocrList = new OcrList();
                            ocrList.setList_name(context);
                            ocrList.AddArraylist(content.toString());
                            content= new StringBuilder();
                            mOcrLists.add(ocrList);

                            int position = spinner_adapter.getPosition(context);
                            spinner_ocr_list.setSelection(position);
                            //清空用于输入的EditText
                            spinner_add_item.setText("");
                        }
                        spinner_ocr_list.setVisibility(View.VISIBLE);
                        spinner_add_item.setVisibility(View.GONE);
                        spinner_add_button.setVisibility(View.VISIBLE);
                        finish_button.setVisibility(View.GONE);
                        ocrRecycViewAdapter.notifyDataSetChanged();
                        content= new StringBuilder();

                    }
                });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        dialog.show();
        materialEditText.setText(content);
        spinner_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinnerAdd();
            }
        });


    }



    private void initSpinner() {


        spinner_adapter = new ArrayAdapter<String>(
                getActivity().getApplication(),R.layout.support_simple_spinner_dropdown_item
                ,spinner_list);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_ocr_list.setAdapter(spinner_adapter);
    }

    public void SpinnerAdd()
    {
        spinner_ocr_list.setVisibility(View.GONE);
        spinner_add_item.setVisibility(View.VISIBLE);
        spinner_add_button.setVisibility(View.GONE);
        finish_button.setVisibility(View.VISIBLE);
    }
    private void Spinnerfinish() {


    }


}

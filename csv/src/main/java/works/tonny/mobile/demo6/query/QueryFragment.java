package works.tonny.mobile.demo6.query;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;


public class QueryFragment extends Fragment {
    private static final Map<String, Integer> qzcxTypes = new HashMap<String, Integer>();

    static {
        qzcxTypes.put("血统证书号", 1);
        qzcxTypes.put("犬只英文名", 2);
        qzcxTypes.put("繁殖人", 3);
        qzcxTypes.put("耳号", 4);
        qzcxTypes.put("芯片号", 5);
    }


    public QueryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.index_fragment_query, container, false);
        TitleHelper.getInstance((ViewGroup) inflate).setTitle("查询");
        Spinner qzcxType = (Spinner) inflate.findViewById(R.id.qzcx_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.query_spinner_item, getResources().getStringArray(R.array.quanzhichaxun)
        );
        adapter.setDropDownViewResource(R.layout.query_spinner_dropdown_item);
        qzcxType.setAdapter(adapter);
        final ActivityHelper helper = ActivityHelper.getInstance((ViewGroup) inflate);


        final EditText qzcx = (EditText) inflate.findViewById(R.id.qzcx);
        final TextView tip = (TextView) inflate.findViewById(R.id.qzcx_tip);
        qzcx.addTextChangedListener(new TipWatcher(tip));


        final EditText pqcx = (EditText) inflate.findViewById(R.id.pqcx);
        final TextView pqcxtip = (TextView) inflate.findViewById(R.id.pqcx_tip);
        pqcx.addTextChangedListener(new TipWatcher(pqcxtip));

        final EditText jpgqzsh = (EditText) inflate.findViewById(R.id.jpgqzsh);
        final TextView jpgqzshtip = (TextView) inflate.findViewById(R.id.jpgqzsh_tip);
        jpgqzsh.addTextChangedListener(new TipWatcher(jpgqzshtip));

        final EditText jpmqzsh = (EditText) inflate.findViewById(R.id.jpmqzsh);
        final TextView jpmqzshtip = (TextView) inflate.findViewById(R.id.jpmqzsh_tip);
        jpmqzsh.addTextChangedListener(new TipWatcher(jpmqzshtip));


        helper.setOnClickListener(R.id.qzcx_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = qzcx.getText().toString();
                if (StringUtils.isEmpty(s)) {
                    Toast.makeText(getActivity(), "请输入查询条件", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String type = helper.getValue(R.id.qzcx_type);
                Intent intent = new Intent();
                intent.setClass(getActivity(), QueryQzcxActivity.class);
                intent.putExtra("name", s);
                intent.putExtra("checktype", qzcxTypes.get(type));
                startActivity(intent);
            }
        });
        helper.setOnClickListener(R.id.pqcx_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = pqcx.getText().toString();
                if (StringUtils.isEmpty(s)) {
                    Toast.makeText(getActivity(), "请输入查询条件", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                intent.setClass(getActivity(), QzcxViewActivity.class);
                intent.putExtra("url", "http://www.csvclub.org:80/jsp/csvclub/csvclient/search/csvdogprove.jsp?blood=" + s);
                startActivity(intent);
            }
        });

        helper.setOnClickListener(R.id.jp_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = jpgqzsh.getText().toString();
                String e = jpmqzsh.getText().toString();
                if (StringUtils.isEmpty(s) && StringUtils.isEmpty(e)) {
                    Toast.makeText(getActivity(), "请输入查询条件", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(getActivity(), QueryMnjpActivity.class);
                intent.putExtra("jpgqzsh", s);
                intent.putExtra("jpmqzsh", e);
                startActivity(intent);
            }
        });


        return inflate;
    }


    class TipWatcher implements TextWatcher {

        View tip;

        public TipWatcher(View tip) {
            this.tip = tip;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0)
                tip.setVisibility(View.GONE);
            else
                tip.setVisibility(View.VISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}

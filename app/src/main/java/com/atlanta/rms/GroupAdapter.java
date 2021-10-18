package com.atlanta.rms;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class GroupAdapter extends BaseAdapter {
    private static final String TAG = "GroupAdapter";
    Activity _context;
    ArrayList<Group> _groups;

    public GroupAdapter(Activity  context, ArrayList<Group> groups) {
        //   super(c, R.layout.listview,workid);
        this._context=context;
        this._groups=groups;
    }
    @Override
    public int getCount() {
        return _groups.size();
    }

    @Override
    public Object getItem(int i) {
        return _groups.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vw=view;
        ViewHolderGroup _viewholder=null;
        if(vw==null)
        {
            LayoutInflater layoutInflater=_context.getLayoutInflater();
            vw=layoutInflater.inflate(R.layout.activity_groupadapter,viewGroup,false);
            _viewholder=new ViewHolderGroup(vw);
            vw.setTag(_viewholder);
        }
        else{
            _viewholder=(ViewHolderGroup) vw.getTag();
        }

        final Group _group = (Group) this.getItem(i);
        _viewholder.txtgroupname.setText(_group.get_GroupName());
        _viewholder.txtgroupname.setTag(_group.get_id());
        Log.d(TAG, "From View" + _group.get_GroupName());
        return vw;
    }
}

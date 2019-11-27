package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Category;
import com.example.myapplication.Interface.IonclickCTGR;
import com.example.myapplication.R;
import com.example.myapplication.databinding.CategoryListBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Category_list extends Fragment {

    CategoryListBinding binding ;
    List<Category> list;

    CategoryAdapter categoryAdapter;

    public Category_list() {

    }




    public static Category_list newInstance() {

        Bundle args = new Bundle();

        Category_list fragment = new Category_list();
        fragment.setArguments(args);
        return fragment;
    }

    public Category_list(List<Category> list) {
        this.list  = list;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.category_list,container,false);
        categoryAdapter = new CategoryAdapter(list);
        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        binding.rcvCtgr.setLayoutManager(layoutManager);
        binding.rcvCtgr.setAdapter(categoryAdapter);
        categoryAdapter.setonClickCTGR(new IonclickCTGR() {
            @Override
            public void onClickCTGR(Category category) {

                    CTGR_Fragment nextFrag = new CTGR_Fragment(category,getContext());
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, nextFrag, "ctgr_item_list")
                            .addToBackStack("ctgr_item_list")
                            .commit();
//                Toast.makeText(getContext(), "0000000000", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getContext(), CTGR_Activity.class);
//
//                startActivity(intent);

            }
        });
        
        return binding.getRoot();
    }


}

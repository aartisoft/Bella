package com.spots.bella.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.spots.bella.R;
import com.spots.bella.activity.Share.ShareActivity;
import com.spots.bella.activity.main_activity.MPresenter;
import com.spots.bella.activity.make_post.MakePostActivity;
import com.spots.bella.constants.Common;
import com.spots.bella.models.Post;

import java.util.ArrayList;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.spots.bella.constants.Common.GALLERY_PICK_POST_IMAGE;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MainRecyclerViewHolder> {

    private final ArrayList<Post> items;
    MPresenter mPresenter;
    Activity context;

    public MainRecyclerViewAdapter(Activity context, MPresenter mMPresenter, ArrayList<Post> posts_list) {
        this.context = context;
        this.mPresenter = mMPresenter;
        this.items = posts_list;
    }

    public static class MainRecyclerViewHolder extends RecyclerView.ViewHolder {
        View item;
        int view_type;
        CircleImageView header_profile;
        LinearLayout header_layout_your_mind;
        LinearLayout header_video;
        LinearLayout header_photos;

        ImageView normal_profile;

        public MainRecyclerViewHolder(View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view_type = viewType;
            item = itemView;
            if (viewType == VIEW_TYPES.HEADER) {
                header_profile = itemView.findViewById(R.id.iv_profile_rv_main_item_header);

                header_layout_your_mind = itemView.findViewById(R.id.layout_your_mind_rv_main_item_header);

                header_video = itemView.findViewById(R.id.video_rv_main_item_header);
                header_photos = itemView.findViewById(R.id.photos_rv_main_item_header);
            } else if (viewType == VIEW_TYPES.NORMAL) {
                normal_profile = itemView.findViewById(R.id.iv_profile_rv_main_item_normal);
            }
        }
    }

    @Override
    public MainRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case VIEW_TYPES.NORMAL:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_main_item_normal, parent, false);
                break;
            case VIEW_TYPES.HEADER:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_main_item_header, parent, false);
                break;
//            case Footer:
//                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_main_item_footer, parent, false);
//                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_main_item_normal, parent, false);
                break;
        }
        return new MainRecyclerViewHolder(v, viewType);
    }

    @Override
    public void onBindViewHolder(MainRecyclerViewHolder holder, int position) {
        if (holder.view_type == VIEW_TYPES.HEADER) {

            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "HEADER", Toast.LENGTH_SHORT).show();
                }
            });
            holder.header_layout_your_mind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context,MakePostActivity.class));
                }
            });
            holder.header_photos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ShareActivity.class));
//                    Common.openGallery(context, GALLERY_PICK_POST_IMAGE);
                    Toast.makeText(context, "Header photos", Toast.LENGTH_SHORT).show();
                }
            });
            holder.header_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Header profile", Toast.LENGTH_SHORT).show();
                }
            });
            holder.header_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Header video", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (holder.view_type == VIEW_TYPES.NORMAL) {
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "NORMAL", Toast.LENGTH_SHORT).show();
                }
            });

            holder.normal_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "normal profile", Toast.LENGTH_SHORT).show();
                }
            });
        }
//        holder.item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPresenter.onItemClicked(context); // add data
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }


    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return VIEW_TYPES.HEADER;
//        else if (position == 9)
//            return Footer;
        else
            return VIEW_TYPES.NORMAL;

    }

    public class VIEW_TYPES {
        public static final int HEADER = 1;
        public static final int NORMAL = 2;
//        public static final int Footer = 3;
    }
}

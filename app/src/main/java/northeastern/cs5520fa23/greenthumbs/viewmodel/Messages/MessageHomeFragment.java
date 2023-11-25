package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MessageHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView msgHistoryRV;
    private MessageHistoryAdapter msgHistoryAdapter;
    private List<MessageHistoryItem> chats;
    private FloatingActionButton fab;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageHomeFragment newInstance(String param1, String param2) {
        MessageHomeFragment fragment = new MessageHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MessageHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        chats = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        msgHistoryRV = view.findViewById(R.id.message_recycler_view);
        msgHistoryRV.setHasFixedSize(true);
        msgHistoryRV.setLayoutManager(new LinearLayoutManager(getContext()));
        msgHistoryAdapter = new MessageHistoryAdapter(chats, getContext());
        msgHistoryRV.setAdapter(msgHistoryAdapter);
        fab = view.findViewById(R.id.post_fab);

        addChats();
        msgHistoryAdapter.notifyItemInserted(0);
    }

    private void addChats() {
        this.chats.add(new MessageHistoryItem("garden_star", "Isn't that plant great :O ?!"));
    }

}
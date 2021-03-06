package com.Chats;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Login.User;
import com.example.clubapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class ChatAdapter extends FirestoreRecyclerAdapter<Chat,ChatAdapter.ChatHolder> {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String currentUserId;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ChatAdapter(FirestoreRecyclerOptions<Chat>options){
        super(options);
    }


    protected void onBindViewHolder(final ChatHolder holder, int position, final Chat model){

        final String selectedUser;
        final ArrayList<String> users = model.getUsers();



        currentUserId = user.getUid();


        if(currentUserId.equals(users.get(0))){
           selectedUser = users.get(1);

        }else{
            selectedUser = users.get(0);
        }

        DocumentReference docRef = db.collection("users").document(selectedUser);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User nameDetail = documentSnapshot.toObject(User.class);
                holder.chatUserName.setText(nameDetail.getUserName());
                Log.d("CHAT", "onSuccess: " + nameDetail.getPhotoUrl());
                Picasso.get().load(nameDetail.getPhotoUrl()).transform(new RoundedCornersTransformation(50,0)).fit().centerCrop().into(holder.chatUserImage);
            }
        });

        //holder.chatUserName.setText(selectedUser);

        holder.chat_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( v.getContext(), message_between_users.class);
                intent.putExtra("selected_user",selectedUser);
                v.getContext().startActivity(intent);
            }
        });



        Log.d("CHAT", "user1:" + users.get(0));
        Log.d("CHAT", "user2:" + users.get(1));
        Log.d("CHAT", "userChatId:" + model.getChatId());


    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card,parent,false);
        final ChatAdapter.ChatHolder holder = new ChatAdapter.ChatHolder(v);

        return holder;
    }

    class ChatHolder extends RecyclerView.ViewHolder{

        TextView chatUserName;
        ImageView chatUserImage;
        CardView chat_selected;

        public ChatHolder(final View itemView){
            super(itemView);

            chatUserName = itemView.findViewById(R.id.chatUserName);
            chatUserImage = itemView.findViewById(R.id.chatUserImage);
            chat_selected = itemView.findViewById(R.id.chat_card);
        }
    }
}

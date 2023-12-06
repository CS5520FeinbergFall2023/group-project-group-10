package northeastern.cs5520fa23.greenthumbs.model;

import com.google.firebase.auth.FirebaseUser;

public interface UserCreationCallback {
    void onSuccess(FirebaseUser fbUser);
    void onFailure(Exception e);
}

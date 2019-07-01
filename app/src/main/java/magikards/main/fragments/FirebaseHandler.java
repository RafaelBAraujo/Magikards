package magikards.main.fragments;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import magikards.main.data.Account;

class UserLogin {

    private boolean validation;

    public UserLogin() { this.validation = false; }

    public boolean getValidation() { return this.validation; }
    public void setValidation(boolean validationStatus) { this.validation = validationStatus; }
}

class FirebaseHandler {


    private static final FirebaseHandler ourInstance = new FirebaseHandler();
    private DatabaseReference mFirebaseDatabaseReference;

    private static UserLogin login;

    static FirebaseHandler getInstance() {
        return ourInstance;
    }

    private FirebaseHandler() {
        login = new UserLogin();
        this.mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void insertAccount(Account newAccount) {
        this.mFirebaseDatabaseReference.child("accounts").push().setValue(newAccount);
    }

    public boolean existAccount(final Account account) {

        final boolean[] existAccount = {false};

        this.mFirebaseDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot remoteRespostas : dataSnapshot.getChildren()) {
                    for (DataSnapshot remoteResposta: remoteRespostas.getChildren()) {
                        Account conta = remoteResposta.getValue(Account.class);

                        if(conta.getEmail().compareTo(account.getEmail()) == 0 || conta.getUsername().compareTo(account.getUsername()) == 0) {
                            // CONTA JÁ EXISTENTE
                            existAccount[0] = true;
                            return;
                        }

                    }
                }

                // CONTA NÃO EXISTE
                return;

            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("ERROR!!FAIL!!DATABASE!!", error.toException());
            }
        });

        return existAccount[0];

    }

    public Boolean validateLogin(final Account account) {


        mFirebaseDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot remoteRespostas : dataSnapshot.getChildren()) {
                    for (DataSnapshot remoteResposta: remoteRespostas.getChildren()) {
                        Account conta = remoteResposta.getValue(Account.class);

                        if(conta.getEmail().compareTo(account.getEmail()) == 0 && conta.getPassword().compareTo(account.getPassword()) == 0) {
                            // LOGIN SUCCESS
                            login.setValidation(true);

                            return;
                        }

                    }
                }

                return;

            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("ERROR!!FAIL!!DATABASE!!", error.toException());
            }
        });

        return login.getValidation();

    }

}

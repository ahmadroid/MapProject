package ir.ahmadandroid.mapproject.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ir.ahmadandroid.mapproject.application.App;
import ir.ahmadandroid.mapproject.database.MyDatabase;
import ir.ahmadandroid.mapproject.model.MessageToken;
import ir.ahmadandroid.mapproject.model.NationalCodePsApiKey;
import ir.ahmadandroid.mapproject.model.Person;
import ir.ahmadandroid.mapproject.model.PersonToken;
import ir.ahmadandroid.mapproject.remote.RetrofitService;
import ir.ahmadandroid.mapproject.utils.ErrorHandler;
import ir.ahmadandroid.mapproject.utils.MyActivity;
import ir.ahmadandroid.mapproject.R;
import ir.ahmadandroid.mapproject.utils.Utility;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends MyActivity implements View.OnClickListener {

    private static final long EXPIRE_DATE = 20201201;
    private static final int REQ_CODE_PER = 123;
    private static final String TAG = "LoginActivity";
    private Button btnInput;
    private EditText edtCode, edtPass;
    private SharedPreferences preferences;
    private MyDatabase myDatabase;
    private boolean isFound=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        chckPermission();
        expire();
        init();

    }

    //TODO delete expire date
    private void expire() {
        Locale locale = new Locale("en");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", locale);
        String dateStr = simpleDateFormat.format(new Date());
        long expDate = Long.parseLong(dateStr);
        if (expDate > EXPIRE_DATE) {
            Toast.makeText(this, getString(R.string.message_expire), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void init() {
        edtCode = findViewById(R.id.edt_code_login);
        edtPass = findViewById(R.id.edt_pass_login);
        btnInput = findViewById(R.id.btn_input_login);
        btnInput.setOnClickListener(this);
        preferences = Utility.getPreferences(LoginActivity.this);
        myDatabase=new MyDatabase(LoginActivity.this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnInput)) {
            if (checkConnected()) {
//                login();
                loginByDatabase();
            } else {
                Toast.makeText(this, getString(R.string.message_not_connect), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loginByFirebaseDatabase() {
        FirebaseDatabase firebaseDb=FirebaseDatabase.getInstance();
        DatabaseReference tb_person_reference = firebaseDb.getReference("tb_person");
        tb_person_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(LoginActivity.this, "login", Toast.LENGTH_SHORT).show();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String nationalCode = (String) dataSnapshot.child("nationalCode").getValue();
                    if (nationalCode.equals(edtCode.getText().toString())){
                        Toast.makeText(LoginActivity.this, "user is found", Toast.LENGTH_SHORT).show();
                        isFound=true;
                        if (edtPass.getText().toString().equals("1")){
                            Intent adminIntent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(adminIntent);
                        }
                        break;
                    }
                }
                if (!isFound){
                    Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String token = "";
                if (preferences != null) {
                    token = preferences.getString(Utility.PREFE_TOKEN_KEY, "");
                }
                Request original = chain.request();
                Request request = original.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader(Utility.TOKEN_KEY, token)
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });
        OkHttpClient client = clientBuilder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        NationalCodePsApiKey nationalCodePsApiKey = new NationalCodePsApiKey();
        String str = edtPass.getText().toString().trim();
//        String encryptStr = "";
//        try {
//            encryptStr = AESCrypt.encrypt("123", str);
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        }
        nationalCodePsApiKey.setPass(str);
        nationalCodePsApiKey.setNationalCode(edtCode.getText().toString().trim());
        Call<PersonToken> call = retrofitService.login(nationalCodePsApiKey);
        call.enqueue(new Callback<PersonToken>() {
            @Override
            public void onResponse(Call<PersonToken> call, retrofit2.Response<PersonToken> response) {
                if (response.isSuccessful()) {
                    Person person = response.body().getPerson();
                    if (preferences != null) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Utility.PREFE_TOKEN_KEY, response.body().getToken());
                        editor.putInt(Utility.PREFE_PERSON_IDENTIFY_CODE_KEY, person.getIdentifyCode());
                        editor.putString(Utility.PREFE_PERSON_NATIONAL_CODE_KEY, person.getNationalCode());
                        editor.putString(Utility.PREFE_PERSON_NAME_KEY, person.getName());
                        editor.putString(Utility.PREFE_PERSON_MOBILE_KEY, person.getMobile());
                        editor.apply();
                    }
                    if (person.getState() == 1) {
                        if (person.getIsAdmin() == 1) {
                            Intent adminIntent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(adminIntent);
                        } else if (person.getIsAdmin() == 0) {
                            Intent userIntent = new Intent(LoginActivity.this, UserActivity.class);
                            startActivity(userIntent);
                        }
                    } else if (person.getState() == 0) {
                        Toast.makeText(LoginActivity.this, getString(R.string.message_not_active), Toast.LENGTH_LONG).show();
                    }
                } else {
                    //this run block if code not 200
                    MessageToken responseError = new ErrorHandler(retrofit).parseError(response);
                    Toast.makeText(LoginActivity.this, responseError.getMessage(), Toast.LENGTH_SHORT).show();
                    if (preferences != null) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Utility.PREFE_TOKEN_KEY, responseError.getToken());
                        editor.apply();
                    }
                }

            }

            @Override
            public void onFailure(Call<PersonToken> call, Throwable t) {
                //this run block if not connect to net or syntax error
                Toast.makeText(LoginActivity.this, "is error", Toast.LENGTH_SHORT).show();
                Log.i(TAG, t.getMessage());
            }
        });
    }

    private void loginByDatabase(){
        List<Person> personList = myDatabase.readPersonListFromDb(edtCode.getText().toString());

        Person person=new Person();
        if (personList.size()==0 || !("1".equals(edtPass.getText().toString()))){
            Toast.makeText(this, getString(R.string.message_mistake_code_pass), Toast.LENGTH_LONG).show();
        }else{
            person=personList.get(0);
            if (person.getState() == 1) {
                if (person.getIsAdmin() == 1) {
                    Intent adminIntent = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(adminIntent);
                } else if (person.getIsAdmin() == 0) {
                    Intent userIntent = new Intent(LoginActivity.this, UserActivity.class);
                    startActivity(userIntent);
                }
            } else if (person.getState() == 0) {
                Toast.makeText(LoginActivity.this, getString(R.string.message_not_active), Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE_PER) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LoginActivity.this, "permok", Toast.LENGTH_SHORT).show();
                myDatabase.insertPersonListTodDB();
            } else {
                Toast.makeText(LoginActivity.this, "PermNo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void chckPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQ_CODE_PER);

        }
    }
}
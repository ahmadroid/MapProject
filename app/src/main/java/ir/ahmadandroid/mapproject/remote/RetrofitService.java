package ir.ahmadandroid.mapproject.remote;

import ir.ahmadandroid.mapproject.model.IdentifyCodeToken;
import ir.ahmadandroid.mapproject.model.NationalCodeApiKey;
import ir.ahmadandroid.mapproject.model.PersonListToken;
import ir.ahmadandroid.mapproject.model.PersonNationalCodeApiKey;
import ir.ahmadandroid.mapproject.model.MessageToken;
import ir.ahmadandroid.mapproject.model.NationalCodePsApiKey;
import ir.ahmadandroid.mapproject.model.PersonToken;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RetrofitService {

//    @POST("authorization.php")
//    Call<ResponseShopListToken> authorization(@Body Login login);

    @POST("Login.php")
    Call<PersonToken> login(@Body NationalCodePsApiKey nationalCodePsApiKey);

    @POST("InsertPersonToDB.php")
    Call<MessageToken> insertPerson(@Body PersonNationalCodeApiKey personNationalCodeApiKey);

    @POST("GetLastIdentifyCode.php")
    Call<IdentifyCodeToken> getLastIdentifyCode(@Body NationalCodeApiKey nationalCodeApiKey);

    @PUT("EditPersonInfo.php")
    Call<MessageToken> editPersonInfo(@Body PersonNationalCodeApiKey personNationalCodeApiKey);

    @POST("GetPersonList.php")
    Call<PersonListToken> getPersonList(@Body NationalCodeApiKey nationalCodeApiKey);
}

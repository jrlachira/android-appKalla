package org.kalla.enterprise.movil.gps.interfaces;

import org.kalla.enterprise.movil.gps.vo.ResponseVO;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SeguimientoManagerInterface {

    /*
     * @FormUrlEncoded : Point out this method will construct a form submit action.
     * @POST : Point out the form submit will use post method, the form action url is the parameter of @POST annotation.
     * @Field("form_field_name") : Indicate the form filed name, the filed value will be assigned to input parameter userNameValue.
     *
     */

    @Headers({"Accept: application/json"})
    @POST("moduloseguimientoservidorcontroller/insertarTracks")
    public Call<ResponseVO> enviarSeguimiento(@Body RequestBody body);

}

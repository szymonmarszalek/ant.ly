import android.content.Context
import com.example.antly.ApiConnection.AntlyApi
import com.example.antly.common.Resource
import com.example.antly.data.dto.LoginResponseDto
import com.example.antly.data.dto.UserDto
import com.example.antly.sharedPreferences.SharedPreferencesService
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    context: Context,
    private val tokenApi: AntlyApi,
   // private val sharedPreferencesService: SharedPreferencesService
) : Authenticator {

    private val appContext = context.applicationContext

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
                   response.request.newBuilder()
                        .header("Authorization", "Bearer ${getUpdatedToken().token}")
                        .build()
                }


        }
    private suspend fun getUpdatedToken(): LoginResponseDto {
       // val refreshToken = userPreferences.refreshToken.first()
        return  tokenApi.loginUser(UserDto("smarszalek","12345678"))
    }
}
 
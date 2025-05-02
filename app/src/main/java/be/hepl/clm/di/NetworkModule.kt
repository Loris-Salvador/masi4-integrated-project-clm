package be.hepl.clm.di

import android.os.Build
import androidx.annotation.RequiresApi
import be.hepl.clm.data.auth.RetrofitAuthApi
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("AuthAPI")
    fun provideRetrofitAuth(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://95.182.245.78:59001/api/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("ArticleAPI")
    fun provideRetrofitArticle(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://95.182.245.78:59006/")
            .client(getUnsafeOkHttpClient()) // 👈 ignore SSL
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("Chat")
    fun provideRetrofitChat( gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://95.182.245.78:59004/")
            .client(getUnsafeOkHttpClient()) // 👈 ignore SSL
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()

        return GsonConverterFactory.create(gson)
    }




}

class LocalDateTimeAdapter : TypeAdapter<LocalDateTime>() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @RequiresApi(Build.VERSION_CODES.O)
    override fun write(out: JsonWriter, value: LocalDateTime?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(formatter.format(value))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun read(reader: JsonReader): LocalDateTime? {
        val dateString = reader.nextString()
        return if (dateString.isNullOrBlank()) {
            null
        } else {
            LocalDateTime.parse(dateString, formatter)
        }
    }
}

fun getUnsafeOkHttpClient(): OkHttpClient {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })

    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, SecureRandom())
    val sslSocketFactory = sslContext.socketFactory

    return OkHttpClient.Builder()
        .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }
        .build()
}
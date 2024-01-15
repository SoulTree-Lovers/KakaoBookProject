package kakao.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kakao.model.Book;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class KakaoBookAPI {
    private static final String API_KEY = "d2835d16cae64fb90955434f3775a5c0"; // 카카오 개발자 앱 키
    private static final String API_BASE_URL = "https://dapi.kakao.com/v3/search/book\t";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    // 책 검색 메소드
    public static List<Book> searchBooks(String title) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("query", title);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Authorization", "KakaoAK " + API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) { // execute() <-- 서버로 요청 보내고 response 객체를 받음
            if (!response.isSuccessful()) throw new IOException("Request failed: " + response);

            JsonObject jsonObject = gson.fromJson(response.body().charStream(), JsonObject.class);
            JsonArray documents = jsonObject.getAsJsonArray("documents");

            List<Book> books = new ArrayList<>();

            for (JsonElement document : documents) {
                JsonObject bookJson = document.getAsJsonObject();
                Book book = new Book(
                        bookJson.get("title").getAsString(),
                        bookJson.get("authors").getAsJsonArray().toString(),
                        bookJson.get("publisher").getAsString(),
                        bookJson.get("thumbnail").getAsString()
                );
                books.add(book);
            }

            return books;
        }
    }
}

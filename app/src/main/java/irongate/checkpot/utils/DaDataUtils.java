/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Savelii Zagurskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package irongate.checkpot.utils;

import irongate.checkpot.App;
import irongate.checkpot.checkpotAPI.models.DaDataQueryBody;
import irongate.checkpot.checkpotAPI.models.DataResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*пример использования
       DaDataUtils.query("400", new OnSuggestionsListener() {
            @Override
            public void onSuggestionsReady(List<Suggestions> suggestions) {

            }

            @Override
            public void onError(String message) {

            }
        });
 */
public class DaDataUtils {

    public static void query(final String query, final OnSuggestionsListener listener) {
        App.retrofitDaDataCheckpotAPI.getPartyInfo(new DaDataQueryBody(query))
                .enqueue(new Callback<DataResponse>() {
                    @Override
                    public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                        listener.onSuggestionsReady(response.body().getSuggestions());
                    }

                    @Override
                    public void onFailure(Call<DataResponse> call, Throwable t) {
                        listener.onError(t.getMessage());
                    }
                });
    }


}
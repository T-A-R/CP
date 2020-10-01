package irongate.checkpot.checkpotAPI;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import irongate.checkpot.checkpotAPI.models.CreatedBy;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.checkpotAPI.models.Restaurant;
import irongate.checkpot.checkpotAPI.models.UserModel;

public class MyResultObjectAdapterFactory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType() != Place.class &&
            type.getRawType() != Event.class &&
            type.getRawType() != UserModel.class &&
            type.getRawType() != Restaurant.class &&
            type.getRawType() != CreatedBy.class)
            return null;

        TypeAdapter<T> defaultAdapter = gson.getDelegateAdapter(this, type);
        return (TypeAdapter<T>) new MyResultObjectAdapter(defaultAdapter);
    }

    public class MyResultObjectAdapter<T> extends TypeAdapter<T> {
        TypeAdapter<T> defaultAdapter;

        MyResultObjectAdapter(TypeAdapter<T> defaultAdapter) {
            this.defaultAdapter = defaultAdapter;
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            defaultAdapter.write(out, value);
        }

        @Override
        public T read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.STRING) {
                in.skipValue();
                return null;
            }
            return defaultAdapter.read(in);
        }
    }
}
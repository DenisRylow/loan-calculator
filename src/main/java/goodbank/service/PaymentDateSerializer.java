package goodbank.service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.ZonedDateTime;

public class PaymentDateSerializer extends StdSerializer<ZonedDateTime> {
    public PaymentDateSerializer(Class t) {
        super(t);
    }

    public PaymentDateSerializer() {
        this(ZonedDateTime.class);
    }

    @Override
    public void serialize(ZonedDateTime date, JsonGenerator jgen, SerializerProvider sp)
            throws IOException, JsonGenerationException {
        StringBuilder sb = new StringBuilder();
        jgen.writeStartObject();
        jgen.writeNumberField("year", date.getYear());
        jgen.writeStringField("month", date.getMonth().toString());
        jgen.writeEndObject();
    }
}

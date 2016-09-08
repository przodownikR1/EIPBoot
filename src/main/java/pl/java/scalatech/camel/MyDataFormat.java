package pl.java.scalatech.camel;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.ExchangeHelper;

public class MyDataFormat implements DataFormat{

    String hash;
    
    public MyDataFormat(String hash) {
      this.hash = hash;
    }
    
    @Override
    public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
        final String str = ExchangeHelper.convertToMandatoryType(exchange,String.class, graph);
                stream.write(str.getBytes(hash));
        
    }

    @Override
    public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
        final byte[] bytes =ExchangeHelper.convertToMandatoryType(exchange,byte[].class, stream);
        return new String(bytes, hash);
    }

}

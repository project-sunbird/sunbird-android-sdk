package org.ekstep.genieservices.commons.network;


public class ByteArrayRequestBody implements IRequestBody<byte[]> {

    private byte[] body;

    @Override
    public byte[] getBody() {
        return this.body;
    }

    @Override
    public void setBody(byte[] s) {
        this.body = s;
    }

    @Override
    public String getContentType() {
        return MIME_TYPE_JSON;
    }
}

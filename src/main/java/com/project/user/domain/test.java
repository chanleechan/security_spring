/*
package com.project.user.domain;

import org.springframework.web.multipart.MultipartFile;

public class test implements MultipartFile {
    private final byte[] data;
    private final String name;
    private final String originalFilename;
    private final String contentType;

    public test(byte[] data, String name, String originalFilename, String contentType) {
        this.data = data;
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return data == null || data.length == 0;
    }

    @Override
    public long getSize() {
        return data.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return data;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new java.io.ByteArrayInputStream(data);
    }

    @Override
    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("This is a read-only MultipartFile");
    }

}
*/

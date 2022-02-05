package com.jmsoftware.maf.osscenter.constant;

import org.springframework.util.unit.DataSize;

/**
 * Description: ChunkSize, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/3/2022 10:28 PM
 **/
@SuppressWarnings("unused")
public class Chunk {
    private Chunk() {
    }

    public static final String BUCKET_OBJECT_NAME_REGEX = "^.+/.+$";
    public static final DataSize TINY_CHUNK_SIZE = DataSize.ofBytes(512);
    public static final DataSize SMALL_CHUNK_SIZE = DataSize.ofMegabytes(1);
    public static final DataSize MEDIUM_CHUNK_SIZE = DataSize.ofMegabytes(4);
    public static final DataSize LARGE_CHUNK_SIZE = DataSize.ofMegabytes(8);
    public static final int MAX_CHUNK_NUMBER = 999;
}

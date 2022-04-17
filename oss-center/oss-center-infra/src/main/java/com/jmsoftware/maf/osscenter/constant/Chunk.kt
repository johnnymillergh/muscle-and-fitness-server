package com.jmsoftware.maf.osscenter.constant

import org.springframework.util.unit.DataSize

/**
 * Description: ChunkSize, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/3/2022 10:28 PM
 */
object Chunk {
    const val BUCKET_OBJECT_NAME_REGEX = "^.+/.+$"
    val TINY_CHUNK_SIZE = DataSize.ofBytes(512)
    val SMALL_CHUNK_SIZE = DataSize.ofMegabytes(1)
    val MEDIUM_CHUNK_SIZE = DataSize.ofMegabytes(4)
    val LARGE_CHUNK_SIZE = DataSize.ofMegabytes(8)
    const val MAX_CHUNK_NUMBER = 999
}

package com.dreamlab.edgefs.edge.handler;

public interface CompressionAndDecompressionIFace{
  // caw => compress and write
  // dar => decompress and read
  // returns size of the block that is written (depends on whether it is compressed or uncompressed)
  int compressAndWriteSnappy(String filePath, byte[] mbDataInBytesArray);
  // returns the data that is read
  byte[] decompressAndReadSnappy(String filePath,long uncompSize);

  // returns size of the block that is written (depends on whether it is compressed or uncompressed)
  int compressAndWriteGzip(String filePath, byte[] mbDataInBytesArray);
  // returns the data that is read
  byte[] decompressAndReadGzip(String filePath,long uncompSize);

  // returns size of the block that is written (depends on whether it is compressed or uncompressed)
  int compressAndWriteNA(String filePath, byte[] mbDataInBytesArray);
  // returns the data that is read
  byte[] decompressAndReadNA(String filePath,long uncompSize);
}
